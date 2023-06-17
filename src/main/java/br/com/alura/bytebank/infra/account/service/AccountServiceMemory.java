package br.com.alura.bytebank.infra.account.service;

import br.com.alura.bytebank.domain.account.Account;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;

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
