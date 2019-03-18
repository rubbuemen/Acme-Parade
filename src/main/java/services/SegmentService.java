
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SegmentRepository;
import domain.Actor;
import domain.Parade;
import domain.Segment;

@Service
@Transactional
public class SegmentService {

	// Managed repository
	@Autowired
	private SegmentRepository	segmentRepository;

	// Supporting services
	@Autowired
	ActorService				actorService;

	@Autowired
	ParadeService				paradeService;


	// Simple CRUD methods
	public Segment create() {
		Segment result;

		result = new Segment();

		return result;
	}

	public Collection<Segment> findAll() {
		Collection<Segment> result;

		result = this.segmentRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Segment findOne(final int segmentId) {
		Assert.isTrue(segmentId != 0);

		Segment result;

		result = this.segmentRepository.findOne(segmentId);
		Assert.notNull(result);

		return result;
	}

	public Segment save(final Segment segment) {
		Assert.notNull(segment);

		Segment result;

		result = this.segmentRepository.save(segment);

		return result;
	}

	public void delete(final Segment segment) {
		Assert.notNull(segment);
		Assert.isTrue(segment.getId() != 0);
		Assert.isTrue(this.segmentRepository.exists(segment.getId()));

		this.segmentRepository.delete(segment);
	}

	// Other business methods
	// R9.3 (Acme-Parade)
	public Collection<Segment> findSegmentsByParade(final int paradeId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Collection<Segment> result;

		final Parade parade = this.paradeService.findParadeBrotherhoodLogged(paradeId);

		result = parade.getSegments();
		Assert.notNull(result);

		return result;
	}

	// Reconstruct methods

}
