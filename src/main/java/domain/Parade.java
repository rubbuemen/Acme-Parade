
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import cz.jirutka.validator.collection.constraints.EachNotNull;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "ticker, title, description, momentOrganise, isFinalMode, status")
})
public class Parade extends DomainEntity {

	// Attributes
	private String	ticker;
	private String	title;
	private String	description;
	private Date	momentOrganise;
	private Boolean	isFinalMode;
	private Integer	maxRows;
	private Integer	maxColumns;
	private String	status;
	private String	rejectReason;


	// Getters and Setters
	@NotBlank
	@Pattern(regexp = "^\\d{2}[0-1]\\d[0-3]\\d[-][A-Z]{5}$")
	@Column(unique = true)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Future
	public Date getMomentOrganise() {
		return this.momentOrganise;
	}

	public void setMomentOrganise(final Date momentOrganise) {
		this.momentOrganise = momentOrganise;
	}

	@NotNull
	public Boolean getIsFinalMode() {
		return this.isFinalMode;
	}

	public void setIsFinalMode(final Boolean isFinalMode) {
		this.isFinalMode = isFinalMode;
	}

	@NotNull
	@Min(1)
	public Integer getMaxRows() {
		return this.maxRows;
	}

	public void setMaxRows(final Integer maxRows) {
		this.maxRows = maxRows;
	}

	@NotNull
	@Min(1)
	public Integer getMaxColumns() {
		return this.maxColumns;
	}

	public void setMaxColumns(final Integer maxColumns) {
		this.maxColumns = maxColumns;
	}

	@Pattern(regexp = "^SUBMITTED|ACCEPTED|REJECTED$")
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


	// Relationships
	private Collection<Float>			floats;
	private Collection<RequestMarch>	requestsMarch;
	private Collection<Segment>			segments;
	private Collection<Sponsorship>		sponsorships;


	@NotEmpty
	@Valid
	@EachNotNull
	@ManyToMany(fetch = FetchType.EAGER)
	public Collection<Float> getFloats() {
		return this.floats;
	}

	public void setFloats(final Collection<Float> floats) {
		this.floats = floats;
	}

	@Valid
	@EachNotNull
	@OneToMany
	public Collection<RequestMarch> getRequestsMarch() {
		return this.requestsMarch;
	}

	public void setRequestsMarch(final Collection<RequestMarch> requestsMarch) {
		this.requestsMarch = requestsMarch;
	}

	@Valid
	@EachNotNull
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	public Collection<Segment> getSegments() {
		return this.segments;
	}

	public void setSegments(final Collection<Segment> segments) {
		this.segments = segments;
	}

	@Valid
	@EachNotNull
	@OneToMany(mappedBy = "parade")
	public Collection<Sponsorship> getSponsorships() {
		return this.sponsorships;
	}

	public void setSponsorships(final Collection<Sponsorship> sponsorships) {
		this.sponsorships = sponsorships;
	}

}
