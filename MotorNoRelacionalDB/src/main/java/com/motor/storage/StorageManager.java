package com.motor.storage;

import com.motor.core.*;
import java.util.List;

/**
 * STORAGEMANAGER - Gestiona persistencia con buffer usando Lista Enlazada
 * 
 * Implementa TDA Lista (estructura lineal del pensum)
 */
public class StorageManager {
    
    private FileHandler fileHandler;
    private WriteBuffer writeBuffer;
    
    // TDA LISTA ENLAZADA SIMPLE para buffer
    private class WriteBuffer {
        private class Node {
            Document data;
            Node next;
            Node(Document data) { this.data = data; this.next = null; }
        }
        
        private Node head, tail;
        private int size;
        
        // Insertar al final - O(1)
        void add(Document doc) {
            Node n = new Node(doc);
            if (tail == null) head = tail = n;
            else { tail.next = n; tail = n; }
            size++;
        }
        
        // Remover del inicio - O(1)
        Document remove() {
            if (head == null) return null;
            Document d = head.data;
            head = head.next;
            if (head == null) tail = null;
            size--;
            return d;
        }
        
        boolean isEmpty() { return size == 0; }
    }
    
    public StorageManager(String basePath) {
        this.fileHandler = new FileHandler(basePath);
        this.writeBuffer = new WriteBuffer();
    }
    
    public void bufferWrite(Document doc) { writeBuffer.add(doc); }
    
    public void flush(String colName) {
        StringBuilder sb = new StringBuilder();
        while (!writeBuffer.isEmpty()) {
            sb.append(writeBuffer.remove().toString()).append("\n");
        }
        fileHandler.write(colName + ".json", sb.toString());
    }
    
    public void saveCollection(Collection col) {
        List<Document> docs = col.getAll();
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < docs.size(); i++) {
            sb.append("  ").append(docs.get(i));
            if (i < docs.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        fileHandler.write(col.getName() + ".json", sb.toString());
    }
}
