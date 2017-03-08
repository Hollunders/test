package rocks.inspectit.ui.rcp.repository.service.storage;

import java.sql.Timestamp;
import java.util.List;

import rocks.inspectit.shared.all.communication.data.MobilePeriodicMeasurement;
import rocks.inspectit.shared.cs.cmr.service.IMobilePeriodicMeasurementAccessService;
import rocks.inspectit.shared.cs.indexing.storage.IStorageTreeComponent;
import rocks.inspectit.shared.cs.indexing.storage.impl.MobilePeriodicMeasurementAwareStorageIndexQuery;

/**
 * {@link IMobilePeriodicMeasurementAccessService} for storage purposes.
 *
 * @author Tobias Angerstein, Manuel Palenga
 *
 */
public class StorageMobilePeriodicMeasurementAccessService extends AbstractStorageService<MobilePeriodicMeasurement> implements IMobilePeriodicMeasurementAccessService {
	
	/**
	 * Indexing tree.
	 */
	private IStorageTreeComponent<MobilePeriodicMeasurement> indexingTree;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances() {
		MobilePeriodicMeasurementAwareStorageIndexQuery query = new MobilePeriodicMeasurementAwareStorageIndexQuery();
		List<MobilePeriodicMeasurement> resultList = super.executeQuery(query);
		return resultList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances(long deviceID) {
		MobilePeriodicMeasurementAwareStorageIndexQuery query = new MobilePeriodicMeasurementAwareStorageIndexQuery();
		query.setDeviceID(deviceID);
		List<MobilePeriodicMeasurement> resultList = super.executeQuery(query);
		return resultList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances(long deviceID, long fromTimestamp, long toTimestamp) {
		MobilePeriodicMeasurementAwareStorageIndexQuery query = new MobilePeriodicMeasurementAwareStorageIndexQuery();
		query.setDeviceID(deviceID);
		query.setFromDate(new Timestamp(fromTimestamp));
		query.setToDate(new Timestamp(toTimestamp));
		List<MobilePeriodicMeasurement> resultList =  super.executeQuery(query);
		return resultList;
	}

	@Override
	protected IStorageTreeComponent<MobilePeriodicMeasurement> getIndexingTree() {
		return indexingTree;
	}

	public void setIndexingTree(IStorageTreeComponent<MobilePeriodicMeasurement> storageTreeComponent) {
		this.indexingTree = storageTreeComponent;		
	}

}
