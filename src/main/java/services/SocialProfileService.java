
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository
	@Autowired
	private SocialProfileRepository	socialProfileRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods
	// R27.1
	public SocialProfile create() {
		SocialProfile result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		result = new SocialProfile();

		result.setActor(actorLogged);

		return result;
	}

	public Collection<SocialProfile> findAll() {
		Collection<SocialProfile> result;

		result = this.socialProfileRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public SocialProfile findOne(final int socialProfileId) {
		Assert.isTrue(socialProfileId != 0);

		SocialProfile result;

		result = this.socialProfileRepository.findOne(socialProfileId);
		Assert.notNull(result);

		return result;
	}

	// R27.1
	public SocialProfile save(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		SocialProfile result;

		if (socialProfile.getId() != 0) {
			final Actor actorOwner = this.actorService.findActorBySocialProfileId(socialProfile.getId());
			Assert.isTrue(actorLogged.equals(actorOwner), "The logged actor is not the owner of this entity");
		}

		socialProfile.setActor(actorLogged);
		result = this.socialProfileRepository.save(socialProfile);

		return result;
	}

	// R27.1
	public void delete(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);
		Assert.isTrue(socialProfile.getId() != 0);
		Assert.isTrue(this.socialProfileRepository.exists(socialProfile.getId()));

		this.socialProfileRepository.delete(socialProfile);
	}

	// Other business methods
	// R27.1
	public Collection<SocialProfile> findSocialProfilesByActorLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		Collection<SocialProfile> result;

		result = this.socialProfileRepository.findSocialProfilesByActorId(actorLogged.getId());

		return result;
	}

	public SocialProfile findSocialProfileActorLogged(final int socialProfileId) {
		Assert.isTrue(socialProfileId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Actor actorOwner = this.actorService.findActorBySocialProfileId(socialProfileId);
		Assert.isTrue(actorLogged.equals(actorOwner), "The logged actor is not the owner of this entity");

		final SocialProfile result;

		result = this.socialProfileRepository.findOne(socialProfileId);
		Assert.notNull(result);

		return result;
	}

	public void deleteSocialProfiles() {
		final Actor actor = this.actorService.findActorLogged();

		final Collection<SocialProfile> socialProfiles = this.socialProfileRepository.findSocialProfilesByActorId(actor.getId());
		for (final SocialProfile sp : socialProfiles)
			this.socialProfileRepository.delete(sp);
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public SocialProfile reconstruct(final SocialProfile socialProfile, final BindingResult binding) {
		SocialProfile result;

		if (socialProfile.getId() == 0) {
			final Actor actorLogged = this.actorService.findActorLogged();
			result = socialProfile;
			result.setActor(actorLogged);
		} else {
			result = this.socialProfileRepository.findOne(socialProfile.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setNick(socialProfile.getNick());
			result.setSocialNetworkName(socialProfile.getSocialNetworkName());
			result.setProfileLink(socialProfile.getProfileLink());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.socialProfileRepository.flush();
	}

}
