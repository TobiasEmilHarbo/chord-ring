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
    String predecessor = this.node.getPredecessor().toJson();
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, predecessor.length());
    OutputStream responseBody = exchange.getResponseBody();
    responseBody.write(predecessor.getBytes());
  }

  @Override
  public void post(HttpExchange exchange, String data) throws IOException {
    this.node.notify(NodePointer.fromJson(data));
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
  }
}