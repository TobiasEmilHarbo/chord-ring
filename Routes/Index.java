package Routes;

import Services.Node;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class Index extends Route {

  private Node node;

  public Index(Node node) {
    this.node = node;
  }

  public void get(HttpExchange exchange, Map<String, String> query) throws IOException {  
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    OutputStream responseBody = exchange.getResponseBody();
    responseBody.write(("Node#"+this.node.getId()).getBytes());
  }
}
