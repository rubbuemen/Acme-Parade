
package forms;

import javax.validation.Valid;

import domain.Chapter;

public class ChapterForm {

	// Attributes
	@Valid
	private Chapter	actor;
	private String	passwordCheck;
	private Boolean	termsConditions;


	// Constructors
	public ChapterForm() {
		super();
	}

	public ChapterForm(final Chapter actor) {
		this.actor = actor;
		this.passwordCheck = "";
		this.termsConditions = false;
	}

	// Getters and Setters
	public Chapter getActor() {
		return this.actor;
	}

	public void setActor(final Chapter actor) {
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
