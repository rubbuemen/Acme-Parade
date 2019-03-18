
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Actor;
import domain.Area;
import domain.Finder;
import domain.Member;
import domain.Parade;

@Service
@Transactional
public class FinderService {

	// Managed repository
	@Autowired
	private FinderRepository			finderRepository;

	// Supporting services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private ParadeService				paradeService;


	// Simple CRUD methods
	public Finder create() {
		Finder result;

		result = new Finder();

		final Collection<Parade> parades = new HashSet<>();

		result.setParades(parades);

		return result;
	}

	public Collection<Finder> findAll() {
		Collection<Finder> result;

		result = this.finderRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Finder findOne(final int finderId) {
		Assert.isTrue(finderId != 0);

		Finder result;

		result = this.finderRepository.findOne(finderId);
		Assert.notNull(result);

		return result;
	}

	public Finder save(final Finder finder) {
		Assert.notNull(finder);

		Finder result;

		if (finder.getId() != 0) {
			final Date searchMoment = new Date(System.currentTimeMillis() - 1);
			finder.setSearchMoment(searchMoment);
		}

		result = this.finderRepository.save(finder);

		return result;
	}

	public void delete(final Finder finder) {
		Assert.notNull(finder);
		Assert.isTrue(finder.getId() != 0);
		Assert.isTrue(this.finderRepository.exists(finder.getId()));

		this.finderRepository.delete(finder);
	}

	// Other business methods
	public Finder findFinderMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Finder result;

		result = memberLogged.getFinder();

		return result;
	}

	// R22.1
	public Finder cleanFinder(final Finder finder) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Finder result;

		Collection<Parade> parades = this.paradeService.findParadesFinalModeAccepted();
		final Date searchMoment = new Date(System.currentTimeMillis() - 1);
		finder.setKeyWord("");
		finder.setMinDate(null);
		finder.setMaxDate(null);
		finder.setArea(null);

		final Integer maxResults = this.systemConfigurationService.getConfiguration().getMaxResultsFinder();

		// R24
		if (parades.size() > maxResults) {
			final List<Parade> resultList = new ArrayList<>(parades);
			parades = new HashSet<>(resultList.subList(0, maxResults));
		}

		finder.setParades(parades);
		finder.setSearchMoment(searchMoment);
		result = this.finderRepository.save(finder);

		return result;
	}

	// R23
	public boolean cleanCache(final Finder finder) {
		Assert.notNull(finder);

		Long searchMoment, nowMomment;
		Boolean result = false;
		final Date dateMoment = finder.getSearchMoment();
		if (dateMoment != null) {
			searchMoment = finder.getSearchMoment().getTime();
			nowMomment = new Date(System.currentTimeMillis()).getTime();
			Long passedTime = (nowMomment - searchMoment) / 1000; //De milisegundos a segundos
			passedTime = passedTime / 3600; //De segundos a horas
			final Integer periodFinder = this.systemConfigurationService.getConfiguration().getPeriodFinder();
			result = passedTime >= periodFinder;
		}

		return result;
	}

	// R21.2
	public void updateCriteria(String keyWord, Date minDate, Date maxDate, final Area area) {
		Finder result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member member = (Member) actorLogged;

		result = member.getFinder();

		final Calendar cal = Calendar.getInstance();

		if (keyWord.isEmpty() && minDate == null && maxDate == null && area == null)
			result = this.cleanFinder(result);
		else {
			keyWord = keyWord.toLowerCase();

			if (keyWord != "")
				result.setKeyWord(keyWord);

			if (minDate == null) {
				cal.set(1000, 0, 1);
				minDate = cal.getTime();
				result.setMinDate(minDate);
			}
			if (maxDate == null) {
				cal.set(3000, 0, 1);
				maxDate = cal.getTime();
				result.setMaxDate(maxDate);
			}

			int areaId = 0;
			if (area == null)
				result.setArea(null);
			else
				areaId = area.getId();

			Collection<Parade> paradesFinder = this.paradeService.findParadesFromFinder(keyWord, minDate, maxDate, areaId);

			final Integer maxResults = this.systemConfigurationService.getConfiguration().getMaxResultsFinder();

			// R24
			if (paradesFinder.size() > maxResults) {
				final List<Parade> resultList = new ArrayList<>(paradesFinder);
				paradesFinder = new HashSet<>(resultList.subList(0, maxResults));
			}

			result.setParades(paradesFinder);

			cal.setTime(result.getMinDate());
			if (cal.get(Calendar.YEAR) == 1000)
				result.setMinDate(null);
			cal.setTime(result.getMaxDate());
			if (cal.get(Calendar.YEAR) == 3000)
				result.setMaxDate(null);
		}

		final Date searchMoment = new Date(System.currentTimeMillis() - 1);
		result.setSearchMoment(searchMoment);

		result = this.finderRepository.save(result);
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Finder reconstruct(final Finder finder, final BindingResult binding) {
		Finder result;

		if (finder.getId() == 0)
			result = finder;
		else {
			result = this.finderRepository.findOne(finder.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setKeyWord(finder.getKeyWord());
			result.setMinDate(finder.getMinDate());
			result.setMaxDate(finder.getMaxDate());
			result.setArea(finder.getArea());
		}

		this.validator.validate(result, binding);

		return result;
	}

}
