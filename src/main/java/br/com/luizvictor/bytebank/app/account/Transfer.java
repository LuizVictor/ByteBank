package br.com.luizvictor.bytebank.app.account;

import br.com.luizvictor.bytebank.domain.account.AccountDetailDto;
import br.com.luizvictor.bytebank.domain.account.AccountDto;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.account.AccountService;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;

import java.math.BigDecimal;
import java.util.Objects;

public class Transfer {
    private final AccountRepository repository;
    private final AccountService service;

    public Transfer(AccountRepository accountRepository, AccountService accountService) {
        repository = accountRepository;
        service = accountService;
    }

    public void execute(Integer fromAccount, Integer toAccount, BigDecimal amount) {
        ListAccount listAccount = new ListAccount(repository);
        AccountDetailDto fromDetailDto = listAccount.searchByNumber(fromAccount);
        AccountDetailDto toDetailDto = listAccount.searchByNumber(toAccount);

        AccountDto from = new AccountDto(fromDetailDto.number(), fromDetailDto.client());
        AccountDto to = new AccountDto(toDetailDto.number(), toDetailDto.client());

        if (Objects.equals(from.number(), to.number())) {
            throw new AccountDomainException("Cannot transfer to the same account");
        }

        Withdraw withdraw = new Withdraw(repository, service);
        withdraw.execute(fromAccount, amount);

        Deposit deposit = new Deposit(repository, service);
        deposit.execute(toAccount, amount);
    }
}
