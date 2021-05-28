package vn.tech.website.store.service;

import vn.tech.website.store.model.Commune;
import vn.tech.website.store.model.District;
import vn.tech.website.store.model.Province;

import java.util.List;

public interface MasterDataService {
    List<Province> getAllProvince();
    List<District> getAllDistrictByCode(String code);
    List<Commune> getAllCommuneByDistrictId(Long districtId);
}
