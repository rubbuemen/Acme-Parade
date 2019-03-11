
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BoxRepository;
import domain.Actor;
import domain.Box;
import domain.Message;

@Service
@Transactional
public class BoxService {

	// Managed repository
	@Autowired
	private BoxRepository	boxRepository;

	// Supporting services
	@Autowired
	private ActorService	actorService;


	// Simple CRUD methods
	// R27.2
	public Box create() {
		Box result;

		final Actor sender = this.actorService.findActorLogged();
		Assert.notNull(sender);

		result = new Box();
		final Collection<Message> messages = new HashSet<>();
		final Collection<Box> childBoxes = new HashSet<>();

		result.setMessages(messages);
		result.setChildsBox(childBoxes);
		result.setIsSystemBox(false);

		return result;
	}

	public Collection<Box> findAll() {
		Collection<Box> result;

		result = this.boxRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Box findOne(final int boxId) {
		Assert.isTrue(boxId != 0);

		Box result;

		result = this.boxRepository.findOne(boxId);
		Assert.notNull(result);

		return result;
	}

	// R27.2
	public Box save(final Box box, final Box oldParentBox) {
		Assert.notNull(box);

		Box result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Collection<Box> boxesActorLogged = actorLogged.getBoxes();

		final String nameBox = box.getName().toLowerCase();
		Assert.isTrue(nameBox != "in box" && nameBox != "out box" && nameBox != "spam box" && nameBox != "trash box" && nameBox != "notification box", "The box can not be called the same as the system boxes");

		if (box.getId() == 0) {
			result = this.boxRepository.save(box);
			boxesActorLogged.add(result);
			actorLogged.setBoxes(boxesActorLogged);
			this.actorService.save(actorLogged);
		} else {
			if (oldParentBox != null) {
				final Collection<Box> childOldParentBoxes = oldParentBox.getChildsBox();
				childOldParentBoxes.remove(box);
				oldParentBox.setChildsBox(childOldParentBoxes);
				this.boxRepository.save(oldParentBox);
			}
			Assert.isTrue(boxesActorLogged.contains(box), "The logged actor is not the owner of this entity");
			Assert.isTrue(!box.getIsSystemBox(), "It is not allowed to edit the system boxes");
			result = this.boxRepository.save(box);
		}

		return result;
	}

	// R27.2
	public void delete(final Box box) {
		Assert.notNull(box);
		Assert.isTrue(box.getId() != 0);
		Assert.isTrue(this.boxRepository.exists(box.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Collection<Box> boxesActorLogged = this.findBoxesByActorLogged();

		Assert.isTrue(boxesActorLogged.contains(box), "The logged actor is not the owner of this entity");
		Assert.isTrue(!box.getIsSystemBox(), "It is not allowed to delete the system boxes");

		final Collection<Box> boxesToDelete = this.findBoxesToDelete(box.getId(), new HashSet<Box>());
		for (final Box b : boxesToDelete) {
			final Box parentBox = b.getParentBox();
			if (parentBox != null) {
				final Collection<Box> childsParentBox = parentBox.getChildsBox();
				childsParentBox.remove(b);
				parentBox.setChildsBox(childsParentBox);
				this.boxRepository.save(parentBox);
			}
			boxesActorLogged.remove(b);
			actorLogged.setBoxes(boxesActorLogged);
			this.actorService.save(actorLogged);
			this.boxRepository.delete(b);
		}

		boxesActorLogged.remove(box);
		actorLogged.setBoxes(boxesActorLogged);
		this.actorService.save(actorLogged);
		this.boxRepository.delete(box);
	}

	// Other business methods
	public Box createForSystemBox() {
		Box result;

		result = new Box();
		final Collection<Message> messages = new HashSet<>();
		final Collection<Box> childBoxes = new HashSet<>();

		result.setMessages(messages);
		result.setChildsBox(childBoxes);
		result.setIsSystemBox(true);

		return result;
	}

	public Box saveForSystemBox(final Box box) {
		Assert.notNull(box);

		Box result;

		result = this.boxRepository.save(box);

		return result;
	}

	public Box findOutBoxByActorId(final int actorId) {
		Assert.isTrue(actorId != 0);

		Box result;

		result = this.boxRepository.findOutBoxByActorId(actorId);
		return result;
	}

	public Box findSpamBoxByActorId(final int actorId) {
		Assert.isTrue(actorId != 0);

		Box result;

		result = this.boxRepository.findSpamBoxByActorId(actorId);
		return result;
	}

	public Box findInBoxByActorId(final int actorId) {
		Assert.isTrue(actorId != 0);

		Box result;

		result = this.boxRepository.findInBoxByActorId(actorId);
		return result;
	}

	public Box findTrashBoxByActorId(final int actorId) {
		Assert.isTrue(actorId != 0);

		Box result;

		result = this.boxRepository.findTrashBoxByActorId(actorId);
		return result;
	}

	public Box findNotificationBoxByActorId(final int actorId) {
		Assert.isTrue(actorId != 0);

		Box result;

		result = this.boxRepository.findNotificationBoxByActorId(actorId);
		return result;
	}

	public Collection<Box> findRootBoxesByActorLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		Collection<Box> result;

		result = this.boxRepository.findRootBoxesByActorId(actorLogged.getId());
		return result;
	}

	public Collection<Box> findBoxesByActorLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		Collection<Box> result;

		result = this.boxRepository.findBoxesActorLogged(actorLogged.getId());
		return result;
	}

	public Box findBoxFromMessageIdActorLogged(final int messageId) {
		Box result;

		final Actor actorLogged = this.actorService.findActorLogged();

		result = this.boxRepository.findBoxFromMessageIdActorLogged(messageId, actorLogged.getId());
		return result;
	}

	public Box findBoxActorLogged(final int boxId) {
		Assert.isTrue(boxId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		Box result;

		result = this.boxRepository.findOne(boxId);

		Assert.isTrue(actorLogged.getBoxes().contains(result), "The logged actor is not the owner of this entity");

		return result;
	}

	public Collection<Box> findBoxesToDelete(final int boxId, final Collection<Box> ac) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Box boxToRemove = this.boxRepository.findOne(boxId);
		final Collection<Box> childBoxesToRemove = boxToRemove.getChildsBox();

		for (final Box b : childBoxesToRemove) {
			this.findBoxesToDelete(b.getId(), ac);
			ac.add(b);
		}

		return ac;
	}

	public Collection<Box> findBoxesByMessageId(final int messageId) {
		Collection<Box> result;

		result = this.boxRepository.findBoxesByMessageId(messageId);
		return result;
	}


	// Reconstruct methods
	@Autowired(required = false)
	private Validator	validator;


	public Box reconstruct(final Box box, final BindingResult binding) {
		Box result;

		if (box.getId() == 0) {
			final Collection<Message> messages = new HashSet<>();
			final Collection<Box> childBoxes = new HashSet<>();
			box.setMessages(messages);
			box.setChildsBox(childBoxes);
			box.setIsSystemBox(false);
			result = box;
		} else {
			result = this.boxRepository.findOne(box.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setName(box.getName());
			result.setParentBox(box.getParentBox());
		}

		this.validator.validate(result, binding);
		this.boxRepository.flush();

		return result;
	}

}
