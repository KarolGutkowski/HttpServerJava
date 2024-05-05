import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class RequestHandler {

    private final RequestLine requestLine;
    private final RequestHeaders requestHeaders;
    private final RequestBody requestBody;
    private final String directory;
    public RequestHandler(RequestLine _requestLine,
                          RequestHeaders _requestHeaders,
                          RequestBody _requestBody,
                          String _directory) {
        requestLine = _requestLine;
        requestBody = _requestBody;
        requestHeaders = _requestHeaders;
        directory = _directory;
    }

    public HttpResponse produceResponse(Boolean is_request_valid) {
        HttpResponse response = new HttpResponse("1.1", 404, "Not Found");

        if(!is_request_valid) {
            response = new HttpResponse("1.1", 400, "Bad Request");
        }else if (requestLine.uri().equals("/")) {
            response = new HttpResponse("1.1", 200, "OK");
        }else if ( requestLine.uri().startsWith("/echo/")){
            var response_body = requestLine.uri().substring("/echo/".length());
            response = new HttpResponse("1.1", 200, "OK", response_body, "text/plain");
        }else if(requestLine.uri().equals("/user-agent")) {
            String response_body = "";
            String user_agent = "User-Agent";
            if(requestHeaders.headerPresent(user_agent))
                response_body = requestHeaders.getHeader(user_agent);
            response = new HttpResponse("1.1", 200, "OK", response_body, "text/plain");
        } else if (requestLine.uri().startsWith("/files") && requestLine.httpMethod().equals("GET")) {
            var filename = requestLine.uri().substring("/files/".length());
            if(!filename.isEmpty()) {
                var file = directory.isEmpty() ? new File(filename): new File(directory, filename);
                if(file.exists())
                    System.out.println("file exists");
                try {
                    var file_bytes = Files.readAllBytes(file.toPath());
                    response = new HttpResponse("1.1",
                            200,
                            "OK",
                            Arrays.toString(file_bytes),
                           "application/octet-stream");
                }catch (IOException exception) {
                    response = new HttpResponse("1.1", 500, "Internal Error");
                    System.err.println("Error while attempting to read bytes of a file: " + exception.getMessage());
                }
            }
        }

        return response;
    }

}

