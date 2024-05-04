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
             new ClientHandler(server.accept()).start();
         }

     } catch (IOException e) {
         System.err.printf("Server failed while running %s \n", e.getMessage());
     }
  }
}
