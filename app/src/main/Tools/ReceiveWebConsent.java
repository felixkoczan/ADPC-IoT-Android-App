import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Controller class for handling HTTP requests.
 * Annotated with @RestController, indicating it's a web controller and its methods return values are automatically written into the response body.
 */
@RestController
public class RequestController {

    /**
     * Method to process POST requests sent to the "/process-headers" path.
     * It extracts headers from the request and prints them to the console.
     *
     * @param headers A map containing all headers from the incoming HTTP request.
     *                The @RequestHeader annotation is used to automatically populate this map.
     * @return Returns a confirmation string as a response to the client.
     */
    @PostMapping("/process-headers")
    public String processHeaders(@RequestHeader Map<String, String> headers) {
        // Iterates over the header map and prints each key-value pair.
        headers.forEach((key, value) -> System.out.println(key + ": " + value));

        // Returns a simple confirmation message as the response body.
        return "Headers processed successfully";
    }
}
