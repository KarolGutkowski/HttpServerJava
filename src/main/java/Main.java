import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

     //Uncomment this block to pass the first stage


     try {
         ServerSocket server = new ServerSocket(4221);
         server.setReuseAddress(true); // important so you can restart on the same port without problems

         while (true) {
             Socket client = server.accept();

             var clientWriter = new PrintWriter(client.getOutputStream());

             var clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

             String request_lines;
             List<String> accepted_methods = Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH");

             request_lines = clientReader.readLine();
             var accepted_method = false;
             String found_method;
             for (var method : accepted_methods) {
                 if (request_lines.startsWith(method)) {
                     accepted_method = true;

                     var path_start_position = request_lines.indexOf(method);
                     var trimmed_request = request_lines.substring(path_start_position + method.length());
                     var http_version_start = trimmed_request.indexOf("HTTP");

                     var path = trimmed_request.substring(0, http_version_start);
                     System.out.println(path);

                     HttpResponse response;
                     if (path.trim().equals("/")) {
                         response = new HttpResponse("1.1", 200, "OK");
                     } else {
                         response = new HttpResponse("1.1", 404, "Not found");
                     }
                     clientWriter.println(response);
                     break;
                 }
             }

             clientWriter.close();
             clientReader.close();
             client.close();
         }

     } catch (IOException e) {
         System.err.printf("Server failed while running %s \n", e.getMessage());
     }
  }
}
