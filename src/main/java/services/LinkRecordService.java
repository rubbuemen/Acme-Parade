
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LinkRecordRepository;
import domain.LinkRecord;

@Service
@Transactional
public class LinkRecordService {

	// Managed repository
	@Autowired
	private LinkRecordRepository	linkRecordRepository;


	// Supporting services

	// Simple CRUD methods
	public LinkRecord create() {
		LinkRecord result;

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

	public LinkRecord save(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);

		LinkRecord result;

		result = this.linkRecordRepository.save(linkRecord);

		return result;
	}

	public void delete(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);
		Assert.isTrue(linkRecord.getId() != 0);
		Assert.isTrue(this.linkRecordRepository.exists(linkRecord.getId()));

		this.linkRecordRepository.delete(linkRecord);
	}

	// Other business methods

	// Reconstruct methods

}
