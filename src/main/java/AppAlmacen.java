
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class AppAlmacen implements Runnable {

    private Map<Integer, Producto> productosAlmacen = new HashMap<>();
    private int puerto = 1234;
    private static int idProd = 100000;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(puerto); Socket socket = serverSocket.accept(); InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream(); Scanner scanner = new Scanner(inputStream); PrintWriter printWriter = new PrintWriter(outputStream, true);) {

            System.out.printf("Escuchando en el puerto %d...\n", puerto);

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                JSONObject jsonObj = new JSONObject(linea);
                String type = jsonObj.getString("type");

                if (type.equals("CREATE")) {
                    JSONArray productosJSON = jsonObj.getJSONArray("products");
                    JSONObject productoJSON = productosJSON.getJSONObject(0);

                    Producto producto = new Producto(productoJSON);
                    producto.setId(idProd);
                    productoJSON.put("id", idProd);
                    idProd++;

                    productosAlmacen.put(producto.getId(), producto);

                    printWriter.println(jsonObj);

                } else if (type.equals("READ")) {
                    JSONArray productosJSON = jsonObj.getJSONArray("products");
                    JSONObject productoJSON = productosJSON.getJSONObject(0);
                    int id = productoJSON.getInt("id");
                    Producto prod = productosAlmacen.get(id);

                    productoJSON = new JSONObject(prod);
                    productosJSON.put(0, productoJSON);

                    printWriter.println(jsonObj);

                } else if (type.equals("READALL")) {
                    JSONArray prodsJSON = new JSONArray(productosAlmacen.values());
                    jsonObj.put("products", prodsJSON);

                    printWriter.println(jsonObj);

                }
                if (type.equals("UPDATE")) {
                    JSONArray productosJSON = jsonObj.getJSONArray("products");
                    JSONObject productoJSON = productosJSON.getJSONObject(0);

                    Producto producto = new Producto(productoJSON);

                    productosAlmacen.put(producto.getId(), producto);

                    printWriter.println(jsonObj);

                }

            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
