package com.motor.core;

import java.util.HashMap;
import java.util.Map;

/**
 * DATABASE - Gestiona múltiples colecciones usando Tabla Hash
 * 
 * Complejidad HashMap:
 * - put(): O(1) promedio, O(n) peor caso (muchas colisiones)
 * - get(): O(1) promedio, O(n) peor caso
 * - remove(): O(1) promedio, O(n) peor caso
 */
public class Database {
    
    private String name;
    private Map<String, Collection> collections;
    
    public Database(String name) {
        this.name = name;
        this.collections = new HashMap<>();
        System.out.println("Base de datos '" + name + "' inicializada.");
    }
    
    // Crear colección - O(1)
    public Collection createCollection(String collectionName) {
        if (!collections.containsKey(collectionName)) {
            Collection col = new Collection(collectionName);
            collections.put(collectionName, col);
            return col;
        }
        return collections.get(collectionName);
    }
    
    // Obtener colección - O(1)
    public Collection getCollection(String collectionName) {
        return collections.get(collectionName);
    }
    
    // Eliminar colección - O(1)
    public boolean dropCollection(String collectionName) {
        return collections.remove(collectionName) != null;
    }
    
    // Listar colecciones - O(n)
    public void listCollections() {
        System.out.println("Colecciones:");
        for (String name : collections.keySet()) {
            System.out.println("  - " + name);
        }
    }
    
    public String getName() { return name; }
}
