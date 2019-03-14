
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	// Managed repository
	@Autowired
	private PeriodRecordRepository	periodRecordRepository;


	// Supporting services

	// Simple CRUD methods
	public PeriodRecord create() {
		PeriodRecord result;

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

	public PeriodRecord save(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);

		PeriodRecord result;

		result = this.periodRecordRepository.save(periodRecord);

		return result;
	}

	public void delete(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		Assert.isTrue(periodRecord.getId() != 0);
		Assert.isTrue(this.periodRecordRepository.exists(periodRecord.getId()));

		this.periodRecordRepository.delete(periodRecord);
	}

	// Other business methods

	// Reconstruct methods

}
