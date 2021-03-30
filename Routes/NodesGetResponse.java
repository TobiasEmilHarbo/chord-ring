package Routes;

import Services.NodePointer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodesGetResponse {
  @JsonProperty("successor")
  public NodePointer successor;
  @JsonProperty("predecessor")
  public NodePointer predecessor;

  public String toJson() throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(this);
  }
}
