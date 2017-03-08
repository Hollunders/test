package rocks.inspectit.shared.cs.indexing.storage.impl;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import rocks.inspectit.shared.all.indexing.IMobilePeriodicMeasurementAwareStorageIndexQuery;
import rocks.inspectit.shared.cs.indexing.storage.IStorageTreeComponent;

/**
 * Extended index query that fits better when querying the {@link IStorageTreeComponent}.
 *
 * @author Ivan Senic
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Lazy
public class MobilePeriodicMeasurementAwareStorageIndexQuery extends StorageIndexQuery implements IMobilePeriodicMeasurementAwareStorageIndexQuery {
	
	/**
	 * Stores the ID of a device.
	 */
	private long deviceID;
	
	/**
	 * From date.
	 */
	private Timestamp fromDate;

	/**
	 * Till date.
	 */
	private Timestamp toDate;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getDeviceID() {
		return deviceID;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDeviceID(long deviceID) {
		this.deviceID = deviceID;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getFromDate() {
		return fromDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToDate(Timestamp toDate) {
		this.toDate = toDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getToDate() {
		return toDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((fromDate == null) ? 0 : fromDate.hashCode());
		result = (prime * result) + ((toDate == null) ? 0 : toDate.hashCode());
		result = (prime * result) + (int) (deviceID ^ (deviceID >>> 32));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MobilePeriodicMeasurementAwareStorageIndexQuery other = (MobilePeriodicMeasurementAwareStorageIndexQuery) obj;
		if (fromDate == null) {
			if (other.fromDate != null) {
				return false;
			}
		} else if (!fromDate.equals(other.fromDate)) {
			return false;
		}
		if (toDate == null) {
			if (other.toDate != null) {
				return false;
			}
		} else if (!toDate.equals(other.toDate)) {
			return false;
		}	
		if (deviceID != other.deviceID) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this);
		toStringBuilder.append(super.toString());
		toStringBuilder.append("deviceID", deviceID);
		toStringBuilder.append("fromDate", fromDate);
		toStringBuilder.append("toDate", toDate);
		return toStringBuilder.toString();
	}
}