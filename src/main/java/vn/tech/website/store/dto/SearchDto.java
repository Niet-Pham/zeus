package vn.tech.website.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {
    @JsonProperty("object_id")
    private Long objectId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
}
