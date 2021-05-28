package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import vn.tech.website.store.dto.CategoryDto;
import vn.tech.website.store.dto.CategorySearchDto;
import vn.tech.website.store.dto.ProductOptionDto;
import vn.tech.website.store.dto.ProductOptionSearchDto;
import vn.tech.website.store.repository.ProductOptionRepositoryCustom;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ProductOptionRepositoryImpl implements ProductOptionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductOptionDto> search(ProductOptionSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT "
                + "po.product_option_id, "
                + "po.type, "
                + "po.name, "
                + "po.value, "
                + "po.status, "
                + "po.create_date, "
                + "po.create_by, "
                + "po.update_date, "
                + "po.update_by ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY po.product_option_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("productOptionId")) {
                sb.append(" po.product_option_id ");
            } else if (searchDto.getSortField().equals("type")) {
                sb.append(" po.type ");
            } else if (searchDto.getSortField().equals("optionName")) {
                sb.append(" po.name collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("optionValue")) {
                sb.append(" po.value ");
            } else if (searchDto.getSortField().equals("status")) {
                sb.append(" po.status ");
            } else if (searchDto.getSortField().equals("updateDate")) {
                sb.append(" po.update_date ");
            } else if (searchDto.getSortField().equals("updateBy")) {
                sb.append(" po.update_by ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY po.product_option_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        List<ProductOptionDto> dtoList = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        for (Object[] obj : result) {
            ProductOptionDto dto = new ProductOptionDto();
            dto.setProductOptionId(ValueUtil.getLongByObject(obj[0]));
            dto.setType(ValueUtil.getIntegerByObject(obj[1]));
            if (dto.getType() == DbConstant.OPTION_TYPE_SIZE) {
                dto.setTypeOptionString(DbConstant.OPTION_TYPE_SIZE_STRING);
            }
            if (dto.getType() == DbConstant.OPTION_TYPE_COLOR) {
                dto.setTypeOptionString(DbConstant.OPTION_TYPE_COLOR_STRING);
            }
            if (dto.getType() == DbConstant.OPTION_TYPE_RELEASE) {
                dto.setTypeOptionString(DbConstant.OPTION_TYPE_RELEASE_STRING);
            }
            if (dto.getType() == DbConstant.OPTION_TYPE_OTHER) {
                dto.setTypeOptionString(DbConstant.OPTION_TYPE_OTHER_STRING);
            }
            dto.setOptionName(ValueUtil.getStringByObject(obj[2]));
            dto.setOptionValue(ValueUtil.getStringByObject(obj[3]));
            dto.setStatus(ValueUtil.getIntegerByObject(obj[4]));
            dto.setCreateDate(ValueUtil.getDateByObject(obj[5]));
            dto.setCreateBy(ValueUtil.getLongByObject(obj[6]));
            dto.setUpdateDate(ValueUtil.getDateByObject(obj[7]));
            dto.setUpdateBy(ValueUtil.getLongByObject(obj[8]));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public Long countSearch(ProductOptionSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(po.product_option_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, ProductOptionSearchDto searchDto) {
        sb.append("FROM product_option po ");
        sb.append(" WHERE 1=1  ");

        if (searchDto.getType() != null) {
            sb.append(" AND po.type = :type ");
        }
        if (StringUtils.isNotBlank(searchDto.getOptionName())) {
            sb.append(" AND po.name LIKE :optionName ");
        }
        if (StringUtils.isNotBlank(searchDto.getOptionValue())) {
            sb.append(" AND po.value LIKE :optionValue ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, ProductOptionSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());

        if (searchDto.getType() != null) {
            query.setParameter("type", searchDto.getType());
        }
        if (StringUtils.isNotBlank(searchDto.getOptionName())) {
            query.setParameter("optionName", "%" + searchDto.getOptionName().trim() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getOptionValue())) {
            query.setParameter("optionValue", "%" + searchDto.getOptionValue().trim() + "%");
        }

        return query;
    }
}
