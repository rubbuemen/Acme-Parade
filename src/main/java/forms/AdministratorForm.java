
package forms;

import javax.validation.Valid;

import domain.Administrator;

public class AdministratorForm {

	// Attributes
	@Valid
	private Administrator	actor;
	private String			passwordCheck;
	private Boolean			termsConditions;


	// Constructors
	public AdministratorForm() {
		super();
	}

	public AdministratorForm(final Administrator actor) {
		this.actor = actor;
		this.passwordCheck = "";
		this.termsConditions = false;
	}

	// Getters and Setters
	public Administrator getActor() {
		return this.actor;
	}

	public void setActor(final Administrator actor) {
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
