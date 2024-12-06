package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientProcessor implements Runnable {

    private Socket socket;
    private StudentHandler studentHandler;

    public ClientProcessor(Socket socket, StudentHandler sh) {
        this.socket = socket;
        this.studentHandler = sh;
    }

    private void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            
        }
    }

    public static Integer extractAlunoNumber(String url) {
        String regex = " /aluno/(\\d+) ";
        
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
                
                String responseCode = "200 OK";
                String responseBody = "";
                String message = in.readLine();
                if (message != null) {
                    if(message.startsWith("POST")) {
                        try {
                            studentHandler.createStudent(this);
                        } catch (InterruptedException e) {
                        }
                    }
                    else if (message.startsWith("GET")) {
                        try {
                            int studentId = extractAlunoNumber(message);
                            if (studentHandler.studentExists(studentId, this))
                                responseBody = studentHandler.getStudentToHTML(studentId, this);
                            else
                                responseBody = "<html><body><h3>Erro: Aluno " + extractAlunoNumber(message) + " nao existe!</h3></body></html>";
                                responseCode = "404 NOT FOUND";
                            
                        } catch (InterruptedException e) {
                        }
                    }
                    else if (message.startsWith("DELETE")) {
                        try {
                            int studentId = extractAlunoNumber(message);
                            if (studentHandler.studentExists(studentId, this)) {
                                studentHandler.deleteStudent(studentId, this);
                                responseBody = "<html><body><h3>Aluno: " + extractAlunoNumber(message) + ". deletado!</h3></body></html>";
                            }
                            else {
                                responseBody = ("<html><body><h3>Erro: Aluno " + extractAlunoNumber(message) + " nao existe!</h3></body></html>");
                                responseCode = "404 NOT FOUND";
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                }

                out.println("HTTP/1.1 " + responseCode);
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
