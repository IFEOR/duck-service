package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true)
public class DuckWithId {

    @JsonProperty
    private String color;

    @JsonProperty
    private Double height;

    @JsonProperty
    private Integer id;

    @JsonProperty
    private String material;

    @JsonProperty
    private String sound;

    @JsonProperty
    private String wingsState;
}
