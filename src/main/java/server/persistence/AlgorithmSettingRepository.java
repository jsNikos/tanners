package server.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlgorithmSettingRepository extends MongoRepository<AlgorithmSetting, String> {	
}


