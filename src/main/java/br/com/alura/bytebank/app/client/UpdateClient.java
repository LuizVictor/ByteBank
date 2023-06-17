package br.com.alura.bytebank.app.client;

import br.com.alura.bytebank.domain.client.Client;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;

public class UpdateClient {
    private final ClientRepository clientRepository;

    public UpdateClient(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(ClientDto data) {
        ListClient listClient = new ListClient(clientRepository);
        listClient.searchByCpf(data.cpf());

        ClientDto clientDto = new ClientDto(
                data.name(),
                data.cpf(),
                data.email()
        );

        clientRepository.update(new Client(clientDto));
    }
}
