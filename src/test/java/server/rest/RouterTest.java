package server.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import server.Application;
import server.business.MiningDataService;
import server.persistence.ItemDefinition;
import server.persistence.MiningRecord;
import server.persistence.MiningRecordRepository;
import utils.JSON;

import java.net.URL;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class RouterTest {

    @Value("${local.server.port}")
    private int port;
    @Autowired(required=true)
    private WebApplicationContext application;
    @Autowired(required=true)
    private MiningRecordRepository miningRecordRepository;
    @Autowired(required=true)
    private MiningDataService miningDataService;
    
	private URL base;
	private MockMvc mockMvc;	
	
	@Before
	public void setUp() throws Exception {		
		this.base = new URL("http://localhost:" + port + "/router/");
		mockMvc = MockMvcBuilders.webAppContextSetup(this.application).build();
		ensureItemDefinition();
	}

	@Test
	public void addMiningData() throws Exception {		
		miningRecordRepository.deleteAll();		
		List<MiningRecord> records = new ArrayList<>();
		
		// record1
		List<Map<String, Object>> items = new ArrayList<>();
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("name", "test asset");
		item.put("quality", "good");
		item.put("displayName", "Kartoffel");
		items.add(item);		
		records.add(new MiningRecord(items, new Date()));		
		
		mockMvc.perform(MockMvcRequestBuilders
			   .post(base+"/addMiningData").contentType(MediaType.APPLICATION_JSON).content(JSON.stringify(records)))			   
			   .andExpect(MockMvcResultMatchers.status().isOk());
		
		miningRecordRepository.deleteAll();
	}	
	
	private void ensureItemDefinition() throws Exception{
		List<String> keyAttr = new ArrayList<>();
		keyAttr.add("name");	
		miningDataService.addItemDefinition(new ItemDefinition(keyAttr, "displayName"));		
	}
	
	@Test
	public void findItemSets(){
		//TODO nikos needs a test
	}
}