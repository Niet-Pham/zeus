package vn.tech.website.store.service;

import vn.tech.website.store.model.District;

public interface DistrictService {
    void deleteDistrict(Long id);
    void deleteById (Long id);
    void save(District district);
    void deleteDistrictByDistrictId(Long id);
}
