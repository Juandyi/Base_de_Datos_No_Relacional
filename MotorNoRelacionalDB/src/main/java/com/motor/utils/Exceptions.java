package com.motor.utils;

/**
 * EXCEPTIONS - Excepciones personalizadas del motor
 */
public class Exceptions {
    
    public static class CollectionNotFoundException extends RuntimeException {
        public CollectionNotFoundException(String name) {
            super("Colección no encontrada: " + name);
        }
    }
    
    public static class DocumentNotFoundException extends RuntimeException {
        public DocumentNotFoundException(String id) {
            super("Documento no encontrado: " + id);
        }
    }
    
    public static class DuplicateKeyException extends RuntimeException {
        public DuplicateKeyException(String id) {
            super("ID duplicado: " + id);
        }
    }
    
    public static class InvalidQueryException extends RuntimeException {
        public InvalidQueryException(String msg) {
            super("Consulta inválida: " + msg);
        }
    }
    
    public static class StorageException extends RuntimeException {
        public StorageException(String msg) {
            super("Error de almacenamiento: " + msg);
        }
    }
}
