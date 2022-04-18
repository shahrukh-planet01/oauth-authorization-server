package net.planet01.oauthauthorizationserver.repository;

import net.planet01.oauthauthorizationserver.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findClientByClientName(String u);
}
