package Routes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

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

    NodePointer joiningNode = NodePointer.fromJson(data);
    NodePointer newSuccessor = this.node.findSuccessor(joiningNode.getId());

    System.out.print("N#" + this.node.getId() + ": ");
    System.out.println("Successor found for #" + joiningNode.getId() + ": #" + newSuccessor.getId());

    if(newSuccessor.pointsTo(this.node)) {
      this.node.setPredecessor(joiningNode);
      this.node.stabilize();
    }
    
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, newSuccessor.toJson().length());
    OutputStream responseBody = exchange.getResponseBody();
    responseBody.write(newSuccessor.toJson().getBytes());
  }
}