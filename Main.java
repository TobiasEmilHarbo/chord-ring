import Services.Node;

public class Main {
  public static void main(String[] args) throws Exception {
   
    Node node1 = new Node("http://localhost", 10);
    Node node2 = new Node("http://localhost", 11);

    String connectionAddress = "http://localhost:10";
    node2.connectToNetwork(connectionAddress);
  }
}
