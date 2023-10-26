package abb.interview.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import abb.interview.domain.Direction;
import abb.interview.domain.Measurement;
import abb.interview.domain.Power;

/**
 * Wrapper class that holds the statistical totals
 * for the power direction (IN/OUT) of a specific group of devices
 */
public class TotalPerGroup {
	
	/**
	 * Type of total that is calculated over either (Average,Minimal,Maximum) 
	 */
	public static enum TotalType { MIN, MAX, AVG };
	
	
	private final String deviceGroup;
	private final Map<Direction,Double> powerPerDirection;
	
	
	public TotalPerGroup(String deviceGroup) {
		assert(deviceGroup != null);
		this.deviceGroup = deviceGroup;
		powerPerDirection = new HashMap<Direction, Double>();
	}
	
	public TotalPerGroup(String deviceGroup, List<Measurement> measurements) {
		this(deviceGroup);
		calculateTotals(measurements, TotalType.AVG);
	}
	
	public String getDeviceGroup() {
		return deviceGroup;
	}
	
	public Map<Direction, Double> getPowerPerDirection() {
		return powerPerDirection;
	}
	
	
	/**
	 * Calculate the total of all power numbers, specific for a given {@link TotalType}(avg,min,max)
	 * 
	 * @param measurements List of all {@link Measurement}
	 * @param type {@link TotalType}
	 * @return {@link TotalPerGroup}  containing per {@link Direction}
	 */
	public TotalPerGroup calculateTotals(List<Measurement> measurements, TotalType type) {
		powerPerDirection.clear();
		if(measurements!=null) {
			Map<Direction, Double> totals = measurements.stream().filter(measurement -> (deviceGroup.equalsIgnoreCase(measurement.getDeviceGroup())))
			.collect(Collectors.groupingBy(
					Measurement::getDirection,
					getTotalSumPerTypeOfTotal(type)));
			
			powerPerDirection.putAll(totals);
		}
		return this;
	}


	/**
	 * Apply based on {@link TotalType} the used field to summing the total
	 */
	private Collector<Measurement, ?, Double> getTotalSumPerTypeOfTotal(TotalType type) {
		switch(type) {
			default:
			case AVG:
				return Collectors.summingDouble(m -> trimDecimals(m.getPower().stream().mapToDouble(Power::getAvg).sum()));
			case MAX:
				return Collectors.summingDouble(m -> trimDecimals(m.getPower().stream().mapToDouble(Power::getMax).sum()));
			case MIN:
				return Collectors.summingDouble(m -> trimDecimals(m.getPower().stream().mapToDouble(Power::getMin).sum()));
		}
	}
	


	private double trimDecimals(double sum) {
		return (Math.round(sum * 10000d))/10000d;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("DeviceGroup: ")
				.append(deviceGroup)
				.append(", Totals: ")
				.append(powerPerDirection.entrySet().stream().map(e->  String.format("%s : %.4f",e.getKey(),e.getValue())).collect(Collectors.joining(", "))).toString();
	}
}
