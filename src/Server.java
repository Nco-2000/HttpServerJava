package src;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private int port;
    public StudentHandler sh;
    
    public Server() {
        this.port = 12345;
        sh = new StudentHandler();
    }

    public Server(int port) {
        this.port = port;
        sh = new StudentHandler();
    }

    public void start() throws IOException {

        try (ServerSocket server = new ServerSocket(this.port)) {
            
            System.out.println("Servidor iniciado na porta: " + this.port);

            while (true) { 
                new Thread(new ClientHandler(server.accept(), this.sh)).start();
            }
        }

        
    }
}
