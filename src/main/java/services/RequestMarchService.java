
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
import domain.Parade;
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
	private ParadeService			paradeService;

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
			final Parade parade = this.paradeService.findParadeByRequestMarchId(requestMarch.getId());
			final Collection<RequestMarch> requestsMarchParade = parade.getRequestsMarch();
			final Collection<RequestMarch> requestsMarchParadeCopy = new HashSet<>(requestsMarchParade);
			requestsMarchParadeCopy.remove(requestMarch);

			if (requestMarch.getStatus().equals("APPROVED")) {
				// When the decision on a pending request is to accept it, the brotherhood must provide a position in the parade
				final Integer rowNew = requestMarch.getPositionRow();
				final Integer columnNew = requestMarch.getPositionColumn();
				Assert.notNull(rowNew, "You must select a row position");
				Assert.notNull(columnNew, "You must select a column position");
				Assert.isTrue(rowNew > 0, "Row position must be greater than 0");
				Assert.isTrue(columnNew > 0, "Column position must be greater than 0");
				Assert.isTrue(columnNew <= parade.getMaxColumns(), "You have exceeded the maximum number of columns established");
				Assert.isTrue(rowNew <= parade.getMaxRows(), "You have exceeded the maximum number of rows established");
				Assert.isTrue(columnNew <= parade.getMaxColumns(), "You have exceeded the maximum number of columns established");
				for (final RequestMarch rm : requestsMarchParadeCopy)
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
		final Parade parade = this.paradeService.findParadeByRequestMarchId(result.getId());

		final Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			message.setSubject("Su petici�n para marchar ha cambiado de estado");
			message.setBody("La petici�n para marchar en " + parade.getTitle() + " ha cambiado de estado.");
		} else {
			message.setSubject("Your request to march has changed state");
			message.setBody("The request to march to " + parade.getTitle() + " has changed its status.");
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
	public RequestMarch save(final RequestMarch requestMarch, final Parade parade) {
		Assert.notNull(requestMarch);
		Assert.notNull(parade);
		Assert.isTrue(requestMarch.getId() == 0); //Never will be edited

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final RequestMarch result;

		final Member memberLogged = (Member) actorLogged;

		final Collection<Brotherhood> brotherhoodsMemberLogged = this.brotherhoodService.findBrotherhoodsByMemberId(memberLogged.getId());
		boolean brotherhoodContainParade = false;
		for (final Brotherhood b : brotherhoodsMemberLogged)
			if (b.getParades().contains(parade)) {
				brotherhoodContainParade = true;
				break;
			}
		Assert.isTrue(brotherhoodContainParade, "The parade does not belong to a brotherhood to which the member belongs");

		result = this.requestMarchRepository.save(requestMarch);

		final Collection<RequestMarch> requestsMarchParade = parade.getRequestsMarch();
		requestsMarchParade.add(result);
		parade.setRequestsMarch(requestsMarchParade);
		this.paradeService.saveAuxiliar(parade);

		final Collection<RequestMarch> requestsMarchMember = result.getMember().getRequestsMarch();
		requestsMarchMember.add(result);
		result.getMember().setRequestsMarch(requestsMarchMember);
		this.memberService.save(result.getMember());

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

		final Member memberOwner = this.memberService.findMemberByRequestMarchId(requestMarch.getId());
		Assert.isTrue(memberOwner.equals(actorLogged), "The logged actor is not the owner of this entity");

		Assert.isTrue(requestMarch.getStatus().equals("PENDING"), "You can only delete a request to march if your status is pending");

		final Parade parade = this.paradeService.findParadeByRequestMarchId(requestMarch.getId());
		final Collection<RequestMarch> requestsMarchParade = parade.getRequestsMarch();
		requestsMarchParade.remove(requestMarch);
		parade.setRequestsMarch(requestsMarchParade);
		this.paradeService.saveAuxiliar(parade);

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
	public Collection<RequestMarch> findRequestsMarchByParade(final int paradeId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Collection<RequestMarch> result;

		final Parade parade = this.paradeService.findParadeBrotherhoodLogged(paradeId);

		result = this.requestMarchRepository.findRequestsMarchByParadeOrderByStatus(parade.getId());

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
	public Collection<RequestMarch> findRequestsMarchByParadeMember(final int paradeId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Collection<RequestMarch> result;

		result = this.requestMarchRepository.findRequestsMarchByParadeMemberOrderByStatus(paradeId, actorLogged.getId());

		Assert.notNull(result);

		return result;
	}

	public Collection<RequestMarch> findRequestsMarchMemberId(final int memberId, final int brotherhoodId) {
		Assert.isTrue(memberId != 0);

		final Collection<RequestMarch> result;

		result = this.requestMarchRepository.findRequestsMarchMemberId(memberId, brotherhoodId);

		return result;
	}

	public boolean memberHasPendingOrApprovedRequestToParade(final int paradeId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		final Collection<RequestMarch> requestsMarchPendingOrApproved = this.requestMarchRepository.findRequestMarchPendingOrApprovedByParadeMember(paradeId, memberLogged.getId());

		boolean result = false;
		if (requestsMarchPendingOrApproved.size() >= 1)
			result = true;

		return result;
	}

	public Collection<RequestMarch> findRequestsMarchByParadeMember(final int paradeId, final int memberId) {
		final Collection<RequestMarch> result;

		result = this.requestMarchRepository.findRequestsMarchByParadeMemberOrderByStatus(paradeId, memberId);

		Assert.notNull(result);

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

	public void flush() {
		this.requestMarchRepository.flush();
	}

}
