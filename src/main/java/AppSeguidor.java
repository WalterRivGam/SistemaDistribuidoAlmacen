
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class AppSeguidor implements Runnable {

    private Map<Integer, Producto> productosAlmacen;
    private int puerto;
    private static int idProd = 100000;
    private Estado estado;
    private int idNodo;
    private int idLider = -1;
    private int voto = -1;
    private int periodo = 1;
    private List<Socket> conexiones = new LinkedList<>();
    private Socket conexion;
    private List<String> registroDeComandos = new ArrayList<>();

    public AppSeguidor(Map<Integer, Producto> productosAlmacen, int puerto, Estado estado, int idNodo) {
        this.productosAlmacen = productosAlmacen;
        this.puerto = puerto;
        this.estado = estado;
        this.idNodo = idNodo;
    }

    @Override
    public void run() {
        establecerConexiones();

        String nombreArchivo = "BD" + idNodo + ".txt";
        File file = new File(nombreArchivo);

        if (!file.exists()) {
            Archivo.crear(nombreArchivo);
        }

        System.out.printf("Seguidor escuchando en el puerto %d...\n", 5100 + idNodo);

        try (InputStream inputStream = conexion.getInputStream(); OutputStream outputStream = conexion.getOutputStream(); Scanner scanner = new Scanner(inputStream); PrintWriter printWriter = new PrintWriter(outputStream, true);) {

            while (scanner.hasNextLine()) {
                String comando = scanner.nextLine();
                
                System.out.println("Comando recibido por nodo con ID:" + idNodo);
                System.out.println(comando);

                if (comando.equals("ejecutar")) {
                    comando = registroDeComandos.get(registroDeComandos.size() - 1);

                    JSONObject jsonObj = new JSONObject(comando);
                    String type = jsonObj.getString("type");

                    switch (type) {
                        case "CREATE": {
                            JSONArray productosJSON = jsonObj.getJSONArray("products");
                            JSONObject productoJSON = productosJSON.getJSONObject(0);
                            Producto producto = new Producto(productoJSON);
                            producto.setId(idProd);
                            productoJSON.put("id", idProd);
                            idProd++;
                            productosAlmacen.put(producto.getId(), producto);
                            printWriter.println("OK");
                            System.out.println("Nodo " + idNodo + " respondió con OK");
                            break;
                        }
                        case "READ": {
                            JSONArray productosJSON = jsonObj.getJSONArray("products");
                            JSONObject productoJSON = productosJSON.getJSONObject(0);
                            int id = productoJSON.getInt("id");
                            Producto prod = productosAlmacen.get(id);
                            productoJSON = new JSONObject(prod);
                            productosJSON.put(0, productoJSON);
                            printWriter.println("OK");
                            System.out.println("Nodo " + idNodo + " respondió con OK");
                            break;
                        }
                        case "READALL":
                            JSONArray prodsJSON = new JSONArray(productosAlmacen.values());
                            jsonObj.put("products", prodsJSON);
                            printWriter.println("OK");
                            System.out.println("Nodo " + idNodo + " respondió con OK");
                            break;
                        case "UPDATE": {
                            JSONArray productosJSON = jsonObj.getJSONArray("products");
                            JSONObject productoJSON = productosJSON.getJSONObject(0);
                            Producto producto = new Producto(productoJSON);
                            productosAlmacen.put(producto.getId(), producto);
                            printWriter.println("OK");
                            System.out.println("Nodo " + idNodo + " respondió con OK");
                            break;
                        }
                        case "DELETE": {
                            JSONArray productosJSON = jsonObj.getJSONArray("products");
                            JSONObject productoJSON = productosJSON.getJSONObject(0);
                            int id = productoJSON.getInt("id");
                            Producto prod = productosAlmacen.remove(id);
                            productoJSON = new JSONObject(prod);
                            productosJSON.put(0, productoJSON);
                            printWriter.println("OK");
                            System.out.println("Nodo " + idNodo + " respondió con OK");
                            break;
                        }
                        default:
                            break;
                    }

                    Archivo.guardar("BD" + idNodo + ".txt", productosAlmacen);
                } else {
                    registroDeComandos.add(comando);
                    printWriter.println("OK");
                    System.out.println("Nodo " + idNodo + " respondió con OK");
                }

            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private void establecerConexiones() {

        try {
            conexion = new Socket("127.0.0.1", 5100 + idNodo);
        } catch (IOException ex) {
            System.out.println("Error establecerConexiones() en seguidor con ID: " + idNodo);
            System.out.println(ex);
        }

    }
}
