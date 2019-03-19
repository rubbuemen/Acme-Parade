
package domain;

import java.sql.Time;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	// Attributes
	private GPSCoordinates	origin;
	private GPSCoordinates	destination;
	private Time			timeReachOrigin;
	private Time			timeReachDestination;


	// Getters and Setters

	@AttributeOverrides({
		@AttributeOverride(name = "latitude", column = @Column(name = "originLatitude")), @AttributeOverride(name = "longitude", column = @Column(name = "originLongitude"))
	})
	@NotNull
	@Valid
	public GPSCoordinates getOrigin() {
		return this.origin;
	}

	public void setOrigin(final GPSCoordinates origin) {
		this.origin = origin;
	}

	@AttributeOverrides({
		@AttributeOverride(name = "latitude", column = @Column(name = "destinationLatitude")), @AttributeOverride(name = "longitude", column = @Column(name = "destinationLongitude"))
	})
	@NotNull
	@Valid
	public GPSCoordinates getDestination() {
		return this.destination;
	}

	public void setDestination(final GPSCoordinates destination) {
		this.destination = destination;
	}

	@NotNull
	@DateTimeFormat(pattern = "HH:mm")
	@Type(type = "time")
	@Temporal(TemporalType.TIME)
	public Time getTimeReachOrigin() {
		return this.timeReachOrigin;
	}

	public void setTimeReachOrigin(final Time timeReachOrigin) {
		this.timeReachOrigin = timeReachOrigin;
	}

	@NotNull
	@DateTimeFormat(pattern = "HH:mm")
	@Type(type = "time")
	@Temporal(TemporalType.TIME)
	public Time getTimeReachDestination() {
		return this.timeReachDestination;
	}

	public void setTimeReachDestination(final Time timeReachDestination) {
		this.timeReachDestination = timeReachDestination;
	}

}
