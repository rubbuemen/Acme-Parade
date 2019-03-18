
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MiscellaneousRecordRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

@Service
@Transactional
public class MiscellaneousRecordService {

	// Managed repository
	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;

	// Supporting services
	@Autowired
	private ActorService					actorService;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private HistoryService					historyService;


	// Simple CRUD methods
	// R2.2(Acme-Parade)
	public MiscellaneousRecord create() {
		MiscellaneousRecord result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		result = new MiscellaneousRecord();

		return result;
	}

	public Collection<MiscellaneousRecord> findAll() {
		Collection<MiscellaneousRecord> result;

		result = this.miscellaneousRecordRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public MiscellaneousRecord findOne(final int miscellaneousRecordId) {
		Assert.isTrue(miscellaneousRecordId != 0);

		MiscellaneousRecord result;

		result = this.miscellaneousRecordRepository.findOne(miscellaneousRecordId);
		Assert.notNull(result);

		return result;
	}

	// R2.2(Acme-Parade)
	public MiscellaneousRecord save(final MiscellaneousRecord miscellaneousRecord) {
		Assert.notNull(miscellaneousRecord);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		MiscellaneousRecord result;

		if (miscellaneousRecord.getId() == 0) {
			result = this.miscellaneousRecordRepository.save(miscellaneousRecord);
			final History historyBrotherhoodLogged = this.historyService.findHistoryByBrotherhoodLogged();
			final Collection<MiscellaneousRecord> miscellaneousRecordsHistory = historyBrotherhoodLogged.getMiscellaneousRecords();
			miscellaneousRecordsHistory.add(result);
			historyBrotherhoodLogged.setMiscellaneousRecords(miscellaneousRecordsHistory);
			this.historyService.saveAuxiliar(historyBrotherhoodLogged);
		} else {
			final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByMiscellaneousRecordId(miscellaneousRecord.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");
			result = this.miscellaneousRecordRepository.save(miscellaneousRecord);
		}

		return result;
	}

	// R2.2(Acme-Parade)
	public void delete(final MiscellaneousRecord miscellaneousRecord) {
		Assert.notNull(miscellaneousRecord);
		Assert.isTrue(miscellaneousRecord.getId() != 0);
		Assert.isTrue(this.miscellaneousRecordRepository.exists(miscellaneousRecord.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final History history = this.historyService.findHistoryByMiscellaneousRecordId(miscellaneousRecord.getId());

		final Collection<MiscellaneousRecord> miscellaneousRecordsHistory = history.getMiscellaneousRecords();
		miscellaneousRecordsHistory.remove(miscellaneousRecord);
		this.historyService.saveAuxiliar(history);

		this.miscellaneousRecordRepository.delete(miscellaneousRecord);
	}

	// Other business methods

	public MiscellaneousRecord findMiscellaneousRecordBrotherhoodLogged(final int miscellaneousRecordId) {
		Assert.isTrue(miscellaneousRecordId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByMiscellaneousRecordId(miscellaneousRecordId);
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		MiscellaneousRecord result;

		result = this.miscellaneousRecordRepository.findOne(miscellaneousRecordId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public MiscellaneousRecord reconstruct(final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {
		MiscellaneousRecord result;

		if (miscellaneousRecord.getId() == 0)
			result = miscellaneousRecord;
		else {
			result = this.miscellaneousRecordRepository.findOne(miscellaneousRecord.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(miscellaneousRecord.getTitle());
			result.setDescription(miscellaneousRecord.getDescription());
		}

		this.validator.validate(result, binding);

		return result;
	}
}
