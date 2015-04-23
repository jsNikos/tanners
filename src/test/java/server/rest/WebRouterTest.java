package server.rest;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






import org.junit.Assert;
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
import server.persistence.BulkUpload;
import server.persistence.BulkUploadRepository;
import server.persistence.ItemDefinition;
import server.persistence.MiningRecord;
import server.persistence.MiningRecordRepository;
import utils.JSON;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class WebRouterTest {

    @Value("${local.server.port}")
    private int port;
    @Autowired(required=true)
    private WebApplicationContext application;   
    @Autowired(required=true)
    private MiningDataService miningDataService;
    @Autowired(required=true)
    private BulkUploadRepository bulkUploadRepository;
    @Autowired(required=true)
    private MiningRecordRepository miningRecordRepository;
       
	private URL base;
	private MockMvc mockMvc;	
	
	@Before
	public void setUp() throws Exception {		
		this.base = new URL("http://localhost:" + port + "/webrouter/");
		mockMvc = MockMvcBuilders.webAppContextSetup(this.application).build();
	}

	@Test
	public void addItemDefinition() throws Exception {		
		List<String> keyAttr = new ArrayList<>();
		keyAttr.add("name");	
		ItemDefinition itemDef = new ItemDefinition(keyAttr, "displayName");		
		
		mockMvc.perform(MockMvcRequestBuilders
			   .post(base+"/addItemDefinition").contentType(MediaType.APPLICATION_JSON).content(JSON.stringify(itemDef)))			   
			   .andExpect(MockMvcResultMatchers.status().isOk());
		
		ItemDefinition itemDefWrong = new ItemDefinition();
		mockMvc.perform(MockMvcRequestBuilders
				   .post(base+"/addItemDefinition").contentType(MediaType.APPLICATION_JSON).content(JSON.stringify(itemDefWrong)))			   
				   .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}	
	
	@Test
	public void findItemDefinition() throws Exception {				
		mockMvc.perform(MockMvcRequestBuilders.get(base+"/findItemDefinition"))
			   .andExpect(MockMvcResultMatchers.status().isOk());
	}	
	
	@Test
	public void uploadMiningData() throws Exception{
		List<Map<String, Object>> items = new ArrayList<>();
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("name", "test asset");
		item.put("quality", "good");
		item.put("displayName", "Kartoffel");
		items.add(item);
		List<MiningRecord> records = new ArrayList<>();
		for(int i = 0; i < 1000; i++){
			records.add(new MiningRecord(items, new Date()));
		}
		String fileContent = JSON.stringify(records);
		
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(base+"/uploadMiningData").file("file", fileContent.getBytes()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo((mvcResult) -> {
			BulkUpload bulkUpload = JSON.parse(mvcResult.getResponse().getContentAsString(), BulkUpload.class);			
			Thread.sleep(5000); // what the async task to have finished
				
			countRecordsSince();
			removeMiningData(bulkUpload);
		});
	}
	
	private void countRecordsSince(){
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2007);
		Assert.assertTrue(miningDataService.countRecordsSince(calendar.getTime()) > 0);
		Assert.assertTrue(miningDataService.countRecordsSince(null) > 0);
	}
	
	private void removeMiningData(BulkUpload bulkUpload) throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post(base+"/removeMiningData")
				        .content(JSON.stringify(bulkUpload))
				        .contentType(MediaType.APPLICATION_JSON))
			   .andExpect(MockMvcResultMatchers.status().isOk());
		Assert.assertNull(bulkUploadRepository.findOne(bulkUpload.getId()));
		Assert.assertTrue(miningRecordRepository.findByBulkUploadId(bulkUpload.getId()).isEmpty());
	}
	
	@Test
	public void findBulkUploads() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get(base+"/findBulkUploads"))
			   .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void removeAllMiningData() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post(base+"/removeAllMiningData"))
			   .andExpect(MockMvcResultMatchers.status().isOk());
		Assert.assertTrue(miningDataService.findData().isEmpty());
		Assert.assertTrue(miningDataService.findBulkUploads().isEmpty());
	}
	
	@Test
	public void cancelBulkUpload() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post(base+"/cancelBulkUpload"))
			   .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void findMiningChartData() throws Exception {				
		mockMvc.perform(MockMvcRequestBuilders.get(base+"/findMiningChartData"))
			   .andExpect(MockMvcResultMatchers.status().isOk());
	}	
	
	@Test
	public void cancelModelCreate() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post(base+"/cancelModelCreate"))
			   .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void triggerModelCreate() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post(base+"/triggerModelCreate"))
			   .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
}