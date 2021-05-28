package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.dto.user.AdministratorsCentralStaffSearchDto;
import vn.tech.website.store.repository.AdministratorsCentralStaffRepositoryCustom;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdministratorsCentralStaffRepositoryImpl implements AdministratorsCentralStaffRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AccountDto> search(AdministratorsCentralStaffSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT "
                + "acc.account_id, "
                + "acc.full_name, "
                + "acc.sex, "
                + "acc.date_of_birth, "
                + "acc.address, "
                + "acc.phone, "
                + "acc.email, "
                + "acc.password, "
                + "acc.salt,"
                + "acc.id_card_number, "
                + "acc.province_id_of_issue, "
                + "acc.date_of_issue, "
                + "acc.relative_name, "
                + "acc.relative_id_card_number, "
                + "acc.permanent_residence, "
                + "acc.org_name, "
                + "acc.business_license, "
                + "acc.org_address, "
                + "acc.position, "
                + "acc.org_phone, "
                + "acc.status, "
                + "acc.is_org, "
                + "acc.role_id, "
                + "acc.username, "
                + "p.province_id, "
                + "p.name as 'province_name', "
                + "d.district_id, "
                + "d.name as 'district_name', "
                + "c.commune_id, "
                + "c.name as 'commune_name', "
                + "acc.create_by, "
                + "acc.create_date, "
                + "r.name as 'role_name', "
                + "acc.login_failed,"
                + "acc.update_date,"
                + "acc.update_by,"
                + "acc.avatar_path,"
                + "acc.first_time_login, "
                + "accbk.full_name as full, "
                + "acc.login_from_sso ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY acc.account_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("fullName")) {
                sb.append(" acc.full_name collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("phone")) {
                sb.append(" acc.phone ");
            } else if (searchDto.getSortField().equals("address")) {
                sb.append(" acc.address collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("email")) {
                sb.append(" acc.email ");
            } else if (searchDto.getSortField().equals("username")) {
                sb.append(" acc.username collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("roleId")) {
                sb.append(" acc.role_id ");
            } else if (searchDto.getSortField().equals("accountStatus")) {
                sb.append(" acc.status ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY acc.account_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        List<AccountDto> accountDtoList = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        return accountDtoList;
    }

    @Override
    public BigInteger countSearch(AdministratorsCentralStaffSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(acc.account_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return (BigInteger) query.getSingleResult();
    }

    @Override
    public boolean checkDeleteStoreReq(Long accountId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(account_id) from (select account_id from account " +
                "    union all select account_id from store_req " +
                "    union all select account_id from store_req " +
                "            ) exist " +
                " where account_id = :accountId");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("accountId", accountId);
        return "1".equals(query.getSingleResult().toString());
    }

    @Override
    public boolean checkDeleteAutionRegister(Long accountId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(account_id) from (select account_id from account " +
                "    union all select account_id from store_register " +
                "            ) exist " +
                " where account_id = :accountId");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("accountId", accountId);
        return "1".equals(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, AdministratorsCentralStaffSearchDto searchDto) {
        sb.append("FROM account acc " +
                " LEFT JOIN account accbk ON acc.update_by = accbk.account_id " +
                "LEFT JOIN province p ON (acc.province_id_of_issue = p.province_id or acc.province_id = p.province_id) " +
                "left join district d on acc.district_id = d.district_id " +
                "left join commune c on acc.commune_id = c.commune_id " +
                "LEFT JOIN role r ON acc.role_id = r.role_id " +
                "WHERE (r.type = 1 OR acc.role_id <> :roleIdUser) AND acc.status <> 3 ");

        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            sb.append(" AND (acc.full_name LIKE :keyword ");
            sb.append(" OR acc.phone LIKE :keyword ");
            sb.append(" OR acc.address LIKE :keyword ");
            sb.append(" OR acc.email LIKE :keyword) ");
        }

        if (StringUtils.isNotBlank(searchDto.getFullName())) {
            sb.append(" AND acc.full_name LIKE :fullname ");
        }
        if (StringUtils.isNotBlank(searchDto.getEmail())) {
            sb.append(" AND acc.email LIKE :email ");
        }
        if (StringUtils.isNotBlank(searchDto.getUsername())) {
            sb.append(" AND acc.username LIKE :username ");
        }
        if (searchDto.getStatus() != -1) {
            sb.append(" AND acc.status = :status ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, AdministratorsCentralStaffSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roleIdUser", DbConstant.ROLE_ID_USER);
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            query.setParameter("keyword", "%" + searchDto.getKeyword().trim() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getFullName())) {
            query.setParameter("fullname", "%" + searchDto.getFullName().trim() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getEmail())) {
            query.setParameter("email", "%" + searchDto.getEmail().trim() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getUsername())) {
            query.setParameter("username", "%" + searchDto.getUsername().trim() + "%");
        }
        if (searchDto.getStatus() != -1) {
            query.setParameter("status", searchDto.getStatus());
        }
        return query;
    }

    @Override
    public BigInteger checkExistEmail(String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(acc.account_id) ");
        sb.append("FROM account acc ");
        sb.append("WHERE acc.email LIKE :email");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("email", "%" + email + "%");

        return (BigInteger) query.getSingleResult();
    }

    @Override
    public BigInteger checkExistPhone(String phone) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(acc.account_id) ");
        sb.append("FROM account acc ");
        sb.append("WHERE acc.phone LIKE :phone ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("phone", "%" + phone + "%");

        return (BigInteger) query.getSingleResult();
    }
}
