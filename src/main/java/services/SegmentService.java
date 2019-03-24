
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SegmentRepository;
import domain.Actor;
import domain.Brotherhood;
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

	@Autowired
	BrotherhoodService			brotherhoodService;


	// Simple CRUD methods
	// R9.3 (Acme-Parade)
	public Segment create() {
		Segment result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

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

	// R9.3 (Acme-Parade)
	public Segment save(final Segment segment, final Parade parade) {
		Assert.notNull(segment);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Segment result;

		if (segment.getTimeReachOrigin() != null && segment.getTimeReachDestination() != null)
			Assert.isTrue(segment.getTimeReachOrigin().compareTo(segment.getTimeReachDestination()) < 0,
				"The time at which the parade is expected to be reaching the origin must be before than the time at which it's expected to be reaching the destination");

		if (segment.getId() == 0) {
			final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByParadeId(parade.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");
			final List<Segment> segmentsParade = new ArrayList<>(parade.getSegments());
			if (segmentsParade.size() > 0) {
				final Segment lastSegment = segmentsParade.get(segmentsParade.size() - 1);
				segment.setOrigin(lastSegment.getDestination());
				segment.setTimeReachOrigin(lastSegment.getTimeReachDestination());
			}
			result = this.segmentRepository.save(segment);
			segmentsParade.add(result);
			parade.setSegments(segmentsParade);
			this.paradeService.saveAuxiliar(parade);
		} else {
			final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByParadeId(parade.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");
			final List<Segment> segmentsParade = new ArrayList<>(parade.getSegments());
			if (segmentsParade.size() > 1)
				if (segmentsParade.get(0).equals(segment)) {
					final Segment nextSegment = segmentsParade.get(1);
					if (segment.getTimeReachOrigin() != null && segment.getTimeReachDestination() != null)
						Assert.isTrue(segment.getTimeReachDestination().compareTo(nextSegment.getTimeReachDestination()) < 0,
							"The time at which the parade is expected to be reaching the destination must be before than the time it is expected to reach the destination of the next segment");
					nextSegment.setOrigin(segment.getDestination());
					nextSegment.setTimeReachOrigin(segment.getTimeReachDestination());
					this.segmentRepository.save(nextSegment);
				} else if (segmentsParade.get(segmentsParade.size() - 1).equals(segment)) {
					final Segment previousSegment = segmentsParade.get(segmentsParade.size() - 2);
					if (segment.getTimeReachOrigin() != null && segment.getTimeReachDestination() != null)
						Assert.isTrue(segment.getTimeReachOrigin().compareTo(previousSegment.getTimeReachOrigin()) > 0,
							"The time at which the parade is expected to be reaching the origin must be after than the time it is expected to reach the origin of the previous segment");
					previousSegment.setDestination(segment.getOrigin());
					previousSegment.setTimeReachDestination(segment.getTimeReachOrigin());
					this.segmentRepository.save(previousSegment);
				} else {
					final int actualIndex = segmentsParade.indexOf(segment);
					final Segment nextSegment = segmentsParade.get(actualIndex + 1);
					final Segment previousSegment = segmentsParade.get(actualIndex - 1);
					if (segment.getTimeReachOrigin() != null && segment.getTimeReachDestination() != null) {
						Assert.isTrue(segment.getTimeReachOrigin().compareTo(previousSegment.getTimeReachOrigin()) > 0,
							"The time at which the parade is expected to be reaching the origin must be after than the time it is expected to reach the origin of the previous segment");
						Assert.isTrue(segment.getTimeReachDestination().compareTo(nextSegment.getTimeReachDestination()) < 0,
							"The time at which the parade is expected to be reaching the destination must be before than the time it is expected to reach the destination of the next segment");
					}
					previousSegment.setDestination(segment.getOrigin());
					previousSegment.setTimeReachDestination(segment.getTimeReachOrigin());
					nextSegment.setOrigin(segment.getDestination());
					nextSegment.setTimeReachOrigin(segment.getTimeReachDestination());
					this.segmentRepository.save(previousSegment);
					this.segmentRepository.save(nextSegment);
				}

			result = this.segmentRepository.save(segment);
		}

		return result;
	}
	public Segment saveAuxiliar(final Segment segment) {
		Assert.notNull(segment);

		Segment result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		result = this.segmentRepository.save(segment);

		return result;
	}

	// R9.3 (Acme-Parade)
	public void delete(final Segment segment, final Parade parade) {
		Assert.notNull(segment);
		Assert.isTrue(segment.getId() != 0);
		Assert.isTrue(this.segmentRepository.exists(segment.getId()));

		final List<Segment> segmentsParade = new ArrayList<>(parade.getSegments());

		if (!segmentsParade.get(0).equals(segment) && !segmentsParade.get(segmentsParade.size() - 1).equals(segment)) {
			final int actualIndex = segmentsParade.indexOf(segment);
			final Segment nextSegment = segmentsParade.get(actualIndex + 1);
			final Segment previousSegment = segmentsParade.get(actualIndex - 1);
			nextSegment.setOrigin(previousSegment.getDestination());
			nextSegment.setTimeReachOrigin(previousSegment.getTimeReachDestination());
			this.segmentRepository.save(nextSegment);
		}

		segmentsParade.remove(segment);
		parade.setSegments(segmentsParade);
		this.paradeService.saveAuxiliar(parade);

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
	@Autowired
	private Validator	validator;


	public Segment reconstruct(final Segment segment, final BindingResult binding) {
		Segment result;

		if (segment.getId() == 0)
			result = segment;
		else {
			final Segment originalSegment = this.segmentRepository.findOne(segment.getId());
			Assert.notNull(originalSegment, "This entity does not exist");
			result = segment;
		}

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public void flush() {
		this.segmentRepository.flush();
	}

}
