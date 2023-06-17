package br.com.alura.bytebank.app.account;

import br.com.alura.bytebank.domain.account.*;
import br.com.alura.bytebank.domain.account.exceptions.AccountDomainException;

import java.math.BigDecimal;

public class Deposit {
    private final AccountRepository repository;
    private final AccountService service;

    public Deposit(AccountRepository accountRepository, AccountService accountService) {
        repository = accountRepository;
        service = accountService;
    }

    public void execute(Integer accountNumber, BigDecimal amount) {
        ListAccount listAccount = new ListAccount(repository);
        AccountDetailDto detailDto = listAccount.searchByNumber(accountNumber);

        if (amount.signum() < 1) {
            throw new AccountDomainException("Cannot deposit an amount equal to zero or a negative amount");
        }

        AccountDto data = new AccountDto(detailDto.number(), detailDto.client());
        service.deposit(new Account(data), amount);
    }
}
