package server.persistence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MiningRecordRepository extends MongoRepository<MiningRecord, String> {
	public Long deleteByBulkUploadId(String bulkUploadId);
	public List<MiningRecord> findByBulkUploadId(String bulkUploadId);
}
