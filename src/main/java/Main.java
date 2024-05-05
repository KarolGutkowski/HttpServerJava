import java.io.*;
import java.net.ServerSocket;
public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    var directory = "";
    for(int i=0;i<args.length;i++) {
        if(args[i].equals("--directory")) {
            if(i+1 < args.length){
                directory = args[i+1];
                break;
            }
        }
    }

     //Uncomment this block to pass the first stage
     try {
         ServerSocket server = new ServerSocket(4221);
         server.setReuseAddress(true); // important so you can restart on the same port without problems

         while (true) {
             // important to call start instead of run, so it spins a new threads instead
             new ClientHandler(server.accept(), directory).start();
         }

     } catch (IOException e) {
         System.err.printf("Server failed while running %s \n", e.getMessage());
     }
  }
}
