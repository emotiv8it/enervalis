package abb.interview.api;

import abb.interview.domain.Direction;
import abb.interview.domain.Measurement;
import abb.interview.domain.Power;

/**
 * Wrapper statistics object for listing all entries with max power
 */
public class MaxPerDeviceAndGroup {

	private String deviceName;
	private String deviceId;
	private String deviceGroup;
	private Direction direction;
	private Power power;
	
	public MaxPerDeviceAndGroup(Measurement m, Power power) {
		deviceName=m.getDeviceName();
		deviceGroup=m.getDeviceGroup();
		deviceId = m.getResourceId().replace("-", "");
		direction=m.getDirection();
		this.power = power;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getDeviceGroup() {
		return deviceGroup;
	}

	public Power getPower() {
		return power;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public double getMaxPower() {
		return Math.round(10000 * power.getMax())/10000d;
	}
	
	@Override
	public String toString() {
		return String.format("%-10s: %s, %s, %-3s, %.4f", this.deviceName,this.deviceId, this.deviceGroup, this.direction, getMaxPower());
	}
	
}
