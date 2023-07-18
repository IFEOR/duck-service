package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.List;

@Setter
@Accessors(fluent = true)
public class Pet {

    @JsonProperty
    private int id;

    @JsonProperty
    private Category category;

    @JsonProperty
    private String name;

    @JsonProperty
    private List<String> photoUrls;

    @JsonProperty
    private List<Tag> tags;

    @JsonProperty
    private String status;
}
