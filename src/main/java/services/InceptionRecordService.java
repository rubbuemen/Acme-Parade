
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.InceptionRecordRepository;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	// Managed repository
	@Autowired
	private InceptionRecordRepository	inceptionRecordRepository;


	// Supporting services

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

	public InceptionRecord save(final InceptionRecord inceptionRecord) {
		Assert.notNull(inceptionRecord);

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

	// Reconstruct methods

}
