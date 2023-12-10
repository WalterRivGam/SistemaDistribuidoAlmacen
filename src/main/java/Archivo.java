
import java.io.File;
import java.io.IOException;
import java.util.Map;

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
}
