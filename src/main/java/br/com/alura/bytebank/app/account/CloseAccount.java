package br.com.alura.bytebank.app.account;

import br.com.alura.bytebank.domain.account.Account;
import br.com.alura.bytebank.domain.account.AccountDetailDto;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.alura.bytebank.domain.account.AccountRepository;

import java.math.BigDecimal;

public class CloseAccount {
    private final AccountRepository accountRepository;

    public CloseAccount(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(Integer number) {
        ListAccount listAccount = new ListAccount(accountRepository);
        AccountDetailDto account = listAccount.searchByNumber(number);

        if (account.balance().compareTo(BigDecimal.ZERO) > 0) {
            throw new AccountDomainException("Cannot close account with balance");
        }

        AccountDto data = new AccountDto(account.number(), account.client());
        this.accountRepository.close(new Account(data));
    }
}
