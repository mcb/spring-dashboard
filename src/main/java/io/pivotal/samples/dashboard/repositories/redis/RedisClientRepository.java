package io.pivotal.samples.dashboard.repositories.redis;

import io.pivotal.samples.dashboard.domain.Client;
import io.pivotal.samples.dashboard.domain.RandomIdGenerator;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RedisClientRepository implements CrudRepository<Client, String> {
    public static final String CLIENTS_KEY = "clients";

    private final RandomIdGenerator idGenerator;
    private final HashOperations<String, String, Client> hashOps;

    public RedisClientRepository(RedisTemplate<String, Client> redisTemplate) {
        this.hashOps = redisTemplate.opsForHash();
        this.idGenerator = new RandomIdGenerator();
    }

    @Override
    public <S extends Client> S save(S client) {
        if (client.getId() == null) {
            client.setId(idGenerator.generateId());
        }

        hashOps.put(CLIENTS_KEY, client.getId(), client);

        return client;
    }

    @Override
    public <S extends Client> Iterable<S> save(Iterable<S> clients) {
        List<S> result = new ArrayList<>();

        for (S entity : clients) {
            save(entity);
            result.add(entity);
        }

        return result;
    }

    @Override
    public Client findOne(String id) {
        return hashOps.get(CLIENTS_KEY, id);
    }

    @Override
    public boolean exists(String id) {
        return hashOps.hasKey(CLIENTS_KEY, id);
    }

    @Override
    public Iterable<Client> findAll() {
        return hashOps.values(CLIENTS_KEY);
    }

    @Override
    public Iterable<Client> findAll(Iterable<String> ids) {
        return hashOps.multiGet(CLIENTS_KEY, convertIterableToList(ids));
    }

    @Override
    public long count() {
        return hashOps.keys(CLIENTS_KEY).size();
    }

    @Override
    public void delete(String id) {
        hashOps.delete(CLIENTS_KEY, id);
    }

    @Override
    public void delete(Client client) {
        hashOps.delete(CLIENTS_KEY, client.getId());
    }

    @Override
    public void delete(Iterable<? extends Client> clients) {
        for (Client client : clients) {
            delete(client);
        }
    }

    @Override
    public void deleteAll() {
        Set<String> ids = hashOps.keys(CLIENTS_KEY);
        for (String id : ids) {
            delete(id);
        }
    }

    private <T> List<T> convertIterableToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T object : iterable) {
            list.add(object);
        }
        return list;
    }
}
