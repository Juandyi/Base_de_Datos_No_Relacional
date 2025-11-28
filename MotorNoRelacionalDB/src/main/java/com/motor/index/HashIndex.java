package com.motor.index;

import java.util.ArrayList;
import java.util.List;

/**
 * HASHINDEX - Índice basado en Tabla Hash
 * 
 * Implementación propia de tabla hash con encadenamiento.
 * 
 * Complejidad:
 * - insert(): O(1) promedio, O(n) peor caso
 * - search(): O(1) promedio, O(n) peor caso
 * - delete(): O(1) promedio, O(n) peor caso
 * 
 * El peor caso ocurre cuando todas las claves colisionan en el mismo bucket.
 */
public class HashIndex<K extends Comparable<K>, V> implements Index<K, V> {
    
    // Nodo para encadenamiento (Lista enlazada simple)
    private class HashNode {
        K key;
        V value;
        HashNode next;  // Enlace al siguiente nodo
        
        HashNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    
    private HashNode[] buckets;  // Arreglo de buckets
    private int capacity;
    private int size;
    
    @SuppressWarnings("unchecked")
    public HashIndex(int capacity) {
        this.capacity = capacity;
        this.buckets = (HashNode[]) new HashIndex.HashNode[capacity];
        this.size = 0;
    }
    
    public HashIndex() {
        this(16);  // Capacidad por defecto
    }
    
    /**
     * Función hash - convierte clave en índice
     * Complejidad: O(1)
     */
    private int hash(K key) {
        return Math.abs(key.hashCode() % capacity);
    }
    
    /**
     * Insertar par clave-valor
     * Complejidad: O(1) promedio
     */
    @Override
    public void insert(K key, V value) {
        int index = hash(key);
        HashNode newNode = new HashNode(key, value);
        
        // Si el bucket está vacío
        if (buckets[index] == null) {
            buckets[index] = newNode;
        } else {
            // Buscar si la clave ya existe o agregar al final
            HashNode current = buckets[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value;  // Actualizar
                    return;
                }
                if (current.next == null) break;
                current = current.next;
            }
            current.next = newNode;  // Agregar al final de la lista
        }
        size++;
    }
    
    /**
     * Buscar por clave
     * Complejidad: O(1) promedio
     */
    @Override
    public V search(K key) {
        int index = hash(key);
        HashNode current = buckets[index];
        
        // Recorrer lista enlazada del bucket
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }
    
    /**
     * Eliminar por clave
     * Complejidad: O(1) promedio
     */
    @Override
    public boolean delete(K key) {
        int index = hash(key);
        HashNode current = buckets[index];
        HashNode prev = null;
        
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }
    
    /**
     * Obtener todos los valores
     * Complejidad: O(n)
     */
    @Override
    public List<V> getAllValues() {
        List<V> values = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            HashNode current = buckets[i];
            while (current != null) {
                values.add(current.value);
                current = current.next;
            }
        }
        return values;
    }
    
    @Override
    public int size() {
        return size;
    }
}
