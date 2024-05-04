
// from documentation here is a BNF for HTTP Request
//  Request  = Request-Line              ; Section 5.1
//             *(( general-header        ; Section 4.5
//             | request-header         ; Section 5.3
//             | entity-header ) CRLF)  ; Section 7.1
//             CRLF
//             [ message-body ]
// where:
//  Request-Line   = Method SP Request-URI SP HTTP-Version CRLF

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class HttpRequestParser {
    private static List<String> accepted_methods = Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH");
    private static Character SP = ' ';
    private Boolean valid_request;
    private String request_line;
    private RequestLine request;

    public HttpRequestParser(InputStream request_stream) {
        valid_request = true;
        var requestReader = new BufferedReader(new InputStreamReader(request_stream));
        parseRequestLine(requestReader);
    }

    private void parseRequestLine(BufferedReader requestReader) {
        try {
            request_line = requestReader.readLine();
            var first_space_postion = request_line.indexOf(SP);

            var request_method = request_line.substring(0, first_space_postion);
            if (!accepted_methods.contains(request_method)) {
                invalidateRequest();
                return;
            }

            var rest_of_request = request_line.substring(first_space_postion + 1);
            var second_space_position = rest_of_request.indexOf(SP);
            var request_uri = rest_of_request.substring(0, second_space_position);
            var request_http_version = rest_of_request.substring(second_space_position + 1);

            request = new RequestLine(request_method, request_uri, request_http_version);

        } catch (IOException exception) {
            invalidateRequest();
        }
    }

    public boolean isRequestValid() {
        return valid_request;
    }

    public String getRequestUri() {
        return request.uri();
    }

    private void invalidateRequest() {
        valid_request = false;
    }



}

