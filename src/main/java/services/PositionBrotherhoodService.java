
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionBrotherhoodRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.PositionBrotherhood;

@Service
@Transactional
public class PositionBrotherhoodService {

	// Managed repository
	@Autowired
	private PositionBrotherhoodRepository	positionBrotherhoodRepository;

	// Supporting services
	@Autowired
	private ActorService					actorService;


	// Simple CRUD methods
	// R12.2
	public PositionBrotherhood create() {
		PositionBrotherhood result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = new PositionBrotherhood();

		return result;
	}

	// R12.2
	public Collection<PositionBrotherhood> findAll() {
		Collection<PositionBrotherhood> result;

		result = this.positionBrotherhoodRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public PositionBrotherhood findOne(final int positionBrotherhoodId) {
		Assert.isTrue(positionBrotherhoodId != 0);

		PositionBrotherhood result;

		result = this.positionBrotherhoodRepository.findOne(positionBrotherhoodId);
		Assert.notNull(result);

		return result;
	}

	// R10.3: When a member is enrolled, a position must be selected, R12.2
	public PositionBrotherhood save(final PositionBrotherhood positionBrotherhood) {
		Assert.notNull(positionBrotherhood);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		if (actorLogged instanceof Brotherhood)
			this.actorService.checkUserLoginBrotherhood(actorLogged);
		else
			this.actorService.checkUserLoginAdministrator(actorLogged);

		PositionBrotherhood result;

		result = this.positionBrotherhoodRepository.save(positionBrotherhood);

		return result;
	}

	// R12.2
	public void delete(final PositionBrotherhood positionBrotherhood) {
		Assert.notNull(positionBrotherhood);
		Assert.isTrue(positionBrotherhood.getId() != 0);
		Assert.isTrue(this.positionBrotherhoodRepository.exists(positionBrotherhood.getId()));

		final Collection<PositionBrotherhood> positionBrotherhoodsUsed = this.positionBrotherhoodRepository.findPositionsBrotherhoodUsed();
		Assert.isTrue(!positionBrotherhoodsUsed.contains(positionBrotherhood), "This position can not be deleted because it is in use");

		this.positionBrotherhoodRepository.delete(positionBrotherhood);
	}

	// Other business methods
	public PositionBrotherhood findPositionBrotherhoodByMemberIdBrotherhoodLogged(final int memberId) {
		Assert.isTrue(memberId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		PositionBrotherhood result;

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		result = this.positionBrotherhoodRepository.findPositionBrotherhoodByMemberIdBrotherhoodId(memberId, brotherhoodLogged.getId());
		Assert.notNull(result);

		return result;
	}

	public Collection<PositionBrotherhood> findPositionsBrotherhoodUsed() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		final Collection<PositionBrotherhood> result = this.positionBrotherhoodRepository.findPositionsBrotherhoodUsed();

		return result;
	}


	// Reconstruct methods
	@Autowired(required = false)
	private Validator	validator;


	public PositionBrotherhood reconstruct(final PositionBrotherhood positionBrotherhood, final BindingResult binding) {
		PositionBrotherhood result;

		if (positionBrotherhood.getId() == 0)
			result = positionBrotherhood;
		else {
			result = this.positionBrotherhoodRepository.findOne(positionBrotherhood.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setNameEnglish(positionBrotherhood.getNameEnglish());
			result.setNameSpanish(positionBrotherhood.getNameSpanish());
		}

		this.validator.validate(result, binding);

		return result;
	}

}
