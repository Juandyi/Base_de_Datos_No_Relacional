package com.motor.index;

import java.util.List;

/**
 * INDEX - Interfaz para todos los tipos de índices
 * 
 * Permite implementar diferentes estructuras:
 * - HashIndex: O(1) promedio para búsquedas exactas
 * - BTreeIndex: O(log n) con búsquedas por rango
 * - BPlusTreeIndex: O(log n) optimizado para disco
 */
public interface Index<K extends Comparable<K>, V> {
    
    void insert(K key, V value);
    
    V search(K key);
    
    boolean delete(K key);
    
    List<V> getAllValues();
    
    int size();
}
