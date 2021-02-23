package Routes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import Services.Node;
import Services.NodePointer;

public class Successors extends Route {

  private Node node;

  public Successors(Node node) {
    this.node = node;
  }

  @Override
  public void get(HttpExchange exchange, Map<String, String> query) throws IOException {
    
    System.out.print("N#" + this.node.getId() + ": ");
    System.out.println("Successors GET");

    NodePointer successor = this.node.findSuccessor(Integer.parseInt(query.get("id")));

    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    
    OutputStream responseBody = exchange.getResponseBody();
    responseBody.write(successor.toJson().getBytes());
  }
}
