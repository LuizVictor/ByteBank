package br.com.luizvictor.bytebank.app.account;

import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.luizvictor.bytebank.app.client.ListClient;
import br.com.luizvictor.bytebank.domain.account.Account;
import br.com.luizvictor.bytebank.domain.account.AccountDto;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;

public class RegisterAccount {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public RegisterAccount(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public void execute(ClientDto clientDto) {
        searchClient(clientDto);
        Account account = new Account(clientDto);
        searchAccount(account);

        accountRepository.register(account);
    }

    private void searchClient(ClientDto clientDto) {
        ListClient listClient = new ListClient(clientRepository);
        listClient.searchByCpf(clientDto.cpf());
    }

    private void searchAccount(Account account) {
        if (accountRepository.searchByNumber(account.number()) != null) {
            throw new AccountDomainException("There is already an account registered with this number");
        }
    }
}
