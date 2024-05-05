import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket client;
    private String directory;

    public ClientHandler(Socket socket, String _directory) {
        client = socket;
        directory = _directory;
    }

    @Override public void run() {
        try {
            var clientWriter = new PrintWriter(client.getOutputStream());
            var request_parser = new HttpRequestParser(client.getInputStream());

            var handler = new RequestHandler(request_parser.getRequestLine(),
                    request_parser.getRequestHeaders(),
                    request_parser.getRequest_body(),
                    directory);

            var response = handler.produceResponse(request_parser.isRequestValid());

            clientWriter.print(response);

            clientWriter.close();
            client.close();
        }catch (Exception exception) {
            System.err.println("Error while processing client request");
        }
    }
}
