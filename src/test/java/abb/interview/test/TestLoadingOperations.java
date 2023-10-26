/**
 * 
 */
package abb.interview.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import abb.interview.domain.Application;
import abb.interview.domain.Key;
import abb.interview.domain.Measurement;
import abb.interview.domain.Power;

/**
 * Test loading of data into model
 */
public class TestLoadingOperations {

	
	@Test
	public void testJsonLoadingNullSource() throws IOException {
		Application application = new Application();
		InputStream in = null;
		Exception exception = assertThrows(IOException.class, () -> application.loadMeasurements(in));
		assertEquals("Failed to read null input stream",exception.getMessage());
	}
	
	@Test
	public void testJsonLoadingInvalidSource() throws IOException{
		Application application = new Application();
		InputStream dummy = new ByteArrayInputStream("this is not a json".getBytes());
		Exception exception = assertThrows(IOException.class, () -> application.loadMeasurements(dummy));
		assertEquals("Failed to load any measurements due to JSON parsing error",exception.getMessage());		
	}
	
	@Test
	public void testJsonLoadingInvalidObjectSource() throws IOException{
		Application application = new Application();
		InputStream dummy = new ByteArrayInputStream("{}".getBytes());
		Exception exception = assertThrows(IOException.class, () -> application.loadMeasurements(dummy));
		assertEquals("Failed to load any measurements due to JSON mapping error",exception.getMessage());
	}
	
	@Test
	public void testJsonLoadingInvalidMappingSource() throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		List<Power> x = new ArrayList<>();
		x.add(new Power(0d,1d,2d,123456l));
		String json = mapper.writeValueAsString(x);
		
		Application application = new Application();
		InputStream dummy = new ByteArrayInputStream(json.getBytes());
		Exception exception = assertThrows(IOException.class, () -> application.loadMeasurements(dummy));
		assertEquals("Failed to load any measurements due to JSON mapping error",exception.getMessage());
	}
	
	@Test
	public void testJsonLoadingEmptySource() throws IOException{
		Application application = new Application();
		InputStream dummy = new ByteArrayInputStream("[]".getBytes());
		boolean loaded = application.loadMeasurements(dummy);
		assertTrue(loaded == true);
		assertEquals(application.getMeasurements().size(), 0);
	}
	
	@Test
	public void testJsonLoadingExpectedSource() throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		List<Measurement> x = new ArrayList<>(); 
		Measurement test = new Measurement();
		test.setDeviceGroup("devicegroup_z");
		test.setDeviceName("devicename_y");
		test.setResourceId("resourceid_x");
		x.add(test);
		String json = mapper.writeValueAsString(x);
		
		Application application = new Application();
		InputStream dummy = new ByteArrayInputStream(json.getBytes());
		boolean loaded = application.loadMeasurements(dummy);
		// check loaded 
		assertTrue(loaded == true);
		assertEquals(application.getMeasurements().size(), 1);
		
		// check result
		Key toBeFound = new Key("resourceid_x", "devicename_y", "devicegroup_z");
		assertNotNull(application.getMeasurements().find(toBeFound));
		List<Measurement> hits = application.getMeasurements().find(toBeFound);
		assertEquals(hits.size(),1);
		for (Measurement measurement : hits) {
			assertTrue(measurement.getKey().toString().equals(toBeFound.toString()));			
		}
		
		// check no other
		assertNull(application.getMeasurements().find(new Key("x","y","z")));
	}
	
	@Test
	public void testGetAllGroupNames()throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<Measurement> x = new ArrayList<>(); 
		Measurement test = new Measurement();
		test.setDeviceGroup("devicegroup_z");
		test.setDeviceName("devicename_y");
		test.setResourceId("resourceid_x");
		x.add(test);
		test = new Measurement();
		test.setDeviceGroup("devicegroup_a");
		test.setDeviceName("devicename_b");
		test.setResourceId("resourceid_c");
		x.add(test);
		String json = mapper.writeValueAsString(x);
		
		Application application = new Application();
		InputStream dummy = new ByteArrayInputStream(json.getBytes());
		boolean loaded = application.loadMeasurements(dummy);
		// check loaded for 2 separate device groups 
		assertTrue(loaded == true);
		assertEquals(application.getMeasurements().size(), 2);	
	}
	
}
