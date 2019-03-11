
package services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RequestMarchRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.Member;
import domain.Message;
import domain.Procession;
import domain.RequestMarch;

@Service
@Transactional
public class RequestMarchService {

	// Managed repository
	@Autowired
	private RequestMarchRepository	requestMarchRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private ProcessionService		processionService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private MessageService			messageService;


	// Simple CRUD methods
	// R11.1
	public RequestMarch create() {
		RequestMarch result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		result = new RequestMarch();
		result.setMember((Member) actorLogged);
		result.setStatus("PENDING");

		return result;
	}

	public Collection<RequestMarch> findAll() {
		Collection<RequestMarch> result;

		result = this.requestMarchRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public RequestMarch findOne(final int requestMarchId) {
		Assert.isTrue(requestMarchId != 0);

		RequestMarch result;

		result = this.requestMarchRepository.findOne(requestMarchId);
		Assert.notNull(result);

		return result;
	}

	// R10.6 (brotherhood deciding on them)
	public RequestMarch save(final RequestMarch requestMarch) {
		Assert.notNull(requestMarch);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		RequestMarch result;

		if (actorLogged instanceof Brotherhood) {
			this.actorService.checkUserLoginBrotherhood(actorLogged);
			final Procession procession = this.processionService.findProcessionByRequestMarchId(requestMarch.getId());
			final Collection<RequestMarch> requestsMarchProcession = procession.getRequestsMarch();

			if (requestMarch.getStatus().equals("APPROVED")) {
				// When the decision on a pending request is to accept it, the brotherhood must provide a position in the procession
				final Integer rowNew = requestMarch.getPositionRow();
				final Integer columnNew = requestMarch.getPositionColumn();
				Assert.notNull(rowNew, "You must select a row position");
				Assert.notNull(columnNew, "You must select a column position");
				Assert.isTrue(rowNew <= procession.getMaxRows(), "You have exceeded the maximum number of rows established");
				Assert.isTrue(columnNew <= procession.getMaxColumns(), "You have exceeded the maximum number of columns established");
				for (final RequestMarch rm : requestsMarchProcession)
					if (rm.getStatus().equals("APPROVED")) {
						final Integer rowCheck = rm.getPositionRow();
						final Integer columnCheck = rm.getPositionColumn();
						Assert.isTrue(rowNew != rowCheck || columnNew != columnCheck, "Two members can not march at the same row/column");
					}
			}
			if (requestMarch.getStatus().equals("REJECTED")) {
				// When the decision is to reject it, the brotherhood must provide an explanation
				Assert.notNull(requestMarch.getRejectReason(), "The brotherhood must provide an explanation about the request march rejected");
				Assert.isTrue(!requestMarch.getRejectReason().isEmpty(), "The brotherhood must provide an explanation about the request march rejected");
			}

		}

		result = this.requestMarchRepository.save(requestMarch);

		// R32
		final Message message = this.messageService.create();
		final Procession procession = this.processionService.findProcessionByRequestMarchId(result.getId());

		final Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			message.setSubject("Su petición para marchar ha cambiado de estado");
			message.setBody("La petición para marchar en " + procession.getTitle() + " ha cambiado de estado.");
		} else {
			message.setSubject("Your request to march has changed state");
			message.setBody("The request to march to " + procession.getTitle() + " has changed its status.");
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

	// R11.1
	public RequestMarch save(final RequestMarch requestMarch, final Procession procession) {
		Assert.notNull(requestMarch);
		Assert.notNull(procession);
		Assert.isTrue(requestMarch.getId() == 0); //Never will be edited

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final RequestMarch result;

		final Member memberLogged = (Member) actorLogged;

		final Collection<Brotherhood> brotherhoodsMemberLogged = this.brotherhoodService.findBrotherhoodsByMemberId(memberLogged.getId());
		boolean brotherhoodContainProcession = false;
		for (final Brotherhood b : brotherhoodsMemberLogged)
			if (b.getProcessions().contains(procession)) {
				brotherhoodContainProcession = true;
				break;
			}
		Assert.isTrue(brotherhoodContainProcession, "The procession does not belong to a brotherhood to which the member belongs");

		result = this.requestMarchRepository.save(requestMarch);

		return result;
	}

	// R11.1
	public void delete(final RequestMarch requestMarch) {
		Assert.notNull(requestMarch);
		Assert.isTrue(requestMarch.getId() != 0);
		Assert.isTrue(this.requestMarchRepository.exists(requestMarch.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Assert.isTrue(requestMarch.getStatus().equals("PENDING"), "You can only delete a request to march if your status is pending");

		final Procession procession = this.processionService.findProcessionByRequestMarchId(requestMarch.getId());
		final Collection<RequestMarch> requestsMarchProcession = procession.getRequestsMarch();
		requestsMarchProcession.remove(requestMarch);
		procession.setRequestsMarch(requestsMarchProcession);
		this.processionService.saveForRequestMarch(procession);

		final Member memberLogged = (Member) actorLogged;
		final Collection<RequestMarch> requestsMarchMember = memberLogged.getRequestsMarch();
		requestsMarchMember.remove(requestMarch);
		memberLogged.setRequestsMarch(requestsMarchMember);
		this.memberService.save(memberLogged);

		this.requestMarchRepository.delete(requestMarch);
	}

	public void deleteAuxiliar(final RequestMarch requestMarch) {
		Assert.notNull(requestMarch);
		Assert.isTrue(requestMarch.getId() != 0);
		Assert.isTrue(this.requestMarchRepository.exists(requestMarch.getId()));

		this.requestMarchRepository.delete(requestMarch);
	}

	// Other business methods
	// R10.6
	public Collection<RequestMarch> findRequestsMarchByProcession(final int processionId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Collection<RequestMarch> result;

		final Procession procession = this.processionService.findProcessionBrotherhoodLogged(processionId);

		result = this.requestMarchRepository.findRequestsMarchByProcessionOrderByStatus(procession.getId());

		Assert.notNull(result);

		return result;
	}

	public RequestMarch findRequestMarchPenddingBrotherhoodLogged(final int requestMarchId) {
		Assert.isTrue(requestMarchId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodOwner = this.brotherhoodService.findBrotherhoodByRequestMarchId(requestMarchId);
		Assert.isTrue(brotherhoodOwner.equals(actorLogged), "The logged actor is not the owner of this entity");

		RequestMarch result;

		result = this.requestMarchRepository.findOne(requestMarchId);
		Assert.notNull(result);

		return result;
	}

	// R11.1 (para el caso de querer mostrar todas en una sola vista)
	//	public Collection<RequestMarch> findRequestsMarchByMemberLogged() {
	//		final Actor actorLogged = this.actorService.findActorLogged();
	//		Assert.notNull(actorLogged);
	//		this.actorService.checkUserLoginMember(actorLogged);
	//
	//		final Collection<RequestMarch> result;
	//
	//		result = this.requestMarchRepository.findRequestsMarchByMemberOrderByStatus(actorLogged.getId());
	//		Assert.notNull(result);
	//
	//		return result;
	//	}

	// R11.1
	public Collection<RequestMarch> findRequestsMarchByProcessionMember(final int processionId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Collection<RequestMarch> result;

		result = this.requestMarchRepository.findRequestsMarchByProcessionMemberOrderByStatus(processionId, actorLogged.getId());

		Assert.notNull(result);

		return result;
	}

	public Collection<RequestMarch> findRequestsMarchMemberId(final int memberId, final int brotherhoodId) {
		Assert.isTrue(memberId != 0);

		final Collection<RequestMarch> result;

		result = this.requestMarchRepository.findRequestsMarchMemberId(memberId, brotherhoodId);

		return result;
	}

	public boolean memberHasPendingOrApprovedRequestToProcession(final int processionId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		final Collection<RequestMarch> requestsMarchPendingOrApproved = this.requestMarchRepository.findRequestMarchPendingOrApprovedByProcessionMember(processionId, memberLogged.getId());

		boolean result = false;
		if (requestsMarchPendingOrApproved.size() >= 1)
			result = true;

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public RequestMarch reconstruct(final RequestMarch requestMarch, final BindingResult binding) {
		RequestMarch result;

		if (requestMarch.getId() == 0)
			result = requestMarch;
		else {
			final RequestMarch originalRequestMarch = this.requestMarchRepository.findOne(requestMarch.getId());
			Assert.notNull(originalRequestMarch, "This entity does not exist");
			result = requestMarch;
			result.setMember(originalRequestMarch.getMember());
		}

		this.validator.validate(result, binding);

		return result;
	}

}
