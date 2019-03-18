
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
import domain.Chapter;
import domain.Finder;
import domain.Float;
import domain.Member;
import domain.Message;
import domain.Parade;
import domain.RequestMarch;
import domain.Segment;
import domain.Sponsorship;

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

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private SegmentService		segmentService;


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
		final Collection<Segment> segments = new HashSet<>();
		final Collection<Sponsorship> sponsorships = new HashSet<>();

		final DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		final Date date = new Date(System.currentTimeMillis() - 1);

		// R4
		final String ticker = dateFormat.format(date).toString() + "-" + RandomStringUtils.randomAlphabetic(5).toUpperCase();

		result.setRequestsMarch(requestsMarch);
		result.setFloats(floats);
		result.setTicker(ticker);
		result.setIsFinalMode(false);
		result.setSegments(segments);
		result.setSponsorships(sponsorships);

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

	// R10.2, R8.2 (Acme-Parade)
	public Parade save(final Parade parade) {
		Assert.notNull(parade);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		Parade result;

		if (actorLogged instanceof Brotherhood) {
			this.actorService.checkUserLoginBrotherhood(actorLogged);

			final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

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
		} else {
			this.actorService.checkUserLoginChapter(actorLogged);

			Assert.isTrue(parade.getIsFinalMode(), "You can only making decisions on parades that are in final mode");

			final Chapter chapterOwner = this.chapterService.findChapterByParadeId(parade.getId());
			Assert.isTrue(actorLogged.equals(chapterOwner), "The logged actor is not the owner of this entity");

			if (parade.getStatus().equals("REJECTED")) {
				// When a parade is rejected by a chapter, the chapter must jot down the reason why
				Assert.notNull(parade.getRejectReason(), "The chapter must provide an explanation about the parade rejected");
				Assert.isTrue(!parade.getRejectReason().isEmpty(), "The chapter must provide an explanation about the parade rejected");
			}

			result = this.paradeRepository.save(parade);
		}

		if (result.getStatus() != null)
			if (result.getStatus().equals("ACCEPTED")) {
				// R32
				final Collection<Member> members = this.memberService.findMembersByBrotherhoodLogged();
				if (!members.isEmpty()) {
					final Message message = this.messageService.create();

					final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByParadeId(result.getId());

					final Locale locale = LocaleContextHolder.getLocale();
					if (locale.getLanguage().equals("es")) {
						message.setSubject("Un nuevo desfile ha sido publicado");
						message.setBody("La hermandad " + brotherhoodOwner.getTitle() + " ha publicado el desfile " + result.getTitle());
					} else {
						message.setSubject("A new parade has been published");
						message.setBody("The brotherhood " + brotherhoodOwner.getTitle() + " has published the parade " + result.getTitle());
					}

					final Actor sender = this.actorService.getSystemActor();
					message.setPriority("HIGH");
					message.setSender(sender);

					final Collection<Actor> recipients = new HashSet<>();
					recipients.addAll(members);
					message.setRecipients(recipients);
					this.messageService.save(message, true);
				}
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
	// R6 (Acme-Parade)
	public Collection<Parade> findParadesAcceptedByBrotherhoodId(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		Collection<Parade> result;

		result = this.paradeRepository.findParadesAcceptedByBrotherhoodId(brotherhoodId);
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

	// R10.2, R9.2(Acme-Parade)
	public Collection<Parade> findParadesByBrotherhoodLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Collection<Parade> result;

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		result = this.paradeRepository.findParadesOrderByStatusByBrotherhoodId(brotherhoodLogged.getId());
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
		parade.setStatus("SUBMITTED");

		result = this.paradeRepository.save(parade);

		return result;
	}

	public Parade saveForRequestMarch(final Parade parade) {
		Assert.notNull(parade);

		Parade result;

		result = this.paradeRepository.save(parade);

		return result;
	}

	public Collection<Parade> findParadesFinalModeAccepted() {
		Collection<Parade> result;

		result = this.paradeRepository.findParadesFinalModeAccepted();
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

	// R8.2 (Acme-Parade)
	public Collection<Parade> findParadesFinalModeOrderByStatusByBrotherhoodId(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		Collection<Parade> result;

		result = this.paradeRepository.findParadesFinalModeOrderByStatusByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Parade findParadeSubmittedChapterLogged(final int paradeId) {
		Assert.isTrue(paradeId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginChapter(actorLogged);

		final Chapter chapterOwner = this.chapterService.findChapterByParadeId(paradeId);
		Assert.isTrue(chapterOwner.equals(actorLogged), "The logged actor is not the owner of this entity");

		Parade result;

		result = this.paradeRepository.findOne(paradeId);
		Assert.notNull(result);

		return result;
	}

	// R8.2 (Acme-Parade)
	public Parade acceptParade(final Parade parade) {
		Parade result;
		Assert.notNull(parade);
		Assert.isTrue(parade.getId() != 0);
		Assert.isTrue(this.paradeRepository.exists(parade.getId()));

		Assert.isTrue(!parade.getStatus().equals("ACCEPTED"), "This parade is already accepted");
		parade.setStatus("ACCEPTED");

		result = this.paradeRepository.save(parade);

		return result;
	}

	// R9.2 (Acme-Parade)
	public Parade copyParade(final Parade parade) {
		Parade result;
		Assert.notNull(parade);
		Assert.isTrue(parade.getId() != 0);
		Assert.isTrue(this.paradeRepository.exists(parade.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		result = this.create();
		result.setTitle(parade.getTitle());
		result.setDescription(parade.getDescription());
		result.setMomentOrganise(parade.getMomentOrganise());
		result.setMaxRows(parade.getMaxRows());
		result.setMaxColumns(parade.getMaxColumns());
		result.setFloats(parade.getFloats());
		final Collection<Segment> segments = new HashSet<>();
		for (final Segment s : parade.getSegments()) {
			Segment seg = this.segmentService.create();
			seg.setOrigin(s.getOrigin());
			seg.setDestination(s.getDestination());
			seg.setTimeReachOrigin(s.getTimeReachOrigin());
			seg.setTimeReachDestination(s.getTimeReachDestination());
			seg = this.segmentService.save(seg);
			segments.add(seg);
		}
		result.setSegments(segments);

		result = this.paradeRepository.save(result);

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		final Collection<Parade> paradesBrotherhoodLogged = brotherhoodLogged.getParades();
		paradesBrotherhoodLogged.add(result);
		brotherhoodLogged.setParades(paradesBrotherhoodLogged);
		this.brotherhoodService.save(brotherhoodLogged);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Parade reconstruct(final Parade parade, final BindingResult binding) {
		Parade result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

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
			if (actorLogged instanceof Chapter) {
				result.setStatus(parade.getStatus());
				result.setRejectReason(parade.getRejectReason());
			} else {
				result.setTitle(parade.getTitle());
				result.setDescription(parade.getDescription());
				result.setMomentOrganise(parade.getMomentOrganise());
				result.setMaxRows(parade.getMaxRows());
				result.setMaxColumns(parade.getMaxColumns());
				result.setFloats(parade.getFloats());
			}
		}

		this.validator.validate(result, binding);

		this.paradeRepository.flush();

		return result;
	}

}
