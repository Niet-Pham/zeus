package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.Category;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto extends Category {
    private String nameUpdate;
}
