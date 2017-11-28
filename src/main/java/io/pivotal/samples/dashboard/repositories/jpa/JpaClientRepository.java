package io.pivotal.samples.dashboard.repositories.jpa;

import io.pivotal.samples.dashboard.domain.Client;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"in-memory", "mysql", "postgres", "oracle", "sqlserver"})
public interface JpaClientRepository extends JpaRepository<Client, String> {
}
