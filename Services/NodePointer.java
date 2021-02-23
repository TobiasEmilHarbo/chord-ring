package Services;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodePointer {

  private int _id;
  private String _address;
  private Pointer _pointer;

  private Http http;

  public NodePointer(Pointer pointer) {
    this(pointer.getId(), pointer.getAddress());
  }

  public NodePointer(int id, String address) {
    this(id, address, new Http());
  }

  public NodePointer(int id, String address, Http http) {
    this.http = http;
    this._id = id;
    this._address = address;

    this._pointer = new Pointer(this._id, this._address);
  }

  public int getId() {
    return this._id;
  }

  public String getAddress() {
    return this._address;
  }

  public void findSuccessor(int data) throws IOException {
    System.out.println("findSuccessor " + this._address + "/successors/?id=" + data);

    String successor = this.http.get(this._address + "/successors/?id=" + data);

    System.out.println("successor" + successor);
  }

  public NodePointer getPredecessor() throws IOException {
    return NodePointer.fromJson(this.http.get(this._address + "/predecessors/"));
  }

  public void notify(Node node) throws JsonProcessingException, IOException {
    this.http.post(this._address + "/predecessors/", node.toJson());
  }

  public Boolean pointsTo(Node node) {
    return this._id == node.getId();
  }

  public static NodePointer fromJson(String json) throws JsonMappingException, JsonProcessingException {
    Pointer pointer = new ObjectMapper().readValue(json.toString(), Pointer.class);
    return new NodePointer(pointer);
  }

  public String toJson() throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(this._pointer);
  }
}
