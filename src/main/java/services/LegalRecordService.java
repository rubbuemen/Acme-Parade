
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LegalRecordRepository;
import domain.LegalRecord;

@Service
@Transactional
public class LegalRecordService {

	// Managed repository
	@Autowired
	private LegalRecordRepository	legalRecordRepository;


	// Supporting services

	// Simple CRUD methods
	public LegalRecord create() {
		LegalRecord result;

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

	public LegalRecord save(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);

		LegalRecord result;

		result = this.legalRecordRepository.save(legalRecord);

		return result;
	}

	public void delete(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);
		Assert.isTrue(legalRecord.getId() != 0);
		Assert.isTrue(this.legalRecordRepository.exists(legalRecord.getId()));

		this.legalRecordRepository.delete(legalRecord);
	}

	// Other business methods

	// Reconstruct methods

}
