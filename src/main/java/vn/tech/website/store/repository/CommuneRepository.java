package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.Commune;

import java.util.List;

public interface CommuneRepository extends CrudRepository<Commune, Long>, CommuneRepositoryCustom {


    List<Commune> deleteCommunesByProvinceId(Long provinceId);

    List<Commune> findByNameAndDistrictId(String name,Long districtId);

    List<Commune> getCommuneByDistrictId(Long districtId);

    @Query(value = "SELECT commune_id FROM commune ORDER BY commune_id DESC LIMIT 1", nativeQuery = true)
    Long getCommuneIdLast();

    List<Commune> deleteCommunesByDistrictId(Long id);
}
