package br.com.alura.bytebank.app.client;

import br.com.alura.bytebank.domain.client.Client;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.exceptions.ClientNotFoundException;
import br.com.alura.bytebank.domain.client.ClientRepository;

import java.util.List;

public class ListClient {
    ClientRepository clientRepository;

    public ListClient(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientDto> list() {
        List<Client> clients = clientRepository.list();

        if (clients.isEmpty()) throw new ClientNotFoundException("There are no registered clients");

        return clients.stream().map(ClientDto::new).toList();
    }

    public ClientDto searchByCpf(String cpf) {
        Client client = clientRepository.searchByCpf(cpf);
        if (client == null) throw new ClientNotFoundException("There is no registered client with this CPF");

        return new ClientDto(client);
    }

    public ClientDto searchByEmail(String email) {
        Client client = clientRepository.searchByEmail(email);

        if (client == null) throw new ClientNotFoundException("There is no registered client with this email");

        return new ClientDto(client);
    }
}
