package org.example.ui;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Menu {
    // El menú puede llamar a las dos opciones de la misma manera
    private final OpcionBackup backupCompleto;
    private final OpcionBackup backupIncremental;

    public Menu(File origen, File backups) {
        this.backupCompleto = new BackupCompletoUI(origen, backups);
        this.backupIncremental = new BackupIncrementalUI(origen, backups);
    }

    public void ejecutar() {
        Scanner teclado = new Scanner(System.in);
        String opcion = "";

        while (!opcion.equals("0")) {
            mostrarMenu();
            // Quitamos los espacios sobrantes y salimos si no hay más líneas para leer
            opcion = teclado.hasNextLine() ? teclado.nextLine().trim() : "0";

            try {
                switch (opcion) {
                    case "1" -> backupCompleto.ejecutar();
                    case "2" -> backupIncremental.ejecutar();
                    case "0" -> System.out.println("Programa terminado.");
                    default -> System.out.println("Opción incorrecta. Introduce 0, 1 o 2.");
                }
            } catch (IOException e) {
                // Mostramos el error y permitimos elegir otra opción
                System.out.println("No se pudo crear el backup: " + e.getMessage());
            }
        }
        teclado.close();
    }

    private void mostrarMenu() {
        System.out.println("\n1. Crear backup completo");
        System.out.println("2. Crear backup incremental");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }
}
