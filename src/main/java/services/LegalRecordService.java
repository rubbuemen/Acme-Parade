
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.LegalRecordRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Service
@Transactional
public class LegalRecordService {

	// Managed repository
	@Autowired
	private LegalRecordRepository	legalRecordRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;


	// Simple CRUD methods
	// R2.2(Acme-Parade)
	public LegalRecord create() {
		LegalRecord result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		result = new LegalRecord();

		return result;
	}

	public Collection<LegalRecord> findAll() {
		Collection<LegalRecord> result;

		result = this.legalRecordRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public LegalRecord findOne(final int legalRecordId) {
		Assert.isTrue(legalRecordId != 0);

		LegalRecord result;

		result = this.legalRecordRepository.findOne(legalRecordId);
		Assert.notNull(result);

		return result;
	}

	// R2.2(Acme-Parade)
	public LegalRecord save(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		LegalRecord result;

		if (legalRecord.getId() == 0) {
			result = this.legalRecordRepository.save(legalRecord);
			final History historyBrotherhoodLogged = this.historyService.findHistoryByBrotherhoodLogged();
			final Collection<LegalRecord> legalRecordsHistory = historyBrotherhoodLogged.getLegalRecords();
			legalRecordsHistory.add(result);
			historyBrotherhoodLogged.setLegalRecords(legalRecordsHistory);
			this.historyService.saveAuxiliar(historyBrotherhoodLogged);
		} else {
			final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByLegalRecordId(legalRecord.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");
			result = this.legalRecordRepository.save(legalRecord);
		}

		return result;
	}

	// R2.2(Acme-Parade)
	public void delete(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);
		Assert.isTrue(legalRecord.getId() != 0);
		Assert.isTrue(this.legalRecordRepository.exists(legalRecord.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final History history = this.historyService.findHistoryByLegalRecordId(legalRecord.getId());

		final Collection<LegalRecord> legalRecordsHistory = history.getLegalRecords();
		legalRecordsHistory.remove(legalRecord);
		this.historyService.saveAuxiliar(history);

		this.legalRecordRepository.delete(legalRecord);
	}

	// Other business methods

	public LegalRecord findLegalRecordBrotherhoodLogged(final int legalRecordId) {
		Assert.isTrue(legalRecordId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByLegalRecordId(legalRecordId);
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		LegalRecord result;

		result = this.legalRecordRepository.findOne(legalRecordId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public LegalRecord reconstruct(final LegalRecord legalRecord, final BindingResult binding) {
		LegalRecord result;

		if (legalRecord.getId() == 0)
			result = legalRecord;
		else {
			result = this.legalRecordRepository.findOne(legalRecord.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(legalRecord.getTitle());
			result.setDescription(legalRecord.getDescription());
			result.setLegalName(legalRecord.getLegalName());
			result.setVATNumber(legalRecord.getVATNumber());
			result.setApplicableLaws(legalRecord.getApplicableLaws());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.legalRecordRepository.flush();
	}
}
