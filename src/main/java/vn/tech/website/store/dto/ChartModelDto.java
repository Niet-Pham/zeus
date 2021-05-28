package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChartModelDto {
    private Long categoryId;
    private Double totalJanuary = 0D;
    private Double totalFebruary = 0D;
    private Double totalMarch = 0D;
    private Double totalApril = 0D;
    private Double totalMay = 0D;
    private Double totalJune = 0D;
    private Double totalJuly = 0D;
    private Double totalAugust = 0D;
    private Double totalSeptember = 0D;
    private Double totalOctober = 0D;
    private Double totalNovember = 0D;
    private Double totalDecember = 0D;
}
