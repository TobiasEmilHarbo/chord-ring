package Services;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pointer {
  @JsonProperty("id")
  private int id;
  @JsonProperty("address")
  private String address;

  public Pointer() {}
  public Pointer(int id, String address) {
    this.id = id;
    this.address = address;
  }

  public int getId() {
    return this.id;
  }

  public String getAddress() {
    return this.address;
  }
}
