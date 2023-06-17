package br.com.alura.bytebank.app.account;

import br.com.alura.bytebank.domain.account.*;
import br.com.alura.bytebank.domain.account.exceptions.AccountDomainException;

import java.math.BigDecimal;
import java.util.Objects;

public class Transfer {
    private final AccountRepository repository;
    private final TransferService transfer;

    public Transfer(AccountRepository accountRepository, TransferService transferService) {
        repository = accountRepository;
        transfer = transferService;
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

        transfer.execute(new Account(from), new Account(to), amount);
    }
}
