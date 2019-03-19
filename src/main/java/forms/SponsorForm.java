
package forms;

import javax.validation.Valid;

import domain.Sponsor;

public class SponsorForm {

	// Attributes
	@Valid
	private Sponsor	actor;
	private String	passwordCheck;
	private Boolean	termsConditions;


	// Constructors
	public SponsorForm() {
		super();
	}

	public SponsorForm(final Sponsor actor) {
		this.actor = actor;
		this.passwordCheck = "";
		this.termsConditions = false;
	}

	// Getters and Setters
	public Sponsor getActor() {
		return this.actor;
	}

	public void setActor(final Sponsor actor) {
		this.actor = actor;
	}

	public String getPasswordCheck() {
		return this.passwordCheck;
	}

	public void setPasswordCheck(final String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	public Boolean getTermsConditions() {
		return this.termsConditions;
	}

	public void setTermsConditions(final Boolean termsConditions) {
		this.termsConditions = termsConditions;
	}
}
