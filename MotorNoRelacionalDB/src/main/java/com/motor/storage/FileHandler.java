package com.motor.storage;

import java.io.*;

/**
 * FILEHANDLER - Manejo de archivos para persistencia
 * 
 * Complejidad de operaciones I/O: O(n) donde n = tamaño del archivo
 */
public class FileHandler {
    
    private String basePath;
    
    public FileHandler(String basePath) {
        this.basePath = basePath;
        new File(basePath).mkdirs();
    }
    
    /**
     * Escribir contenido a archivo
     * Complejidad: O(n)
     */
    public void write(String filename, String content) {
        try (FileWriter writer = new FileWriter(basePath + "/" + filename)) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error escribiendo: " + e.getMessage());
        }
    }
    
    /**
     * Leer contenido de archivo
     * Complejidad: O(n)
     */
    public String read(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(basePath + "/" + filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            return null;
        }
        return content.toString();
    }
    
    /**
     * Verificar si archivo existe
     * Complejidad: O(1)
     */
    public boolean exists(String filename) {
        return new File(basePath + "/" + filename).exists();
    }
    
    /**
     * Eliminar archivo
     * Complejidad: O(1)
     */
    public boolean delete(String filename) {
        return new File(basePath + "/" + filename).delete();
    }
}
