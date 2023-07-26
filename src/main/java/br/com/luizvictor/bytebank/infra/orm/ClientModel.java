package br.com.luizvictor.bytebank.infra.orm;

import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientUpdateDto;
import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class ClientModel {
    @Id
    private String cpf;
    private String name;
    private String email;

    public ClientModel(ClientDto data) {
        this.name = data.name();
        this.cpf = data.cpf();
        this.email = data.email();
    }

    public ClientModel() {

    }

    public String name() {
        return name;
    }

    public String cpf() {
        return cpf;
    }

    public String email() {
        return email;
    }

    public void update(ClientUpdateDto data) {
        if (data.name() != null) {
            this.name = data.name();
        }

        if (data.email() != null) {
            this.email = data.email();
        }
    }
}