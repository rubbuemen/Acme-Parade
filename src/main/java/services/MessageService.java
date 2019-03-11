
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Box;
import domain.Message;

@Service
@Transactional
public class MessageService {

	// Managed repository
	@Autowired
	private MessageRepository			messageRepository;

	// Supporting services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private BoxService					boxService;


	// Simple CRUD methods
	public Message create() {
		Message result;

		final Actor sender = this.actorService.findActorLogged();
		Assert.notNull(sender);

		result = new Message();
		final Collection<Actor> recipients = new HashSet<>();
		final Date moment = new Date(System.currentTimeMillis() - 1);

		result.setRecipients(recipients);
		result.setSender(sender);
		result.setMoment(moment);
		result.setFlagSpam(false);

		return result;
	}

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Message findOne(final int messageId) {
		Assert.isTrue(messageId != 0);

		Message result;

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);

		return result;
	}

	public Message save(final Message message, final boolean notification) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0); //Un mensaje no tiene sentido que se edite, por lo que sólo vendrá del create

		Message result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		// R26
		final Collection<String> spamWords = this.systemConfigurationService.getConfiguration().getSpamWords();
		for (final String sw : spamWords) {
			final Boolean flagSpam = (message.getBody().toLowerCase().contains(sw) || message.getSubject().toLowerCase().contains(sw)) ? true : false;
			if (flagSpam) {
				message.setFlagSpam(flagSpam);
				break;
			}
		}

		final Date moment = new Date(System.currentTimeMillis() - 1);
		message.setMoment(moment);

		result = this.messageRepository.save(message);

		final Box outBox = this.boxService.findOutBoxByActorId(result.getSender().getId());
		Assert.notNull(outBox);
		final Collection<Message> messagesOutBox = outBox.getMessages();
		messagesOutBox.add(result);
		outBox.setMessages(messagesOutBox);
		this.boxService.saveForSystemBox(outBox);

		for (final Actor recipient : result.getRecipients())
			if (result.getFlagSpam()) {
				final Box spamBox = this.boxService.findSpamBoxByActorId(recipient.getId());
				Assert.notNull(spamBox);
				final Collection<Message> messagesSpamBox = spamBox.getMessages();
				messagesSpamBox.add(result);
				spamBox.setMessages(messagesSpamBox);
				this.boxService.saveForSystemBox(spamBox);
			} else if (notification) {
				final Box notificationBox = this.boxService.findNotificationBoxByActorId(recipient.getId());
				Assert.notNull(notificationBox);
				final Collection<Message> messagesNotificationBox = notificationBox.getMessages();
				messagesNotificationBox.add(result);
				notificationBox.setMessages(messagesNotificationBox);
				this.boxService.saveForSystemBox(notificationBox);
			} else {
				final Box inBox = this.boxService.findInBoxByActorId(recipient.getId());
				Assert.notNull(inBox);
				final Collection<Message> messagesInBox = inBox.getMessages();
				messagesInBox.add(result);
				inBox.setMessages(messagesInBox);
				this.boxService.saveForSystemBox(inBox);
			}

		return result;
	}
	public void delete(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(this.messageRepository.exists(message.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		//R26: When an actor removes a message from a box other than trash box, it is moved to the trash box; when he or she removes it from the trash box, then it is actually removed from his folders; when a message is not in any folder, it's actually removed from the system
		final Collection<Message> messagesActorLogged = this.findMessagesByActorId(actorLogged.getId());
		Assert.isTrue(messagesActorLogged.contains(message));

		final Box trashBox = this.boxService.findTrashBoxByActorId(actorLogged.getId());
		Assert.notNull(trashBox);

		final Collection<Message> messagesTrashBox = trashBox.getMessages();

		if (messagesTrashBox.contains(message)) {
			messagesTrashBox.remove(message);
			trashBox.setMessages(messagesTrashBox);
			this.boxService.saveForSystemBox(trashBox);
			final Collection<Box> boxesMessage = this.boxService.findBoxesByMessageId(message.getId());
			if (boxesMessage.isEmpty() || boxesMessage == null)
				this.messageRepository.delete(message);
		} else {
			Collection<Message> messages = new HashSet<>();
			for (final Box b : actorLogged.getBoxes()) {
				messages = b.getMessages();
				messages.remove(message);
				b.setMessages(messages);
				this.boxService.saveForSystemBox(b);
			}
			messagesTrashBox.add(message);
			trashBox.setMessages(messagesTrashBox);
			this.boxService.saveForSystemBox(trashBox);
		}
	}

	// Other business methods
	public Collection<Message> findMessagesByActorId(final int actorId) {
		Assert.isTrue(actorId != 0);

		Collection<Message> result;

		result = this.messageRepository.findMessagesByActorId(actorId);
		return result;
	}

	public void saveToMove(final Message message, final Box box) {
		Assert.notNull(message);
		Assert.notNull(box);

		final Box currentBox = this.boxService.findBoxFromMessageIdActorLogged(message.getId());
		final Collection<Message> currentBoxMessages = currentBox.getMessages();
		currentBoxMessages.remove(message);
		currentBox.setMessages(currentBoxMessages);
		this.boxService.saveForSystemBox(currentBox);
		final Collection<Message> boxMessages = box.getMessages();
		boxMessages.add(message);
		box.setMessages(boxMessages);
		this.boxService.saveForSystemBox(box);
	}

	public Collection<Message> findMessagesSentByActorId(final int actorId) {
		Assert.isTrue(actorId != 0);

		Collection<Message> result;

		result = this.messageRepository.findMessagesSentByActorId(actorId);
		return result;
	}


	// Reconstruct methods
	@Autowired(required = false)
	private Validator	validator;


	public Message reconstruct(final Message message, final BindingResult binding) {
		Message result;

		final Actor sender = this.actorService.findActorLogged();
		Assert.notNull(sender);

		final Date moment = new Date(System.currentTimeMillis() - 1);

		message.setSender(sender);
		message.setMoment(moment);
		message.setFlagSpam(false);

		result = message;

		this.validator.validate(result, binding);

		return result;
	}

}
