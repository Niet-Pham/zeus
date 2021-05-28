package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LineChartModelDto {
    private ChartModelDto chartModelPhoneDto;
    private ChartModelDto chartModelDesktopDto;
    private ChartModelDto chartModelWirelessDto;
    private ChartModelDto chartModelWatchDto;

    private ChartModelDto chartModelAoDto;
    private ChartModelDto chartModelQuanDto;
    private ChartModelDto chartModelGiayDto;
    private ChartModelDto chartModelPKDto;
}
