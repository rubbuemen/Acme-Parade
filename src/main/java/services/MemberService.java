
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MemberRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Box;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Finder;
import domain.Member;
import domain.RequestMarch;
import forms.MemberForm;

@Service
@Transactional
public class MemberService {

	// Managed repository
	@Autowired
	private MemberRepository	memberRepository;

	// Supporting services
	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Simple CRUD methods
	// R8.1, R9.1
	public Member create() {
		Member result;

		result = new Member();
		final Collection<RequestMarch> requestsMarch = new HashSet<>();
		final Collection<Enrolment> enrolments = new HashSet<>();
		final Collection<Box> boxes = new HashSet<>();
		final Finder finder = this.finderService.create();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.MEMBER);
		userAccount.addAuthority(auth);
		result.setRequestsMarch(requestsMarch);
		result.setEnrolments(enrolments);
		result.setBoxes(boxes);
		result.setFinder(finder);
		result.setUserAccount(userAccount);

		return result;
	}

	public Collection<Member> findAll() {
		Collection<Member> result;

		result = this.memberRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Member findOne(final int memberId) {
		Assert.isTrue(memberId != 0);

		Member result;

		result = this.memberRepository.findOne(memberId);
		Assert.notNull(result);

		return result;
	}

	// R8.1, R9.1
	public Member save(final Member member) {
		Assert.notNull(member);

		Member result;

		if (member.getId() == 0) {
			final Finder finder = this.finderService.save(member.getFinder());
			member.setFinder(finder);
		}

		result = (Member) this.actorService.save(member);
		result = this.memberRepository.save(result);

		return result;
	}

	public void delete(final Member member) {
		Assert.notNull(member);
		Assert.isTrue(member.getId() != 0);
		Assert.isTrue(this.memberRepository.exists(member.getId()));

		this.memberRepository.delete(member);
	}

	// Other business methods
	// R8.2
	public Collection<Member> findMembersByBrotherhoodId(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		Collection<Member> result;

		result = this.memberRepository.findMembersByBrotherhoodId(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	// R10.3
	public Collection<Member> findMembersByBrotherhoodLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		Collection<Member> result;

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		result = this.memberRepository.findMembersByBrotherhoodId(brotherhoodLogged.getId());
		Assert.notNull(result);

		return result;
	}

	// R10.3
	public Member findMemberBrotherhoodLogged(final int memberId) {
		Assert.isTrue(memberId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Collection<Brotherhood> brotherhoodsOwner = this.brotherhoodService.findBrotherhoodsByMemberId(memberId);
		Assert.isTrue(brotherhoodsOwner.contains(actorLogged), "The logged actor is not the owner of this entity");

		Member result;

		result = this.memberRepository.findOne(memberId);
		Assert.notNull(result);

		return result;
	}

	public Member saveAuxiliar(final Member member) {
		Assert.notNull(member);

		Member result;

		result = this.memberRepository.save(member);

		return result;
	}

	public Collection<Member> findMembersByBrotherhoodIdAll(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		Collection<Member> result;

		result = this.memberRepository.findMembersByBrotherhoodIdAll(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Member findMemberByRequestMarchId(final int requestMarchId) {
		Assert.isTrue(requestMarchId != 0);

		Member result;

		result = this.memberRepository.findMemberByRequestMarchId(requestMarchId);
		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public MemberForm reconstruct(final MemberForm memberForm, final BindingResult binding) {
		MemberForm result;
		final Member member = memberForm.getActor();

		if (member.getId() == 0) {
			final Collection<RequestMarch> requestsMarch = new HashSet<>();
			final Collection<Enrolment> enrolments = new HashSet<>();
			final Collection<Box> boxes = new HashSet<>();
			final Finder finder = this.finderService.create();
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.MEMBER);
			userAccount.addAuthority(auth);
			userAccount.setUsername(memberForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(memberForm.getActor().getUserAccount().getPassword());
			member.setRequestsMarch(requestsMarch);
			member.setEnrolments(enrolments);
			member.setBoxes(boxes);
			member.setFinder(finder);
			member.setUserAccount(userAccount);
			memberForm.setActor(member);
		} else {
			final Member res = this.memberRepository.findOne(member.getId());
			Assert.notNull(res, "This entity does not exist");
			res.setName(member.getName());
			res.setMiddleName(member.getMiddleName());
			res.setSurname(member.getSurname());
			res.setPhoto(member.getPhoto());
			res.setEmail(member.getEmail());
			res.setPhoneNumber(member.getPhoneNumber());
			res.setAddress(member.getAddress());
			memberForm.setActor(res);
		}

		result = memberForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.memberRepository.flush();
	}

}
