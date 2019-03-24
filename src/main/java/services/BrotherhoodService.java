
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Area;
import domain.Box;
import domain.Brotherhood;
import domain.Chapter;
import domain.Enrolment;
import domain.Float;
import domain.Member;
import domain.Parade;
import forms.BrotherhoodForm;

@Service
@Transactional
public class BrotherhoodService {

	// Managed repository
	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;

	// Supporting services
	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private AreaService				areaService;


	// Simple CRUD methods
	// R8.1, R9.1
	public Brotherhood create() {
		Brotherhood result;

		result = new Brotherhood();
		final Collection<Float> floats = new HashSet<>();
		final Collection<Parade> parades = new HashSet<>();
		final Collection<Enrolment> enrolments = new HashSet<>();
		final Collection<Box> boxes = new HashSet<>();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.BROTHERHOOD);
		userAccount.addAuthority(auth);
		result.setFloats(floats);
		result.setParades(parades);
		result.setEnrolments(enrolments);
		result.setBoxes(boxes);
		result.setUserAccount(userAccount);

		return result;
	}

	public Collection<Brotherhood> findAll() {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Brotherhood findOne(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findOne(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Brotherhood save(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);

		Brotherhood result;

		result = (Brotherhood) this.actorService.save(brotherhood);
		result = this.brotherhoodRepository.save(result);

		return result;
	}

	public void delete(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		Assert.isTrue(brotherhood.getId() != 0);
		Assert.isTrue(this.brotherhoodRepository.exists(brotherhood.getId()));

		this.brotherhoodRepository.delete(brotherhood);
	}

	// Other business methods
	public Brotherhood findBrotherhoodByFloatId(final int floatId) {
		Assert.isTrue(floatId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findBrotherhoodByFloatId(floatId);
		return result;
	}

	public Brotherhood findBrotherhoodByParadeId(final int paradeId) {
		Assert.isTrue(paradeId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findBrotherhoodByParadeId(paradeId);
		return result;
	}

	public Brotherhood findBrotherhoodByEnrolmentId(final int enrolmentId) {
		Assert.isTrue(enrolmentId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findBrotherhoodByEnrolmentId(enrolmentId);
		return result;
	}

	public Collection<Brotherhood> findBrotherhoodsByMemberId(final int memberId) {
		Assert.isTrue(memberId != 0);

		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findBrotherhoodsByMemberId(memberId);
		return result;
	}

	public Brotherhood findBrotherhoodByRequestMarchId(final int requestMarchId) {
		Assert.isTrue(requestMarchId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findBrotherhoodByRequestMarchId(requestMarchId);
		return result;
	}

	// R11.3
	public Collection<Brotherhood> findBrotherhoodsByMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findBrotherhoodsByMemberId(memberLogged.getId());
		return result;
	}

	// R11.3
	public Collection<Brotherhood> findBrotherhoodsNotBelongsByMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findBrotherhoodsNotBelongsByMemberLogged(memberLogged.getId());
		return result;
	}

	public Brotherhood findBrotherhoodMemberLogged(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Collection<Member> membersOwner = this.memberService.findMembersByBrotherhoodId(brotherhoodId);
		Assert.isTrue(membersOwner.contains(actorLogged), "The logged actor is not the owner of this entity");

		Brotherhood result;

		result = this.brotherhoodRepository.findOne(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Brotherhood> findBrotherhoodsAcceptedOrPendingByMemberId(final int memberId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findBrotherhoodsAcceptedOrPendingByMemberId(memberLogged.getId());
		return result;
	}

	public Brotherhood saveAuxiliar(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);

		Brotherhood result;
		result = this.brotherhoodRepository.save(brotherhood);

		return result;
	}

	// R20.1
	public Brotherhood selectArea(final int areaId) {
		Assert.isTrue(areaId != 0);

		Brotherhood result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		Assert.isNull(brotherhoodLogged.getArea(), "You already have an assigned area");

		final Area area = this.areaService.findOne(areaId);
		brotherhoodLogged.setArea(area);

		result = this.brotherhoodRepository.save(brotherhoodLogged);
		return result;
	}

	public Brotherhood findBrotherhoodByInceptionRecordId(final int inceptionRecordId) {
		Assert.isTrue(inceptionRecordId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findBrotherhoodByInceptionRecordId(inceptionRecordId);
		return result;
	}

	public Brotherhood findBrotherhoodByPeriodRecordId(final int periodRecordId) {
		Assert.isTrue(periodRecordId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findBrotherhoodByPeriodRecordId(periodRecordId);
		return result;
	}

	public Brotherhood findBrotherhoodByLegalRecordId(final int legalRecordId) {
		Assert.isTrue(legalRecordId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findBrotherhoodByLegalRecordId(legalRecordId);
		return result;
	}

	public Brotherhood findBrotherhoodByLinkRecordId(final int linkRecordId) {
		Assert.isTrue(linkRecordId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findBrotherhoodByLinkRecordId(linkRecordId);
		return result;
	}

	public Brotherhood findBrotherhoodByMiscellaneousRecordId(final int miscellaneousRecordId) {
		Assert.isTrue(miscellaneousRecordId != 0);

		Brotherhood result;

		result = this.brotherhoodRepository.findBrotherhoodByMiscellaneousRecordId(miscellaneousRecordId);
		return result;
	}

	public Collection<Brotherhood> findBrotherhoodsToLink() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginBrotherhood(actorLogged);

		final Brotherhood brotherhoodLogged = (Brotherhood) actorLogged;

		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findAll();
		result.remove(brotherhoodLogged);

		return result;
	}

	// R8.2(Acme-Parade)
	public Collection<Brotherhood> findBrotherhoodsByChapterLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginChapter(actorLogged);

		final Chapter chapterLogged = (Chapter) actorLogged;

		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findBrotherhoodsByChapterId(chapterLogged.getId());
		return result;
	}

	// R14.1(Acme-Parade)
	public Collection<Brotherhood> findBrotherhoodsByAreaId(final int areaId) {
		Assert.isTrue(areaId != 0);

		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findBrotherhoodsByAreaId(areaId);
		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public BrotherhoodForm reconstruct(final BrotherhoodForm brotherhoodForm, final BindingResult binding) {
		BrotherhoodForm result;
		final Brotherhood brotherhood = brotherhoodForm.getActor();

		if (brotherhood.getId() == 0) {
			final Collection<Float> floats = new HashSet<>();
			final Collection<Parade> parades = new HashSet<>();
			final Collection<Enrolment> enrolments = new HashSet<>();
			final Collection<Box> boxes = new HashSet<>();
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.BROTHERHOOD);
			userAccount.addAuthority(auth);
			userAccount.setUsername(brotherhoodForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(brotherhoodForm.getActor().getUserAccount().getPassword());
			brotherhood.setFloats(floats);
			brotherhood.setParades(parades);
			brotherhood.setEnrolments(enrolments);
			brotherhood.setBoxes(boxes);
			brotherhood.setUserAccount(userAccount);
			brotherhoodForm.setActor(brotherhood);
		} else {
			final Brotherhood res = this.brotherhoodRepository.findOne(brotherhood.getId());
			Assert.notNull(res, "This entity does not exist");
			res.setName(brotherhood.getName());
			res.setMiddleName(brotherhood.getMiddleName());
			res.setSurname(brotherhood.getSurname());
			res.setPhoto(brotherhood.getPhoto());
			res.setEmail(brotherhood.getEmail());
			res.setPhoneNumber(brotherhood.getPhoneNumber());
			res.setAddress(brotherhood.getAddress());
			res.setTitle(brotherhood.getTitle());
			res.setEstablishmentDate(brotherhood.getEstablishmentDate());
			res.setComments(brotherhood.getComments());
			brotherhoodForm.setActor(res);
		}

		result = brotherhoodForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.brotherhoodRepository.flush();
	}

}
