package com.motor.query;

import com.motor.core.*;
import java.util.List;

/**
 * QUERYPROCESSOR - Procesa y ejecuta consultas
 * 
 * Soporta:
 * - SELECT * FROM coleccion
 * - SELECT * FROM coleccion WHERE campo=valor
 * - INSERT INTO coleccion VALUES (id, datos)
 * - DELETE FROM coleccion WHERE _id=valor
 */
public class QueryProcessor {
    
    private Database database;
    private QueryParser parser;
    
    public QueryProcessor(Database database) {
        this.database = database;
        this.parser = new QueryParser();
    }
    
    public void execute(String query) {
        try {
            QueryParser.ParsedQuery parsed = parser.parse(query);
            
            switch (parsed.operation) {
                case "SELECT": executeSelect(parsed); break;
                case "INSERT": executeInsert(parsed); break;
                case "DELETE": executeDelete(parsed); break;
                default: System.out.println("Operación no soportada");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void executeSelect(QueryParser.ParsedQuery p) {
        Collection col = database.getCollection(p.collection);
        if (col == null) {
            System.out.println("Colección no encontrada");
            return;
        }
        
        if (p.whereField == null) {
            col.showAll();
            System.out.println("Complejidad: O(n)");
        } else if (p.whereField.equals("_id")) {
            Document doc = col.findById(p.whereValue);
            System.out.println(doc != null ? doc : "No encontrado");
            System.out.println("Complejidad: O(log n)");
        } else {
            List<Document> results = col.findByField(p.whereField, p.whereValue);
            results.forEach(System.out::println);
            System.out.println("Total: " + results.size() + " | Complejidad: O(n)");
        }
    }
    
    private void executeInsert(QueryParser.ParsedQuery p) {
        Collection col = database.getCollection(p.collection);
        if (col == null) col = database.createCollection(p.collection);
        
        Document doc = new Document(p.docId);
        if (p.data != null) {
            for (String pair : p.data.split(",")) {
                String[] kv = pair.split("=");
                if (kv.length == 2) doc.put(kv[0].trim(), kv[1].trim());
            }
        }
        col.insert(doc);
        System.out.println("✓ Insertado | Complejidad: O(log n)");
    }
    
    private void executeDelete(QueryParser.ParsedQuery p) {
        Collection col = database.getCollection(p.collection);
        if (col == null) { System.out.println("Colección no encontrada"); return; }
        
        if ("_id".equals(p.whereField)) {
            System.out.println(col.delete(p.whereValue) ? "✓ Eliminado" : "No encontrado");
            System.out.println("Complejidad: O(log n)");
        }
    }
}
