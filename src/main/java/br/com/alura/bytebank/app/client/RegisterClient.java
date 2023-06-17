package br.com.alura.bytebank.app.client;

import br.com.alura.bytebank.domain.client.Client;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.domain.client.exceptions.ClientDomainException;

public class RegisterClient {
    private final ClientRepository clientRepository;

    public RegisterClient(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(ClientDto data) {
        Client hasClientCpf = clientRepository.searchByCpf(data.cpf());
        Client hasClientEmail = clientRepository.searchByEmail(data.email());

        if (hasClientCpf != null)
            throw new ClientDomainException("There is already a client registered with this CPF");

        if (hasClientEmail != null)
            throw new ClientDomainException("There is already a client registered with this email");

        clientRepository.register(new Client(data));
    }
}
