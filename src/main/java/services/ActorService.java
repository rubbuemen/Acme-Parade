
package services;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Box;

@Service
@Transactional
public class ActorService {

	// Managed repository
	@Autowired
	private ActorRepository				actorRepository;

	// Supporting services
	@Autowired
	private UserAccountService			userAccountService;

	@Autowired
	private BoxService					boxService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Simple CRUD methods
	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result;

		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;
	}

	// R9.1, R9.2
	public Actor save(final Actor actor) {
		Assert.notNull(actor);

		Actor result;

		Assert.isTrue(actor.getUserAccount().getStatusAccount());

		if (actor.getId() == 0) {
			final UserAccount userAccount = this.userAccountService.save(actor.getUserAccount());
			Box inBox = this.boxService.createForSystemBox();
			inBox.setName("In Box");
			inBox.setIsSystemBox(true);
			inBox = this.boxService.saveForSystemBox(inBox);
			Box outBox = this.boxService.createForSystemBox();
			outBox.setName("Out Box");
			outBox.setIsSystemBox(true);
			outBox = this.boxService.saveForSystemBox(outBox);
			Box trashBox = this.boxService.createForSystemBox();
			trashBox.setName("Trash Box");
			trashBox.setIsSystemBox(true);
			trashBox = this.boxService.saveForSystemBox(trashBox);
			Box spamBox = this.boxService.createForSystemBox();
			spamBox.setName("Spam Box");
			spamBox.setIsSystemBox(true);
			spamBox = this.boxService.saveForSystemBox(spamBox);
			Box notificationBox = this.boxService.createForSystemBox();
			notificationBox.setName("Notification Box");
			notificationBox.setIsSystemBox(true);
			notificationBox = this.boxService.saveForSystemBox(notificationBox);
			final Collection<Box> systemBoxes = new HashSet<>();
			Collections.addAll(systemBoxes, inBox, outBox, trashBox, spamBox, notificationBox);
			actor.setUserAccount(userAccount);
			actor.setBoxes(systemBoxes);
		} else {
			final Actor actorLogged = this.findActorLogged();
			Assert.notNull(actorLogged);
			Assert.isTrue(actor.equals(actorLogged));
		}

		if (actor.getPhoneNumber() != null) {
			String phoneNumber = actor.getPhoneNumber();
			final String phoneCountryCode = this.systemConfigurationService.getConfiguration().getPhoneCountryCode();
			if (!actor.getPhoneNumber().isEmpty() && !actor.getPhoneNumber().startsWith("+"))
				phoneNumber = phoneCountryCode + " " + phoneNumber;
			actor.setPhoneNumber(phoneNumber);
		}

		result = this.actorRepository.save(actor);

		return result;
	}

	public Actor saveForComputes(final Actor actor) {
		Assert.notNull(actor);

		Actor result;

		result = this.actorRepository.save(actor);

		return result;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));

		this.actorRepository.delete(actor);
	}

	// Other business methods

	public Actor findActorLogged() {
		Actor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		result = this.actorRepository.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Actor getSystemActor() {
		Actor result;

		result = this.actorRepository.getSystemActor();
		Assert.notNull(result);

		return result;
	}

	public void checkUserLoginBrotherhood(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.BROTHERHOOD);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a brotherhood");
	}

	public void checkUserLoginMember(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.MEMBER);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a member");
	}

	public void checkUserLoginAdministrator(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a administrator");
	}

	public void checkUserLoginChapter(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.CHAPTER);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a chapter");
	}

	public void checkUserLoginSponsor(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.SPONSOR);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a sposor");
	}

	// Other business methods
	public Actor findActorBySocialProfileId(final int socialProfileId) {
		Assert.isTrue(socialProfileId != 0);

		Actor result;

		result = this.actorRepository.findActorBySocialProfileId(socialProfileId);
		return result;
	}

	public Collection<Actor> findAllActorsExceptLogged() {
		Collection<Actor> result;

		final Actor actorLogged = this.findActorLogged();
		final Actor systemActor = this.actorRepository.getSystemActor();
		result = this.actorRepository.findAll();
		result.remove(actorLogged);
		result.remove(systemActor);

		return result;
	}

	public Collection<Actor> findActorsToBan() {
		final Actor actorLogged = this.findActorLogged();
		Assert.notNull(actorLogged);
		this.checkUserLoginAdministrator(actorLogged);

		Collection<Actor> result;

		result = this.actorRepository.findActorsToBan();
		return result;
	}

	public Collection<Actor> findActorsBanned() {
		final Actor actorLogged = this.findActorLogged();
		Assert.notNull(actorLogged);
		this.checkUserLoginAdministrator(actorLogged);

		Collection<Actor> result;

		result = this.actorRepository.findActorsBanned();
		return result;
	}

	public void banActor(final Actor actor) {
		final Actor actorLogged = this.findActorLogged();
		Assert.notNull(actorLogged);
		this.checkUserLoginAdministrator(actorLogged);

		final UserAccount userAccount = actor.getUserAccount();
		userAccount.setStatusAccount(false);
		this.userAccountService.save(userAccount);
	}

	public void unbanActor(final Actor actor) {
		final Actor actorLogged = this.findActorLogged();
		Assert.notNull(actorLogged);
		this.checkUserLoginAdministrator(actorLogged);

		final UserAccount userAccount = actor.getUserAccount();
		userAccount.setStatusAccount(true);
		this.userAccountService.save(userAccount);
	}

	// Reconstruct methods

}
