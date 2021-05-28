package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.News;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto extends News {
    private String categoryName;
    private String nameUpdate;
}
