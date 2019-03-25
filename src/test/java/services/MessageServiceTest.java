/*
 * SampleTest.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Box;
import domain.Brotherhood;
import domain.Member;
import domain.Message;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private MessageService	messageService;

	@Autowired
	private BoxService		boxService;

	@Autowired
	private ActorService	actorService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.2 (Acme-Madrugá)
	 *         Caso de uso: listar "Messages"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Messages" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Messages" sin estar logeado
	 *         Analisis de cobertura de sentencias: 100% 13/13 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListMessages() {
		final Object testingData[][] = {
			{
				"admin", "box2", null
			}, {
				null, "box2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listMessagesTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.2 (Acme-Madrugá)
	 *         Caso de uso: crear un "Message"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Message" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de creación de un "Message" sin estar logeado
	 *         *** 2. Intento de creación de un "Message" con asunto vacío
	 *         *** 3. Intento de creación de un "Message" con cuerpo vacío
	 *         *** 4. Intento de creación de un "Message" con prioridad vacía
	 *         Analisis de cobertura de sentencias: 86,09% 192/223 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateMessage() {
		final Collection<Actor> recipientsMessage = this.recipientsMessage();
		final Object testingData[][] = {
			{
				"admin", "subjectTest", "bodyTest", "priorityTest", recipientsMessage, null
			}, {
				null, "subjectTest", "bodyTest", "priorityTest", recipientsMessage, IllegalArgumentException.class
			}, {
				"admin", "", "bodyTest", "priorityTest", recipientsMessage, ConstraintViolationException.class
			}, {
				"admin", "subjectTest", "", "priorityTest", recipientsMessage, ConstraintViolationException.class
			}, {
				"admin", "subjectTest", "bodyTest", "", recipientsMessage, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.createMessageTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Collection<Actor>) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.2 (Acme-Madrugá)
	 *         Caso de uso: eliminar un "Message"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Message" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "Message" sin estar logeado
	 *         *** 2. Intento de eliminación de un "Message" que no es del "Actor" logeado
	 *         Analisis de cobertura de sentencias: 75,4% 89/118 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteMessage() {
		final Object testingData[][] = {
			{
				"admin", "message1", null
			}, {
				null, "message1", IllegalArgumentException.class
			}, {
				"chapter1", "message1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteMessageTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.2 (Acme-Madrugá)
	 *         Caso de uso: broadcast un "Message"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Message" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de creación de un "Message" sin estar logeado
	 *         *** 2. Intento de creación de un "Message" con asunto vacío
	 *         *** 3. Intento de creación de un "Message" con cuerpo vacío
	 *         *** 4. Intento de creación de un "Message" con prioridad vacía
	 *         Analisis de cobertura de sentencias: 86,09% 192/223 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverBroadcastMessage() {
		final Object testingData[][] = {
			{
				"admin", "subjectTest", "bodyTest", "priorityTest", null
			}, {
				null, "subjectTest", "bodyTest", "priorityTest", IllegalArgumentException.class
			}, {
				"admin", "", "bodyTest", "priorityTest", ConstraintViolationException.class
			}, {
				"admin", "subjectTest", "", "priorityTest", ConstraintViolationException.class
			}, {
				"admin", "subjectTest", "bodyTest", "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.broadcastMessageTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	// Template methods ------------------------------------------------------

	protected void listMessagesTemplate(final String username, final String box, final Class<?> expected) {
		Class<?> caught = null;
		Box boxEntity;
		Collection<Message> messages;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			boxEntity = this.boxService.findBoxActorLogged(super.getEntityId(box));
			messages = boxEntity.getMessages();
			Assert.notNull(messages);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createMessageTemplate(final String username, final String subject, final String body, final String priority, final Collection<Actor> recipients, final Class<?> expected) {
		Class<?> caught = null;
		Message messageEntity;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			messageEntity = this.messageService.create();
			messageEntity.setSubject(subject);
			messageEntity.setBody(body);
			messageEntity.setPriority(priority);
			messageEntity.setRecipients(recipients);
			this.messageService.save(messageEntity, false);
			this.messageService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteMessageTemplate(final String username, final String message, final Class<?> expected) {
		Class<?> caught = null;
		Message messageEntity;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			messageEntity = this.messageService.findOne(super.getEntityId(message));
			this.messageService.delete(messageEntity);
			this.messageService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void broadcastMessageTemplate(final String username, final String subject, final String body, final String priority, final Class<?> expected) {
		Class<?> caught = null;
		Message messageEntity;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			messageEntity = this.messageService.create();
			messageEntity.setSubject(subject);
			messageEntity.setBody(body);
			messageEntity.setPriority(priority);
			final Collection<Actor> actorsSystem = this.actorService.findAllActorsExceptLogged();
			messageEntity.setRecipients(actorsSystem);
			this.messageService.save(messageEntity, true);
			this.messageService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	// Auxiliar methods ------------------------------------------------------

	private Collection<Actor> recipientsMessage() {
		final Collection<Actor> recipientsMessage = new HashSet<>();
		final Brotherhood brotherhood = (Brotherhood) this.actorService.findOne(super.getEntityId("brotherhood1"));
		final Member member = (Member) this.actorService.findOne(super.getEntityId("member1"));
		recipientsMessage.add(brotherhood);
		recipientsMessage.add(member);

		return recipientsMessage;
	}
}
