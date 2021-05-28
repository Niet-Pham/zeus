package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsSearchDto extends BaseSearchDto {
    private Long categoryId;
    private String title;
    private String shortContent;
    private Integer status;
}
