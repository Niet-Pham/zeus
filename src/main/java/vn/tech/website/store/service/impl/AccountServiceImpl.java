package vn.tech.website.store.service.impl;

import org.springframework.stereotype.Service;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
}
