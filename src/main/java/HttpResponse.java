import jdk.jfr.Unsigned;

import javax.swing.text.html.Option;
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


    @Override
    public String toString() {
        var line_separator = "\r\n";
        var response_message = new StringBuilder();
        response_message.append("HTTP/").append(http_version).append(" ");
        response_message.append(status_code).append(" ").append(reason_phrase);
        response_message.append(line_separator);
        if(headers.isPresent()) {
            for (var header: headers.get()) {
                response_message.append(header);
            }
        }
        response_message.append(line_separator);

        if(body_message.isPresent()) {
            response_message.append(body_message);
        }
        response_message.append(line_separator);
        return response_message.toString();
    }
}