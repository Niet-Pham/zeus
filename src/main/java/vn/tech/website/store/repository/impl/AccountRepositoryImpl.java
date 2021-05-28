package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.dto.user.AccountSearchDto;
import vn.tech.website.store.entity.EntityMapper;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.repository.AccountRepositoryCustom;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class AccountRepositoryImpl implements AccountRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List search(AccountSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT acc.account_id AS accountId, " +
                "       acc.user_name AS userName, " +
                "       acc.full_name AS fullName, " +
                "       acc.date_of_birth AS dateOfBirth, " +
                "       acc.gender AS gender, " +
                "       acc.role_id AS roleId, " +
                "       acc.password AS password, " +
                "       acc.salt AS salt, " +
                "       acc.email AS email, " +
                "       acc.phone AS phone, " +
                "       acc.address AS address, " +
                "       acc.avatar_path AS imagePath, " +
                "       acc.status AS status, " +
                "       acc.province_id AS provinceId, " +
                "       acc.district_id AS districtId, " +
                "       acc.commune_id AS communeId, " +
                "       acc.create_date AS createDate, " +
                "       acc.create_by AS createBy, " +
                "       acc.update_date AS updateDate, " +
                "       acc.update_by AS updateBy, " +
                "       p.name AS provinceName, " +
                "       d.name AS districtName, " +
                "       c.name AS communeName, " +
                "       r.name AS roleName ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY acc.account_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("username")) {
                sb.append(" acc.username ");
            } else if (searchDto.getSortField().equals("fullName")) {
                sb.append(" acc.full_name collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("gender")) {
                sb.append(" acc.gender ");
            } else if (searchDto.getSortField().equals("dateOfBirth")) {
                sb.append(" acc.date_of_birth ");
            } else if (searchDto.getSortField().equals("address")) {
                sb.append(" acc.address ");
            } else if (searchDto.getSortField().equals("phone")) {
                sb.append(" acc.phone ");
            } else if (searchDto.getSortField().equals("email")) {
                sb.append(" acc.email ");
            } else if (searchDto.getSortField().equals("accountStatus")) {
                sb.append(" acc.status ");
            } else if (searchDto.getSortField().equals("updateDate")) {
                sb.append(" acc.update_date ");
            } else if (searchDto.getSortField().equals("createDate")) {
                sb.append(" acc.create_date ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY acc.status ASC, acc.create_date DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        return EntityMapper.mapper(query, sb.toString(), AccountDto.class);
    }

    @Override
    public BigInteger countSearch(AccountSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(1) from ( SELECT COUNT(acc.account_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" group by acc.account_id) as countAccount ");
        Query query = createQueryObjForSearch(sb, searchDto);
        return (BigInteger) query.getSingleResult();
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, AccountSearchDto searchDto) {
        sb.append(" FROM account acc " +
                "         LEFT JOIN province p on acc.province_id = p.province_id " +
                "         LEFT JOIN district d on acc.district_id = d.district_id " +
                "         LEFT JOIN commune c on acc.commune_id = c.commune_id " +
                "         INNER JOIN role r on acc.role_id = r.role_id " +
                " WHERE 1 = 1 ");
        if (searchDto.getRoleId() != null) {
            sb.append("AND acc.role_id = :roleId ");
        }
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            sb.append(" AND (acc.user_name LIKE :keyword OR acc.full_name LIKE :keyword OR acc.email LIKE :keyword OR acc.address LIKE :keyword) ");
        }
        if (StringUtils.isNotBlank(searchDto.getPhone())) {
            sb.append(" AND acc.phone LIKE :phone ");
        }
        if (StringUtils.isNotBlank(searchDto.getUsername())) {
            sb.append(" AND acc.user_name LIKE :username ");
        }
        if (StringUtils.isNotBlank(searchDto.getEmail())) {
            sb.append(" AND acc.email LIKE :email ");
        }
        if (searchDto.getAccountStatus() != null) {
            sb.append(" AND acc.status = :status ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, AccountSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (searchDto.getRoleId() != null) {
            query.setParameter("roleId", searchDto.getRoleId());
        }
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            query.setParameter("keyword", "%" + searchDto.getKeyword().trim() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getPhone())) {
            query.setParameter("phone", "%" + searchDto.getPhone().trim() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getUsername())) {
            query.setParameter("username", "%" + searchDto.getUsername().trim() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getEmail())) {
            query.setParameter("email", "%" + searchDto.getEmail().trim() + "%");
        }
        if (searchDto.getAccountStatus() != null) {
            query.setParameter("status", searchDto.getAccountStatus());
        }
        return query;
    }
}
