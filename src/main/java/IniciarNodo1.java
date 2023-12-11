
import java.util.HashMap;
import java.util.Map;

public class IniciarNodo1 {

    private static Map<Integer, Producto> productosAlmacen = new HashMap<>();

    public static void main(String[] args) {

        int puerto = 5000;
        Estado estado = Estado.LIDER;

        new Thread(new AppLider(productosAlmacen, puerto, estado, 1)).start();

    }

}
