package abb.interview.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;

import abb.interview.api.MaxPerDeviceAndGroup;
import abb.interview.domain.Direction;

/**
 * Test listing  all data entries for specific conditions
 */
public class TestListingOperations extends TestHelper {

	
	@Test
	public void testSingleGroupEntries() {
		addMeasurement("x", "a", Direction.IN, 10, 100, 1);
		addMeasurement("x", "b", Direction.OUT, 1, 50, 2);
		addMeasurement("x", "c", Direction.IN, 5, 200, 1);
		loadMeasurements();
		
		List<MaxPerDeviceAndGroup> list = app.getMeasurements().getMaxForAllDevices();
		
		assertEquals(list.size(), 3);
		assertEquals(list.get(0).getDeviceGroup(),"x");
		assertEquals(list.get(0).getDeviceName(),"device_a");
		assertEquals(list.get(0).getDirection(),Direction.IN);
		assertEquals(list.get(0).getMaxPower(),100);
		
		assertEquals(list.get(1).getDeviceGroup(),"x");
		assertEquals(list.get(1).getDeviceName(),"device_c");
		assertEquals(list.get(1).getDirection(),Direction.IN);
		assertEquals(list.get(1).getMaxPower(),200);
		
		assertEquals(list.get(2).getDeviceGroup(),"x");
		assertEquals(list.get(2).getDeviceName(),"device_b");
		assertEquals(list.get(2).getDirection(),Direction.OUT);
		assertEquals(list.get(2).getMaxPower(),50);
	}
	
	@Test
	public void testMultiGroupEntries() {
		addMeasurement("x", "a", Direction.IN, 10, 500, 1);
		addMeasurement("y", "b", Direction.OUT, 10, 100, 2);
		addMeasurement("z", "c", Direction.IN, 10, 200, 1);
		loadMeasurements();
		
		List<MaxPerDeviceAndGroup> list = app.getMeasurements().getMaxForAllDevices();
		
		assertEquals(list.size(), 3);
		assertEquals(list.get(0).getDeviceGroup(),"x");
		assertEquals(list.get(0).getDeviceName(),"device_a");
		assertEquals(list.get(0).getDirection(),Direction.IN);
		assertEquals(list.get(0).getMaxPower(),500);
		
		assertEquals(list.get(1).getDeviceGroup(),"y");
		assertEquals(list.get(1).getDeviceName(),"device_b");
		assertEquals(list.get(1).getDirection(),Direction.OUT);
		assertEquals(list.get(1).getMaxPower(),100);
		
		assertEquals(list.get(2).getDeviceGroup(),"z");
		assertEquals(list.get(2).getDeviceName(),"device_c");
		assertEquals(list.get(2).getDirection(),Direction.IN);
		assertEquals(list.get(2).getMaxPower(),200);
	}
	
	@Test
	public void testMultiGroupMultiEntries() {
		addMeasurement("x", "a", Direction.IN, 10, 500, 1);
		addMeasurement("y", "b", Direction.OUT, 10, 100, 2);
		addMeasurement("y", "c", Direction.IN, 10, 200, 1);
		addMeasurement("x", "a", Direction.OUT, 10, 200, 1);
		addMeasurement("y", "b", Direction.IN, 10, 500, 1);
		loadMeasurements();
		
		List<MaxPerDeviceAndGroup> list = app.getMeasurements().getMaxForAllDevices();
		
		assertEquals(list.size(), 5);
		
		assertEquals(list.get(0).getDeviceGroup(),"x");
		assertEquals(list.get(0).getDeviceName(),"device_a");
		assertEquals(list.get(0).getDirection(),Direction.IN);
		assertEquals(list.get(0).getMaxPower(),500);
		
		assertEquals(list.get(1).getDeviceGroup(),"x");
		assertEquals(list.get(1).getDeviceName(),"device_a");
		assertEquals(list.get(1).getDirection(),Direction.OUT);
		assertEquals(list.get(1).getMaxPower(),200);
		
		assertEquals(list.get(2).getDeviceGroup(),"y");
		assertEquals(list.get(2).getDeviceName(),"device_c");
		assertEquals(list.get(2).getDirection(),Direction.IN);
		assertEquals(list.get(2).getMaxPower(),200);
		
		assertEquals(list.get(3).getDeviceGroup(),"y");
		assertEquals(list.get(3).getDeviceName(),"device_b");
		assertEquals(list.get(3).getDirection(),Direction.IN);
		assertEquals(list.get(3).getMaxPower(),500);
		
		assertEquals(list.get(4).getDeviceGroup(),"y");
		assertEquals(list.get(4).getDeviceName(),"device_b");
		assertEquals(list.get(4).getDirection(),Direction.OUT);
		assertEquals(list.get(4).getMaxPower(),100);
		
	}
	
	
}
