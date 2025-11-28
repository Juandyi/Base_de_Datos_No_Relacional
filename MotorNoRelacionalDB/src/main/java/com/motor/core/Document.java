package com.motor.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DOCUMENT - Representa un documento (similar a JSON)
 * 
 * Usa HashMap internamente para almacenar pares clave-valor
 * 
 * Complejidad:
 * - put(): O(1) promedio
 * - get(): O(1) promedio
 * - remove(): O(1) promedio
 */
public class Document {
    
    private String id;
    private Map<String, Object> data;
    
    public Document(String id) {
        this.id = id;
        this.data = new HashMap<>();
    }
    
    // Agregar campo - O(1)
    public void put(String key, Object value) {
        data.put(key, value);
    }
    
    // Obtener campo - O(1)
    public Object get(String key) {
        return data.get(key);
    }
    
    // Eliminar campo - O(1)
    public Object remove(String key) {
        return data.remove(key);
    }
    
    // Verificar si existe campo - O(1)
    public boolean hasField(String key) {
        return data.containsKey(key);
    }
    
    // Obtener todas las claves - O(1)
    public Set<String> getKeys() {
        return data.keySet();
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ _id: \"").append(id).append("\"");
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sb.append(", ").append(entry.getKey()).append(": ");
            if (entry.getValue() instanceof String) {
                sb.append("\"").append(entry.getValue()).append("\"");
            } else {
                sb.append(entry.getValue());
            }
        }
        sb.append(" }");
        return sb.toString();
    }
}
