import Services.Node;

public class Main {
  public static void main(String[] args) throws Exception {
   
    Node node1 = new Node("http://localhost", 10);
    Node node2 = new Node("http://localhost", 200);
    Node node3 = new Node("http://localhost", 100);
    Node node4 = new Node("http://localhost", 56);
    Node node5 = new Node("http://localhost", 25);

    String connectionAddress = "http://localhost:10";
    node2.connectToNetwork(connectionAddress);
    // node3.connectToNetwork(connectionAddress);
    // node4.connectToNetwork(connectionAddress);
    // node5.connectToNetwork(connectionAddress);
  }
}
