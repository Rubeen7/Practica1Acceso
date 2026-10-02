package org.example.ui;

import java.io.File;
import java.io.IOException;

// Es una interfaz porque define lo que debe hacer cada opción, sin guardar estado común
// Así el menú puede usar ambas opciones y cada una implementa ejecutar a su manera
public interface OpcionBackup {
    void ejecutar() throws IOException;

    default void mostrarResultado(File carpeta, int cantidad) {
        System.out.println("Backup creado en: " + carpeta.getAbsolutePath());
        System.out.println("Archivos copiados: " + cantidad);
    }
}
