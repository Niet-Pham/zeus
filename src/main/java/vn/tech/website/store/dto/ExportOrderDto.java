package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportOrderDto {
    private String manufactureName;
    private String orderCode;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    List<ExportProductDetailDto> exportProductDetailDtoList = new ArrayList<>();
    private Double amountProduct;
    private Double amountShipping;
    private Double amountTotal;
    private String founder;
}
