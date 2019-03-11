
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EnrolmentRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Message;
import domain.Parade;
import domain.PositionBrotherhood;
import domain.RequestMarch;

@Service
@Transactional
public class EnrolmentService {

	// Managed repository
	@Autowired
	private EnrolmentRepository			enrolmentRepository;

	// Supporting services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private PositionBrotherhoodService	positionBrotherhoodService;

	@Autowired
	private BrotherhoodService			brotherhoodService;

	@Autowired
	private RequestMarchService			requestMarchService;

	@Autowired
	private MemberService				memberService;

	@Autowired
	private ParadeService				paradeService;

	@Autowired
	private MessageService				messageService;


	// Simple CRUD methods
	public Enrolment create() {
		Enrolment result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		result = new Enrolment();

		result.setMember(memberLogged);

		return result;
	}

	public Collection<Enrolment> findAll() {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Enrolment findOne(final int enrolmentId) {
		Assert.isTrue(enrolmentId != 0);

		Enrolment result;

		result = this.enrolmentRepository.findOne(enrolmentId);
		Assert.notNull(result);

		return result;
	}

	// R10.3 (save for brotherhood)
	public Enrolment save(final Enrolment enrolment) {
		Assert.notNull(enrolment);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		Enrolment result;

		if (actorLogged instanceof Brotherhood) {
			this.actorService.checkUserLoginBrotherhood(actorLogged);

			final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

			Assert.notNull(brotherhoodLogged.getArea(), "You can not enroll members until you selected an area");

			final Date momentRegistered = new Date(System.currentTimeMillis() - 1);
			enrolment.setMomentRegistered(momentRegistered);

			// When a member is enrolled, a position must be selected
			Assert.notNull(enrolment.getPositionBrotherhood(), "You must select a position");
			final PositionBrotherhood positionBrotherhood = this.positionBrotherhoodService.save(enrolment.getPositionBrotherhood());
			enrolment.setPositionBrotherhood(positionBrotherhood);
		}

		result = this.enrolmentRepository.save(enrolment);

		// R32
		final Message message = this.messageService.create();
		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		final Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			message.setSubject("Su petición para inscribirse a la hermandad ha sido aceptada");
			message.setBody("La petición incribirse a " + brotherhoodLogged.getTitle() + " ha sido aceptada.");
		} else {
			message.setSubject("Your request to join the brotherhood has been accepted");
			message.setBody("The request to enroll " + brotherhoodLogged.getTitle() + " has been accepted.");
		}

		final Actor sender = this.actorService.getSystemActor();
		message.setPriority("HIGH");
		message.setSender(sender);

		final Member memberOwner = result.getMember();
		final Collection<Actor> recipients = new HashSet<>();
		recipients.add(memberOwner);
		message.setRecipients(recipients);
		this.messageService.save(message, true);

		return result;
	}

	public Enrolment save(final Enrolment enrolment, final Brotherhood brotherhood) {
		Assert.notNull(enrolment);
		Assert.notNull(brotherhood);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Enrolment result;

		enrolment.setBrotherhood(brotherhood);

		result = this.enrolmentRepository.save(enrolment);

		return result;
	}

	public void delete(final Enrolment enrolment) {
		// En principio nunca se borrará un enrolment
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);
		Assert.isTrue(this.enrolmentRepository.exists(enrolment.getId()));

		this.enrolmentRepository.delete(enrolment);
	}

	// Other business methods
	// R10.3
	public Collection<Enrolment> findEnrolmentsPendingByBrotherhoodLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Collection<Enrolment> result;

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		result = this.enrolmentRepository.findEnrolmentsPendingByBrotherhoodId(brotherhoodLogged.getId());
		Assert.notNull(result);

		return result;
	}

	// R10.3
	public void removeMemberOfBrotherhood(final int memberId) {
		Enrolment result;

		final Actor actorLogged = this.actorService.findActorLogged();

		result = this.findEnrolmentMemberBrotherhoodLogged(memberId);

		final Collection<RequestMarch> requestsMarchMemberBrotherhoodLogged = this.requestMarchService.findRequestsMarchMemberId(memberId, actorLogged.getId());

		final Member member = (Member) this.actorService.findOne(memberId);
		final Collection<RequestMarch> requestsMember = member.getRequestsMarch();

		requestsMember.removeAll(requestsMarchMemberBrotherhoodLogged);
		member.setRequestsMarch(requestsMarchMemberBrotherhoodLogged);
		this.memberService.saveAuxiliar(member);
		for (final RequestMarch rm : requestsMarchMemberBrotherhoodLogged) {
			final Parade pro = this.paradeService.findParadeByRequestMarchId(rm.getId());
			final Collection<RequestMarch> requests = pro.getRequestsMarch();
			requests.remove(rm);
			pro.setRequestsMarch(requests);
			this.paradeService.saveForRequestMarch(pro);
			this.requestMarchService.deleteAuxiliar(rm);
		}

		final Date momentDropOut = new Date(System.currentTimeMillis() - 1);
		result.setMomentDropOut(momentDropOut);

		result = this.enrolmentRepository.save(result);

		// R32
		final Message message = this.messageService.create();
		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		final Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			message.setSubject("Usted ha sido expulsado de la hermandad");
			message.setBody("Ha sido expulsado de la hermandad " + brotherhoodLogged.getTitle() + ".");
		} else {
			message.setSubject("You have been dropped out from the brotherhood");
			message.setBody("You have been dropped out from the brotherhood " + brotherhoodLogged.getTitle() + ".");
		}

		final Actor sender = this.actorService.getSystemActor();
		message.setPriority("HIGH");
		message.setSender(sender);

		final Member memberOwner = result.getMember();
		final Collection<Actor> recipients = new HashSet<>();
		recipients.add(memberOwner);
		message.setRecipients(recipients);
		this.messageService.save(message, true);

	}

	// R11.2
	public void dropOutOfBrotherhood(final int brotherhoodId) {
		Enrolment result;

		final Member memberLogged = (Member) this.actorService.findActorLogged();

		result = this.findEnrolmentBrotherhoodMemberLogged(brotherhoodId);

		final Collection<RequestMarch> requestsMarchMemberBrotherhoodLogged = this.requestMarchService.findRequestsMarchMemberId(memberLogged.getId(), brotherhoodId);

		final Collection<RequestMarch> requestsMember = memberLogged.getRequestsMarch();

		requestsMember.removeAll(requestsMarchMemberBrotherhoodLogged);
		memberLogged.setRequestsMarch(requestsMarchMemberBrotherhoodLogged);
		this.memberService.saveAuxiliar(memberLogged);
		for (final RequestMarch rm : requestsMarchMemberBrotherhoodLogged) {
			final Parade pro = this.paradeService.findParadeByRequestMarchId(rm.getId());
			final Collection<RequestMarch> requests = pro.getRequestsMarch();
			requests.remove(rm);
			pro.setRequestsMarch(requests);
			this.paradeService.saveForRequestMarch(pro);
			this.requestMarchService.deleteAuxiliar(rm);
		}

		final Date momentDropOut = new Date(System.currentTimeMillis() - 1);
		result.setMomentDropOut(momentDropOut);

		result = this.enrolmentRepository.save(result);

		// R32
		final Message message = this.messageService.create();

		final Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			message.setSubject("Un miembro dejó su hermandad");
			message.setBody("El miembro " + memberLogged.getName() + " " + memberLogged.getSurname() + " ha dejado la hermandad.");
		} else {
			message.setSubject("A member dropped out its brotherhood");
			message.setBody("The member " + memberLogged.getName() + " " + memberLogged.getSurname() + " has dropped out the brotherhood.");
		}

		final Actor sender = this.actorService.getSystemActor();
		message.setPriority("HIGH");
		message.setSender(sender);

		final Brotherhood brotherhoodOwner = result.getBrotherhood();
		final Collection<Actor> recipients = new HashSet<>();
		recipients.add(brotherhoodOwner);
		message.setRecipients(recipients);
		this.messageService.save(message, true);
	}

	public Enrolment findEnrolmentMemberBrotherhoodLogged(final int memberId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Enrolment result;

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		result = this.enrolmentRepository.findEnrolmentPendingOfBrotherhoodByMemberId(brotherhoodLogged.getId(), memberId);
		if (result == null)
			result = this.enrolmentRepository.findEnrolmentActualOfBrotherhoodByMemberId(brotherhoodLogged.getId(), memberId);
		Assert.notNull(result);

		return result;
	}

	public Enrolment findEnrolmentBrotherhoodMemberLogged(final int brotherhoodId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Enrolment result;

		final Member memberLogged = (Member) actorLogged;

		result = this.enrolmentRepository.findEnrolmentPendingOfBrotherhoodByMemberId(brotherhoodId, memberLogged.getId());
		if (result == null)
			result = this.enrolmentRepository.findEnrolmentActualOfBrotherhoodByMemberId(brotherhoodId, memberLogged.getId());
		Assert.notNull(result);

		return result;
	}

	public Enrolment findEnrolmentPenddingBrotherhoodLogged(final int enrolmentId) {
		Assert.isTrue(enrolmentId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByEnrolmentId(enrolmentId);
		Assert.isTrue(brotherhoodOwner.equals(actorLogged), "The logged actor is not the owner of this entity");

		Enrolment result;

		result = this.enrolmentRepository.findOne(enrolmentId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Enrolment> findEnrolmentsByBrotherhoodId(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Enrolment> result;

		final Collection<Member> memberOwner = this.memberService.findMembersByBrotherhoodIdAll(brotherhoodId);
		Assert.isTrue(memberOwner.contains(actorLogged), "The logged actor is not the owner of this entity");

		result = this.enrolmentRepository.findEnrolmentsOfBrotherhoodByMemberId(brotherhoodId, actorLogged.getId());

		return result;
	}


	// Reconstruct methods
	@Autowired(required = false)
	private Validator	validator;


	public Enrolment reconstruct(final Enrolment enrolment, final BindingResult binding) {
		Enrolment result;

		if (enrolment.getId() == 0)
			result = enrolment;
		else {
			result = this.enrolmentRepository.findOne(enrolment.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setPositionBrotherhood(enrolment.getPositionBrotherhood());
		}

		this.validator.validate(result, binding);

		return result;
	}

}
