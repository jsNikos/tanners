package server.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleSettingRepository extends MongoRepository<ScheduleSetting, String>{

}
