package server.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemDefinitionRepository extends MongoRepository<ItemDefinition, String> {
}
