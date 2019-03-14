
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity {

	// Attributes
	private String	banner;
	private String	targetURL;
	private Boolean	isActivated;


	// Getters and Setters
	@NotBlank
	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotBlank
	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTargetURL() {
		return this.targetURL;
	}

	public void setTargetURL(final String targetURL) {
		this.targetURL = targetURL;
	}

	@NotNull
	public Boolean getIsActivated() {
		return this.isActivated;
	}

	public void setIsActivated(final Boolean isActivated) {
		this.isActivated = isActivated;
	}


	// Relationships
	private Sponsor		sponsor;
	private Parade		parade;
	private CreditCard	creditCard;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Sponsor getSponsor() {
		return this.sponsor;
	}
	public void setSponsor(final Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Parade getParade() {
		return this.parade;
	}
	public void setParade(final Parade parade) {
		this.parade = parade;
	}

	@NotNull
	@Valid
	@OneToOne(optional = false)
	public CreditCard getCreditCard() {
		return this.creditCard;
	}
	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

}
