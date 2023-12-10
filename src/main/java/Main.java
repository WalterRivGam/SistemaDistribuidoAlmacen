
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        new Thread(new AppAlmacen()).start();

        // prueba
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "CREATE");

        JSONArray prods = new JSONArray();

        JSONObject prod1 = new JSONObject();
        prod1.put("id", 1);
        prod1.put("name", "Televisor Samsung 98\"");
        prod1.put("desc", "Televisor Samsung Smart TV 98\" QLED 4K QN98Q80CAGXPE (2023)");
        prod1.put("price", 19999.00);
        prod1.put("quant", 15);
        prod1.put("img", "https://hiraoka.com.pe/media/catalog/product/1/3/130880_0.jpg?quality=80&bg-color=255,255,255&fit=bounds&height=&width=&canvas=:");

        prods.put(prod1);

        jsonObject.put("products", prods);

        try {
            Socket socket = new Socket("127.0.0.1", 1234);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);

            printWriter.println(jsonObject);

            boolean terminado = false;

            while (!terminado && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("\nProducto obtenido de CREATE");
                System.out.println(line);
                terminado = true;
            }
            
            ///////////CREATE//////////////

            JSONObject prod2 = new JSONObject();
            prod2.put("id", 1);
            prod2.put("name", "Parlante Harman\"");
            prod2.put("desc", "Parlante Harman Kardon Onyx Studio 8 Negro");
            prod2.put("price", 849.00);
            prod2.put("quant", 27);
            prod2.put("img", "https://hiraoka.com.pe/media/catalog/product/1/3/130284-1.jpg?quality=80&bg-color=255,255,255&fit=bounds&height=&width=&canvas=:");

            prods.clear();
            prods.put(prod2);
            
            jsonObject.put("products", prods);
            
            printWriter.println(jsonObject);

            terminado = false;

            while (!terminado && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("\nProducto obtenido de CREATE");
                System.out.println(line);
                terminado = true;
            }
            
            ///////////READ ALL//////////////

            jsonObject = new JSONObject();
            jsonObject.put("type", "READALL");

            prods = new JSONArray();

            prod1 = new JSONObject();
            prod1.put("id", 0);
            prod1.put("name", "");
            prod1.put("desc", "");
            prod1.put("price", 0);
            prod1.put("quant", 0);
            prod1.put("img", "");

            prods.put(prod1);

            jsonObject.put("products", prods);

            printWriter.println(jsonObject);

            terminado = false;

            while (!terminado && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("\nProducto obtenido de READALL");
                System.out.println(line);
                terminado = true;
            }
            
            ///////////READ//////////////
            
            jsonObject = new JSONObject();
            jsonObject.put("type", "READ");

            prods = new JSONArray();

            prod1 = new JSONObject();
            prod1.put("id", 100001);
            prod1.put("name", "");
            prod1.put("desc", "");
            prod1.put("price", 0);
            prod1.put("quant", 0);
            prod1.put("img", "");

            prods.put(prod1);

            jsonObject.put("products", prods);

            printWriter.println(jsonObject);

            terminado = false;

            while (!terminado && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("\nProducto obtenido de READ");
                System.out.println(line);
                terminado = true;
            }
            
            

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

}
