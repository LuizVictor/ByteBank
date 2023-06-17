package br.com.alura.bytebank.infra.client;

import br.com.alura.bytebank.domain.client.Client;
import br.com.alura.bytebank.domain.client.ClientRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class ClientRepositoryMemory implements ClientRepository {
    private final List<Client> clients = new ArrayList<>();

    @Override
    public void register(Client client) {
        this.clients.add(client);
    }

    @Override
    public List<Client> list() {
        return this.clients;
    }

    @Override
    public Client searchByCpf(String cpf) {
        return this.clients
                .stream()
                .filter(client -> client.cpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Client searchByEmail(String email) {
        return this.clients
                .stream()
                .filter(client -> client.email().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Client client) {
        var index = IntStream.range(0, clients.size())
                .filter(i -> clients.get(i)
                        .cpf()
                        .equals(client.cpf()))
                .findFirst();

        clients.set(index.getAsInt(), client);
    }

    @Override
    public void remove(Client client) {
        client = searchByCpf(client.cpf());
        clients.remove(client);
    }
}
