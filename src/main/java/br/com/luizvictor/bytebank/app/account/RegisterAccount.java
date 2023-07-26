package br.com.luizvictor.bytebank.app.account;

import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.luizvictor.bytebank.app.client.ListClient;
import br.com.luizvictor.bytebank.domain.account.Account;
import br.com.luizvictor.bytebank.domain.account.AccountDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;

public class RegisterAccount {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public RegisterAccount(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public void execute(AccountDto data) {
        ListClient listClient = new ListClient(clientRepository);
        listClient.searchByCpf(data.client().cpf());

        Account hasAccount = accountRepository.searchByNumber(data.number());

        if (hasAccount != null) {
            throw new AccountDomainException("There is already an account registered with this number");
        }

        accountRepository.register(new Account(data));
    }
}
