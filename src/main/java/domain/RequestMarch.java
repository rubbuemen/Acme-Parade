
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "status")
})
public class RequestMarch extends DomainEntity {

	// Attributes
	private String	status;
	private String	rejectReason;
	private Integer	positionRow;
	private Integer	positionColumn;


	// Getters and Setters
	@NotBlank
	@Pattern(regexp = "^PENDING|APPROVED|REJECTED$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getRejectReason() {
		return this.rejectReason;
	}

	public void setRejectReason(final String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Integer getPositionRow() {
		return this.positionRow;
	}

	public void setPositionRow(final Integer positionRow) {
		this.positionRow = positionRow;
	}

	public Integer getPositionColumn() {
		return this.positionColumn;
	}

	public void setPositionColumn(final Integer positionColumn) {
		this.positionColumn = positionColumn;
	}


	// Relationships
	private Member	member;


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
