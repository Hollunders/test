package rocks.inspectit.shared.all.indexing;

//TODO: Edit MobilePeriodicMeasurement
public interface IMobilePeriodicMeasurementAwareStorageIndexQuery extends IIndexQuery{
	
	/**
	 * Returns the ID of the device.
	 * 
	 * @return
	 */
	public long getDeviceID();
	
	/**
	 * Sets the ID of the device.
	 * 
	 * @param deviceID
	 */
	public void setDeviceID(long deviceID);
}
