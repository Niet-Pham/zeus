package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.model.Brand;
import vn.tech.website.store.util.DbConstant;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long>, AccountRepositoryCustom {
    Account findByEmail(String email);

    Account findAccountByUserNameAndRoleId(String username, Integer roleId);

    Account findAccountByEmailAndRoleId(String email, Integer roleId);

    Account findByPhone(String phone);

    Account findAccountByEmail(String email);

    Account findAccountByUserName(String username);

    Account findAccountByAccountId(Long accountId);

    List<Account> findAccountByRoleId(Integer roleId);

    Account findByAccountId(Long AccountId);

    List<Account> findAccountsByProvinceId(Long provinceId);

    List<Account> findAccountsByDistrictId(Long districtId);

    List<Account> findAccountsByCommuneId(Long communeId);

    List<Account> findAll();


    @Query("select a from Account a where a.userName like :username and a.roleId <> :roleId")
    Account getAccountByUserNameAndRoleId(@Param("username") String username, @Param("roleId") Integer roleId);

    @Query("select a.provinceId from Account a ")
    List<Long> findProvinceId();

    @Query("select a.roleId from Account a")
    List<Long> findRoleId();

    @Query("select a from Account a where a.accountId = :accountId")
    Account getAccountByAccountId(@Param("accountId") Long id);

    @Query("select acc from Account acc where acc.email <>:email")
    List<Account> getAllAccountExpertEmail(@Param("email") String email);

    @Query("select acc from Account acc where acc.accountId <> :accountId")
    List<Account> findAllAccountExpertId(@Param("accountId") Long accountId);

    @Query("select acc from Account acc where acc.roleId = :roleId and acc.accountId <> :accountId")
    List<Account> findAllAccountByRoleIdExpertId(@Param("roleId") Integer roleId, @Param("accountId") Long accountId);
}

