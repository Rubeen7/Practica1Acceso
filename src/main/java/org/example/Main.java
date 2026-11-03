package org.example;

import org.example.ui.Menu;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Buscamos estas carpetas desde donde se inicia el programa
        File origen = new File("src/main/resources/images");
        File backups = new File("target/backups");

        new Menu(origen, backups).ejecutar();
    }
}
