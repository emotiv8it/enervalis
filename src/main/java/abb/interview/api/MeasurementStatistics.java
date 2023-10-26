package abb.interview.api;

import java.util.List;

import abb.interview.api.TotalPerGroup.TotalType;
import abb.interview.domain.Direction;
import abb.interview.domain.Key;
import abb.interview.domain.Measurement;

public interface MeasurementStatistics {

	/**
	 * Return a specific list of {@link Measurement} for a given {@link Key}.
	 * If no match is found, will return null
	 * 
	 * @param key {@link Key}
	 * @return List of multiple {@link Measurement} or null
	 */
	List<Measurement> find(Key key);

	
	/**
	 * return amount of entries
	 * @return int displaying the occupation
	 */
	int size();
	
	
	/**
	 * Get a list of all totals, per device group, for all {@link Direction}.
	 * This will be the total of the AVERAGE power
	 * @return List of {@link TotalPerGroup} 
	 */
	List<TotalPerGroup> getTotalsPerGroup();
	
	/**
	 * Get a list of all totals, per device group, for all {@link Direction}.
	 * This will be the total of the specific type of power  (See {@link TotalType})
	 * 
	 * @param type {@link TotalType} requested type of power
	 * @return List of {@link TotalPerGroup} 
	 */
	List<TotalPerGroup> getTotalsPerGroup(TotalType type);


	/**
	 * Return an ordered list of all devices grouped by groupname, direction, and max power ascending
	 * @return List of {@link MaxPerDeviceAndGroup}
	 */
	List<MaxPerDeviceAndGroup> getMaxForAllDevices();
	
	

}
