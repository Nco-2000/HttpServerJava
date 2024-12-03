package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private StudentHandler sh;

    public ClientHandler(Socket socket, StudentHandler sh) {
        this.socket = socket;
        this.sh = sh;
    }

    private void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            
        }
    }

    private String getMethod(String message) {
        if (message.startsWith("GET"))
            return "GET";
        else if (message.startsWith("POST"))
            return "POST";
        else if (message.startsWith("DELETE"))
            return "DELETE";
        return null;
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
                if (message != null) {
                    if(this.getMethod(message) == "POST") {
                        try {
                            sh.createStudent(this);
                        } catch (InterruptedException e) {
                        }
                    }
                }

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
