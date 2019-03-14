
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ProclaimRepository;
import domain.Proclaim;

@Service
@Transactional
public class ProclaimService {

	// Managed repository
	@Autowired
	private ProclaimRepository	proclaimRepository;


	// Supporting services

	// Simple CRUD methods
	public Proclaim create() {
		Proclaim result;

		result = new Proclaim();

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

	public Proclaim save(final Proclaim proclaim) {
		Assert.notNull(proclaim);

		Proclaim result;

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

}
