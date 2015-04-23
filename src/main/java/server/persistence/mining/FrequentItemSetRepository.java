package server.persistence.mining;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FrequentItemSetRepository extends MongoRepository<FrequentItemSet, String> {
    //public int getSupport(ArrayList<String> items);
}



