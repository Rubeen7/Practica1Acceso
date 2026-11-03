package org.example.ui;

import java.io.File;
import java.io.IOException;

// Usamos una interfaz porque las dos opciones deben poder ejecutarse,
// pero cada una hace una copia distinta. Aquí indicamos lo que tienen en común.
public interface                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 OpcionBackup {
    // Cada opción explica aquí cómo hacer su copia
    void ejecutar() throws IOException;

    // Con default, las dos opciones usan el mismo mensaje de resultado
    default void mostrarResultado(File carpeta, int cantidad) {
        System.out.println("Backup creado en: " + carpeta.getAbsolutePath());
        System.out.println("Archivos copiados: " + cantidad);
    }
}
