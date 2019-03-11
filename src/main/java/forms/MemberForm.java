
package forms;

import javax.validation.Valid;

import domain.Member;

public class MemberForm {

	// Attributes
	@Valid
	private Member	actor;
	private String	passwordCheck;
	private Boolean	termsConditions;


	// Constructors
	public MemberForm() {
		super();
	}

	public MemberForm(final Member actor) {
		this.actor = actor;
		this.passwordCheck = "";
		this.termsConditions = false;
	}

	// Getters and Setters
	public Member getActor() {
		return this.actor;
	}

	public void setActor(final Member actor) {
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
