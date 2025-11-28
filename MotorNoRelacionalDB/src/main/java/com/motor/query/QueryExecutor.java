package com.motor.query;

import com.motor.core.*;

/**
 * QUERYEXECUTOR - Ejecuta las consultas parseadas
 * 
 * Separación de responsabilidades:
 * - QueryParser: Analiza la sintaxis
 * - QueryExecutor: Ejecuta las operaciones
 * - QueryProcessor: Coordina ambos
 */
public class QueryExecutor {
    
    private Database database;
    
    public QueryExecutor(Database database) {
        this.database = database;
    }
    
    public Database getDatabase() {
        return database;
    }
}
