package server.business.mining;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import server.Application;
import server.business.mining.associators.FPGrowthModel;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class FPGrowthModelTest {

    FPGrowthModel fPGrowthModel;

    @Before
    public void init() throws Exception {
        fPGrowthModel = new FPGrowthModel();
    }

   
	@Test
    public void should_build_fpgrowth_model() {
        List<ArrayList<String>> miningRecords = new ArrayList<ArrayList<String>>();

        fPGrowthModel.buildModel(miningRecords, 0);
       // assert(fPGrowthModel.getRecommendations()).isEmpty();
    }
}
