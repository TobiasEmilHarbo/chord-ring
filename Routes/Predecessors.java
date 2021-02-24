package Routes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import Services.Node;
import Services.NodePointer;

import com.sun.net.httpserver.HttpExchange;

public class Predecessors extends Route {
  private Node node;

  public Predecessors(Node node) {
    this.node = node;
  }

  @Override
  public void get(HttpExchange exchange, Map<String, String> query) throws IOException {

    long stamp = System.currentTimeMillis();

    System.out.print("N#" + this.node.getId() + ": ");
    System.out.println("Predecessor GET " + stamp);

    String predecessor = this.node.getPredecessor().toJson();
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, predecessor.length());
    OutputStream responseBody = exchange.getResponseBody();
    responseBody.write(predecessor.getBytes());

    System.out.println("END GET " + stamp); 
  }

  @Override
  public void post(HttpExchange exchange, String data) throws IOException {

    long stamp = System.currentTimeMillis();

    System.out.print("N#" + this.node.getId() + ": ");
    System.out.println("Predecessor POST " + stamp);
    
    this.node.notify(NodePointer.fromJson(data));
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

    System.out.println("END POST " + stamp); 
  }
}
