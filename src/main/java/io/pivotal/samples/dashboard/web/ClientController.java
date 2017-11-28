package io.pivotal.samples.dashboard.web;

import io.pivotal.samples.dashboard.domain.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private CrudRepository<Client, String> repository;

    @Autowired
    public ClientController(CrudRepository<Client, String> repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Client> clients() {
        return repository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Client add(@RequestBody @Valid Client client) {
        logger.info("Adding client " + client.getId());
        return repository.save(client);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Client update(@RequestBody @Valid Client client) {
        logger.info("Updating client " + client.getId());
        return repository.save(client);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Client getById(@PathVariable String id) {
        logger.info("Getting client " + id);
        return repository.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable String id) {
        logger.info("Deleting client " + id);
        repository.delete(id);
    }
}