package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import vn.tech.website.store.dto.ChartModelDto;
import vn.tech.website.store.dto.LineChartModelDto;
import vn.tech.website.store.dto.payment.PaymentDto;
import vn.tech.website.store.dto.payment.PaymentSearchDto;
import vn.tech.website.store.entity.EntityMapper;
import vn.tech.website.store.repository.PaymentsRepositoryCustom;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentsRepositoryImpl implements PaymentsRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PaymentDto> search(PaymentSearchDto searchDto) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT "
                + "p.payment_id, "
                + "p.orders_id, "
                + "p.code, "
                + "p.total_amount as 'totalAmount', "
                + "o.account_id, "
                + "acc.user_name, "
                + "o.customer_name, "
                + "o.address, "
                + "o.phone, "
                + "o.email, "
                + "o.note, "
                + "o.total_amount as 'amountProduct', "
                + "o.shipping_id, "
                + "o.shipping, "
                + "p.create_date,"
                + "s.name as shippingName ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY p.payment_id ");
        if (searchDto.getSortField() != null) {
            if (searchDto.getSortField().equals("customerName")) {
                sb.append(" ,o.customer_name ");
            }
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("customerName")) {
                sb.append(" o.customer_name ");
            } else if (searchDto.getSortField().equals("code")) {
                sb.append(" p.code ");
            } else if (searchDto.getSortField().equals("totalAmount")) {
                sb.append(" p.total_amount ");
            } else if (searchDto.getSortField().equals("createDate")) {
                sb.append(" p.create_date ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY p.payment_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }
        List<PaymentDto> paymentList = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        for (Object[] obj : result) {
            PaymentDto dto = new PaymentDto();
            dto.setPaymentId(ValueUtil.getLongByObject(obj[0]));
            dto.setOrdersId(ValueUtil.getLongByObject(obj[1]));
            dto.getOrdersDto().setOrdersId(ValueUtil.getLongByObject(obj[1]));
            dto.setCode(ValueUtil.getStringByObject(obj[2]));
            dto.setTotalAmount(ValueUtil.getDoubleByObject(obj[3]));
            dto.getOrdersDto().setAccountId(ValueUtil.getLongByObject(obj[4]));
            dto.setUserName(ValueUtil.getStringByObject(obj[5]));
            dto.getOrdersDto().setCustomerName(ValueUtil.getStringByObject(obj[6]));
            dto.getOrdersDto().setAddress(ValueUtil.getStringByObject(obj[7]));
            dto.getOrdersDto().setPhone(ValueUtil.getStringByObject(obj[8]));
            dto.getOrdersDto().setEmail(ValueUtil.getStringByObject(obj[9]));
            dto.getOrdersDto().setNote(ValueUtil.getStringByObject(obj[10]));
            dto.setAmountProduct(ValueUtil.getDoubleByObject(obj[11]));
            dto.getOrdersDto().setShippingId(ValueUtil.getLongByObject(obj[12]));
            dto.getOrdersDto().setShipping(ValueUtil.getDoubleByObject(obj[13]));
            dto.setCreateDate(ValueUtil.getDateByObject(obj[14]));
            dto.setShippingName(ValueUtil.getStringByObject(obj[15]));
            paymentList.add(dto);
        }
        return paymentList;
    }

    @Override
    public Long countSearch(PaymentSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(p.payment_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, PaymentSearchDto searchDto) {
        sb.append(" FROM payments p " +
                "   INNER JOIN orders o ON p.orders_id = o.orders_id" +
                "   LEFT JOIN account acc ON o.account_id = acc.account_id" +
                "   INNER JOIN shipping s ON o.shipping_id = s.shipping_id ");
        sb.append(" WHERE 1 = 1 ");
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            sb.append(" AND (o.customer_name LIKE :keyword OR o.code LIKE :keyword) ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, PaymentSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            query.setParameter("keyword", "%" + searchDto.getKeyword().trim() + "%");
        }
        return query;
    }

    @Override
    public List getDataModelChart(Integer year) {
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct " +
                "    (select sum(p_1.total_amount) from payments p_1 where month(p_1.create_date) = 1 and year(p_1.create_date) = year(p_df.create_date)) as totalJanuary, " +
                "    (select sum(p_2.total_amount) from payments p_2 where month(p_2.create_date) = 2 and year(p_2.create_date) = year(p_df.create_date)) as totalFebruary, " +
                "    (select sum(p_3.total_amount) from payments p_3 where month(p_3.create_date) = 3 and year(p_3.create_date) = year(p_df.create_date)) as totalMarch, " +
                "    (select sum(p_4.total_amount) from payments p_4 where month(p_4.create_date) = 4 and year(p_4.create_date) = year(p_df.create_date)) as totalApril, " +
                "    (select sum(p_5.total_amount) from payments p_5 where month(p_5.create_date) = 5 and year(p_5.create_date) = year(p_df.create_date)) as totalMay, " +
                "    (select sum(p_6.total_amount) from payments p_6 where month(p_6.create_date) = 6 and year(p_6.create_date) = year(p_df.create_date)) as totalJune, " +
                "    (select sum(p_7.total_amount) from payments p_7 where month(p_7.create_date) = 7 and year(p_7.create_date) = year(p_df.create_date)) as totalJuly, " +
                "    (select sum(p_8.total_amount) from payments p_8 where month(p_8.create_date) = 8 and year(p_8.create_date) = year(p_df.create_date)) as totalAugust, " +
                "    (select sum(p_9.total_amount) from payments p_9 where month(p_9.create_date) = 9 and year(p_9.create_date) = year(p_df.create_date)) as totalSeptember, " +
                "    (select sum(p_10.total_amount) from payments p_10 where month(p_10.create_date) = 10 and year(p_10.create_date) = year(p_df.create_date)) as totalOctober, " +
                "    (select sum(p_11.total_amount) from payments p_11 where month(p_11.create_date) = 11 and year(p_11.create_date) = year(p_df.create_date)) as totalNovember, " +
                "    (select sum(p_12.total_amount) from payments p_12 where month(p_12.create_date) = 12 and year(p_12.create_date) = year(p_df.create_date)) as totalDecember " +
                "from payments p_df " +
                "where year(p_df.create_date) = :year ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("year", year);
        return EntityMapper.mapper(query, sb.toString(), ChartModelDto.class);
    }

    @Override
    public LineChartModelDto getDataLineChart(Integer year, Long catePhone, Long catePc, Long cateWireless, Long cateWatch) {
        String condition = "(" + catePhone + ", " + catePc + ", " + cateWireless + ", " + cateWatch + ")";
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct pro_df.category_id as categoryId, " +
                "    (select sum(od_1.amount * od_1.quantity) from order_detail od_1 inner join product p_1 on od_1.product_id = p_1.product_id where od_1.orders_id = p_df.orders_id and month(p_df.create_date) = 1 and p_1.category_id = pro_df.category_id) as totalJanuary, " +
                "    (select sum(od_2.amount * od_2.quantity) from order_detail od_2 inner join product p_2 on od_2.product_id = p_2.product_id where od_2.orders_id = p_df.orders_id and month(p_df.create_date) = 2 and p_2.category_id = pro_df.category_id) as totalFebruary, " +
                "    (select sum(od_3.amount * od_3.quantity) from order_detail od_3 inner join product p_3 on od_3.product_id = p_3.product_id where od_3.orders_id = p_df.orders_id and month(p_df.create_date) = 3 and p_3.category_id = pro_df.category_id) as totalMarch, " +
                "    (select sum(od_4.amount * od_4.quantity) from order_detail od_4 inner join product p_4 on od_4.product_id = p_4.product_id where od_4.orders_id = p_df.orders_id and month(p_df.create_date) = 4 and p_4.category_id = pro_df.category_id) as totalApril, " +
                "    (select sum(od_5.amount * od_5.quantity) from order_detail od_5 inner join product p_5 on od_5.product_id = p_5.product_id where od_5.orders_id = p_df.orders_id and month(p_df.create_date) = 5 and p_5.category_id = pro_df.category_id) as totalMay, " +
                "    (select sum(od_6.amount * od_6.quantity) from order_detail od_6 inner join product p_6 on od_6.product_id = p_6.product_id where od_6.orders_id = p_df.orders_id and month(p_df.create_date) = 6 and p_6.category_id = pro_df.category_id) as totalJune, " +
                "    (select sum(od_7.amount * od_7.quantity) from order_detail od_7 inner join product p_7 on od_7.product_id = p_7.product_id where od_7.orders_id = p_df.orders_id and month(p_df.create_date) = 7 and p_7.category_id = pro_df.category_id) as totalJuly, " +
                "    (select sum(od_8.amount * od_8.quantity) from order_detail od_8 inner join product p_8 on od_8.product_id = p_8.product_id where od_8.orders_id = p_df.orders_id and month(p_df.create_date) = 8 and p_8.category_id = pro_df.category_id) as totalAugust, " +
                "    (select sum(od_9.amount * od_9.quantity) from order_detail od_9 inner join product p_9 on od_9.product_id = p_9.product_id where od_9.orders_id = p_df.orders_id and month(p_df.create_date) = 9 and p_9.category_id = pro_df.category_id) as totalSeptember, " +
                "    (select sum(od_10.amount * od_10.quantity) from order_detail od_10 inner join product p_10 on od_10.product_id = p_10.product_id where od_10.orders_id = p_df.orders_id and month(p_df.create_date) = 10 and p_10.category_id = pro_df.category_id) as totalOctober, " +
                "    (select sum(od_11.amount * od_11.quantity) from order_detail od_11 inner join product p_11 on od_11.product_id = p_11.product_id where od_11.orders_id = p_df.orders_id and month(p_df.create_date) = 11 and p_11.category_id = pro_df.category_id) as totalNovember, " +
                "    (select sum(od_12.amount * od_12.quantity) from order_detail od_12 inner join product p_12 on od_12.product_id = p_12.product_id where od_12.orders_id = p_df.orders_id and month(p_df.create_date) = 12 and p_12.category_id = pro_df.category_id) as totalDecember " +
                "from payments p_df " +
                "inner join order_detail od_df on p_df.orders_id = od_df.orders_id " +
                "inner join product pro_df on od_df.product_id = pro_df.product_id " +
                "where year(p_df.create_date) = :year and pro_df.category_id in " + condition);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("year", year);
        List<ChartModelDto> chartModelDtoList = (List<ChartModelDto>) EntityMapper.mapper(query, sb.toString(), ChartModelDto.class);

        ChartModelDto totalPhone = new ChartModelDto();
        ChartModelDto totalPc = new ChartModelDto();
        ChartModelDto totalWireless = new ChartModelDto();
        ChartModelDto totalWatch = new ChartModelDto();
        for (ChartModelDto c : chartModelDtoList) {
            if (c.getCategoryId().equals(catePhone)) {
                setData(c, totalPhone);
            } else if (c.getCategoryId().equals(catePc)) {
                setData(c, totalPc);
            } else if (c.getCategoryId().equals(cateWireless)) {
                setData(c, totalWireless);
            } else if (c.getCategoryId().equals(cateWatch)) {
                setData(c, totalWatch);
            } else {
                System.out.println("None");
            }
        }
        LineChartModelDto lineChartModelDto = new LineChartModelDto();
        lineChartModelDto.setChartModelAoDto(totalPhone);
        lineChartModelDto.setChartModelQuanDto(totalPc);
        lineChartModelDto.setChartModelGiayDto(totalWireless);
        lineChartModelDto.setChartModelPKDto(totalWatch);
        return lineChartModelDto;
    }

    private void setData(ChartModelDto src, ChartModelDto target) {
        target.setTotalJanuary(target.getTotalJanuary() + (src.getTotalJanuary() == null ? 0D : src.getTotalJanuary()));
        target.setTotalFebruary(target.getTotalFebruary() + (src.getTotalFebruary() == null ? 0D : src.getTotalFebruary()));
        target.setTotalMarch(target.getTotalMarch() + (src.getTotalMarch() == null ? 0D : src.getTotalMarch()));
        target.setTotalApril(target.getTotalApril() + (src.getTotalApril() == null ? 0D : src.getTotalApril()));
        target.setTotalMay(target.getTotalMay() + (src.getTotalMay() == null ? 0D : src.getTotalMay()));
        target.setTotalJune(target.getTotalJune() + (src.getTotalJune() == null ? 0D : src.getTotalJune()));
        target.setTotalJuly(target.getTotalJuly() + (src.getTotalJuly() == null ? 0D : src.getTotalJuly()));
        target.setTotalAugust(target.getTotalAugust() + (src.getTotalAugust() == null ? 0D : src.getTotalAugust()));
        target.setTotalSeptember(target.getTotalSeptember() + (src.getTotalSeptember() == null ? 0D : src.getTotalSeptember()));
        target.setTotalOctober(target.getTotalOctober() + (src.getTotalOctober() == null ? 0D : src.getTotalOctober()));
        target.setTotalNovember(target.getTotalNovember() + (src.getTotalNovember() == null ? 0D : src.getTotalNovember()));
        target.setTotalDecember(target.getTotalDecember() + (src.getTotalDecember() == null ? 0D : src.getTotalDecember()));
    }

}

