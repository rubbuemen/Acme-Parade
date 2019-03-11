
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

import repositories.ProcessionRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.Finder;
import domain.Float;
import domain.Member;
import domain.Message;
import domain.Procession;
import domain.RequestMarch;

@Service
@Transactional
public class ProcessionService {

	// Managed repository
	@Autowired
	private ProcessionRepository	processionRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private MemberService			memberService;


	// Simple CRUD methods
	// R10.2
	public Procession create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Procession result;

		result = new Procession();
		final Collection<RequestMarch> requestsMarch = new HashSet<>();
		final Collection<Float> floats = new HashSet<>();
		final Float procession = new Float(); // Ghost Float because it is mandatory to have at least one
		floats.add(procession);

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

	public Collection<Procession> findAll() {
		Collection<Procession> result;

		result = this.processionRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Procession findOne(final int processionId) {
		Assert.isTrue(processionId != 0);

		Procession result;

		result = this.processionRepository.findOne(processionId);
		Assert.notNull(result);

		return result;
	}

	// R10.2
	public Procession save(final Procession procession) {
		Assert.notNull(procession);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		Procession result;

		Assert.notNull(brotherhoodLogged.getArea(), "You can not organise any processions until you selected an area");
		Assert.isTrue(!procession.getIsFinalMode(), "You can only save processions that are not in final mode");

		final Collection<Float> floatsProcession = procession.getFloats();
		for (final Float f : floatsProcession) {
			final Brotherhood brotherhoodOwnerFloat = this.brotherhoodService.findBrotherhoodByFloatId(f.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwnerFloat), "The logged brotherhood is not the owner of this float");
		}

		if (procession.getId() == 0) {
			result = this.processionRepository.save(procession);
			final Collection<Procession> processionsBrotherhoodLogged = brotherhoodLogged.getProcessions();
			processionsBrotherhoodLogged.add(result);
			brotherhoodLogged.setProcessions(processionsBrotherhoodLogged);
			this.brotherhoodService.save(brotherhoodLogged);

		} else {
			final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByProcessionId(procession.getId());
			Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");
			result = this.processionRepository.save(procession);
		}

		return result;
	}

	// R10.2
	public void delete(final Procession procession) {
		Assert.notNull(procession);
		Assert.isTrue(procession.getId() != 0);
		Assert.isTrue(this.processionRepository.exists(procession.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByProcessionId(procession.getId());
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(!procession.getIsFinalMode(), "You can only delete processions that are not in final mode");

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		final Collection<Procession> processionsActorLogged = brotherhoodLogged.getProcessions();
		processionsActorLogged.remove(procession);
		brotherhoodLogged.setProcessions(processionsActorLogged);
		this.brotherhoodService.save(brotherhoodLogged);

		final Collection<Finder> finders = this.finderService.findAll();
		for (final Finder f : finders) {
			final Collection<Procession> processionsFinders = f.getProcessions();
			processionsFinders.remove(procession);
			f.setProcessions(processionsFinders);
			this.finderService.save(f);
		}

		this.processionRepository.delete(procession);
	}
	// Other business methods
	// R8.2
	public Collection<Procession> findProcessionsFinalModeByBrotherhoodId(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		Collection<Procession> result;

		result = this.processionRepository.findProcessionsFinalModeByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Procession> findProcessionsByFloatId(final int floatId) {
		Assert.isTrue(floatId != 0);

		Collection<Procession> result;

		result = this.processionRepository.findProcessionsByFloatId(floatId);
		Assert.notNull(result);

		return result;
	}

	// R10.2
	public Collection<Procession> findProcessionsByBrotherhoodLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Collection<Procession> result;

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		result = brotherhoodLogged.getProcessions();
		Assert.notNull(result);

		return result;
	}

	// R10.2
	public Procession findProcessionBrotherhoodLogged(final int processionId) {
		Assert.isTrue(processionId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByProcessionId(processionId);
		Assert.isTrue(actorLogged.equals(brotherhoodOwner), "The logged actor is not the owner of this entity");

		Procession result;

		result = this.processionRepository.findOne(processionId);
		Assert.notNull(result);

		return result;
	}

	public Procession findProcessionByRequestMarchId(final int requestMarchId) {
		Assert.isTrue(requestMarchId != 0);

		Procession result;

		result = this.processionRepository.findProcessionByRequestMarchId(requestMarchId);
		Assert.notNull(result);

		return result;
	}

	public Procession changeFinalMode(final Procession procession) {
		Procession result;
		Assert.notNull(procession);
		Assert.isTrue(procession.getId() != 0);
		Assert.isTrue(this.processionRepository.exists(procession.getId()));

		Assert.isTrue(!procession.getIsFinalMode(), "This procession is already in final mode");
		procession.setIsFinalMode(true);

		result = this.processionRepository.save(procession);

		// R32
		final Collection<Member> members = this.memberService.findMembersByBrotherhoodLogged();
		if (!members.isEmpty()) {
			final Message message = this.messageService.create();

			final Actor actorLogged = this.actorService.findActorLogged();
			final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

			final Locale locale = LocaleContextHolder.getLocale();
			if (locale.getLanguage().equals("es")) {
				message.setSubject("Una nueva procesión ha sido publicada");
				message.setBody("La hermandad " + brotherhoodLogged.getTitle() + " ha publicado la procesión " + result.getTitle());
			} else {
				message.setSubject("A new procession has been published");
				message.setBody("The brotherhood " + brotherhoodLogged.getTitle() + " has published the procession " + result.getTitle());
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

	public Procession saveForRequestMarch(final Procession procession) {
		Assert.notNull(procession);

		Procession result;

		result = this.processionRepository.save(procession);

		return result;
	}

	public Collection<Procession> findProcessionsFinalMode() {
		Collection<Procession> result;

		result = this.processionRepository.findProcessionsFinalMode();
		Assert.notNull(result);

		return result;
	}

	public Collection<Procession> findProcessionsFromFinder(final String keyWord, final Date minDate, final Date maxDate, final int areaId) {
		final Collection<Procession> result = new HashSet<>();
		Collection<Procession> findProcessionsFilterByKeyWord = new HashSet<>();
		Collection<Procession> findProcessionsFilterDate = new HashSet<>();
		Collection<Procession> findProcessionsFilterByAreaId = new HashSet<>();
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal1.setTime(minDate);
		cal2.setTime(maxDate);

		if (!keyWord.isEmpty())
			findProcessionsFilterByKeyWord = this.processionRepository.findProcessionsFilterByKeyWord(keyWord);
		if (cal1.get(Calendar.YEAR) != 1000 || cal2.get(Calendar.YEAR) != 3000)
			findProcessionsFilterDate = this.processionRepository.findProcessionsFilterDate(minDate, maxDate);
		if (areaId != 0)
			findProcessionsFilterByAreaId = this.processionRepository.findProcessionsFilterByAreaId(areaId);

		result.addAll(findProcessionsFilterByKeyWord);
		result.addAll(findProcessionsFilterDate);
		result.addAll(findProcessionsFilterByAreaId);

		if (!keyWord.isEmpty())
			result.retainAll(findProcessionsFilterByKeyWord);
		if (cal1.get(Calendar.YEAR) != 1000 || cal2.get(Calendar.YEAR) != 3000)
			result.retainAll(findProcessionsFilterDate);
		if (areaId != 0)
			result.retainAll(findProcessionsFilterByAreaId);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Procession reconstruct(final Procession procession, final BindingResult binding) {
		Procession result;

		if (procession.getFloats() == null || procession.getFloats().contains(null)) {
			final Collection<Float> floats = new HashSet<>();
			procession.setFloats(floats);
		}

		if (procession.getId() == 0) {
			final Collection<RequestMarch> requestsMarch = new HashSet<>();
			final DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
			final Date date = new Date(System.currentTimeMillis() - 1);
			final String ticker = dateFormat.format(date).toString() + "-" + RandomStringUtils.randomAlphabetic(5).toUpperCase(); // R4
			procession.setRequestsMarch(requestsMarch);
			procession.setIsFinalMode(false);
			procession.setTicker(ticker);
			result = procession;
		} else {
			result = this.processionRepository.findOne(procession.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(procession.getTitle());
			result.setDescription(procession.getDescription());
			result.setMomentOrganise(procession.getMomentOrganise());
			result.setMaxRows(procession.getMaxRows());
			result.setMaxColumns(procession.getMaxColumns());
			result.setFloats(procession.getFloats());
		}

		this.validator.validate(result, binding);

		this.processionRepository.flush();

		return result;
	}

}
