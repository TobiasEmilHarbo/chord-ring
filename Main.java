
public class Main {
  public static void main(String[] args) throws Exception {
   
    Node node1 = new Node(8001);
    Node node2 = new Node(8002);

    String connectionAddress = "http://localhost:8001";
    node2.connectToNetwork(connectionAddress);

  }
}
