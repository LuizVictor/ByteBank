package br.com.luizvictor.bytebank.tests.persistance.util;

import br.com.luizvictor.bytebank.app.client.ListClient;
import br.com.luizvictor.bytebank.app.client.RegisterClient;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import jakarta.persistence.EntityManager;

public class ClientUtil {
    public static void create(EntityManager entityManager, ClientRepository repository) {
        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto clientDto2 = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");
        RegisterClient registerClient = new RegisterClient(repository);
        entityManager.getTransaction().begin();
        registerClient.execute(clientDto);
        registerClient.execute(clientDto2);
        entityManager.flush();
    }

    public static ClientDto list(ClientRepository repository, String cpf) {
        ListClient listClient = new ListClient(repository);
        return listClient.searchByCpf(cpf);
    }
}
