
import java.util.HashMap;
import java.util.Map;

public class IniciarNodo5 {

    private static Map<Integer, Producto> productosAlmacen = new HashMap<>();

    public static void main(String[] args) {

        int puerto = 5004;
        Estado estado = Estado.SEGUIDOR;

        new Thread(new AppSeguidor(productosAlmacen, puerto, estado, 5)).start();

    }

}
