
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FloatRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.Float;
import domain.Parade;

@Service
@Transactional
public class FloatService {

	// Managed repository
	@Autowired
	private FloatRepository		floatRepository;

	// Supporting services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ParadeService		paradeService;


	// Simple CRUD methods
	// R10.1
	public Float create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Float result;

		result = new Float();

		return result;
	}

	public Collection<Float> findAll() {
		Collection<Float> result;

		result = this.floatRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Float findOne(final int floatId) {
		Assert.isTrue(floatId != 0);
		Float result;

		result = this.floatRepository.findOne(floatId);
		Assert.notNull(result);

		return result;
	}

	// R10.1
	public Float save(final Float floatE) {
		Assert.notNull(floatE);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		Float result;

		if (floatE.getId() == 0) {
			result = this.floatRepository.save(floatE);
			final Collection<Float> floatsBrotherhoodLogged = brotherhoodLogged.getFloats();
			floatsBrotherhoodLogged.add(result);
			brotherhoodLogged.setFloats(floatsBrotherhoodLogged);
			this.brotherhoodService.save(brotherhoodLogged);
		} else {
			final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByFloatId(floatE.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");
			result = this.floatRepository.save(floatE);
		}

		return result;
	}

	// R10.1
	public void delete(final Float floatE) {
		Assert.notNull(floatE);
		Assert.isTrue(floatE.getId() != 0);
		Assert.isTrue(this.floatRepository.exists(floatE.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByFloatId(floatE.getId());
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		final Collection<Parade> paradesFloat = this.paradeService.findParadesByFloatId(floatE.getId());
		for (final Parade p : paradesFloat) {
			Assert.isTrue(p.getFloats().size() > 1, "You can not eliminate this float because the parade would run out of floats");
			final Collection<Float> floatsParade = p.getFloats();
			floatsParade.remove(floatE);
			p.setFloats(floatsParade);
			this.paradeService.save(p);
		}

		final Collection<Float> floatsActorLogged = brotherhoodLogged.getFloats();
		floatsActorLogged.remove(floatE);
		brotherhoodLogged.setFloats(floatsActorLogged);
		this.brotherhoodService.save(brotherhoodLogged);

		this.floatRepository.delete(floatE);
	}

	// Other business methods
	// R8.2
	public Collection<Float> findFloatsByBrotherhoodId(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		Collection<Float> result;

		result = this.floatRepository.findFloatsByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	// R10.1
	public Collection<Float> findFloatsByBrotherhoodLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Collection<Float> result;

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		result = brotherhoodLogged.getFloats();
		Assert.notNull(result);

		return result;
	}

	// R10.1
	public Float findFloatBrotherhoodLogged(final int floatId) {
		Assert.isTrue(floatId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByFloatId(floatId);
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		Float result;

		result = this.floatRepository.findOne(floatId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Float reconstruct(final Float floatE, final BindingResult binding) {
		Float result;

		if (floatE.getId() == 0)
			result = floatE;
		else {
			result = this.floatRepository.findOne(floatE.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(floatE.getTitle());
			result.setDescription(floatE.getDescription());
			result.setPictures(floatE.getPictures());
		}

		this.validator.validate(result, binding);

		return result;
	}

}
