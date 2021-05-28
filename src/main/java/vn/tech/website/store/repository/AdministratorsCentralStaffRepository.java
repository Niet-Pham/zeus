package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.model.Account;

public interface AdministratorsCentralStaffRepository extends CrudRepository<Account ,Long>,AdministratorsCentralStaffRepositoryCustom {
//    @Query("select acc.fullName from Account acc where acc.accountId = :accountId")
//    String getFullnameByAccountId(@Param("accountId") Long accountId);
}
