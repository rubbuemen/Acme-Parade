
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProclaimRepository;
import domain.Actor;
import domain.Chapter;
import domain.Proclaim;

@Service
@Transactional
public class ProclaimService {

	// Managed repository
	@Autowired
	private ProclaimRepository	proclaimRepository;

	// Supporting services
	@Autowired
	ActorService				actorService;


	// Simple CRUD methods
	//R17.1 (Acme-Parade)
	public Proclaim create() {
		Proclaim result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginChapter(actorLogged);

		result = new Proclaim();
		final Date moment = new Date(System.currentTimeMillis() - 1);

		result.setMoment(moment);
		result.setChapter((Chapter) actorLogged);

		return result;
	}

	public Collection<Proclaim> findAll() {
		Collection<Proclaim> result;

		result = this.proclaimRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Proclaim findOne(final int proclaimId) {
		Assert.isTrue(proclaimId != 0);

		Proclaim result;

		result = this.proclaimRepository.findOne(proclaimId);
		Assert.notNull(result);

		return result;
	}

	//R17.1 (Acme-Parade)
	public Proclaim save(final Proclaim proclaim) {
		Assert.notNull(proclaim);
		Assert.isTrue(proclaim.getId() == 0, "There's no way to update a proclaim");

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginChapter(actorLogged);

		Proclaim result;
		final Date moment = new Date(System.currentTimeMillis() - 1);

		proclaim.setMoment(moment);

		result = this.proclaimRepository.save(proclaim);

		return result;
	}

	public void delete(final Proclaim proclaim) {
		Assert.notNull(proclaim);
		Assert.isTrue(proclaim.getId() != 0);
		Assert.isTrue(this.proclaimRepository.exists(proclaim.getId()));

		this.proclaimRepository.delete(proclaim);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Proclaim reconstruct(final Proclaim proclaim, final BindingResult binding) {
		Proclaim result;

		//Nunca se va a editar un proclaim
		final Actor actorLogged = this.actorService.findActorLogged();
		final Date moment = new Date(System.currentTimeMillis() - 1);
		proclaim.setMoment(moment);
		proclaim.setChapter((Chapter) actorLogged);
		result = proclaim;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.proclaimRepository.flush();
	}

}
