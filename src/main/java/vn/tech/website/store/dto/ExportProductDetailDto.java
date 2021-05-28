package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportProductDetailDto {
    private String productName;
    private Double price;
    private Float discount;
    private Long quantity;
    private Double totalAmount;
}
