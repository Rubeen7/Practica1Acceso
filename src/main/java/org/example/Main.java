package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        MyFunctions funciones = new MyFunctions();
        String opcion = "";

        while (!opcion.equals("0")) {
            mostrarMenu();
            if (teclado.hasNextLine()) {
                opcion = teclado.nextLine().trim();
            } else {
                opcion = "0";
            }

            try {
                switch (opcion) {
                    case "1" -> mostrarResultado(funciones.crearCompleto());

                    case "2" -> mostrarResultado(funciones.crearIncremental());

                    case "0" -> System.out.println("Programa terminado.");

                    default -> System.out.println("Opción incorrecta. Introduce 0, 1 o 2.");
                }
            } catch (IOException e) {
                System.out.println("No se pudo crear el backup: " + e.getMessage());
            }
        }
        teclado.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n1. Crear backup completo");
        System.out.println("2. Crear backup incremental");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void mostrarResultado(ResultadoBackup resultado) {
        System.out.println("Backup creado en: " + resultado.getCarpeta().getAbsolutePath());
        System.out.println("Archivos copiados: " + resultado.getCantidad());
    }
}
