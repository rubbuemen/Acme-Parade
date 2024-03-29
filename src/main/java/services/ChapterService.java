
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChapterRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Area;
import domain.Box;
import domain.Chapter;
import domain.Proclaim;
import forms.ChapterForm;

@Service
@Transactional
public class ChapterService {

	// Managed repository
	@Autowired
	private ChapterRepository	chapterRepository;

	// Supporting services
	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private AreaService			areaService;

	@Autowired
	private ProclaimService		proclaimService;


	// Simple CRUD methods
	// R7.1 (Acme-Parade)
	public Chapter create() {
		Chapter result;

		result = new Chapter();
		final Collection<Box> boxes = new HashSet<>();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.CHAPTER);
		userAccount.addAuthority(auth);
		result.setBoxes(boxes);
		result.setUserAccount(userAccount);

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

	// R7.1 (Acme-Parade)
	public Chapter save(final Chapter chapter) {
		Assert.notNull(chapter);

		Chapter result;

		result = (Chapter) this.actorService.save(chapter);
		result = this.chapterRepository.save(result);

		return result;
	}

	public void delete(final Chapter chapter) {
		Assert.notNull(chapter);
		Assert.isTrue(chapter.getId() != 0);
		Assert.isTrue(this.chapterRepository.exists(chapter.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginChapter(actorLogged);

		final Chapter chapterLogged = (Chapter) actorLogged;

		this.actorService.deleteEntities(chapterLogged);

		final Collection<Proclaim> proclaims = this.proclaimService.findProclaimsByChapterId(chapterLogged.getId());
		for (final Proclaim p : proclaims)
			this.proclaimService.delete(p);

		this.chapterRepository.flush();
		this.chapterRepository.delete(chapter);
	}

	// Other business methods
	// R8.1
	public Chapter selfAssign(final int areaId) {
		Assert.isTrue(areaId != 0);

		Chapter result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginChapter(actorLogged);

		final Chapter chapterLogged = (Chapter) actorLogged;

		Assert.isNull(chapterLogged.getArea(), "You already have an assigned area");

		final Area area = this.areaService.findOne(areaId);
		Assert.isTrue(this.areaService.findAreasToSelfAssign().contains(area), "This area is already assigned");
		chapterLogged.setArea(area);

		result = this.chapterRepository.save(chapterLogged);
		return result;
	}

	public Chapter findChapterByParadeId(final int paradeId) {
		Assert.isTrue(paradeId != 0);

		Chapter result;

		result = this.chapterRepository.findChapterByParadeId(paradeId);
		return result;
	}

	public Chapter findChapterByAreaId(final int areaId) {
		Assert.isTrue(areaId != 0);

		Chapter result;

		result = this.chapterRepository.findChapterByAreaId(areaId);
		return result;
	}

	public Chapter findChapterByBrotherhoodId(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		Chapter result;

		result = this.chapterRepository.findChapterByBrotherhoodId(brotherhoodId);
		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public ChapterForm reconstruct(final ChapterForm chapterForm, final BindingResult binding) {
		ChapterForm result;
		final Chapter chapter = chapterForm.getActor();

		if (chapter.getId() == 0) {
			final Collection<Box> boxes = new HashSet<>();
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.CHAPTER);
			userAccount.addAuthority(auth);
			userAccount.setUsername(chapterForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(chapterForm.getActor().getUserAccount().getPassword());
			chapter.setBoxes(boxes);
			chapter.setUserAccount(userAccount);
			chapterForm.setActor(chapter);
		} else {
			final Chapter res = this.chapterRepository.findOne(chapter.getId());
			Assert.notNull(res, "This entity does not exist");
			res.setName(chapter.getName());
			res.setMiddleName(chapter.getMiddleName());
			res.setSurname(chapter.getSurname());
			res.setPhoto(chapter.getPhoto());
			res.setEmail(chapter.getEmail());
			res.setPhoneNumber(chapter.getPhoneNumber());
			res.setAddress(chapter.getAddress());
			res.setTitle(chapter.getTitle());
			chapterForm.setActor(res);
		}

		result = chapterForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.chapterRepository.flush();
	}

}
