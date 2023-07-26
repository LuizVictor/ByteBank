package br.com.luizvictor.bytebank.infra.account.service;

import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.account.AccountService;

import java.math.BigDecimal;

public class AccountServiceMemory implements AccountService {
    private final AccountRepository repository;

    public AccountServiceMemory(AccountRepository repository) {
        this.repository = repository;
    }

    public void deposit(Integer accountNumber, BigDecimal amount) {
        repository.searchByNumber(accountNumber).setBalance(amount);
    }

    public void withdraw(Integer accountNumber, BigDecimal amount) {
        repository.searchByNumber(accountNumber).setBalance(amount);
    }
}
