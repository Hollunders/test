package rocks.inspectit.server.dao.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rocks.inspectit.shared.all.communication.data.MobilePeriodicMeasurement;
import rocks.inspectit.shared.cs.indexing.impl.IndexQuery;
import rocks.inspectit.shared.cs.indexing.restriction.IIndexQueryRestrictionProcessor;
import rocks.inspectit.shared.cs.indexing.restriction.impl.IndexQueryRestrictionFactory;

@Repository
public class BufferPeriodicMeasurementDao extends AbstractBufferDataDao<MobilePeriodicMeasurement> {
	/**
	 * List of span classes used in the queries.
	 */
	private static final List<Class<?>> CLASSES_LIST = Collections.unmodifiableList(Arrays.<Class<?>> asList(MobilePeriodicMeasurement.class));

	/**
	 * Processor that checks if the given restrictions that are set in the query are fulfilled for
	 * any object.
	 * 
	 * TODO: Should be done analog to the SpanServiceQuery Factory
	 */
	@Autowired
	IIndexQueryRestrictionProcessor restrictionProcessor;

	/**
	 * Returns all available {@link MobilePeriodicMeasurement} instances.
	 * 
	 * @return
	 */
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances() {
		IndexQuery query = new IndexQuery();
		query.setObjectClasses(CLASSES_LIST);
		query.setRestrictionProcessor(restrictionProcessor);
		return super.executeQuery(query, false);

	}

	/**
	 * Returns all {@link MobilePeriodicMeasurement} instances from a specific
	 * deviceID.
	 * 
	 * @return
	 */
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances(long deviceID) {
		IndexQuery query = new IndexQuery();
		query.setObjectClasses(CLASSES_LIST);
		query.setRestrictionProcessor(restrictionProcessor);
		query.addIndexingRestriction(IndexQueryRestrictionFactory.equal("deviceID", deviceID));
		return super.executeQuery(query, false);

	}

	/**
	 * Returns all {@link MobilePeriodicMeasurement} instances from a specific
	 * deviceID in an interval.
	 * 
	 * @return
	 */
	public List<MobilePeriodicMeasurement> getMobilePeriodicMeasurementInstances(long deviceID, long fromTimestamp,
			long toTimestamp) {
		IndexQuery query = new IndexQuery();
		query.setObjectClasses(CLASSES_LIST);
		query.setRestrictionProcessor(restrictionProcessor);
		query.addIndexingRestriction(IndexQueryRestrictionFactory.equal("deviceID", deviceID));
		query.setFromDate(new Timestamp(fromTimestamp));
		query.setToDate(new Timestamp(toTimestamp));
		return super.executeQuery(query, false);

	}

}
