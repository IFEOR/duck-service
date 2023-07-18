package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true)
public class Category {

    @JsonProperty
    private int id;

    @JsonProperty
    private String name;
}
