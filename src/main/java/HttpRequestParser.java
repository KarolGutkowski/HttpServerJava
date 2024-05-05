
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpRequestParser {
    private static List<String> accepted_methods = Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "TRACE", "CONNECT");
    private Boolean valid_request;
    private RequestLine request;
    private List<String> request_headers_list;
    private RequestHeaders requestHeaders;
    private RequestBody request_body;
    private Boolean has_body = false;
    private Integer body_length;

    public HttpRequestParser(InputStream request_stream) {
        valid_request = true;
        var requestReader = new BufferedReader(new InputStreamReader(request_stream));
        parseRequestLine(requestReader);

        if(!valid_request)
            return;

        parseRequestHeaders(requestReader);
        if(!valid_request)
            return;

        parseRequestBody(requestReader);
        if(!valid_request)
            return;

        requestHeaders = new RequestHeaders(request_headers_list);
    }

    private void parseRequestBody(BufferedReader requestReader) {
        if(has_body) {
            var bodyStringBuilder = new StringBuilder();
            var read_length = body_length;
            while (read_length > 0) {
                try {
                    char[] buf = new char[read_length];
                    var actually_read = requestReader.read(buf,0 , read_length);
                    read_length -= actually_read;

                    bodyStringBuilder.append(buf);
                }catch (IOException ioException) {
                    invalidateRequest();
                    return;
                }
            }

            request_body = new RequestBody(bodyStringBuilder.toString());
        }
    }

    // for now handle simple case of just reading line by line and assuming body doesn't span many lines
    private void parseRequestHeaders(BufferedReader requestReader) {
        try {
            request_headers_list = new ArrayList<>();
            String current_line;
            while (!(current_line = requestReader.readLine()).isEmpty()) {
                request_headers_list.add(current_line);
                determine_if_indicates_content(current_line);
            }
        }catch (IOException exception) {
            invalidateRequest();
        }
    }

    private void determine_if_indicates_content(String line) {
        var content_length_string = "Content-Length";
        if(line.startsWith("Content-Length")) {
            has_body = true;

            var contentLengthValueString = line.substring(content_length_string.length()+1);
            body_length = Integer.valueOf(contentLengthValueString.trim());
            System.out.println("body length is " + body_length);
        }
    }

    private void parseRequestLine(BufferedReader requestReader) {
        try {
            String request_line = requestReader.readLine();

            if(request_line == null || request_line.isEmpty()) {
                invalidateRequest();
                return;
            }

            var first_space_position = request_line.indexOf(WebConstants.SP);
            if(first_space_position == -1) {
                invalidateRequest();
                return;
            }

            var request_method = request_line.substring(0, first_space_position);
            if (!accepted_methods.contains(request_method)) {
                invalidateRequest();
                return;
            }

            var rest_of_request = request_line.substring(first_space_position + 1);
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

    public RequestBody getRequest_body() {
        return request_body;
    }

    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public RequestLine getRequestLine() {
        return request;
    }

    private void invalidateRequest() {
        valid_request = false;
    }



}

