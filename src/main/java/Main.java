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
             var request_parser = new HttpRequestParser(client.getInputStream());

             var handler = new RequestHandler(request_parser.getRequestLine(),
                     request_parser.getRequestHeaders(),
                     request_parser.getRequest_body());

             var response = handler.produceResponse(request_parser.isRequestValid());

             clientWriter.print(response);

             clientWriter.close();
             client.close();
         }

     } catch (IOException e) {
         System.err.printf("Server failed while running %s \n", e.getMessage());
     }
  }
}
