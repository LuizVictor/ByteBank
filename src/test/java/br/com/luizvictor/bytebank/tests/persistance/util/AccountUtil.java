package br.com.luizvictor.bytebank.tests.persistance.util;

import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.RegisterAccount;
import br.com.luizvictor.bytebank.domain.account.AccountDetailDto;
import br.com.luizvictor.bytebank.domain.account.AccountDto;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AccountUtil {
    public static void create(EntityManager entityManager, AccountRepository accountRepository) {
        ClientRepository clientRepository = RepositoryUtil.clientRepository();
        ClientUtil.create(entityManager, clientRepository);

        AccountDto dto1 = new AccountDto(1234, ClientUtil.list(clientRepository, "123.123.123-12"));
        AccountDto dto2 = new AccountDto(4321, ClientUtil.list(clientRepository, "123.123.123-21"));

        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);
        registerAccount.execute(dto1);
        registerAccount.execute(dto2);

        entityManager.flush();
    }

    public static AccountDetailDto list(AccountRepository accountRepository, Integer number) {
        ListAccount list = new ListAccount(accountRepository);
        return list.searchByNumber(number);
    }

    public static List<AccountDetailDto> all(AccountRepository accountRepository) {
        ListAccount listAccount = new ListAccount(accountRepository);
        return listAccount.list();
    }
}
