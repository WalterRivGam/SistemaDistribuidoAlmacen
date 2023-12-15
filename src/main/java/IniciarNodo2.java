
import java.util.HashMap;
import java.util.Map;

public class IniciarNodo2 {

    private static Map<Integer, Producto> productosAlmacen = new HashMap<>();

    public static void main(String[] args) {
        
        Producto.crearProductosIniciales(productosAlmacen);

        int puerto = 5001;
        Estado estado = Estado.SEGUIDOR;

        new Thread(new AppSeguidor(productosAlmacen, puerto, estado, 2)).start();

    }

}
