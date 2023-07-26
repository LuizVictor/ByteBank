package br.com.luizvictor.bytebank.app.account;

import br.com.luizvictor.bytebank.domain.account.AccountDetailDto;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.account.AccountService;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;

import java.math.BigDecimal;

public class Withdraw {
    private final AccountRepository repository;
    private final AccountService service;

    public Withdraw(AccountRepository accountRepository, AccountService accountService) {
        repository = accountRepository;
        service = accountService;
    }

    public void execute(Integer number, BigDecimal amount) {
        ListAccount listAccount = new ListAccount(repository);
        AccountDetailDto detailDto = listAccount.searchByNumber(number);

        if (amount.compareTo(detailDto.balance()) > 0) {
            throw new AccountDomainException("Amount greater than balance");
        }

        if (amount.signum() < 1) {
            throw new AccountDomainException("Cannot withdraw an amount equal to zero or a negative amount");
        }

        BigDecimal value = detailDto.balance().subtract(amount);

        service.withdraw(number, value);
    }
}
