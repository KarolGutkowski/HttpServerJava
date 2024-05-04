import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHeaders {
    private final HashMap<String, String> headers;
    public RequestHeaders(List<String> headers) {
        this.headers = new HashMap<String, String>();
        for(var header: headers) {
            var separator = ':';
            var separator_position = header.indexOf(separator);
            var field_name = header.substring(0, separator_position);
            var field_value = header.substring(separator_position+1).trim();
            this.headers.put(field_name, field_value);
        }
    }
    public Boolean headerPresent(String key) {
        return headers.containsKey(key);
    }
    public String getHeader(String key) {
        return headers.get(key);
    }
}
