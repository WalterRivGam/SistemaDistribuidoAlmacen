
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AppLider implements Runnable {

    private Map<Integer, Producto> productosAlmacen;
    private int puerto;
    private static int idProd = 100000;
    private Estado estado;
    private int idNodo = 1;
    private int idLider = -1;
    private int voto = -1;
    private int periodo = 1;
    private List<Socket> conexiones = new LinkedList<>();
    private Socket conexion;
    private List<String> registroDeComandos = new ArrayList<>();
    private int nroSeguidores = 4;

    public AppLider(Map<Integer, Producto> productosAlmacen, int puerto, Estado estado, int idNodo) {
        this.productosAlmacen = productosAlmacen;
        this.puerto = puerto;
        this.estado = estado;
        this.idNodo = idNodo;
    }

    @Override
    public void run() {

        int nroConexionesEst = establecerConexiones();
        
        System.out.println("El lider estableció " + nroConexionesEst + " conexiones.");

        String nombreArchivo = "BD" + idNodo + ".txt";
        File file = new File(nombreArchivo);

        if (!file.exists()) {
            Archivo.crear(nombreArchivo);
        }

        System.out.printf("Lider escuchando a cliente en el puerto %d...\n", puerto);

        try (ServerSocket serverSocket = new ServerSocket(puerto); Socket socket = serverSocket.accept(); InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream(); Scanner scanner = new Scanner(inputStream); PrintWriter printWriter = new PrintWriter(outputStream, true);) {

            System.out.println("Conexión establecida con el cliente");
            
            while (scanner.hasNextLine()) {
                String comando = scanner.nextLine();
                
                System.out.println("Lider recibió comando:");
                System.out.println(comando);

                registroDeComandos.add(comando);

                int respuestasOK = 0;

                List<Future<String>> resultados = new ArrayList<>();

                for (int i = 0; i < conexiones.size(); i++) {
                    ManejadorNodo manejador = new ManejadorNodo(conexiones.get(i), comando);
                    FutureTask<String> tarea = new FutureTask<>(manejador);
                    resultados.add(tarea);
                    Thread thread = new Thread(tarea);
                    thread.start();
                }

                for (Future<String> resultado : resultados) {
                    try {
                        String respuesta = resultado.get(1, TimeUnit.SECONDS);
                        if (respuesta.equals("OK")) {
                            respuestasOK++;
                        }

                    } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                        System.out.println(ex);
                    }
                }

                System.out.println(respuestasOK + " registraron el comando");

                if (respuestasOK > 2) {
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
                            printWriter.println(jsonObj);
                            break;
                        }
                        case "READ": {
                            JSONArray productosJSON = jsonObj.getJSONArray("products");
                            JSONObject productoJSON = productosJSON.getJSONObject(0);
                            int id = productoJSON.getInt("id");
                            Producto prod = productosAlmacen.get(id);
                            productoJSON = new JSONObject(prod);
                            productosJSON.put(0, productoJSON);
                            printWriter.println(jsonObj);
                            break;
                        }
                        case "READALL":
                            JSONArray prodsJSON = new JSONArray(productosAlmacen.values());
                            jsonObj.put("products", prodsJSON);
                            printWriter.println(jsonObj);
                            break;
                        case "UPDATE": {
                            JSONArray productosJSON = jsonObj.getJSONArray("products");
                            JSONObject productoJSON = productosJSON.getJSONObject(0);
                            Producto producto = new Producto(productoJSON);
                            productosAlmacen.put(producto.getId(), producto);
                            printWriter.println(jsonObj);
                            break;
                        }
                        case "DELETE": {
                            JSONArray productosJSON = jsonObj.getJSONArray("products");
                            JSONObject productoJSON = productosJSON.getJSONObject(0);
                            int id = productoJSON.getInt("id");
                            Producto prod = productosAlmacen.remove(id);
                            productoJSON = new JSONObject(prod);
                            productosJSON.put(0, productoJSON);
                            printWriter.println(jsonObj);
                            break;
                        }
                        default:
                            break;
                    }

                    Archivo.guardar("BD" + idNodo + ".txt", productosAlmacen);

                    respuestasOK = 0;

                    resultados = new ArrayList<Future<String>>();

                    for (int i = 0; i < conexiones.size(); i++) {
                        ManejadorNodo manejador = new ManejadorNodo(conexiones.get(i), "ejecutar");
                        FutureTask<String> tarea = new FutureTask<String>(manejador);
                        resultados.add(tarea);
                        Thread thread = new Thread(tarea);
                        thread.start();
                    }

                    for (Future<String> resultado : resultados) {
                        try {
                            String respuesta = resultado.get(1, TimeUnit.SECONDS);
                            if (respuesta.equals("OK")) {
                                respuestasOK++;
                            }

                        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                            System.out.println(ex);
                        }
                    }

                    System.out.println(respuestasOK + " ejecutaron el comando");

                } else {
                    printWriter.println("Error interno del servidor");
                }

            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private int establecerConexiones() {
        
        int nroConexiones = 0;
        
        List<Future<Socket>> resultados = new ArrayList<>();

        for (int i = 0; i < nroSeguidores; i++) {
            Conector conector = new Conector(5102 + i);
            FutureTask<Socket> tarea = new FutureTask<>(conector);
            resultados.add(tarea);
            Thread thread = new Thread(tarea);
            thread.start();
        }

        for (Future<Socket> resultado : resultados) {
            try {
                Socket conn = resultado.get(120, TimeUnit.SECONDS);
                conexiones.add(conn);
                nroConexiones++;

            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                System.out.println(ex);
            }
        }

        return nroConexiones;

    }
}
