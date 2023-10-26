package abb.interview.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import abb.interview.api.MaxPerDeviceAndGroup;
import abb.interview.api.MeasurementStatistics;
import abb.interview.api.TotalPerGroup;
import abb.interview.api.TotalPerGroup.TotalType;

/**
 * Container  class that holds the loaded measurements 
 * from external source, and provides statistical methods on the data
 * 
 */
public class Measurements extends HashMap<Key, List<Measurement>> implements MeasurementStatistics {
	/* Comment Peter De Leuze:  changed the mapping signature from Key/Measurement to Key/List<Measurement> */
	/*  otherwise, duplicate recorded entries would be overwritten for different Direction  */
	
    private static final long serialVersionUID = 1L;

    /**
     * Find the measurement based on the {@link Key}
     * 
     * @param key {@link Key} the unique key
     * @return {@link Measurement} or null depending on match.
     */
	public List<Measurement> find(Key key) {
        return get(key);
    }
       
	/**
	 * Returns the totals per device group, for both IN and OUT based on the AVERAGE of all power
	 * 
	 * @return List of {@link TotalPerGroup}
	 */
    public List<TotalPerGroup> getTotalsPerGroup(){
    	return getTotalsPerGroup(TotalType.AVG);
    }
    
    /**
     * Returns the totals per device group, for both IN and OUT based on the requested {@link TotalType}
     * (Avg, Min, Max)
     * 
     * @param type {@link TotalType} type of summing  (default = average)
     * @return List of {@link TotalType}
     */
	public List<TotalPerGroup> getTotalsPerGroup(TotalType type){
    	List<TotalPerGroup> totals = new ArrayList<>();
    	
    	List<Measurement> temp = entrySet().stream().flatMap(entry -> entry.getValue().stream()).collect(Collectors.toList());

    	getAllGroupNames().forEach(name -> {
    		totals.add(new TotalPerGroup(name).calculateTotals(temp.stream().filter(x -> name.equalsIgnoreCase(x.getKey().getDeviceGroup())).collect(Collectors.toList()),type));
    	});
    	return totals;
    }
    
	/**
	 * Return all the unique groupnames that are present
	 * @return Set of groupnames
	 */
    public Set<String> getAllGroupNames() {
    	Set<String> groupNames = keySet().stream().map(e -> e.getDeviceGroup()).sorted().collect(Collectors.toSet());
    	return groupNames;
    }
    
    
    /**
     * 
     * @return
     */
	public List<MaxPerDeviceAndGroup> getMaxForAllDevices() {
		List<MaxPerDeviceAndGroup> maxList = new ArrayList<>();
		
		List<String> deviceNames = keySet().stream().map(e -> e.getDeviceName()).sorted(String::compareTo).collect(Collectors.toList());
	
		for (String name : deviceNames) {
	    	
			List<Measurement> measurements = entrySet().stream().flatMap(entry -> entry.getValue().stream()).filter(x -> name.equalsIgnoreCase(x.getKey().getDeviceName()))
					.collect(Collectors.toList());
	    	
			for (Direction dir : Direction.values()) {

				Map<Key, Optional<Measurement>> measurementsWithMaxPower = measurements.stream()
						.filter(x -> x.getDirection() == dir)
						.collect(Collectors.groupingBy(
								Measurement::getKey,
								Collectors.maxBy(Comparator.comparingDouble(m ->
								m.getPower().stream().mapToDouble(Power::getMax).max().orElse(Double.NEGATIVE_INFINITY))
										)));

				if(!measurementsWithMaxPower.isEmpty() && !measurementsWithMaxPower.values().isEmpty()) {
					Measurement m = measurementsWithMaxPower.values().stream().findFirst().get().get();
					Optional<Power> maxPower = m.getPower().stream().collect(Collectors.maxBy(Comparator.comparingDouble(x -> x.getMax())));
					maxList.add(new MaxPerDeviceAndGroup(m, maxPower.get()));
				}
				
			}
			Collections.sort(maxList, new Comparator<MaxPerDeviceAndGroup>() {
				@Override
				public int compare(MaxPerDeviceAndGroup o1, MaxPerDeviceAndGroup o2) {
					int c = o1.getDeviceGroup().compareTo(o2.getDeviceGroup());
					if(c==0) c = o1.getDirection().compareTo(o2.getDirection());
					if(c==0) c = (int) Math.floor(o1.getMaxPower() - o2.getMaxPower());
					if(c==0) c = o1.getDeviceName().compareTo(o2.getDeviceName());
					return c;
				}
			});
			
		}
		
		
		return maxList;
	}

    
}
