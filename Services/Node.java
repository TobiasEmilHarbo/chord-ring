package Services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpServer;

public class Node {
  private int id;
  private int bit;
  private Http http;
  private HttpServer server;
  private NodePointer pointer;
  private NodePointer _predecessor;
  private ArrayList<NodePointer> fingerTable;

  public Node(String host, int port, int bit, Http http) throws IOException {
    this.id = port;
    this.bit = bit;
    this.http = http;

    this.pointer = new NodePointer(this.id, host + ":" + port);
    this._predecessor = this.pointer;
    
    this.initializeFingerTable();
    this.initializeServer(port);
  }

  public Node(String host, Integer port) throws IOException {
    this(host, port, 8, new Http());
  }

  public int getId() {
    return id;
  }

  public void setSuccessor(NodePointer successor) {
    this.fingerTable.set(0, successor);
    System.out.print("N#" + this.getId() + ": ");
    System.out.println("New successor: #" + this.getSuccessor().getId());
  }

  public NodePointer getSuccessor() {
    return this.fingerTable.get(0);
  }

  public void setPredecessor(NodePointer predecessor) {
    this._predecessor = predecessor;
    System.out.print("N#" + this.getId() + ": ");
    System.out.println("New predecessor: #" + this.getPredecessor().getId());
  }

  public NodePointer getPredecessor() {
    return this._predecessor;
  }

  private void initializeFingerTable() {
    System.out.print("N#" + this.getId() + ": ");
    System.out.println("init finger table");

    this.fingerTable = new ArrayList<NodePointer>(this.bit);
    for (int i = 0; i < this.bit; i++) {
      // System.out.println("Fingers" + ((this.id + Math.pow(2, i-1)) % Math.pow(2, this.bit)));
      this.fingerTable.add(i, this.pointer);
    }
  }
 
  // private int getHash(String str) {
	// 	final int p = 16777619;
	// 	int hash = (int) 2166136261L;
	// 	for (int i = 0; i < str.length(); i++)
	// 		hash = (hash ^ str.charAt(i)) * p;
	// 	hash += hash << 13;
	// 	hash ^= hash >> 7;
	// 	hash += hash << 3;
	// 	hash ^= hash >> 17;
	// 	hash += hash << 5;

	// 	// If the calculated value is negative, take its absolute value.
	// 	if (hash < 0)
	// 		hash = Math.abs(hash);
	// 	return hash;
  // }

  public void connectToNetwork(String address) throws IOException {
    
    System.out.print("N#" + this.getId() + ": ");
    System.out.println("Connect to network via "+ address);

    NodePointer successor = NodePointer.fromJson(
      this.http.post(address + "/nodes", this.pointer.toJson())
      );

    System.out.print("N#" + this.getId() + ": ");
    System.out.println("Successor found: " + successor.getId());

    this.setSuccessor(successor);
    // this.stabilize();

    // nodePointer.notify(this.pointer);
  }

  public void stabilize() throws IOException {
    System.out.print("N#" + this.getId() + ": ");
    // x = successor.predecessor
    // if x ∈ (n, successor) then
    //     successor := x
    // successor.notify(n)

    NodePointer successor = this.getSuccessor();
    NodePointer successorsPredecessor;

    if(!successor.pointsTo(this)) {
      successorsPredecessor = successor.getPredecessor();
    } else {
      successorsPredecessor = this.getPredecessor();
    }

    System.out.print("N#" + this.getId() + ": ");
    System.out.println("Stabilize. successorsPredecessor: #" + successorsPredecessor.getId());
    // (this.start < key && key <= maxId
      // || 0 <= key && key < this.stop);
    if(this.getId() < successorsPredecessor.getId() && successorsPredecessor.getId() <= 256 || 0 <= successorsPredecessor.getId() && successorsPredecessor.getId() < this.getSuccessor().getId()) {
      this.setSuccessor(successorsPredecessor);
    }
  }

  private void initializeServer(int port) throws IOException {
    this.server = HttpServer.create(new InetSocketAddress(port), 0);
    this.setupEndpoints();
    this.server.setExecutor(null);
    this.server.start();
  }

  private void setupEndpoints() {
    this.server.createContext("/nodes", new Routes.Nodes(this));
    this.server.createContext("/successors", new Routes.Successors(this));
    this.server.createContext("/predecessors", new Routes.Predecessors(this));
  }

  // public void addNode(String data) throws IOException {
  //   System.out.print("N#" + this.id() + ": ");
  //   System.out.println("add node " + data);

  //   if(!this.predecessor.pointsTo(this)) {
  //     this.predecessor.findSuccessor(data);
  //   }
  //   else {
  //     NodePointer np = this.findSuccessor(data);
  //     System.out.println("successor found " + np.id());
  //   }
  // }

  public NodePointer findSuccessor(int id) throws IOException {
    System.out.print("N#" + this.getId() + ": ");
    System.out.println("find successor for " + id);

    if(!this._predecessor.pointsTo(this)) {
      this._predecessor.findSuccessor(id);
    }

    // if id ∈ (n, successor] then
      // return successor

    if(id > this.id && id <= 256 || 0 <= id && id <= this.getSuccessor().getId()) {
      return this.getSuccessor(); 
    }
    return null;
  }

  public void notify(NodePointer pointer) {
    // if predecessor is nil or n'∈(predecessor, n) then
    // predecessor := n'
    if(pointer.getId() > this._predecessor.getId() || id < this.getId()) {
      this.setPredecessor(pointer);
    }
  }
}
  