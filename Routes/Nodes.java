package Routes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import Services.Node;
import Services.NodePointer;

public class Nodes extends Route {

  private Node node;

  public Nodes(Node node) {
    this.node = node;
  }

  @Override
  public void post(HttpExchange exchange, String data) throws IOException {

    long stamp = System.currentTimeMillis();

    System.out.print("N#" + this.node.getId() + ": ");
    System.out.println("Nodes POST " + stamp);

    NodePointer joiningNode = NodePointer.fromJson(data);
    NodePointer newSuccessor = this.node.findSuccessor(joiningNode);

    System.out.print("N#" + this.node.getId() + ": ");
    System.out.println("Successor found for #" + joiningNode.getId() + ": #" + newSuccessor.getId());

    // if(newSuccessor.pointsTo(this.node)) {
    //   this.node.setPredecessor(joiningNode);
    //   joiningNode.notify(this.node);
    // }
    
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, newSuccessor.toJson().length());
    OutputStream responseBody = exchange.getResponseBody();
    responseBody.write(newSuccessor.toJson().getBytes());

    System.out.println("END Nodes POST " + stamp);
  }

  @Override
  public void get(HttpExchange exchange, Map<String, String> query) throws IOException {
    
    long stamp = System.currentTimeMillis();

    System.out.print("N#" + this.node.getId() + ": ");
    System.out.println("Successors GET "+ stamp);

    this.node.stabilize();

    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    
    System.out.println("END GET "+ stamp);
  }
}