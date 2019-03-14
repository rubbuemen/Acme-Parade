
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

import cz.jirutka.validator.collection.constraints.EachNotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	// Attributes
	private String				nameSystem;
	private String				bannerUrl;
	private String				welcomeMessageEnglish;
	private String				welcomeMessageSpanish;
	private String				phoneCountryCode;
	private Integer				periodFinder;
	private Integer				maxResultsFinder;
	private Collection<String>	prioritiesMessagesList;
	private Collection<String>	positiveWords;
	private Collection<String>	negativeWords;
	private Collection<String>	spamWords;
	private Collection<String>	creditCardMakes;
	private Double				fare;
	private Double				VATPercentage;


	// Getters and Setters
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNameSystem() {
		return this.nameSystem;
	}

	public void setNameSystem(final String nameSystem) {
		this.nameSystem = nameSystem;
	}

	@NotBlank
	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBannerUrl() {
		return this.bannerUrl;
	}

	public void setBannerUrl(final String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageEnglish() {
		return this.welcomeMessageEnglish;
	}

	public void setWelcomeMessageEnglish(final String welcomeMessageEnglish) {
		this.welcomeMessageEnglish = welcomeMessageEnglish;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageSpanish() {
		return this.welcomeMessageSpanish;
	}

	public void setWelcomeMessageSpanish(final String welcomeMessageSpanish) {
		this.welcomeMessageSpanish = welcomeMessageSpanish;
	}

	@NotBlank
	@Pattern(regexp = "^[+][1-9]\\d{0,2}$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPhoneCountryCode() {
		return this.phoneCountryCode;
	}

	public void setPhoneCountryCode(final String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	@NotNull
	@Range(min = 1, max = 24)
	public Integer getPeriodFinder() {
		return this.periodFinder;
	}

	public void setPeriodFinder(final Integer periodFinder) {
		this.periodFinder = periodFinder;
	}

	@NotNull
	@Max(100)
	public Integer getMaxResultsFinder() {
		return this.maxResultsFinder;
	}

	public void setMaxResultsFinder(final Integer maxResultsFinder) {
		this.maxResultsFinder = maxResultsFinder;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@NotEmpty
	@EachNotBlank
	public Collection<String> getPrioritiesMessagesList() {
		return this.prioritiesMessagesList;
	}

	public void setPrioritiesMessagesList(final Collection<String> prioritiesMessagesList) {
		this.prioritiesMessagesList = prioritiesMessagesList;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@NotEmpty
	@EachNotBlank
	public Collection<String> getPositiveWords() {
		return this.positiveWords;
	}

	public void setPositiveWords(final Collection<String> positiveWords) {
		this.positiveWords = positiveWords;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@NotEmpty
	@EachNotBlank
	public Collection<String> getNegativeWords() {
		return this.negativeWords;
	}

	public void setNegativeWords(final Collection<String> negativeWords) {
		this.negativeWords = negativeWords;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@NotEmpty
	@EachNotBlank
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@NotEmpty
	@EachNotBlank
	public Collection<String> getCreditCardMakes() {
		return this.creditCardMakes;
	}

	public void setCreditCardMakes(final Collection<String> creditCardMakes) {
		this.creditCardMakes = creditCardMakes;
	}

	@NotNull
	@Min(0)
	@Digits(integer = 2, fraction = 2)
	public Double getFare() {
		return this.fare;
	}

	public void setFare(final Double fare) {
		this.fare = fare;
	}

	@NotNull
	@Min(0)
	@Digits(integer = 2, fraction = 2)
	public Double getVATPercentage() {
		return this.VATPercentage;
	}

	public void setVATPercentage(final Double VATPercentage) {
		this.VATPercentage = VATPercentage;
	}

}
