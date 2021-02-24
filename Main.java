import Services.Node;

public class Main {
  public static void main(String[] args) throws Exception {
   
    new Node("http://localhost", 100);
    
    Node node2 = new Node("http://localhost", 120);
    // Node node3 = new Node("http://localhost", 140);
    // Node node4 = new Node("http://localhost", 180);
    // Node node5 = new Node("http://localhost", 200);

    String connectionAddress = "http://localhost:100";
    node2.connectToNetwork(connectionAddress);
    // node3.connectToNetwork(connectionAddress);
    // node4.connectToNetwork(connectionAddress);
    // node5.connectToNetwork(connectionAddress);
  }
}
