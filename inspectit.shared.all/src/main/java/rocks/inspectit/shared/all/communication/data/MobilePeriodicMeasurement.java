package rocks.inspectit.shared.all.communication.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import rocks.inspectit.shared.all.communication.DefaultData;

/**
 * 
 * @author Tobias Angerstein, Alper Hidiroglu, Manuel Palenga
 *
 */
public class MobilePeriodicMeasurement extends DefaultData {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -8195087403345499757L;

	@JsonIgnore
	private long deviceID;
	
	@JsonProperty
	private long timestamp;
	
	@JsonProperty
	private double batteryPower;
	
	@JsonProperty
	private double cpuUsage;
	
	@JsonProperty
	private double memoryUsage;
	
	@JsonProperty
	private double storageUsage;

	/**
	 * Constructor.
	 * 
	 * @param timestamp
	 * @param battery power
	 * @param cpu usage
	 * @param memory usage
	 */
	public MobilePeriodicMeasurement(long timestamp, double batteryPower, double cpuUsage, double memoryUsage, double storageUsage) {
		this.timestamp = timestamp;
		this.batteryPower = batteryPower;
		this.cpuUsage = cpuUsage;
		this.memoryUsage = memoryUsage;
		this.storageUsage = storageUsage;
	}
	
	/**
	 * Kryo.
	 */
	public MobilePeriodicMeasurement() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param deviceID
	 */
	public void setDeviceID(long deviceID) {
		this.deviceID = deviceID;
	}
	
	/**
	 * @return timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * @return deviceID
	 */
	public long getDeviceID() {
		return deviceID;
	}
	
	/**
	 * @return the battery power
	 */
	public double getBatteryPower() {
		return batteryPower;
	}
	
	/**
	 * @return the cpu usage
	 */
	public double getCpuUsage() {
		return cpuUsage;
	}
	
	/**
	 * @return the memory usage
	 */
	public double getMemoryUsage() {
		return memoryUsage;
	}
	
	/**
	 * @return storage usage
	 */
	public double getStorageUsage() {
		return storageUsage;
	}
}