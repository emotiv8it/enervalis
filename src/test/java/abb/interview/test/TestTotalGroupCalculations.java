package abb.interview.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import abb.interview.api.TotalPerGroup;
import abb.interview.api.TotalPerGroup.TotalType;
import abb.interview.domain.Application;
import abb.interview.domain.Direction;


/**
 * Test totals calculation in groups
 */
public class TestTotalGroupCalculations extends TestHelper {
	
	
	@Test
	public void testSingleExpectedAvgPower() {
		addMeasurement("x","a",Direction.IN,10,100, 1);
		loadMeasurements();
		
		List<TotalPerGroup> total = app.getMeasurements().getTotalsPerGroup();
		assertEquals(total.size(), 1);
		assertEquals(total.get(0).getDeviceGroup(), "x");
		Map<Direction,Double> result = total.get(0).getPowerPerDirection();
		assertEquals(result.get(Direction.IN), 55 );
	}
	
	@Test
	public void testSingleExpectedMaxPower() {
		addMeasurement("x","b",Direction.OUT,10,100, 1);
		loadMeasurements();
		
		List<TotalPerGroup> total = app.getMeasurements().getTotalsPerGroup(TotalType.MAX);
		assertEquals(total.size(), 1);
		assertEquals(total.get(0).getDeviceGroup(), "x");
		Map<Direction,Double> result = total.get(0).getPowerPerDirection();
		assertEquals(result.get(Direction.OUT), 100 );
	}
	
	@Test
	public void testSingleExpectedMinPower() {
		addMeasurement("x","a",Direction.OUT,10,100, 1);
		addMeasurement("x","a",Direction.IN,1,10, 1);
		loadMeasurements();
		
		List<TotalPerGroup> total = app.getMeasurements().getTotalsPerGroup(TotalType.MIN);
		assertEquals(total.size(), 1);
		assertEquals(total.get(0).getDeviceGroup(), "x");
		Map<Direction,Double> result = total.get(0).getPowerPerDirection();
		assertEquals(result.get(Direction.IN), 1 );
		assertEquals(result.get(Direction.OUT), 10 );
	}
	
	@Test
	public void testMultiExpectedAvgPower() {
		addMeasurement("x","a",Direction.OUT,10,100, 3); // average = 55 * 3
		addMeasurement("x","b",Direction.IN,2,10, 2);    // average = 6 * 2 
		loadMeasurements();
		
		List<TotalPerGroup> total = app.getMeasurements().getTotalsPerGroup(TotalType.AVG);
		assertEquals(total.size(), 1);
		assertEquals(total.get(0).getDeviceGroup(), "x");
		Map<Direction,Double> result = total.get(0).getPowerPerDirection();
		assertEquals(result.get(Direction.IN), 2*(2+10)/2 );
		assertEquals(result.get(Direction.OUT), 3*(10+100)/2 );
	}
		
	
	@Test
	public void testMultiGroupsMaxPower() {
		addMeasurement("x","a",Direction.OUT,10,100, 3);  // 300
		addMeasurement("x","b",Direction.IN,2,10, 2);      // 20
		addMeasurement("y","c",Direction.OUT,20,50, 5);    // 250
		addMeasurement("y","c",Direction.IN,10,70, 2);     // 140
		loadMeasurements();
		
		List<TotalPerGroup> total = app.getMeasurements().getTotalsPerGroup(TotalType.MAX);
		assertEquals(total.size(), 2);
		total.forEach(x -> {
			if("x".equals(x.getDeviceGroup())) {
				assertEquals(x.getPowerPerDirection().get(Direction.OUT),300);
				assertEquals(x.getPowerPerDirection().get(Direction.IN),20);
			}
			if("y".equals(x.getDeviceGroup())) {
				assertEquals(x.getPowerPerDirection().get(Direction.OUT),250);
				assertEquals(x.getPowerPerDirection().get(Direction.IN),140);				
			}
		});
	}

	
	@Test
	public void testExpectedToBeEmpty() throws IOException {
		// reset & reinit clean...
		app = new Application();
		app.loadMeasurements(new ByteArrayInputStream("[]".getBytes()));
		// should still work, and be empty
		List<TotalPerGroup> total = app.getMeasurements().getTotalsPerGroup();
		assertEquals(total.size(), 0);
	}
	
}
