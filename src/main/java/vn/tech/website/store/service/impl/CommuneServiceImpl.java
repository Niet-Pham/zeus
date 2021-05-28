package vn.tech.website.store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.tech.website.store.model.Commune;
import vn.tech.website.store.repository.CommuneRepository;
import vn.tech.website.store.service.CommuneService;

import javax.transaction.Transactional;

@Service
public class CommuneServiceImpl implements CommuneService {
    @Autowired
    protected CommuneRepository communeRepository;

    @Override
    @Transactional
    public void deleteCommune(Long id) {
        communeRepository.deleteCommunesByProvinceId(id);
    }

    @Override
    @Transactional
    public void deleteCommunesByDistrictId(Long id) {
        communeRepository.deleteCommunesByDistrictId(id);
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        communeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void save(Commune commune) {
        communeRepository.save(commune);
    }

    @Override
    @Transactional
    public void deleteCommunebyCommuneId(Long id) {
        communeRepository.deleteById(id);
    }

}
