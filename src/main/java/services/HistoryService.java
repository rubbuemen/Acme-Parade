
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import domain.History;

@Service
@Transactional
public class HistoryService {

	// Managed repository
	@Autowired
	private HistoryRepository	historyRepository;


	// Supporting services

	// Simple CRUD methods
	public History create() {
		History result;

		result = new History();

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

	public History save(final History history) {
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

	// Reconstruct methods

}
