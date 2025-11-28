package com.motor.utils;

/**
 * COMPLEXITYANALYZER - Documenta complejidades del motor
 * 
 * Notación Asintótica:
 * - O (Big-O): Cota superior - peor caso
 * - Ω (Omega): Cota inferior - mejor caso  
 * - Θ (Theta): Cota ajustada - caso exacto
 */
public class ComplexityAnalyzer {
    
    public static long measureTime(Runnable op) {
        long start = System.nanoTime();
        op.run();
        return System.nanoTime() - start;
    }
    
    public static void printAnalysis() {
        System.out.println("\n=== ANÁLISIS DE COMPLEJIDAD ===");
        System.out.println("┌─────────────────┬──────────┬──────────┬──────────┐");
        System.out.println("│ Estructura      │ Insertar │ Buscar   │ Eliminar │");
        System.out.println("├─────────────────┼──────────┼──────────┼──────────┤");
        System.out.println("│ Árbol B+        │ O(log n) │ O(log n) │ O(log n) │");
        System.out.println("│ Árbol B         │ O(log n) │ O(log n) │ O(log n) │");
        System.out.println("│ Hash Table      │ O(1)*    │ O(1)*    │ O(1)*    │");
        System.out.println("│ Lista Enlazada  │ O(1)     │ O(n)     │ O(n)     │");
        System.out.println("└─────────────────┴──────────┴──────────┴──────────┘");
        System.out.println("* = promedio, peor caso O(n)");
        
        System.out.println("\n┌─────────────────┬──────────────┬──────────────┐");
        System.out.println("│ Ordenamiento    │ Promedio     │ Peor Caso    │");
        System.out.println("├─────────────────┼──────────────┼──────────────┤");
        System.out.println("│ QuickSort       │ O(n log n)   │ O(n²)        │");
        System.out.println("│ MergeSort       │ O(n log n)   │ O(n log n)   │");
        System.out.println("│ BubbleSort      │ O(n²)        │ O(n²)        │");
        System.out.println("└─────────────────┴──────────────┴──────────────┘");
        
        System.out.println("\nTipos de Algoritmos:");
        System.out.println("• Divide y Vencerás: QuickSort, MergeSort, Búsq. Binaria");
        System.out.println("• Fuerza Bruta: BubbleSort, Búsqueda Secuencial");
    }
}
