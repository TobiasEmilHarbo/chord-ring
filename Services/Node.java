package Services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpServer;

public class Node {
  private int _id;
  private int bit;
  private Http http;
  private HttpServer server;
  private NodePointer _pointer;
  private NodePointer _predecessor;
  private ArrayList<NodePointer> fingerTable;

  public Node(String host, int port, int bit, Http http) throws IOException {
    this._id = port;
    this.bit = bit;
    this.http = http;

    this._pointer = new NodePointer(this._id, host + ":" + port);
    this._predecessor = this._pointer;

    this.initializeFingerTable();
    this.initializeServer(port);
  }

  public Node(String host, Integer port) throws IOException {
    this(host, port, 8, new Http());
  }

  public int getId() {
    return _id;
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
      // System.out.println("Fingers" + ((this.id + Math.pow(2, i-1)) % Math.pow(2,
      // this.bit)));
      this.fingerTable.add(i, this._pointer);
    }
  }

  // private int getHash(String str) {
  // final int p = 16777619;
  // int hash = (int) 2166136261L;
  // for (int i = 0; i < str.length(); i++)
  // hash = (hash ^ str.charAt(i)) * p;
  // hash += hash << 13;
  // hash ^= hash >> 7;
  // hash += hash << 3;
  // hash ^= hash >> 17;
  // hash += hash << 5;

  // // If the calculated value is negative, take its absolute value.
  // if (hash < 0)
  // hash = Math.abs(hash);
  // return hash;
  // }

  // public void join(String address) throws IOException {
  //   NodePointer hookupNode = new NodePointer(address);

  //   NodePointer successor = hookupNode.findSuccessor(this._pointer);
  // }

  // public NodePointer findSuccessor2() {
  //   this.findPreddecessor();
  // }

  // public NodePointer findPredecessor() {

  // }

  public void connectToNetwork(String address) throws IOException {

    System.out.print("N#" + this.getId() + ": ");
    System.out.println("Connect to network via " + address);

    NodePointer successor = NodePointer.fromJson(this.http.post(address + "/nodes", this._pointer.toJson()));

    System.out.print("N#" + this.getId() + ": ");
    System.out.println("Successor found: " + successor.getId());

    this.setSuccessor(successor);
    // NodePointer predecessor = successor.getPredecessor();
    // this.setPredecessor(predecessor);

    // this.getSuccessor().notifyOf(this);
    this.stabilize();
  }

  public void stabilize() throws IOException {
    System.out.print("N#" + this.getId() + ": ");
    System.out.println("Stabilize");
    /*
      x = successor.predecessor
      if x ∈ (n, successor) then
          successor := x
      successor.notify(n)
    */

    NodePointer successor = this.getSuccessor();
    NodePointer successorsPredecessor;

    if (!successor.pointsTo(this)) {
      successorsPredecessor = successor.getPredecessor();
    } else {
      successorsPredecessor = this.getPredecessor();
    }

    Interval interval = Interval.asOpen(this.getId(), this.getSuccessor().getId());

    if (interval.includes(successorsPredecessor.getId())) {
      this.setSuccessor(successorsPredecessor);
    }

    if (!successor.pointsTo(this)) {
      successor.notifyOf(this);
    }
  }

  private void initializeServer(int port) throws IOException {
    this.server = HttpServer.create(new InetSocketAddress(port), 0);
    this.setupEndpoints();
    this.server.setExecutor(null);
    this.server.start();
  }

  private void setupEndpoints() {
    this.server.createContext("/", new Routes.Index(this));
    this.server.createContext("/nodes", new Routes.Nodes(this));
    this.server.createContext("/successors", new Routes.Successors(this));
    this.server.createContext("/predecessors", new Routes.Predecessors(this));
  }

  public NodePointer findSuccessor(NodePointer node) throws IOException {
    System.out.print("N#" + this.getId() + ": ");
    System.out.println("find successor for " + node.getId());
    /*
      if id ∈ (n, successor] then
        return successor
      else
        // forward the query around the circle
        n0 := closest_preceding_node(id)
        return n0.find_successor(id)
    */
    // if (!this._predecessor.pointsTo(this)) {
    //   this._predecessor.findSuccessor(node);
    // }

    Interval interval = Interval.withClosedEnd(this.getId(), this.getSuccessor().getId());

    if (interval.includes(node.getId())) {
      return this.getSuccessor();
    }

    System.out.println("FORWARD QUERY AROUND THE CIRCLE");
    return null;
  }

  public void notify(NodePointer potentialPredecessor) throws IOException {
    /*
      if predecessor is nil or n'∈(predecessor, n) then
      predecessor := n'
    */

    System.out.print("N#" + this.getId() + ": ");
    System.out.println("Notification from #" + potentialPredecessor.getId());

    Interval interval = Interval.asOpen(this._predecessor.getId(), this.getId());

    if (interval.includes(potentialPredecessor.getId())) { //if null
      this.setPredecessor(potentialPredecessor);
    }
  }

  public String toJson() throws JsonProcessingException {
    return this._pointer.toJson();
  }
}
  