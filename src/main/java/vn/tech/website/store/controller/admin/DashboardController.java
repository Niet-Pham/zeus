package vn.tech.website.store.controller.admin;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.dto.ChartModelDto;
import vn.tech.website.store.dto.LineChartModelDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.repository.CategoryRepository;
import vn.tech.website.store.repository.PaymentsRepository;
import vn.tech.website.store.util.StringUtil;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@Named
@Scope(value = "session")
public class DashboardController extends BaseController {
    @Autowired
    private PaymentsRepository paymentsRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Long storeRegisterForOthers;
    private BarChartModel barModel;
    private LineChartModel lineModel;
    private Date time;
    private List<SelectItem> listYear;
    private Integer year;
    private Long selectedYear;
    private ChartModelDto chartModelDto;
    private LineChartModelDto lineChartModelDto;

    @PostConstruct
    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAllNiet();
        }
    }

    public void resetAll() {
        time = new Date();
        year = time.getYear() + 1900;
        chartModelDto = paymentsRepository.getDataModelChart(year).get(0);

        Long phone = categoryRepository.getCateIdByCode("phone").getCategoryId();
        Long headphone = categoryRepository.getCateIdByCode("headphone").getCategoryId();
        Long pc = categoryRepository.getCateIdByCode("pc").getCategoryId();
        Long watch = categoryRepository.getCateIdByCode("watch").getCategoryId();
        lineChartModelDto = paymentsRepository.getDataLineChart(year, phone, pc, headphone, watch);

        createLineModel();
        createBarModel();
    }

    public void resetAllNiet() {
        time = new Date();
        year = time.getYear() + 1900;
        chartModelDto = paymentsRepository.getDataModelChart(year).get(0);

        Long ao = categoryRepository.getCateIdByCode("AO").getCategoryId();
        Long quan = categoryRepository.getCateIdByCode("QUAN").getCategoryId();
        Long giay = categoryRepository.getCateIdByCode("GIAY").getCategoryId();
        Long pk = categoryRepository.getCateIdByCode("PHUKIEN").getCategoryId();
        lineChartModelDto = paymentsRepository.getDataLineChart(year, ao, quan, giay, pk);

        createLineModelNiet();
        createBarModel();
    }

    public void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSetPhone = new LineChartDataSet();
        List<Number> valuesPhone = addValue(lineChartModelDto.getChartModelPhoneDto() != null ? lineChartModelDto.getChartModelPhoneDto() : new ChartModelDto());
        dataSetPhone.setData(valuesPhone);
        dataSetPhone.setFill(false);
        dataSetPhone.setLabel("Điện thoại");
        dataSetPhone.setBorderColor("rgb(255, 0, 0)");
        dataSetPhone.setLineTension(0.1);
        data.addChartDataSet(dataSetPhone);

        LineChartDataSet dataSetDesktop = new LineChartDataSet();
        List<Number> valuesDesktop = addValue(lineChartModelDto.getChartModelDesktopDto() != null ? lineChartModelDto.getChartModelDesktopDto() : new ChartModelDto());
        dataSetDesktop.setData(valuesDesktop);
        dataSetDesktop.setFill(false);
        dataSetDesktop.setLabel("Máy tính");
        dataSetDesktop.setBorderColor("rgb(4, 255, 21)");
        dataSetDesktop.setLineTension(0.1);
        data.addChartDataSet(dataSetDesktop);

        LineChartDataSet dataSetWireless = new LineChartDataSet();
        List<Number> valuesWireless = addValue(lineChartModelDto.getChartModelWirelessDto() != null ? lineChartModelDto.getChartModelWirelessDto() : new ChartModelDto());
        dataSetWireless.setData(valuesWireless);
        dataSetWireless.setFill(false);
        dataSetWireless.setLabel("Tai nghe");
        dataSetWireless.setBorderColor("rgb(4, 55, 255)");
        dataSetWireless.setLineTension(0.1);
        data.addChartDataSet(dataSetWireless);

        LineChartDataSet dataSetWatch = new LineChartDataSet();
        List<Number> valuesWatch = addValue(lineChartModelDto.getChartModelWatchDto() != null ? lineChartModelDto.getChartModelWatchDto() : new ChartModelDto());
        dataSetWatch.setData(valuesWatch);
        dataSetWatch.setFill(false);
        dataSetWatch.setLabel("Đồng hồ");
        dataSetWatch.setBorderColor("rgb(255, 214, 4)");
        dataSetWatch.setLineTension(0.1);
        data.addChartDataSet(dataSetWatch);

        List<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");
        data.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText("THỐNG KÊ DOANH THU THEO LOẠI SẢN PHẨM");
        title.setFontSize(25);
        options.setTitle(title);

        lineModel.setOptions(options);
        lineModel.setData(data);
    }

    public void createLineModelNiet() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSetAo = new LineChartDataSet();
        List<Number> valuesAo = addValue(lineChartModelDto.getChartModelAoDto() != null ? lineChartModelDto.getChartModelAoDto() : new ChartModelDto());
        dataSetAo.setData(valuesAo);
        dataSetAo.setFill(false);
        dataSetAo.setLabel("Áo");
        dataSetAo.setBorderColor("rgb(255, 0, 0)");
        dataSetAo.setLineTension(0.1);
        data.addChartDataSet(dataSetAo);

        LineChartDataSet dataSetQuan = new LineChartDataSet();
        List<Number> valuesQuan = addValue(lineChartModelDto.getChartModelQuanDto() != null ? lineChartModelDto.getChartModelQuanDto() : new ChartModelDto());
        dataSetQuan.setData(valuesQuan);
        dataSetQuan.setFill(false);
        dataSetQuan.setLabel("Quần");
        dataSetQuan.setBorderColor("rgb(4, 255, 21)");
        dataSetQuan.setLineTension(0.1);
        data.addChartDataSet(dataSetQuan);

        LineChartDataSet dataSetGiay = new LineChartDataSet();
        List<Number> valuesGiay = addValue(lineChartModelDto.getChartModelGiayDto() != null ? lineChartModelDto.getChartModelGiayDto() : new ChartModelDto());
        dataSetGiay.setData(valuesGiay);
        dataSetGiay.setFill(false);
        dataSetGiay.setLabel("Giày");
        dataSetGiay.setBorderColor("rgb(4, 55, 255)");
        dataSetGiay.setLineTension(0.1);
        data.addChartDataSet(dataSetGiay);

        LineChartDataSet dataSetPK = new LineChartDataSet();
        List<Number> valuesPK = addValue(lineChartModelDto.getChartModelPKDto() != null ? lineChartModelDto.getChartModelPKDto() : new ChartModelDto());
        dataSetPK.setData(valuesPK);
        dataSetPK.setFill(false);
        dataSetPK.setLabel("Phụ kiện");
        dataSetPK.setBorderColor("rgb(255, 214, 4)");
        dataSetPK.setLineTension(0.1);
        data.addChartDataSet(dataSetPK);

        List<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");
        data.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText("THỐNG KÊ DOANH THU THEO LOẠI SẢN PHẨM");
        title.setFontSize(25);
        options.setTitle(title);

        lineModel.setOptions(options);
        lineModel.setData(data);
    }

    public void createBarModel() {
        barModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Doanh thu hàng tháng");

        List<Number> values = addValue(chartModelDto);
        barDataSet.setData(values);

        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
        barDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        barDataSet.setBorderColor(borderColor);
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

        List<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");
        data.setLabels(labels);
        barModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("THỐNG KÊ DOANH THU THEO THÁNG NĂM " + year);
        title.setFontSize(25);
        options.setTitle(title);

        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("bold");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);

        barModel.setOptions(options);
    }

    private List<Number> addValue(ChartModelDto chartModelDto) {
        List<Number> dataValues = new ArrayList<>();
        dataValues.add(chartModelDto.getTotalJanuary() != null ? chartModelDto.getTotalJanuary().intValue() : 0);
        dataValues.add(chartModelDto.getTotalFebruary() != null ? chartModelDto.getTotalFebruary().intValue() : 0);
        dataValues.add(chartModelDto.getTotalMarch() != null ? chartModelDto.getTotalMarch().intValue() : 0);
        dataValues.add(chartModelDto.getTotalApril() != null ? chartModelDto.getTotalApril().intValue() : 0);
        dataValues.add(chartModelDto.getTotalMay() != null ? chartModelDto.getTotalMay().intValue() : 0);
        dataValues.add(chartModelDto.getTotalJune() != null ? chartModelDto.getTotalJune().intValue() : 0);
        dataValues.add(chartModelDto.getTotalJuly() != null ? chartModelDto.getTotalJuly().intValue() : 0);
        dataValues.add(chartModelDto.getTotalAugust() != null ? chartModelDto.getTotalAugust().intValue() : 0);
        dataValues.add(chartModelDto.getTotalSeptember() != null ? chartModelDto.getTotalSeptember().intValue() : 0);
        dataValues.add(chartModelDto.getTotalOctober() != null ? chartModelDto.getTotalOctober().intValue() : 0);
        dataValues.add(chartModelDto.getTotalNovember() != null ? chartModelDto.getTotalNovember().intValue() : 0);
        dataValues.add(chartModelDto.getTotalDecember() != null ? chartModelDto.getTotalDecember().intValue() : 0);
        return dataValues;
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
