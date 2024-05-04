public class RequestHandler {

    private final RequestLine requestLine;
    private final RequestHeaders requestHeaders;
    private final RequestBody requestBody;
    public RequestHandler(RequestLine _requestLine, RequestHeaders _requestHeaders, RequestBody _requestBody) {
        requestLine = _requestLine;
        requestBody = _requestBody;
        requestHeaders = _requestHeaders;
    }

    public HttpResponse produceResponse(Boolean is_request_valid) {
        HttpResponse response = new HttpResponse("1.1", 404, "Not Found");

        if(!is_request_valid) {
            response = new HttpResponse("1.1", 400, "Bad Request");
        }
        else if (requestLine.uri().equals("/")) {
            response = new HttpResponse("1.1", 200, "OK");
        } else if ( requestLine.uri().startsWith("/echo/")){
            var response_body = requestLine.uri().substring("/echo/".length());
            response = new HttpResponse("1.1", 200, "OK", response_body, "text/plain");
        }  else if(requestLine.uri().equals("/user-agent")) {
            String response_body = "";
            String user_agent = "User-Agent";
            if(requestHeaders.headerPresent(user_agent))
                response_body = requestHeaders.getHeader(user_agent);
            response = new HttpResponse("1.1", 200, "OK", response_body, "text/plain");
        }

        return response;
    }

}

