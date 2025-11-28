package com.motor.core;

import com.motor.index.BPlusTreeIndex;
import java.util.ArrayList;
import java.util.List;

/**
 * COLLECTION - Almacena documentos usando Árbol B+ como índice principal
 * 
 * El Árbol B+ es ideal para bases de datos porque:
 * - Todas las claves están en las hojas (recorrido eficiente)
 * - Hojas enlazadas (búsquedas por rango eficientes)
 * - Altura balanceada (O(log n) garantizado)
 * 
 * Complejidad:
 * - insert(): O(log n)
 * - findById(): O(log n)
 * - delete(): O(log n)
 * - getAll(): O(n)
 */
public class Collection {
    
    private String name;
    private BPlusTreeIndex<String, Document> index;  // Índice Árbol B+
    private int documentCount;
    
    public Collection(String name) {
        this.name = name;
        this.index = new BPlusTreeIndex<>(4);  // Orden 4
        this.documentCount = 0;
    }
    
    /**
     * Insertar documento
     * Complejidad: O(log n) - inserción en Árbol B+
     */
    public void insert(Document doc) {
        index.insert(doc.getId(), doc);
        documentCount++;
    }
    
    /**
     * Buscar por ID
     * Complejidad: O(log n) - búsqueda en Árbol B+
     */
    public Document findById(String id) {
        return index.search(id);
    }
    
    /**
     * Buscar por campo específico
     * Complejidad: O(n) - debe recorrer todos los documentos
     */
    public List<Document> findByField(String field, Object value) {
        List<Document> results = new ArrayList<>();
        List<Document> all = index.getAllValues();
        
        for (Document doc : all) {
            Object docValue = doc.get(field);
            if (docValue != null && docValue.equals(value)) {
                results.add(doc);
            }
        }
        return results;
    }
    
    /**
     * Eliminar documento
     * Complejidad: O(log n)
     */
    public boolean delete(String id) {
        boolean deleted = index.delete(id);
        if (deleted) documentCount--;
        return deleted;
    }
    
    /**
     * Actualizar documento
     * Complejidad: O(log n)
     */
    public boolean update(String id, Document newDoc) {
        if (index.search(id) != null) {
            index.delete(id);
            newDoc.setId(id);
            index.insert(id, newDoc);
            return true;
        }
        return false;
    }
    
    /**
     * Mostrar todos los documentos
     * Complejidad: O(n)
     */
    public void showAll() {
        System.out.println("\n=== Colección: " + name + " ===");
        List<Document> docs = index.getAllValues();
        if (docs.isEmpty()) {
            System.out.println("(vacía)");
        } else {
            for (Document doc : docs) {
                System.out.println(doc);
            }
        }
        System.out.println("Total: " + documentCount + " documentos");
    }
    
    /**
     * Obtener todos los documentos
     * Complejidad: O(n)
     */
    public List<Document> getAll() {
        return index.getAllValues();
    }
    
    public String getName() { return name; }
    public int size() { return documentCount; }
}
