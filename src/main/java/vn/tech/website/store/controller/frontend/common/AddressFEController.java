package vn.tech.website.store.controller.frontend.common;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.dto.common.CityDistrictDto;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.model.Commune;
import vn.tech.website.store.model.District;
import vn.tech.website.store.model.Province;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.repository.CommuneRepository;
import vn.tech.website.store.repository.DistrictRepository;
import vn.tech.website.store.repository.ProvinceRepository;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@Getter
@Setter
@Scope(value = "session")
public class AddressFEController implements Serializable {
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private AccountRepository accountRepository;

    private List<Province> listProvince;
    private List<District> listDistrict;
    private List<Commune> listCommune;

    private CityDistrictDto cityDistrictDto;

    public void resetAll() {
        cityDistrictDto = new CityDistrictDto();
        listProvince = (List<Province>) provinceRepository.findAll();
        onChangeCity();
    }

    public void onChangeCity() {
        if (cityDistrictDto.getProvinceId() != null) {
            listDistrict = districtRepository.findDistrictsByProvinceId(cityDistrictDto.getProvinceId());
            listCommune = new ArrayList<>();
            cityDistrictDto = new CityDistrictDto();
        } else {
            listDistrict = new ArrayList<>();
            cityDistrictDto = new CityDistrictDto();
            listCommune = new ArrayList<>();
        }
        onChangeDistrict();
    }

    public void onChangeDistrict() {
        if (cityDistrictDto.getDistrictId() != null) {
            listCommune = communeRepository.getCommuneByDistrictId(cityDistrictDto.getDistrictId());
        } else {
            listCommune = new ArrayList<>();
        }
    }

    public void loadData() {
        if (cityDistrictDto.getProvinceId() != null) {
            listDistrict = districtRepository.findDistrictsByProvinceId(cityDistrictDto.getProvinceId());
        }
        if (cityDistrictDto.getDistrictId() != null) {
            listCommune = communeRepository.getCommuneByDistrictId(cityDistrictDto.getDistrictId());
        }
    }

    public String upperCaseFirstChar(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
