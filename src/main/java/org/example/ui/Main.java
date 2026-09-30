package org.example.ui;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File origen = new File("src/main/resources/images");
        File backups = new File("target/backups");

        new Menu(origen, backups).ejecutar();
    }
}