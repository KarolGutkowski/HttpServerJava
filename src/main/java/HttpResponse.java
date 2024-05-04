import jdk.jfr.Unsigned;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HttpResponse
{
    private String http_version = "1.1";
    private int status_code;
    private String reason_phrase;
    private Optional<String> body_message;
    private Optional<List<String>> headers;

    public HttpResponse(String _http_version,
                        int _status_code,
                        String _reason_phrase)
    {
        this.http_version = _http_version;
        this.status_code = _status_code;
        this.reason_phrase = _reason_phrase;
        this.body_message = Optional.empty();
        this.headers = Optional.empty();
    }

    public HttpResponse(String _http_version,
                        int _status_code,
                        String _reason_phrase,
                        String _response_body,
                        String content_type_header) {
        this(_http_version, _status_code, _reason_phrase);

        var content_type_header_whole = "Content-Type: " + content_type_header + WebConstants.CRLF;

        this.headers = Optional.of(Collections.singletonList(content_type_header_whole));
        this.body_message = Optional.of(_response_body);
    }

    @Override
    public String toString() {
        var response_message = new StringBuilder();
        response_message.append("HTTP/").append(http_version).append(" ");
        response_message.append(status_code).append(" ").append(reason_phrase);
        response_message.append(WebConstants.CRLF);
        if(headers.isPresent()) {
            for (var header: headers.get()) {
                response_message.append(header);
            }
        }
        response_message.append(WebConstants.CRLF);

        body_message.ifPresent(response_message::append);

        response_message.append(WebConstants.CRLF);
        return response_message.toString();
    }
}