
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ChapterRepository;
import domain.Chapter;

@Service
@Transactional
public class ChapterService {

	// Managed repository
	@Autowired
	private ChapterRepository	chapterRepository;


	// Supporting services

	// Simple CRUD methods
	public Chapter create() {
		Chapter result;

		result = new Chapter();

		return result;
	}

	public Collection<Chapter> findAll() {
		Collection<Chapter> result;

		result = this.chapterRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Chapter findOne(final int chapterId) {
		Assert.isTrue(chapterId != 0);

		Chapter result;

		result = this.chapterRepository.findOne(chapterId);
		Assert.notNull(result);

		return result;
	}

	public Chapter save(final Chapter chapter) {
		Assert.notNull(chapter);

		Chapter result;

		result = this.chapterRepository.save(chapter);

		return result;
	}

	public void delete(final Chapter chapter) {
		Assert.notNull(chapter);
		Assert.isTrue(chapter.getId() != 0);
		Assert.isTrue(this.chapterRepository.exists(chapter.getId()));

		this.chapterRepository.delete(chapter);
	}

	// Other business methods

	// Reconstruct methods

}
