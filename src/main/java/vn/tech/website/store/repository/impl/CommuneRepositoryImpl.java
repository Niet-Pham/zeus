package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import vn.tech.website.store.dto.base.CommuneDto;
import vn.tech.website.store.dto.base.CommuneSearchDto;
import vn.tech.website.store.repository.CommuneRepositoryCustom;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommuneRepositoryImpl implements CommuneRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CommuneDto> search(CommuneSearchDto searchDto) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT "
                + "n.commune_id,"
                + "n.province_id,"
                + "n.district_id,"
                + "n.name as communeName, "
                + "dtr.name as districtName,"
                + "pr.name as provinceName ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY n.commune_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("code")) {
                sb.append(" n.code ");
            }  else if (searchDto.getSortField().equals("name")) {
                sb.append(" n.name collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("status")) {
                sb.append(" n.status ");
            } else if (searchDto.getSortField().equals("updateDate")) {
                sb.append(" n.update_date ");
            } else if (searchDto.getSortField().equals("provinceName")) {
                sb.append(" pr.name collate utf8mb4_vietnamese_ci ");
            }   else if (searchDto.getSortField().equals("districtName")) {
                sb.append(" dtr.name collate utf8mb4_vietnamese_ci ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY n.commune_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        List<CommuneDto> communeList = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        for (Object[] obj : result) {
            CommuneDto dto = new CommuneDto();
            dto.setCommuneId(ValueUtil.getLongByObject(obj[0]));
            dto.setProvinceId(ValueUtil.getLongByObject(obj[1]));
            dto.setDistrictId(ValueUtil.getLongByObject(obj[2]));
            dto.setName(ValueUtil.getStringByObject(obj[3]));
            dto.setDistrictName(ValueUtil.getStringByObject(obj[4]));
            dto.setProvinceName(ValueUtil.getStringByObject(obj[5]));

            communeList.add(dto);
        }
        return communeList;

    }

    @Override
    public Long countSearch(CommuneSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(n.commune_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());

    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, CommuneSearchDto searchDto) {
        sb.append(" FROM commune n,province pr, district dtr " +
                " WHERE 1 = 1 AND pr.province_id = n.province_id " +
                " AND n.district_id = dtr.district_id " +
                " AND pr.province_id = dtr.province_id ");

        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            sb.append(" AND (n.code LIKE :keyword ");
            sb.append(" OR n.name LIKE :keyword) ");
        }
        if (searchDto.getProvinceId() != null) {

            sb.append(" AND dtr.province_id = :province_id ");
        }
        if (searchDto.getDistrictId() != null) {

            sb.append(" AND dtr.district_id = :district_id ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, CommuneSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            query.setParameter("keyword", "%" + searchDto.getKeyword().trim() + "%");
        }

        if (searchDto.getProvinceId() != null) {
            query.setParameter("province_id", searchDto.getProvinceId());
        }

        if (searchDto.getDistrictId() != null) {
            query.setParameter("district_id",  searchDto.getDistrictId() );
        }
        return query;
    }

    @Transactional
    @Modifying
    @Override
    public void deleteAllRecords() {
        Query query = entityManager.createNativeQuery("TRUNCATE TABLE commune");
        query.executeUpdate();
    }
}
