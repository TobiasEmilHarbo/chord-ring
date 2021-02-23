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

    String data = 
        "<html><body>" 
      + "<h2>"
      + "Node#"+this.node.getId()
      + "</h2>"
      + "\n"
      + "\n"
      + "Successor: "
      + "<a href=\"" + this.node.getSuccessor().getAddress() + "\">"
      + "#" + this.node.getSuccessor().getId()
      + "</a>"
      + "<br/>"
      + "Predecessor: "
      + "<a href=\"" + this.node.getPredecessor().getAddress() + "\">"
      + "#" + this.node.getPredecessor().getId()
      + "</a>"
      + "</body></html>"
      ;

    responseBody.write(data.getBytes());
  }
}
