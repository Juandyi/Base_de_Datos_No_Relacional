package com.motor.algorithms;

/**
 * ALGORITMOS DE BÚSQUEDA
 * 
 * Tipos de algoritmos implementados:
 * 1. Búsqueda Secuencial (Fuerza Bruta)
 * 2. Búsqueda Binaria (Divide y Vencerás)
 */
public class SearchAlgorithms {
    
    /**
     * BÚSQUEDA SECUENCIAL (Fuerza Bruta)
     * 
     * Estrategia: Recorrer todos los elementos uno por uno.
     * 
     * Complejidad:
     * - Mejor caso: O(1) - elemento en primera posición
     * - Caso promedio: O(n/2) = O(n)
     * - Peor caso: O(n) - elemento al final o no existe
     * 
     * Ventajas: Funciona en arreglos no ordenados
     * Desventajas: Lento para grandes volúmenes
     */
    public static <T extends Comparable<T>> int busquedaSecuencial(T[] arr, T target) {
        // Operación elemental: comparación
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].compareTo(target) == 0) {
                return i;  // Encontrado
            }
        }
        return -1;  // No encontrado
    }
    
    /**
     * BÚSQUEDA BINARIA (Divide y Vencerás - Iterativa)
     * 
     * Estrategia: Dividir el espacio de búsqueda a la mitad en cada paso.
     * 
     * Recurrencia: T(n) = T(n/2) + O(1)
     * 
     * Complejidad:
     * - Mejor caso: O(1) - elemento en el medio
     * - Caso promedio: O(log n)
     * - Peor caso: O(log n)
     * 
     * Requisito: El arreglo DEBE estar ordenado
     * Ventajas: Muy eficiente para grandes volúmenes
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] arr, T target) {
        int izq = 0;
        int der = arr.length - 1;
        
        while (izq <= der) {
            int mid = izq + (der - izq) / 2;  // Evita overflow
            
            int cmp = arr[mid].compareTo(target);
            
            if (cmp == 0) {
                return mid;      // Encontrado
            } else if (cmp < 0) {
                izq = mid + 1;   // Buscar en mitad derecha
            } else {
                der = mid - 1;   // Buscar en mitad izquierda
            }
        }
        return -1;  // No encontrado
    }
    
    /**
     * BÚSQUEDA BINARIA RECURSIVA
     * 
     * Misma lógica pero usando recursión.
     * Útil para entender la recurrencia T(n) = T(n/2) + O(1)
     */
    public static <T extends Comparable<T>> int busquedaBinariaRecursiva(T[] arr, T target, int izq, int der) {
        // Caso base
        if (izq > der) {
            return -1;
        }
        
        int mid = izq + (der - izq) / 2;
        int cmp = arr[mid].compareTo(target);
        
        if (cmp == 0) {
            return mid;
        } else if (cmp < 0) {
            return busquedaBinariaRecursiva(arr, target, mid + 1, der);  // T(n/2)
        } else {
            return busquedaBinariaRecursiva(arr, target, izq, mid - 1);  // T(n/2)
        }
    }
    
    /**
     * Demostración de los algoritmos
     */
    public static void demo() {
        System.out.println("\n=== DEMOSTRACIÓN: ALGORITMOS DE BÚSQUEDA ===");
        
        Integer[] datos = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        Integer objetivo = 70;
        
        System.out.println("Arreglo: [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]");
        System.out.println("Buscando: " + objetivo);
        
        // Búsqueda Secuencial
        long inicio = System.nanoTime();
        int resultadoSeq = busquedaSecuencial(datos, objetivo);
        long tiempoSeq = System.nanoTime() - inicio;
        System.out.println("\nBúsqueda Secuencial:");
        System.out.println("  Posición: " + resultadoSeq);
        System.out.println("  Tiempo: " + tiempoSeq + " ns");
        System.out.println("  Complejidad: O(n)");
        
        // Búsqueda Binaria
        inicio = System.nanoTime();
        int resultadoBin = busquedaBinaria(datos, objetivo);
        long tiempoBin = System.nanoTime() - inicio;
        System.out.println("\nBúsqueda Binaria:");
        System.out.println("  Posición: " + resultadoBin);
        System.out.println("  Tiempo: " + tiempoBin + " ns");
        System.out.println("  Complejidad: O(log n)");
    }
}
