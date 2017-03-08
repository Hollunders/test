package rocks.inspectit.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rocks.inspectit.server.dao.impl.BufferPeriodicMeasurementDao;
import rocks.inspectit.shared.all.communication.data.MobilePeriodicMeasurement;
import rocks.inspectit.shared.cs.cmr.service.IMobilePeriodicMeasurementAccessService;
import rocks.inspectit.shared.cs.cmr.service.ISpanService;

/**
 * Implementation of the {@link ISpanService} that reads data from the buffer.
 *
 * @author Ivan Senic
 *
 */
@Service
public class MobilePeriodicMeasurementAccessService implements IMobilePeriodicMeasurementAccessService {

	//TODO: Edit MobilePeriodicMeasurement
	
	/**
	 * The mobilePeriodicMeasurement DAO.
	 */
	@Autowired
	private BufferPeriodicMeasurementDao periodicMeasurementDao;

	@Override
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances() {
		return periodicMeasurementDao.getMobilePeriodicMeasurementInstances();
	}

	@Override
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances(
			long deviceID) {
		
		return periodicMeasurementDao.getMobilePeriodicMeasurementInstances(deviceID);
	}

	@Override
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances(
			long deviceID, long fromTimestamp, long toTimestamp) {
		// TODO Auto-generated method stub
		return periodicMeasurementDao.getMobilePeriodicMeasurementInstances(deviceID, fromTimestamp, toTimestamp);
	}
}
