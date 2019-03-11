
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

import security.UserAccount;
import cz.jirutka.validator.collection.constraints.EachNotNull;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "name, isSpammer, polarityScore")
})
public abstract class Actor extends DomainEntity {

	// Attributes
	private String	name;
	private String	middleName;
	private String	surname;
	private String	photo;
	private String	email;
	private String	phoneNumber;
	private String	address;
	private Boolean	isSpammer;
	private Double	polarityScore;


	// Getters and Setters
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9 ]*[<]?\\w+[@][a-zA-Z0-9.]+[>]?$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public Boolean getIsSpammer() {
		return this.isSpammer;
	}

	public void setIsSpammer(final Boolean isSpammer) {
		this.isSpammer = isSpammer;
	}

	public Double getPolarityScore() {
		return this.polarityScore;
	}

	public void setPolarityScore(final Double polarityScore) {
		this.polarityScore = polarityScore;
	}


	// Relationships
	private UserAccount		userAccount;
	private Collection<Box>	boxes;


	@NotNull
	@Valid
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@Valid
	@EachNotNull
	@OneToMany
	public Collection<Box> getBoxes() {
		return this.boxes;
	}

	public void setBoxes(final Collection<Box> boxes) {
		this.boxes = boxes;
	}

}
