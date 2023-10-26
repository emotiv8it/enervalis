package abb.interview.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import abb.interview.domain.Application;
import abb.interview.domain.Direction;
import abb.interview.domain.Measurement;
import abb.interview.domain.Power;

public abstract class TestHelper {

	private ObjectMapper mapper = new ObjectMapper();
	protected Application app;
	protected List<Measurement> measurements;

	@Before
	public void setUp() throws JsonProcessingException, IOException {
		app = new Application();
		measurements = new ArrayList<>();
	}
	
	// Helper to dynamically add measurements
	protected void addMeasurement(String group, String device, Direction dir, double min, double max, int amountPowers) {
		Measurement test = new Measurement();
		test.setDeviceGroup(group);
		test.setResourceId(String.format("id-%s", device));
		test.setDeviceName(String.format("device_%s", device));
		test.setDirection(dir);
		List<Power> powerlist = new ArrayList<>();
		for (int i = 0; i < amountPowers; i++) {
			powerlist.add(new Power(min, max, (min + max) / 2, System.currentTimeMillis()));
		}
		test.setPower(powerlist);
		measurements.add(test);
	}

	// Helper to load test data in application
	protected void loadMeasurements() {
		try {
			assertTrue(app.loadMeasurements(new ByteArrayInputStream(mapper.writeValueAsBytes(measurements))));
		} catch (JsonProcessingException e) {
			assertFalse("Should load JSON !", true);
		} catch (IOException e) {
			assertFalse("Should load JSON !", true);
		}
	}

}
