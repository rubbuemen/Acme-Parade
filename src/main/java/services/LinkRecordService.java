
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.LinkRecordRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Service
@Transactional
public class LinkRecordService {

	// Managed repository
	@Autowired
	private LinkRecordRepository	linkRecordRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;


	// Simple CRUD methods
	// R2.2(Acme-Parade)
	public LinkRecord create() {
		LinkRecord result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		result = new LinkRecord();

		return result;
	}

	public Collection<LinkRecord> findAll() {
		Collection<LinkRecord> result;

		result = this.linkRecordRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public LinkRecord findOne(final int linkRecordId) {
		Assert.isTrue(linkRecordId != 0);

		LinkRecord result;

		result = this.linkRecordRepository.findOne(linkRecordId);
		Assert.notNull(result);

		return result;
	}

	// R2.2(Acme-Parade)
	public LinkRecord save(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		LinkRecord result;

		if (linkRecord.getId() == 0) {
			result = this.linkRecordRepository.save(linkRecord);
			final History historyBrotherhoodLogged = this.historyService.findHistoryByBrotherhoodLogged();
			final Collection<LinkRecord> linkRecordsHistory = historyBrotherhoodLogged.getLinkRecords();
			linkRecordsHistory.add(result);
			historyBrotherhoodLogged.setLinkRecords(linkRecordsHistory);
			this.historyService.saveAuxiliar(historyBrotherhoodLogged);
		} else {
			final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByLinkRecordId(linkRecord.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");
			result = this.linkRecordRepository.save(linkRecord);
		}

		return result;
	}

	// R2.2(Acme-Parade)
	public void delete(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);
		Assert.isTrue(linkRecord.getId() != 0);
		Assert.isTrue(this.linkRecordRepository.exists(linkRecord.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final History history = this.historyService.findHistoryByLinkRecordId(linkRecord.getId());

		final Collection<LinkRecord> linkRecordsHistory = history.getLinkRecords();
		linkRecordsHistory.remove(linkRecord);
		this.historyService.saveAuxiliar(history);

		this.linkRecordRepository.delete(linkRecord);
	}

	// Other business methods

	public LinkRecord findLinkRecordBrotherhoodLogged(final int linkRecordId) {
		Assert.isTrue(linkRecordId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByLinkRecordId(linkRecordId);
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		LinkRecord result;

		result = this.linkRecordRepository.findOne(linkRecordId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public LinkRecord reconstruct(final LinkRecord linkRecord, final BindingResult binding) {
		LinkRecord result;

		if (linkRecord.getId() == 0)
			result = linkRecord;
		else {
			result = this.linkRecordRepository.findOne(linkRecord.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(linkRecord.getTitle());
			result.setDescription(linkRecord.getDescription());
			result.setBrotherhood(linkRecord.getBrotherhood());
		}

		this.validator.validate(result, binding);
		this.linkRecordRepository.flush();

		return result;
	}

	public void flush() {
		this.linkRecordRepository.flush();
	}
}
