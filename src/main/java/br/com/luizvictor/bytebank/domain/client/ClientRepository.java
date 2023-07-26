package br.com.luizvictor.bytebank.domain.client;

import java.util.List;

public interface ClientRepository {
    void register(Client data);

    List<Client> list();

    Client searchByCpf(String cpf);

    Client searchByEmail(String email);

    void remove(Client client);

    void update(Client client);
}
