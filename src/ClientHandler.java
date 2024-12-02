package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            
        }
    }

    @Override
    public void run() {

        try {

            System.out.println(
                    "Novo cliente conectado: " + this.socket.getInetAddress().getHostAddress().toString()
                            + " at port " + this.socket.getPort());

            try (
                    PrintWriter out = new PrintWriter(this.socket.getOutputStream());
                    BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));) {

                String message = in.readLine();

                String responseBody = "<html><body><h3>Olá, mundo!</h3></body></html>";

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html; charset=UTF-8");
                out.println("Content-Length: " + responseBody.length());
                out.println();
                out.println(responseBody);
                out.flush();
            }
            
        } catch (IOException e) {

        } finally {
            this.close();
        }
    }

    
}
