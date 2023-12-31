package br.com.luizvictor.bytebank.app.client;

import br.com.luizvictor.bytebank.domain.client.Client;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;

public class RemoveClient {
    private final ClientRepository clientRepository;

    public RemoveClient(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(String cpf) {
        ListClient listClient = new ListClient(clientRepository);
        ClientDto clientDto = listClient.searchByCpf(cpf);

        clientRepository.remove(new Client(clientDto));
    }
}
