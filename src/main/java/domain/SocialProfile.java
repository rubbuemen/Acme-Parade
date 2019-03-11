
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SocialProfile extends DomainEntity {

	// Attributes
	private String	nick;
	private String	socialNetworkName;
	private String	profileLink;


	// Getters and Setters
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNick() {
		return this.nick;
	}

	public void setNick(final String nick) {
		this.nick = nick;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSocialNetworkName() {
		return this.socialNetworkName;
	}

	public void setSocialNetworkName(final String socialNetworkName) {
		this.socialNetworkName = socialNetworkName;
	}

	@NotBlank
	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getProfileLink() {
		return this.profileLink;
	}

	public void setProfileLink(final String profileLink) {
		this.profileLink = profileLink;
	}


	// Relationships
	private Actor	actor;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

}
