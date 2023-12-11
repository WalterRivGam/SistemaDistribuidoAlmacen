
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

public class Conector implements Callable<Socket> {
    
    private int puerto;

    public Conector(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public Socket call() throws Exception {
        ServerSocket serverSocket = new ServerSocket(puerto);
        System.out.println("Lider escuchando en el puerto " + puerto + "...");
        Socket conexion = serverSocket.accept();
        System.out.println("Conexión de lider con seguidor establecida en el puerto " + puerto);
        return conexion;
    }



}
