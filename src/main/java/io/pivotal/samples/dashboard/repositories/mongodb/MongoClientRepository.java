package io.pivotal.samples.dashboard.repositories.mongodb;

import io.pivotal.samples.dashboard.domain.Client;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("mongodb")
public interface MongoClientRepository extends MongoRepository<Client, String> {
}