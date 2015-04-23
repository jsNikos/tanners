package server.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BulkUploadRepository extends MongoRepository<BulkUpload, String> {	
}
