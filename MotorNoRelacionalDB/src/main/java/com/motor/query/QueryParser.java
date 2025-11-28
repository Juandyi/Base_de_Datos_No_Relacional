package com.motor.query;

/**
 * QUERYPARSER - Analiza sintácticamente las consultas
 * 
 * Formato soportado:
 * - SELECT * FROM coleccion
 * - SELECT * FROM coleccion WHERE campo=valor
 * - INSERT INTO coleccion VALUES (id, campo=valor,campo2=valor2)
 * - DELETE FROM coleccion WHERE _id=valor
 * 
 * Complejidad del parsing: O(n) donde n = longitud de la consulta
 */
public class QueryParser {
    
    // Clase para almacenar consulta parseada
    public static class ParsedQuery {
        public String operation;     // SELECT, INSERT, DELETE
        public String collection;    // Nombre de colección
        public String whereField;    // Campo del WHERE
        public String whereValue;    // Valor del WHERE
        public String docId;         // ID para INSERT
        public String data;          // Datos para INSERT
    }
    
    /**
     * Parsear consulta
     * Complejidad: O(n)
     */
    public ParsedQuery parse(String query) throws Exception {
        ParsedQuery result = new ParsedQuery();
        query = query.trim().toUpperCase();
        String originalQuery = query;
        
        // Detectar operación
        if (query.startsWith("SELECT")) {
            result.operation = "SELECT";
            parseSelect(originalQuery, result);
        } else if (query.startsWith("INSERT")) {
            result.operation = "INSERT";
            parseInsert(originalQuery, result);
        } else if (query.startsWith("DELETE")) {
            result.operation = "DELETE";
            parseDelete(originalQuery, result);
        } else {
            throw new Exception("Operación no reconocida");
        }
        
        return result;
    }
    
    private void parseSelect(String query, ParsedQuery result) throws Exception {
        // SELECT * FROM coleccion [WHERE campo=valor]
        query = query.trim();
        
        int fromIndex = query.toUpperCase().indexOf("FROM");
        if (fromIndex == -1) throw new Exception("Falta FROM");
        
        int whereIndex = query.toUpperCase().indexOf("WHERE");
        
        if (whereIndex == -1) {
            result.collection = query.substring(fromIndex + 4).trim();
        } else {
            result.collection = query.substring(fromIndex + 4, whereIndex).trim();
            String whereClause = query.substring(whereIndex + 5).trim();
            String[] parts = whereClause.split("=");
            if (parts.length == 2) {
                result.whereField = parts[0].trim();
                result.whereValue = parts[1].trim();
            }
        }
    }
    
    private void parseInsert(String query, ParsedQuery result) throws Exception {
        // INSERT INTO coleccion VALUES (id, datos)
        query = query.trim();
        
        int intoIndex = query.toUpperCase().indexOf("INTO");
        int valuesIndex = query.toUpperCase().indexOf("VALUES");
        
        if (intoIndex == -1 || valuesIndex == -1) {
            throw new Exception("Formato: INSERT INTO col VALUES (id, datos)");
        }
        
        result.collection = query.substring(intoIndex + 4, valuesIndex).trim();
        
        String valuesStr = query.substring(valuesIndex + 6).trim();
        if (valuesStr.startsWith("(") && valuesStr.endsWith(")")) {
            valuesStr = valuesStr.substring(1, valuesStr.length() - 1);
            String[] parts = valuesStr.split(",", 2);
            result.docId = parts[0].trim();
            if (parts.length > 1) {
                result.data = parts[1].trim();
            }
        }
    }
    
    private void parseDelete(String query, ParsedQuery result) throws Exception {
        // DELETE FROM coleccion WHERE _id=valor
        query = query.trim();
        
        int fromIndex = query.toUpperCase().indexOf("FROM");
        int whereIndex = query.toUpperCase().indexOf("WHERE");
        
        if (fromIndex == -1) throw new Exception("Falta FROM");
        
        if (whereIndex == -1) {
            result.collection = query.substring(fromIndex + 4).trim();
        } else {
            result.collection = query.substring(fromIndex + 4, whereIndex).trim();
            String whereClause = query.substring(whereIndex + 5).trim();
            String[] parts = whereClause.split("=");
            if (parts.length == 2) {
                result.whereField = parts[0].trim();
                result.whereValue = parts[1].trim();
            }
        }
    }
}
