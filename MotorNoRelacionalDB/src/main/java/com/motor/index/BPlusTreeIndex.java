package com.motor.index;

import java.util.ArrayList;
import java.util.List;

/**
 * BPLUSTREEINDEX - Índice basado en Árbol B+
 * 
 * Diferencias con Árbol B:
 * - TODAS las claves están en las hojas
 * - Nodos internos solo tienen claves de referencia
 * - Hojas enlazadas entre sí (ideal para recorridos secuenciales)
 * 
 * ¿Por qué es mejor para bases de datos?
 * 1. Búsquedas por rango muy eficientes (hojas enlazadas)
 * 2. Nodos internos más pequeños = más claves por nodo = menos I/O
 * 3. Todas las búsquedas terminan en hojas (consistente)
 * 
 * Complejidad:
 * - Búsqueda: O(log n)
 * - Inserción: O(log n)
 * - Eliminación: O(log n)
 * - Búsqueda por rango: O(log n + k) donde k = elementos en rango
 */
public class BPlusTreeIndex<K extends Comparable<K>, V> implements Index<K, V> {
    
    private int order;      // Máximo de hijos por nodo interno
    private Node root;
    private LeafNode firstLeaf;  // Primer nodo hoja (para recorridos)
    private int size;
    
    // Clase base para nodos
    private abstract class Node {
        int numKeys;
        Object[] keys;
        
        Node() {
            this.keys = new Object[order];
            this.numKeys = 0;
        }
        
        abstract boolean isLeaf();
    }
    
    // Nodo interno (solo claves de referencia, sin valores)
    private class InternalNode extends Node {
        Node[] children;
        
        InternalNode() {
            super();
            this.children = new BPlusTreeIndex.Node[order + 1];
        }
        
        @Override
        boolean isLeaf() { return false; }
    }
    
    // Nodo hoja (contiene claves Y valores, enlazado con siguiente hoja)
    private class LeafNode extends Node {
        Object[] values;
        LeafNode next;  // Enlace a siguiente hoja
        
        LeafNode() {
            super();
            this.values = new Object[order];
            this.next = null;
        }
        
        @Override
        boolean isLeaf() { return true; }
    }
    
    public BPlusTreeIndex(int order) {
        this.order = order;
        this.root = new LeafNode();
        this.firstLeaf = (LeafNode) root;
        this.size = 0;
    }
    
    /**
     * Buscar valor por clave
     * Complejidad: O(log n)
     */
    @Override
    @SuppressWarnings("unchecked")
    public V search(K key) {
        LeafNode leaf = findLeaf(key);
        
        for (int i = 0; i < leaf.numKeys; i++) {
            if (key.compareTo((K) leaf.keys[i]) == 0) {
                return (V) leaf.values[i];
            }
        }
        return null;
    }
    
    // Encontrar nodo hoja donde debería estar la clave
    @SuppressWarnings("unchecked")
    private LeafNode findLeaf(K key) {
        Node current = root;
        
        while (!current.isLeaf()) {
            InternalNode internal = (InternalNode) current;
            int i = 0;
            while (i < internal.numKeys && key.compareTo((K) internal.keys[i]) >= 0) {
                i++;
            }
            current = internal.children[i];
        }
        
        return (LeafNode) current;
    }
    
    /**
     * Insertar par clave-valor
     * Complejidad: O(log n)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void insert(K key, V value) {
        LeafNode leaf = findLeaf(key);
        
        // Verificar si la clave ya existe
        for (int i = 0; i < leaf.numKeys; i++) {
            if (key.compareTo((K) leaf.keys[i]) == 0) {
                leaf.values[i] = value;  // Actualizar valor existente
                return;
            }
        }
        
        // Insertar nueva clave
        if (leaf.numKeys < order - 1) {
            insertInLeaf(leaf, key, value);
        } else {
            // Nodo lleno, necesita división
            LeafNode newLeaf = new LeafNode();
            Object[] tempKeys = new Object[order];
            Object[] tempValues = new Object[order];
            
            // Copiar y ordenar
            int i, insertPos = 0;
            for (i = 0; i < leaf.numKeys; i++) {
                if (key.compareTo((K) leaf.keys[i]) < 0 && insertPos == i) {
                    tempKeys[insertPos] = key;
                    tempValues[insertPos] = value;
                    insertPos++;
                }
                tempKeys[insertPos] = leaf.keys[i];
                tempValues[insertPos] = leaf.values[i];
                insertPos++;
            }
            if (insertPos == i) {
                tempKeys[insertPos] = key;
                tempValues[insertPos] = value;
            }
            
            // Dividir
            int mid = order / 2;
            leaf.numKeys = 0;
            for (i = 0; i < mid; i++) {
                leaf.keys[i] = tempKeys[i];
                leaf.values[i] = tempValues[i];
                leaf.numKeys++;
            }
            
            for (i = mid; i < order; i++) {
                newLeaf.keys[i - mid] = tempKeys[i];
                newLeaf.values[i - mid] = tempValues[i];
                newLeaf.numKeys++;
            }
            
            // Enlazar hojas
            newLeaf.next = leaf.next;
            leaf.next = newLeaf;
            
            // Propagar división hacia arriba
            insertInParent(leaf, (K) newLeaf.keys[0], newLeaf);
        }
        size++;
    }
    
    // Insertar en nodo hoja no lleno
    @SuppressWarnings("unchecked")
    private void insertInLeaf(LeafNode leaf, K key, V value) {
        int i = leaf.numKeys - 1;
        while (i >= 0 && key.compareTo((K) leaf.keys[i]) < 0) {
            leaf.keys[i + 1] = leaf.keys[i];
            leaf.values[i + 1] = leaf.values[i];
            i--;
        }
        leaf.keys[i + 1] = key;
        leaf.values[i + 1] = value;
        leaf.numKeys++;
    }
    
    // Insertar clave en nodo padre después de división
    @SuppressWarnings("unchecked")
    private void insertInParent(Node left, K key, Node right) {
        if (left == root) {
            InternalNode newRoot = new InternalNode();
            newRoot.keys[0] = key;
            newRoot.children[0] = left;
            newRoot.children[1] = right;
            newRoot.numKeys = 1;
            root = newRoot;
            return;
        }
        
        InternalNode parent = findParent(root, left);
        
        if (parent.numKeys < order - 1) {
            // Hay espacio en el padre
            int i = parent.numKeys - 1;
            while (i >= 0 && key.compareTo((K) parent.keys[i]) < 0) {
                parent.keys[i + 1] = parent.keys[i];
                parent.children[i + 2] = parent.children[i + 1];
                i--;
            }
            parent.keys[i + 1] = key;
            parent.children[i + 2] = right;
            parent.numKeys++;
        } else {
            // Dividir nodo interno (simplificado)
            InternalNode newInternal = new InternalNode();
            parent.keys[parent.numKeys] = key;
            parent.children[parent.numKeys + 1] = right;
            parent.numKeys++;
            
            int mid = parent.numKeys / 2;
            K midKey = (K) parent.keys[mid];
            
            // Mover mitad superior al nuevo nodo
            for (int i = mid + 1; i < parent.numKeys; i++) {
                newInternal.keys[i - mid - 1] = parent.keys[i];
                newInternal.children[i - mid - 1] = parent.children[i];
                newInternal.numKeys++;
            }
            newInternal.children[newInternal.numKeys] = parent.children[parent.numKeys];
            parent.numKeys = mid;
            
            insertInParent(parent, midKey, newInternal);
        }
    }
    
    // Encontrar nodo padre
    @SuppressWarnings("unchecked")
    private InternalNode findParent(Node current, Node child) {
        if (current.isLeaf() || ((InternalNode) current).children[0].isLeaf()) {
            InternalNode internal = (InternalNode) current;
            for (int i = 0; i <= internal.numKeys; i++) {
                if (internal.children[i] == child) {
                    return internal;
                }
            }
            return null;
        }
        
        InternalNode internal = (InternalNode) current;
        for (int i = 0; i <= internal.numKeys; i++) {
            InternalNode result = findParent(internal.children[i], child);
            if (result != null) return result;
        }
        return null;
    }
    
    /**
     * Eliminar por clave (simplificado)
     * Complejidad: O(log n)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean delete(K key) {
        LeafNode leaf = findLeaf(key);
        
        int pos = -1;
        for (int i = 0; i < leaf.numKeys; i++) {
            if (key.compareTo((K) leaf.keys[i]) == 0) {
                pos = i;
                break;
            }
        }
        
        if (pos == -1) return false;
        
        // Eliminar desplazando
        for (int i = pos; i < leaf.numKeys - 1; i++) {
            leaf.keys[i] = leaf.keys[i + 1];
            leaf.values[i] = leaf.values[i + 1];
        }
        leaf.numKeys--;
        size--;
        return true;
    }
    
    /**
     * Búsqueda por rango (ventaja del B+)
     * Complejidad: O(log n + k) donde k = elementos en rango
     */
    @SuppressWarnings("unchecked")
    public List<V> rangeSearch(K startKey, K endKey) {
        List<V> results = new ArrayList<>();
        LeafNode leaf = findLeaf(startKey);
        
        boolean started = false;
        while (leaf != null) {
            for (int i = 0; i < leaf.numKeys; i++) {
                K currentKey = (K) leaf.keys[i];
                if (currentKey.compareTo(startKey) >= 0) started = true;
                if (started) {
                    if (currentKey.compareTo(endKey) > 0) return results;
                    results.add((V) leaf.values[i]);
                }
            }
            leaf = leaf.next;  // Seguir enlace a siguiente hoja
        }
        return results;
    }
    
    /**
     * Obtener todos los valores (recorriendo hojas enlazadas)
     * Complejidad: O(n)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<V> getAllValues() {
        List<V> values = new ArrayList<>();
        LeafNode current = firstLeaf;
        
        // Recorrer todas las hojas usando el enlace next
        while (current != null) {
            for (int i = 0; i < current.numKeys; i++) {
                values.add((V) current.values[i]);
            }
            current = current.next;
        }
        return values;
    }
    
    @Override
    public int size() {
        return size;
    }
}
