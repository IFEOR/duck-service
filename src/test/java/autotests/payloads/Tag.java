package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true)
public class Tag {

    @JsonProperty
    private int id;

    @JsonProperty
    private String name;
}
