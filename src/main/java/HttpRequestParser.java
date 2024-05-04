
// from documentation here is a BNF for HTTP Request
//  Request  = Request-Line              ; Section 5.1
//             *(( general-header        ; Section 4.5
//             | request-header         ; Section 5.3
//             | entity-header ) CRLF)  ; Section 7.1
//             CRLF
//             [ message-body ]
// where:
//  Request-Line   = Method SP Request-URI SP HTTP-Version CRLF
// for method i dont yet understand n method meaning so we can say my server supporst only standard methods :)
//  Method         = "OPTIONS"                ; Section 9.2
//                      | "GET"                    ; Section 9.3
//                      | "HEAD"                   ; Section 9.4
//                      | "POST"                   ; Section 9.5
//                      | "PUT"                    ; Section 9.6
//                      | "DELETE"                 ; Section 9.7
//                      | "TRACE"                  ; Section 9.8
//                      | "CONNECT"                ; Section 9.9
//                      | extension-method

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class HttpRequestParser {
    private static List<String> accepted_methods = Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "TRACE", "CONNECT");
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
            var first_space_postion = request_line.indexOf(WebConstants.SP);

            var request_method = request_line.substring(0, first_space_postion);
            if (!accepted_methods.contains(request_method)) {
                invalidateRequest();
                return;
            }

            var rest_of_request = request_line.substring(first_space_postion + 1);
            var second_space_position = rest_of_request.indexOf(WebConstants.SP);
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

