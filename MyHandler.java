import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class MyHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange t) throws IOException {
    byte[] response = "Welcome Real's HowTo test page".getBytes();
    t.sendResponseHeaders(200, response.length);
    OutputStream os = t.getResponseBody();
    os.write(response);
    os.close();
  }
}