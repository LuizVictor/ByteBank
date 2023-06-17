package br.com.alura.bytebank.infra.account.service;

import br.com.alura.bytebank.domain.account.Account;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;
import br.com.alura.bytebank.model.AccountModel;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;

public class AccountServiceDb implements AccountService {
    private final EntityManager entityManager;

    public AccountServiceDb(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deposit(Account account, BigDecimal amount) {
        AccountModel accountModel = entityManager.find(AccountModel.class, account.number());
        accountModel.setBalance(amount);
    }

    @Override
    public void withdraw(Account account, BigDecimal amount) {
        entityManager.find(AccountModel.class, account.number()).setBalance(amount);
    }
}
