
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class ManejadorNodo implements Callable<String> {

    private Socket conexion;
    private String mensaje;

    public ManejadorNodo() {
    }

    public ManejadorNodo(Socket conexion, String mensaje) {
        this.conexion = conexion;
        this.mensaje = mensaje;
    }

    @Override
    public String call() {
        String linea = "valor_por_defecto";
        try {
            OutputStream salida = conexion.getOutputStream();
            PrintWriter pw = new PrintWriter(salida, true);

            InputStream entrada = conexion.getInputStream();
            Scanner scanner = new Scanner(entrada);

            pw.println(mensaje);

            linea = scanner.nextLine();
            System.out.println("Se recibió de seguidor:" + linea);

        } catch (IOException ex) {
            System.out.println(ex);
        }
        System.out.println("Se retornó " + linea + " desde ManejadorNodo");
        return linea;
    }

}
