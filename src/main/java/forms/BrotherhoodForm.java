
package forms;

import javax.validation.Valid;

import domain.Brotherhood;

public class BrotherhoodForm {

	// Attributes
	@Valid
	private Brotherhood	actor;
	private String		passwordCheck;
	private Boolean		termsConditions;


	// Constructors
	public BrotherhoodForm() {
		super();
	}

	public BrotherhoodForm(final Brotherhood actor) {
		this.actor = actor;
		this.passwordCheck = "";
		this.termsConditions = false;
	}

	// Getters and Setters
	public Brotherhood getActor() {
		return this.actor;
	}

	public void setActor(final Brotherhood actor) {
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
