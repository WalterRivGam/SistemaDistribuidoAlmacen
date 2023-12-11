
import java.util.HashMap;
import java.util.Map;

public class IniciarNodo3 {

    private static Map<Integer, Producto> productosAlmacen = new HashMap<>();

    public static void main(String[] args) {

        int puerto = 5002;
        Estado estado = Estado.SEGUIDOR;

        new Thread(new AppSeguidor(productosAlmacen, puerto, estado, 3)).start();

    }

}
