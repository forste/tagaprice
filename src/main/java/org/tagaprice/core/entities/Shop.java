package org.tagaprice.core.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.tagaprice.server.dao.interfaces.IReceiptDAO;

/**
 * <p>
 * This represents a shop.
 * </p>
 * <p>
 * A shop has a title and coordinates associated. TODO add more properties / make revisionable.
 * </p>
 * 
 * 
 * <p>
 * TODO This class should be immutable. control collections access.
 * </p>
 * 
 * @author haja
 * 
 */
@Entity
@SuppressWarnings("unused")
public class Shop implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long _id;
	private String _title;
	private double _latitude;
	private double _longitude;
	private Set<ReceiptEntry> _receiptEntries = new HashSet<ReceiptEntry>();



	protected Shop() {
	}

	/**
	 * Initializes a new {@link Shop}.
	 * 
	 * @param id
	 *            Id of this shop. If this is null, this entity is regarded to be new to the database. If set, must
	 *            be > 0.
	 * @param title
	 *            short text describing this shop.
	 * @param receiptEntries
	 *            {@link ReceiptEntry}s holding price-information about each product available at this shop.
	 */
	public Shop(Long id, String title, double latitude, double longitude, Set<ReceiptEntry> receiptEntries) {
		_id = id;
		_title = title;
		_latitude = latitude;
		_longitude = longitude;
		_receiptEntries = receiptEntries;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "shop_id")
	public Long getId() {
		return _id;
	}

	private void setId(Long id) {
		_id = id;
	}


	public String getTitle() {
		return _title;
	}

	private void setTitle(String title) {
		_title = title;
	}


	public double getLatitude() {
		return _latitude;
	}

	private void setLatitude(double latitude) {
		_latitude = latitude;
	}


	public double getLongitude() {
		return _longitude;
	}

	private void setLongitude(double longitude) {
		_longitude = longitude;
	}


	/**
	 * This {@link ReceiptEntry}s won't get saved or updated. Save them using {@link IReceiptDAO}.
	 */
	@Transient
	public Set<ReceiptEntry> getReceiptEntries() {
		return _receiptEntries;
	}

	public void setReceiptEntries(Collection<ReceiptEntry> receipts) {
		_receiptEntries.clear();
		_receiptEntries.addAll(receipts);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(_latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(_longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((_receiptEntries == null) ? 0 : _receiptEntries.hashCode());
		result = prime * result + ((_title == null) ? 0 : _title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shop other = (Shop) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (Double.doubleToLongBits(_latitude) != Double.doubleToLongBits(other._latitude))
			return false;
		if (Double.doubleToLongBits(_longitude) != Double.doubleToLongBits(other._longitude))
			return false;
		if (_receiptEntries == null) {
			if (other._receiptEntries != null)
				return false;
		} else if (!_receiptEntries.equals(other._receiptEntries))
			return false;
		if (_title == null) {
			if (other._title != null)
				return false;
		} else if (!_title.equals(other._title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Shop [_id=" + _id + ", _title=" + _title + ", _latitude=" + _latitude + ", _longitude=" + _longitude
		+ ", _receiptEntries=" + _receiptEntries + "]";
	}
}