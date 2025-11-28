package com.motor.index;

import java.util.ArrayList;
import java.util.List;

/**
 * BTREEINDEX - Índice basado en Árbol B
 * 
 * Árbol B: Árbol de búsqueda balanceado donde cada nodo puede tener múltiples hijos.
 * 
 * Propiedades (orden m):
 * - Cada nodo tiene máximo m hijos
 * - Cada nodo (excepto raíz) tiene mínimo ⌈m/2⌉ hijos
 * - La raíz tiene mínimo 2 hijos (si no es hoja)
 * - Todas las hojas están al mismo nivel
 * 
 * Complejidad (todas las operaciones):
 * - Búsqueda: O(log n)
 * - Inserción: O(log n)
 * - Eliminación: O(log n)
 * 
 * Ventaja: Minimiza accesos a disco (nodos grandes = menos niveles)
 */
public class BTreeIndex<K extends Comparable<K>, V> implements Index<K, V> {
    
    private int order;  // Orden del árbol (máximo de hijos por nodo)
    private BTreeNode root;
    private int size;
    
    // Clase interna para nodos del Árbol B
    private class BTreeNode {
        int numKeys;                    // Número actual de claves
        Object[] keys;                  // Arreglo de claves
        Object[] values;                // Arreglo de valores
        BTreeNode[] children;           // Arreglo de hijos
        boolean isLeaf;                 // ¿Es nodo hoja?
        
        @SuppressWarnings("unchecked")
        BTreeNode(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new Object[2 * order - 1];
            this.values = new Object[2 * order - 1];
            this.children = new BTreeIndex.BTreeNode[2 * order];
            this.numKeys = 0;
        }
    }
    
    public BTreeIndex(int order) {
        this.order = order;
        this.root = new BTreeNode(true);
        this.size = 0;
    }
    
    /**
     * Buscar valor por clave
     * Complejidad: O(log n) - altura del árbol es O(log n)
     */
    @Override
    @SuppressWarnings("unchecked")
    public V search(K key) {
        return searchInNode(root, key);
    }
    
    private V searchInNode(BTreeNode node, K key) {
        int i = 0;
        
        // Buscar posición de la clave en el nodo actual
        while (i < node.numKeys && key.compareTo((K) node.keys[i]) > 0) {
            i++;
        }
        
        // Si encontramos la clave
        if (i < node.numKeys && key.compareTo((K) node.keys[i]) == 0) {
            return (V) node.values[i];
        }
        
        // Si es hoja y no encontramos, no existe
        if (node.isLeaf) {
            return null;
        }
        
        // Buscar recursivamente en el hijo correspondiente
        return searchInNode(node.children[i], key);
    }
    
    /**
     * Insertar par clave-valor
     * Complejidad: O(log n)
     */
    @Override
    public void insert(K key, V value) {
        BTreeNode r = root;
        
        // Si la raíz está llena, crear nueva raíz
        if (r.numKeys == 2 * order - 1) {
            BTreeNode newRoot = new BTreeNode(false);
            newRoot.children[0] = root;
            splitChild(newRoot, 0, root);
            root = newRoot;
            insertNonFull(root, key, value);
        } else {
            insertNonFull(r, key, value);
        }
        size++;
    }
    
    // Dividir nodo hijo cuando está lleno
    @SuppressWarnings("unchecked")
    private void splitChild(BTreeNode parent, int index, BTreeNode fullChild) {
        BTreeNode newNode = new BTreeNode(fullChild.isLeaf);
        newNode.numKeys = order - 1;
        
        // Copiar la segunda mitad de claves al nuevo nodo
        for (int j = 0; j < order - 1; j++) {
            newNode.keys[j] = fullChild.keys[j + order];
            newNode.values[j] = fullChild.values[j + order];
        }
        
        // Si no es hoja, copiar hijos también
        if (!fullChild.isLeaf) {
            for (int j = 0; j < order; j++) {
                newNode.children[j] = fullChild.children[j + order];
            }
        }
        
        fullChild.numKeys = order - 1;
        
        // Insertar nuevo hijo en el padre
        for (int j = parent.numKeys; j > index; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[index + 1] = newNode;
        
        // Subir la clave media al padre
        for (int j = parent.numKeys - 1; j >= index; j--) {
            parent.keys[j + 1] = parent.keys[j];
            parent.values[j + 1] = parent.values[j];
        }
        parent.keys[index] = fullChild.keys[order - 1];
        parent.values[index] = fullChild.values[order - 1];
        parent.numKeys++;
    }
    
    // Insertar en nodo no lleno
    @SuppressWarnings("unchecked")
    private void insertNonFull(BTreeNode node, K key, V value) {
        int i = node.numKeys - 1;
        
        if (node.isLeaf) {
            // Desplazar claves mayores y insertar
            while (i >= 0 && key.compareTo((K) node.keys[i]) < 0) {
                node.keys[i + 1] = node.keys[i];
                node.values[i + 1] = node.values[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.values[i + 1] = value;
            node.numKeys++;
        } else {
            // Encontrar hijo donde insertar
            while (i >= 0 && key.compareTo((K) node.keys[i]) < 0) {
                i--;
            }
            i++;
            
            // Si el hijo está lleno, dividirlo
            if (node.children[i].numKeys == 2 * order - 1) {
                splitChild(node, i, node.children[i]);
                if (key.compareTo((K) node.keys[i]) > 0) {
                    i++;
                }
            }
            insertNonFull(node.children[i], key, value);
        }
    }
    
    /**
     * Eliminar por clave (simplificado)
     * Complejidad: O(log n)
     */
    @Override
    public boolean delete(K key) {
        if (search(key) == null) return false;
        deleteFromNode(root, key);
        size--;
        return true;
    }
    
    @SuppressWarnings("unchecked")
    private void deleteFromNode(BTreeNode node, K key) {
        int i = 0;
        while (i < node.numKeys && key.compareTo((K) node.keys[i]) > 0) {
            i++;
        }
        
        if (i < node.numKeys && key.compareTo((K) node.keys[i]) == 0 && node.isLeaf) {
            // Eliminar de nodo hoja
            for (int j = i; j < node.numKeys - 1; j++) {
                node.keys[j] = node.keys[j + 1];
                node.values[j] = node.values[j + 1];
            }
            node.numKeys--;
        }
    }
    
    /**
     * Obtener todos los valores (recorrido inorden)
     * Complejidad: O(n)
     */
    @Override
    public List<V> getAllValues() {
        List<V> values = new ArrayList<>();
        collectValues(root, values);
        return values;
    }
    
    @SuppressWarnings("unchecked")
    private void collectValues(BTreeNode node, List<V> values) {
        if (node == null) return;
        
        for (int i = 0; i < node.numKeys; i++) {
            if (!node.isLeaf) {
                collectValues(node.children[i], values);
            }
            values.add((V) node.values[i]);
        }
        if (!node.isLeaf) {
            collectValues(node.children[node.numKeys], values);
        }
    }
    
    @Override
    public int size() {
        return size;
    }
}
