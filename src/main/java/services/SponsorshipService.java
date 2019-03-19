
package services;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.Actor;
import domain.CreditCard;
import domain.Message;
import domain.Parade;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	// Managed repository
	@Autowired
	private SponsorshipRepository		sponsorshipRepository;

	// Supporting services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private SponsorService				sponsorService;

	@Autowired
	private ParadeService				paradeService;

	@Autowired
	private MessageService				messageService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Simple CRUD methods
	// R16.1 (Acme-Parade)
	public Sponsorship create() {
		Sponsorship result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		result = new Sponsorship();

		result.setSponsor((Sponsor) actorLogged);
		result.setIsActivated(true);

		return result;
	}
	public Collection<Sponsorship> findAll() {
		Collection<Sponsorship> result;

		result = this.sponsorshipRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Sponsorship findOne(final int sponsorshipId) {
		Assert.isTrue(sponsorshipId != 0);

		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);
		Assert.notNull(result);

		return result;
	}

	// R16.1 (Acme-Parade)
	public Sponsorship save(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		final Sponsor sponsorLogged = (Sponsor) actorLogged;

		Sponsorship result;

		Assert.isTrue(this.isNumeric(sponsorship.getCreditCard().getNumber()), "Invalid credit card");
		Assert.isTrue(this.checkCreditCard(sponsorship.getCreditCard()), "Expired credit card");

		if (sponsorship.getId() == 0) {
			result = this.sponsorshipRepository.save(sponsorship);
			final Parade paradeSponsorship = result.getParade();
			final Collection<Sponsorship> sponsorshipsParade = paradeSponsorship.getSponsorships();
			sponsorshipsParade.add(result);
			paradeSponsorship.setSponsorships(sponsorshipsParade);
			this.paradeService.saveAuxiliar(paradeSponsorship);
			final Collection<Sponsorship> sponsorshipsSponsor = sponsorLogged.getSponsorships();
			sponsorshipsSponsor.add(result);
			sponsorLogged.setSponsorships(sponsorshipsSponsor);
			this.sponsorService.save(sponsorLogged);
		} else {
			final Sponsor sponsorOwner = this.sponsorService.findSponsorBySponsorshipId(sponsorship.getId());
			Assert.isTrue(actorLogged.equals(sponsorOwner), "The logged actor is not the owner of this entity");
			result = this.sponsorshipRepository.save(sponsorship);
		}

		return result;
	}

	public void delete(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		this.sponsorshipRepository.delete(sponsorship);
	}

	// Other business methods
	// R16.1 (Acme-Parade)
	public Collection<Sponsorship> findSponsorshipsBySponsorLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		Collection<Sponsorship> result;

		final Sponsor sponsorLogged = (Sponsor) actorLogged;

		result = sponsorLogged.getSponsorships();
		Assert.notNull(result);

		return result;
	}

	public Sponsorship findSponsorshipSponsorLogged(final int sponsorshipId) {
		Assert.isTrue(sponsorshipId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		final Sponsor sponsorOwner = this.sponsorService.findSponsorBySponsorshipId(sponsorshipId);
		Assert.isTrue(actorLogged.equals(sponsorOwner), "The logged actor is not the owner of this entity");

		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);
		Assert.notNull(result);

		return result;
	}

	// R16.1 (Acme-Parade)
	public void deactivate(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		final Sponsor sponsorOwner = this.sponsorService.findSponsorBySponsorshipId(sponsorship.getId());
		Assert.isTrue(actorLogged.equals(sponsorOwner), "The logged actor is not the owner of this entity");

		sponsorship.setIsActivated(false);
		this.sponsorshipRepository.save(sponsorship);
	}

	// R18.1 (Acme-Parade)
	public void deactivateByAdmin(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		sponsorship.setIsActivated(false);
		this.sponsorshipRepository.save(sponsorship);
	}

	// R16.1 (Acme-Parade)
	public void activate(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		final Sponsor sponsorOwner = this.sponsorService.findSponsorBySponsorshipId(sponsorship.getId());
		Assert.isTrue(actorLogged.equals(sponsorOwner), "The logged actor is not the owner of this entity");

		sponsorship.setIsActivated(true);
		this.sponsorshipRepository.save(sponsorship);
	}

	public boolean isNumeric(final String cadena) {

		boolean resultado;

		try {
			Long.parseLong(cadena);
			resultado = true;
		} catch (final NumberFormatException excepcion) {
			resultado = false;
		}

		return resultado;
	}

	public boolean checkCreditCard(final CreditCard creditCard) {
		boolean result;
		Calendar calendar;
		int actualYear, actualMonth;

		result = false;
		calendar = new GregorianCalendar();
		actualYear = calendar.get(Calendar.YEAR);
		actualMonth = calendar.get(Calendar.MONTH) + 1;
		actualYear = actualYear % 100;
		if (creditCard.getExpirationYear() != null)
			if (creditCard.getExpirationYear() > actualYear)
				result = true;
			else if (creditCard.getExpirationYear() == actualYear && creditCard.getExpirationMonth() >= actualMonth)
				result = true;
		return result;
	}

	public Collection<Sponsorship> findActiveSponsorshipsByParade(final Parade parade) {
		final Collection<Sponsorship> result = new HashSet<>();

		final Collection<Sponsorship> sponsorships = parade.getSponsorships();
		for (final Sponsorship ss : sponsorships)
			if (ss.getIsActivated())
				result.add(ss);

		return result;
	}

	//R20 (Acme-Parade)
	public Sponsorship findRandomSponsorShip(final Parade parade) {
		Sponsorship result = null;

		final Random r = new Random();
		final Collection<Sponsorship> sponsorships = this.findActiveSponsorshipsByParade(parade);
		if (!sponsorships.isEmpty()) {
			final int i = r.nextInt(sponsorships.size());
			result = (Sponsorship) sponsorships.toArray()[i];

			final Sponsor sponsor = result.getSponsor();
			final Message message = this.messageService.createAuxiliar();
			final Double fare = this.systemConfigurationService.getConfiguration().getFare();
			final Double vatPercentage = this.systemConfigurationService.getConfiguration().getVATPercentage();
			Double total = fare + (fare * (vatPercentage / 100));
			final DecimalFormat formatDecimals = new DecimalFormat(".##");
			total = Double.valueOf(formatDecimals.format(total));

			final Locale locale = LocaleContextHolder.getLocale();
			if (locale.getLanguage().equals("es")) {
				message.setSubject("Se le ha realizado un cargo en la tarjeta de crédito");
				message.setBody("Se le ha realizado un cargo de " + total + " [" + fare + " + IVA (" + vatPercentage + "%)] en la tarjeta de crédito con número " + result.getCreditCard().getNumber());
			} else {
				message.setSubject("A charge has been made to your credit card");
				message.setBody("You have been charged " + total + " [" + fare + " + VAT (" + vatPercentage + "%)] on the credit card number " + result.getCreditCard().getNumber());
			}

			final Actor sender = this.actorService.getSystemActor();
			message.setPriority("HIGH");
			message.setSender(sender);

			final Collection<Actor> recipients = new HashSet<>();
			recipients.add(sponsor);
			message.setRecipients(recipients);
			this.messageService.save(message, true);
		}

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Sponsorship reconstruct(final Sponsorship sponsorship, final BindingResult binding) {
		Sponsorship result;

		if (sponsorship.getId() == 0) {
			final Actor actorLogged = this.actorService.findActorLogged();
			sponsorship.setSponsor((Sponsor) actorLogged);
			sponsorship.setIsActivated(true);
			result = sponsorship;
		} else {
			final Sponsorship originalSponsorship = this.sponsorshipRepository.findOne(sponsorship.getId());
			Assert.notNull(originalSponsorship, "This entity does not exist");
			result = sponsorship;
			result.setSponsor(originalSponsorship.getSponsor());
			result.setIsActivated(originalSponsorship.getIsActivated());
		}

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

}
