
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.HistoryRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Service
@Transactional
public class HistoryService {

	// Managed repository
	@Autowired
	private HistoryRepository		historyRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private InceptionRecordService	inceptionRecordService;


	// Simple CRUD methods
	public History create() {
		History result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		result = new History();
		final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
		final Collection<PeriodRecord> periodRecords = new HashSet<>();
		final Collection<LegalRecord> legalRecords = new HashSet<>();
		final Collection<LinkRecord> linkRecords = new HashSet<>();
		final Collection<MiscellaneousRecord> miscellaneousRecords = new HashSet<>();

		result.setBrotherhood((Brotherhood) actorLogged);
		result.setInceptionRecord(inceptionRecord);
		result.setPeriodRecords(periodRecords);
		result.setLegalRecords(legalRecords);
		result.setLinkRecords(linkRecords);
		result.setMiscellaneousRecords(miscellaneousRecords);

		return result;
	}

	public Collection<History> findAll() {
		Collection<History> result;

		result = this.historyRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public History findOne(final int historyId) {
		Assert.isTrue(historyId != 0);

		History result;

		result = this.historyRepository.findOne(historyId);
		Assert.notNull(result);

		return result;
	}

	// R2.2(Acme-Parade)
	public History save(final History history) {
		Assert.notNull(history);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		History result;

		InceptionRecord inceptionRecord = history.getInceptionRecord();
		inceptionRecord = this.inceptionRecordService.save(inceptionRecord);

		history.setInceptionRecord(inceptionRecord);

		result = this.historyRepository.save(history);

		return result;
	}

	public History saveAuxiliar(final History history) {
		Assert.notNull(history);

		History result;

		result = this.historyRepository.save(history);

		return result;
	}

	public void delete(final History history) {
		Assert.notNull(history);
		Assert.isTrue(history.getId() != 0);
		Assert.isTrue(this.historyRepository.exists(history.getId()));

		this.historyRepository.delete(history);
	}

	// Other business methods
	// R2.1(Acme-Parade)
	public History findHistoryByBrotherhoodId(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		History result;

		result = this.historyRepository.findHistoryByBrotherhoodId(brotherhoodId);

		return result;
	}

	// R2.2(Acme-Parade)
	public History findHistoryByBrotherhoodLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		History result;

		result = this.historyRepository.findHistoryByBrotherhoodId(actorLogged.getId());

		return result;
	}

	public History findHistoryByPeriodRecordId(final int periodRecordId) {
		History result;

		result = this.historyRepository.findHistoryByPeriodRecordId(periodRecordId);

		return result;
	}

	public History findHistoryByLegalRecordId(final int legalRecordId) {
		History result;

		result = this.historyRepository.findHistoryByLegalRecordId(legalRecordId);

		return result;
	}

	public History findHistoryByLinkRecordId(final int linkRecordId) {
		History result;

		result = this.historyRepository.findHistoryByLinkRecordId(linkRecordId);

		return result;
	}

	public History findHistoryByMiscellaneousRecordId(final int miscellaneousRecordId) {
		History result;

		result = this.historyRepository.findHistoryByMiscellaneousRecordId(miscellaneousRecordId);

		return result;
	}

	public Collection<History> findHistoriesByLinkRecordBrotherhoodId(final int brotherhoodId) {
		Collection<History> result;

		result = this.historyRepository.findHistoriesByLinkRecordBrotherhoodId(brotherhoodId);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public History reconstruct(final History history, final BindingResult binding) {
		History result;

		//Nunca se va a editar una historia como tal, si no sus records
		final Collection<PeriodRecord> periodRecords = new HashSet<>();
		final Collection<LegalRecord> legalRecords = new HashSet<>();
		final Collection<LinkRecord> linkRecords = new HashSet<>();
		final Collection<MiscellaneousRecord> miscellaneousRecords = new HashSet<>();
		final Actor actorLogged = this.actorService.findActorLogged();

		history.setBrotherhood((Brotherhood) actorLogged);
		history.setPeriodRecords(periodRecords);
		history.setLegalRecords(legalRecords);
		history.setLinkRecords(linkRecords);
		history.setMiscellaneousRecords(miscellaneousRecords);
		result = history;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.historyRepository.flush();
	}

}
