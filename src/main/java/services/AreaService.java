
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AreaRepository;
import domain.Actor;
import domain.Area;
import domain.Chapter;

@Service
@Transactional
public class AreaService {

	// Managed repository
	@Autowired
	private AreaRepository	areaRepository;

	// Supporting services
	@Autowired
	private ActorService	actorService;


	// Simple CRUD methods
	public Area create() {
		Area result;

		result = new Area();

		return result;
	}

	public Collection<Area> findAll() {
		Collection<Area> result;

		result = this.areaRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Area findOne(final int areaId) {
		Assert.isTrue(areaId != 0);

		Area result;

		result = this.areaRepository.findOne(areaId);
		Assert.notNull(result);

		return result;
	}

	public Area save(final Area area) {
		Assert.notNull(area);

		Area result;

		result = this.areaRepository.save(area);

		return result;
	}

	public void delete(final Area area) {
		Assert.notNull(area);
		Assert.isTrue(area.getId() != 0);
		Assert.isTrue(this.areaRepository.exists(area.getId()));

		final Collection<Area> areasUsed = this.areaRepository.findAreasBrotherhoodUsed();
		Assert.isTrue(!areasUsed.contains(area), "This position can not be deleted because it is in use");

		this.areaRepository.delete(area);
	}

	// Other business methods
	public Collection<Area> findAreasBrotherhoodUsed() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		final Collection<Area> result = this.areaRepository.findAreasBrotherhoodUsed();

		return result;
	}

	// R8.1
	public Collection<Area> findAreasToSelfAssign() {
		final Collection<Area> result = this.areaRepository.findAreasBrotherhoodUsed();
		final Collection<Area> freeAreas = this.areaRepository.findFreeAreas();
		result.retainAll(freeAreas);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginChapter(actorLogged);

		final Chapter chapterLogged = (Chapter) actorLogged;

		Assert.isNull(chapterLogged.getArea(), "You already have an assigned area");

		return result;
	}

	//R14.1 (Acme-Parade)
	public Area findAreaByChapterId(final int chapterId) {
		Assert.isTrue(chapterId != 0);

		Area result;

		result = this.areaRepository.findAreaByChapterId(chapterId);

		return result;
	}

	public Area findAreaByParadeId(final int paradeId) {
		Area result;

		result = this.areaRepository.findAreaByParadeId(paradeId);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Area reconstruct(final Area area, final BindingResult binding) {
		Area result;

		if (area.getId() == 0)
			result = area;
		else {
			result = this.areaRepository.findOne(area.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setName(area.getName());
			result.setPictures(area.getPictures());
		}

		this.validator.validate(result, binding);

		return result;
	}

}
