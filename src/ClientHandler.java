package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Integer extractAlunoNumber(String url) {
        String regex = "/aluno/(\\d+)";
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
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

                String responseBody = "404";
                String message = in.readLine();
                System.out.println(message);
                if (message != null) {
                    if(this.getMethod(message) == "POST") {
                        try {
                            sh.createStudent(this);
                        } catch (InterruptedException e) {
                        }
                    }
                    else if (message.startsWith("GET")) {
                        try {
                            //System.out.println(sh.getStudentToHTML(extractAlunoNumber(message), this));
                            int studentId = extractAlunoNumber(message);
                            if (sh.studentExists(studentId, this))
                                responseBody = sh.getStudentToHTML(studentId, this);
                            else
                                responseBody = "<html><body><h3>Erro 404: Aluno " + extractAlunoNumber(message) + " não existe!<h3/><body/><html/>";
                            
                        } catch (InterruptedException e) {
                        }
                    }
                }

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
