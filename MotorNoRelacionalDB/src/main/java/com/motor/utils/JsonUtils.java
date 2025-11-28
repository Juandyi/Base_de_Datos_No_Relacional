package com.motor.utils;

import com.motor.core.Document;
import java.util.*;

/**
 * JSONUTILS - Utilidades para manejo de JSON simple
 * 
 * Parser básico sin dependencias externas
 */
public class JsonUtils {
    
    /**
     * Convertir Document a JSON string
     * Complejidad: O(n) donde n = número de campos
     */
    public static String toJson(Document doc) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"_id\": \"").append(doc.getId()).append("\"");
        
        for (String key : doc.getKeys()) {
            sb.append(",\n  \"").append(key).append("\": ");
            Object value = doc.get(key);
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else {
                sb.append(value);
            }
        }
        sb.append("\n}");
        return sb.toString();
    }
    
    /**
     * Parsear JSON simple a Document
     * Complejidad: O(n) donde n = longitud del string
     */
    public static Document fromJson(String json) {
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            return null;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        String[] pairs = json.split(",");
        
        String id = "unknown";
        Document doc = null;
        
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String key = kv[0].trim().replace("\"", "");
                String value = kv[1].trim().replace("\"", "");
                
                if (key.equals("_id")) {
                    id = value;
                    doc = new Document(id);
                } else if (doc != null) {
                    doc.put(key, value);
                }
            }
        }
        
        return doc != null ? doc : new Document(id);
    }
    
    /**
     * Convertir lista de documentos a JSON array
     * Complejidad: O(n * m) donde n = docs, m = campos promedio
     */
    public static String toJsonArray(List<Document> docs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < docs.size(); i++) {
            sb.append(toJson(docs.get(i)));
            if (i < docs.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
}
