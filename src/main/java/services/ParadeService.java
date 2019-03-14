
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ParadeRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.Finder;
import domain.Float;
import domain.Member;
import domain.Message;
import domain.Parade;
import domain.RequestMarch;

@Service
@Transactional
public class ParadeService {

	// Managed repository
	@Autowired
	private ParadeRepository	paradeRepository;

	// Supporting services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private MessageService		messageService;

	@Autowired
	private MemberService		memberService;


	// Simple CRUD methods
	// R10.2
	public Parade create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Parade result;

		result = new Parade();
		final Collection<RequestMarch> requestsMarch = new HashSet<>();
		final Collection<Float> floats = new HashSet<>();
		final Float parade = new Float(); // Ghost Float because it is mandatory to have at least one
		floats.add(parade);

		final DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		final Date date = new Date(System.currentTimeMillis() - 1);

		// R4
		final String ticker = dateFormat.format(date).toString() + "-" + RandomStringUtils.randomAlphabetic(5).toUpperCase();

		result.setRequestsMarch(requestsMarch);
		result.setFloats(floats);
		result.setTicker(ticker);
		result.setIsFinalMode(false);

		return result;
	}

	public Collection<Parade> findAll() {
		Collection<Parade> result;

		result = this.paradeRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Parade findOne(final int paradeId) {
		Assert.isTrue(paradeId != 0);

		Parade result;

		result = this.paradeRepository.findOne(paradeId);
		Assert.notNull(result);

		return result;
	}

	// R10.2
	public Parade save(final Parade parade) {
		Assert.notNull(parade);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		Parade result;

		Assert.notNull(brotherhoodLogged.getArea(), "You can not organise any parades until you selected an area");
		Assert.isTrue(!parade.getIsFinalMode(), "You can only save parades that are not in final mode");

		final Collection<Float> floatsParade = parade.getFloats();
		for (final Float f : floatsParade) {
			final Brotherhood brotherhoodOwnerFloat = this.brotherhoodService.findBrotherhoodByFloatId(f.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwnerFloat), "The logged brotherhood is not the owner of this float");
		}

		if (parade.getId() == 0) {
			result = this.paradeRepository.save(parade);
			final Collection<Parade> paradesBrotherhoodLogged = brotherhoodLogged.getParades();
			paradesBrotherhoodLogged.add(result);
			brotherhoodLogged.setParades(paradesBrotherhoodLogged);
			this.brotherhoodService.save(brotherhoodLogged);

		} else {
			final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByParadeId(parade.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");
			result = this.paradeRepository.save(parade);
		}

		return result;
	}

	// R10.2
	public void delete(final Parade parade) {
		Assert.notNull(parade);
		Assert.isTrue(parade.getId() != 0);
		Assert.isTrue(this.paradeRepository.exists(parade.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByParadeId(parade.getId());
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(!parade.getIsFinalMode(), "You can only delete parades that are not in final mode");

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		final Collection<Parade> paradesActorLogged = brotherhoodLogged.getParades();
		paradesActorLogged.remove(parade);
		brotherhoodLogged.setParades(paradesActorLogged);
		this.brotherhoodService.save(brotherhoodLogged);

		final Collection<Finder> finders = this.finderService.findAll();
		for (final Finder f : finders) {
			final Collection<Parade> paradesFinders = f.getParades();
			paradesFinders.remove(parade);
			f.setParades(paradesFinders);
			this.finderService.save(f);
		}

		this.paradeRepository.delete(parade);
	}
	// Other business methods
	// R8.2
	public Collection<Parade> findParadesFinalModeByBrotherhoodId(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		Collection<Parade> result;

		result = this.paradeRepository.findParadesFinalModeByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Parade> findParadesByFloatId(final int floatId) {
		Assert.isTrue(floatId != 0);

		Collection<Parade> result;

		result = this.paradeRepository.findParadesByFloatId(floatId);
		Assert.notNull(result);

		return result;
	}

	// R10.2
	public Collection<Parade> findParadesByBrotherhoodLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Collection<Parade> result;

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		result = brotherhoodLogged.getParades();
		Assert.notNull(result);

		return result;
	}

	// R10.2
	public Parade findParadeBrotherhoodLogged(final int paradeId) {
		Assert.isTrue(paradeId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByParadeId(paradeId);
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		Parade result;

		result = this.paradeRepository.findOne(paradeId);
		Assert.notNull(result);

		return result;
	}

	public Parade findParadeByRequestMarchId(final int requestMarchId) {
		Assert.isTrue(requestMarchId != 0);

		Parade result;

		result = this.paradeRepository.findParadeByRequestMarchId(requestMarchId);
		Assert.notNull(result);

		return result;
	}

	public Parade changeFinalMode(final Parade parade) {
		Parade result;
		Assert.notNull(parade);
		Assert.isTrue(parade.getId() != 0);
		Assert.isTrue(this.paradeRepository.exists(parade.getId()));

		Assert.isTrue(!parade.getIsFinalMode(), "This parade is already in final mode");
		parade.setIsFinalMode(true);

		result = this.paradeRepository.save(parade);

		// R32
		final Collection<Member> members = this.memberService.findMembersByBrotherhoodLogged();
		if (!members.isEmpty()) {
			final Message message = this.messageService.create();

			final Actor actorLogged = this.actorService.findActorLogged();
			final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

			final Locale locale = LocaleContextHolder.getLocale();
			if (locale.getLanguage().equals("es")) {
				message.setSubject("Una nueva desfile ha sido publicada");
				message.setBody("La hermandad " + brotherhoodLogged.getTitle() + " ha publicado la desfile " + result.getTitle());
			} else {
				message.setSubject("A new parade has been published");
				message.setBody("The brotherhood " + brotherhoodLogged.getTitle() + " has published the parade " + result.getTitle());
			}

			final Actor sender = this.actorService.getSystemActor();
			message.setPriority("HIGH");
			message.setSender(sender);

			final Collection<Actor> recipients = new HashSet<>();
			recipients.addAll(members);
			message.setRecipients(recipients);
			this.messageService.save(message, true);
		}

		return result;
	}

	public Parade saveForRequestMarch(final Parade parade) {
		Assert.notNull(parade);

		Parade result;

		result = this.paradeRepository.save(parade);

		return result;
	}

	public Collection<Parade> findParadesFinalMode() {
		Collection<Parade> result;

		result = this.paradeRepository.findParadesFinalMode();
		Assert.notNull(result);

		return result;
	}

	public Collection<Parade> findParadesFromFinder(final String keyWord, final Date minDate, final Date maxDate, final int areaId) {
		final Collection<Parade> result = new HashSet<>();
		Collection<Parade> findParadesFilterByKeyWord = new HashSet<>();
		Collection<Parade> findParadesFilterDate = new HashSet<>();
		Collection<Parade> findParadesFilterByAreaId = new HashSet<>();
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal1.setTime(minDate);
		cal2.setTime(maxDate);

		if (!keyWord.isEmpty())
			findParadesFilterByKeyWord = this.paradeRepository.findParadesFilterByKeyWord(keyWord);
		if (cal1.get(Calendar.YEAR) != 1000 || cal2.get(Calendar.YEAR) != 3000)
			findParadesFilterDate = this.paradeRepository.findParadesFilterDate(minDate, maxDate);
		if (areaId != 0)
			findParadesFilterByAreaId = this.paradeRepository.findParadesFilterByAreaId(areaId);

		result.addAll(findParadesFilterByKeyWord);
		result.addAll(findParadesFilterDate);
		result.addAll(findParadesFilterByAreaId);

		if (!keyWord.isEmpty())
			result.retainAll(findParadesFilterByKeyWord);
		if (cal1.get(Calendar.YEAR) != 1000 || cal2.get(Calendar.YEAR) != 3000)
			result.retainAll(findParadesFilterDate);
		if (areaId != 0)
			result.retainAll(findParadesFilterByAreaId);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Parade reconstruct(final Parade parade, final BindingResult binding) {
		Parade result;

		if (parade.getFloats() == null || parade.getFloats().contains(null)) {
			final Collection<Float> floats = new HashSet<>();
			parade.setFloats(floats);
		}

		if (parade.getId() == 0) {
			final Collection<RequestMarch> requestsMarch = new HashSet<>();
			final DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
			final Date date = new Date(System.currentTimeMillis() - 1);
			final String ticker = dateFormat.format(date).toString() + "-" + RandomStringUtils.randomAlphabetic(5).toUpperCase(); // R4
			parade.setRequestsMarch(requestsMarch);
			parade.setIsFinalMode(false);
			parade.setTicker(ticker);
			result = parade;
		} else {
			result = this.paradeRepository.findOne(parade.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(parade.getTitle());
			result.setDescription(parade.getDescription());
			result.setMomentOrganise(parade.getMomentOrganise());
			result.setMaxRows(parade.getMaxRows());
			result.setMaxColumns(parade.getMaxColumns());
			result.setFloats(parade.getFloats());
		}

		this.validator.validate(result, binding);

		this.paradeRepository.flush();

		return result;
	}

}
