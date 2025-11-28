package com.motor;

import com.motor.core.*;
import com.motor.query.QueryProcessor;
import java.util.Scanner;

/**
 * MOTOR DE BASE DE DATOS NO RELACIONAL
 * Proyecto de Ciencias de la Computación 1
 */
public class Main {
    
    private static Database database;
    private static QueryProcessor queryProcessor;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   MOTOR DE BASE DE DATOS NO RELACIONAL     ║");
        System.out.println("║   Ciencias de la Computación 1             ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        database = new Database("MiBaseDeDatos");
        queryProcessor = new QueryProcessor(database);
        scanner = new Scanner(System.in);
        
        menuPrincipal();
    }
    
    private static void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Crear colección");
            System.out.println("2. Insertar documento");
            System.out.println("3. Buscar documento");
            System.out.println("4. Eliminar documento");
            System.out.println("5. Mostrar colección");
            System.out.println("6. Ejecutar consulta");
            System.out.println("7. Ver análisis de complejidad");
            System.out.println("8. Demostración de algoritmos");
            System.out.println("9. Cargar datos de ejemplo");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: crearColeccion(); break;
                case 2: insertarDocumento(); break;
                case 3: buscarDocumento(); break;
                case 4: eliminarDocumento(); break;
                case 5: mostrarColeccion(); break;
                case 6: ejecutarConsulta(); break;
                case 7: verEstadisticas(); break;
                case 8: demoAlgoritmos(); break;
                case 9: cargarDatosEjemplo(); break;
                case 0: System.out.println("¡Hasta luego!"); break;
                default: System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }
    
    private static void crearColeccion() {
        System.out.print("Nombre de la colección: ");
        String nombre = scanner.nextLine();
        database.createCollection(nombre);
        System.out.println("Colección '" + nombre + "' creada.");
    }
    
    private static void insertarDocumento() {
        System.out.print("Colección: ");
        String coleccion = scanner.nextLine();
        System.out.print("ID del documento: ");
        String id = scanner.nextLine();
        System.out.print("Datos (clave1=valor1,clave2=valor2): ");
        String datos = scanner.nextLine();
        
        Collection col = database.getCollection(coleccion);
        if (col != null) {
            Document doc = new Document(id);
            String[] pares = datos.split(",");
            for (String par : pares) {
                String[] kv = par.split("=");
                if (kv.length == 2) {
                    doc.put(kv[0].trim(), kv[1].trim());
                }
            }
            col.insert(doc);
            String nombre = doc.get("nombre") != null ? doc.get("nombre").toString() : "(sin nombre)";
            System.out.println("Documento insertado. ID: " + doc.getId() + ", nombre: " + nombre + ". Complejidad: O(log n)");
        } else {
            System.out.println("Colección no encontrada.");
        }
    }
    
    private static void buscarDocumento() {
        System.out.print("Colección: ");
        String coleccion = scanner.nextLine();
        System.out.print("ID a buscar: ");
        String id = scanner.nextLine();
        
        Collection col = database.getCollection(coleccion);
        if (col != null) {
            Document doc = col.findById(id);
            if (doc != null) {
                String nombre = doc.get("nombre") != null ? doc.get("nombre").toString() : "(sin nombre)";
                System.out.println("Encontrado: ID: " + doc.getId() + ", nombre: " + nombre);
                System.out.println(doc);
                System.out.println("Complejidad: O(log n)");
            } else {
                System.out.println("No encontrado.");
            }
        } else {
            System.out.println("Colección no encontrada.");
        }
    }
    
    private static void eliminarDocumento() {
        System.out.print("Colección: ");
        String coleccion = scanner.nextLine();
        System.out.print("ID a eliminar: ");
        String id = scanner.nextLine();
        
        Collection col = database.getCollection(coleccion);
        if (col != null) {
            Document doc = col.findById(id);
            if (doc != null) {
                boolean ok = col.delete(id);
                String nombre = doc.get("nombre") != null ? doc.get("nombre").toString() : "(sin nombre)";
                System.out.println(ok ? ("Eliminado: ID: " + id + ", nombre: " + nombre) : "No encontrado");
            } else {
                System.out.println("No encontrado");
            }
        }
    }
    
    private static void mostrarColeccion() {
        System.out.print("Colección: ");
        String coleccion = scanner.nextLine();
        Collection col = database.getCollection(coleccion);
        if (col != null) {
            System.out.println("\n=== Colección: " + coleccion + " ===");
            java.util.List<Document> docs = col.getAll();
            if (docs.isEmpty()) {
                System.out.println("(vacía)");
            } else {
                for (Document doc : docs) {
                    String nombre = doc.get("nombre") != null ? doc.get("nombre").toString() : "(sin nombre)";
                    System.out.println("ID: " + doc.getId() + " | nombre: " + nombre);
                }
            }
            System.out.println("Total: " + col.size() + " documentos");
        } else {
            System.out.println("Colección no encontrada.");
        }
    }
    
    private static void ejecutarConsulta() {
        System.out.print("Consulta: ");
        String consulta = scanner.nextLine();
        queryProcessor.execute(consulta);
    }
    
    private static void verEstadisticas() {
        System.out.println("\n=== ANÁLISIS DE COMPLEJIDAD ===");
        System.out.println("┌─────────────────┬──────────┬──────────┐");
        System.out.println("│ Operación       │ Promedio │ Peor     │");
        System.out.println("├─────────────────┼──────────┼──────────┤");
        System.out.println("│ Árbol B+ Insert │ O(log n) │ O(log n) │");
        System.out.println("│ Árbol B+ Search │ O(log n) │ O(log n) │");
        System.out.println("│ Hash Insert     │ O(1)     │ O(n)     │");
        System.out.println("│ QuickSort       │ O(n lg n)│ O(n²)    │");
        System.out.println("│ MergeSort       │ O(n lg n)│ O(n lg n)│");
        System.out.println("│ Búsq. Binaria   │ O(log n) │ O(log n) │");
        System.out.println("│ Búsq. Secuenc.  │ O(n)     │ O(n)     │");
        System.out.println("└─────────────────┴──────────┴──────────┘");
    }
    
    private static void demoAlgoritmos() {
        com.motor.algorithms.SearchAlgorithms.demo();
        com.motor.algorithms.SortAlgorithms.demo();
    }
    
    private static void cargarDatosEjemplo() {
        Collection estudiantes = database.createCollection("estudiantes");
        
        String[][] datos = {
            {"1", "nombre=Juan,edad=20,carrera=Sistemas"},
            {"2", "nombre=Maria,edad=21,carrera=Informatica"},
            {"3", "nombre=Carlos,edad=19,carrera=Sistemas"},
            {"4", "nombre=Ana,edad=22,carrera=Computacion"},
            {"5", "nombre=Pedro,edad=20,carrera=Informatica"}
        };
        
        for (String[] d : datos) {
            Document doc = new Document(d[0]);
            String[] pares = d[1].split(",");
            for (String par : pares) {
                String[] kv = par.split("=");
                doc.put(kv[0], kv[1]);
            }
            estudiantes.insert(doc);
        }
        
        System.out.println("Colección 'estudiantes' creada con " + estudiantes.size() + " documentos");
    }
}