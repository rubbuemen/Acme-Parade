
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.InceptionRecordRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	// Managed repository
	@Autowired
	private InceptionRecordRepository	inceptionRecordRepository;

	// Supporting services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private BrotherhoodService			brotherhoodService;


	// Simple CRUD methods
	public InceptionRecord create() {
		InceptionRecord result;

		result = new InceptionRecord();

		return result;
	}

	public Collection<InceptionRecord> findAll() {
		Collection<InceptionRecord> result;

		result = this.inceptionRecordRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public InceptionRecord findOne(final int inceptionRecordId) {
		Assert.isTrue(inceptionRecordId != 0);

		InceptionRecord result;

		result = this.inceptionRecordRepository.findOne(inceptionRecordId);
		Assert.notNull(result);

		return result;
	}

	// R2.2(Acme-Parade)
	public InceptionRecord save(final InceptionRecord inceptionRecord) {
		Assert.notNull(inceptionRecord);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		InceptionRecord result;

		result = this.inceptionRecordRepository.save(inceptionRecord);

		return result;
	}

	public void delete(final InceptionRecord inceptionRecord) {
		Assert.notNull(inceptionRecord);
		Assert.isTrue(inceptionRecord.getId() != 0);
		Assert.isTrue(this.inceptionRecordRepository.exists(inceptionRecord.getId()));

		this.inceptionRecordRepository.delete(inceptionRecord);
	}

	// Other business methods
	public InceptionRecord findInceptionRecordBrotherhoodLogged(final int inceptionRecordId) {
		Assert.isTrue(inceptionRecordId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByInceptionRecordId(inceptionRecordId);
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		InceptionRecord result;

		result = this.inceptionRecordRepository.findOne(inceptionRecordId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public InceptionRecord reconstruct(final InceptionRecord inceptionRecord, final BindingResult binding) {
		InceptionRecord result;

		//No se estará creando desde aquí, unicamente se editará
		result = this.inceptionRecordRepository.findOne(inceptionRecord.getId());
		Assert.notNull(result, "This entity does not exist");
		result.setTitle(inceptionRecord.getTitle());
		result.setDescription(inceptionRecord.getDescription());
		result.setPhotos(inceptionRecord.getPhotos());

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.inceptionRecordRepository.flush();
	}

}
