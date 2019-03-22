
package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PeriodRecordRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	// Managed repository
	@Autowired
	private PeriodRecordRepository	periodRecordRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;


	// Simple CRUD methods
	// R2.2(Acme-Parade)
	public PeriodRecord create() {
		PeriodRecord result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		result = new PeriodRecord();

		return result;
	}

	public Collection<PeriodRecord> findAll() {
		Collection<PeriodRecord> result;

		result = this.periodRecordRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public PeriodRecord findOne(final int periodRecordId) {
		Assert.isTrue(periodRecordId != 0);

		PeriodRecord result;

		result = this.periodRecordRepository.findOne(periodRecordId);
		Assert.notNull(result);

		return result;
	}

	// R2.2(Acme-Parade)
	public PeriodRecord save(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		PeriodRecord result;

		final Integer startYear = periodRecord.getStartYear();
		final Integer endYear = periodRecord.getEndYear();

		final Calendar cal = Calendar.getInstance();
		final Integer actualYear = cal.get(Calendar.YEAR);

		if (startYear != null && endYear != null) {
			Assert.isTrue(startYear <= actualYear, "Start year must be past");
			Assert.isTrue(endYear <= actualYear, "End year must be past");
			Assert.isTrue(startYear <= endYear, "Start year must be before end year");
		}

		if (periodRecord.getId() == 0) {
			result = this.periodRecordRepository.save(periodRecord);
			final History historyBrotherhoodLogged = this.historyService.findHistoryByBrotherhoodLogged();
			final Collection<PeriodRecord> periodRecordsHistory = historyBrotherhoodLogged.getPeriodRecords();
			periodRecordsHistory.add(result);
			historyBrotherhoodLogged.setPeriodRecords(periodRecordsHistory);
			this.historyService.saveAuxiliar(historyBrotherhoodLogged);
		} else {
			final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByPeriodRecordId(periodRecord.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");
			result = this.periodRecordRepository.save(periodRecord);
		}

		return result;
	}

	// R2.2(Acme-Parade)
	public void delete(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		Assert.isTrue(periodRecord.getId() != 0);
		Assert.isTrue(this.periodRecordRepository.exists(periodRecord.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final History history = this.historyService.findHistoryByPeriodRecordId(periodRecord.getId());

		final Collection<PeriodRecord> periodRecordsHistory = history.getPeriodRecords();
		periodRecordsHistory.remove(periodRecord);
		this.historyService.saveAuxiliar(history);

		this.periodRecordRepository.delete(periodRecord);
	}

	// Other business methods

	public PeriodRecord findPeriodRecordBrotherhoodLogged(final int periodRecordId) {
		Assert.isTrue(periodRecordId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByPeriodRecordId(periodRecordId);
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		PeriodRecord result;

		result = this.periodRecordRepository.findOne(periodRecordId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public PeriodRecord reconstruct(final PeriodRecord periodRecord, final BindingResult binding) {
		PeriodRecord result;

		if (periodRecord.getId() == 0)
			result = periodRecord;
		else {
			result = this.periodRecordRepository.findOne(periodRecord.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(periodRecord.getTitle());
			result.setDescription(periodRecord.getDescription());
			result.setStartYear(periodRecord.getStartYear());
			result.setEndYear(periodRecord.getEndYear());
			result.setPhotos(periodRecord.getPhotos());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.periodRecordRepository.flush();
	}

}
