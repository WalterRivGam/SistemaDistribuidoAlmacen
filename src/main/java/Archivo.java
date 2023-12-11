
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Archivo {

    public static void crear(String nombre_Archivo) {
        try {
            File archivo = new File(nombre_Archivo);
            if (archivo.createNewFile()) {
                System.out.println("Archivo creado: " + archivo.getName());
            } else {
                System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Ocurrio un error.");
            e.printStackTrace();
        }
    }

    public static void imprimir(String nombre_Archivo, Map<Integer, Producto> productos) {
        for (Producto producto : productos.values()) {
            System.out.println(producto);
        }
    }

    public static void guardar(String nombre_archivo, Map<Integer, Producto> productos) {
        try {
            FileWriter fileWriter = new FileWriter(nombre_archivo);

            for (Producto producto : productos.values()) {
                fileWriter.write(producto.toString());
            }
            
            fileWriter.close();

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }
}
