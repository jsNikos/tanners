package server.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModelRepository extends MongoRepository<ModelCreate, String> {	
}
