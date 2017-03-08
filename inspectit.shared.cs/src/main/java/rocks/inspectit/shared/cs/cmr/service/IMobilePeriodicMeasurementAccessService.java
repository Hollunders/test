package rocks.inspectit.shared.cs.cmr.service;

import java.util.List;

import rocks.inspectit.shared.all.cmr.service.ServiceExporterType;
import rocks.inspectit.shared.all.cmr.service.ServiceInterface;
import rocks.inspectit.shared.all.communication.data.MobilePeriodicMeasurement;

/**
 * Storage methods for {@link MobilePeriodicMeasurement} instances.
 * 
 * @author Tobias Angerstein, Manuel Palenga
 * 
 */
@ServiceInterface(exporter = ServiceExporterType.HTTP)
public interface IMobilePeriodicMeasurementAccessService {
	
	/**
	 * Returns all available {@link MobilePeriodicMeasurement} instances.
	 * 
	 * @return
	 */
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances();

	/**
	 * Returns all {@link MobilePeriodicMeasurement} instances from a specific deviceID.
	 * 
	 * @return
	 */
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances(long deviceID);
	
	/**
	 * Returns all {@link MobilePeriodicMeasurement} instances from a specific deviceID in an interval.
	 * 
	 * @return
	 */
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances(long deviceID, long fromTimestamp, long toTimestamp);

}
