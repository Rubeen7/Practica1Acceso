package org.example.ui;

import java.io.File;
import java.io.IOException;

public interface OpcionBackup {
    void ejecutar() throws IOException;

    default void mostrarResultado(File carpeta, int cantidad) {
        System.out.println("Backup creado en: " + carpeta.getAbsolutePath());
        System.out.println("Archivos copiados: " + cantidad);
    }
}
