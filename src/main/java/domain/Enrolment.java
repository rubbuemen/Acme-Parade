
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "momentRegistered, momentDropOut")
})
public class Enrolment extends DomainEntity {

	// Attributes
	private Date	momentRegistered;
	private Date	momentDropOut;


	// Getters and Setters
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	public Date getMomentRegistered() {
		return this.momentRegistered;
	}

	public void setMomentRegistered(final Date momentRegistered) {
		this.momentRegistered = momentRegistered;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	public Date getMomentDropOut() {
		return this.momentDropOut;
	}

	public void setMomentDropOut(final Date momentDropOut) {
		this.momentDropOut = momentDropOut;
	}


	// Relationships
	private PositionBrotherhood	positionBrotherhood;
	private Brotherhood			brotherhood;
	private Member				member;


	@Valid
	@ManyToOne(optional = true)
	public PositionBrotherhood getPositionBrotherhood() {
		return this.positionBrotherhood;
	}

	public void setPositionBrotherhood(final PositionBrotherhood positionBrotherhood) {
		this.positionBrotherhood = positionBrotherhood;
	}
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}

	public void setBrotherhood(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

}
