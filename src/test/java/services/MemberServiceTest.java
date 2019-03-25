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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Member;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MemberServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private MemberService		memberService;

	@Autowired
	private EnrolmentService	enrolmentService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 8.1 (Acme-Madrugá)
	 *         Caso de uso: registrarse como "Member" en el sistema
	 *         Tests positivos: 1
	 *         *** 1. Registrarse como "Member" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de registro como "Member" con el nombre vacío
	 *         *** 2. Intento de registro como "Member" con el apellido vacío
	 *         *** 3. Intento de registro como "Member" con el email vacío
	 *         *** 4. Intento de registro como "Member" con el email sin cumplir el patrón adecuado
	 *         *** 5. Intento de registro como "Member" con el usuario vacío
	 *         *** 6. Intento de registro como "Member" con tamaño del usuario menor a 5 caracteres
	 *         *** 7. Intento de registro como "Member" con tamaño del usuario mayor a 32 caracteres
	 *         *** 8. Intento de registro como "Member" usuario ya usado
	 *         Analisis de cobertura de sentencias: 100% 262/262 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRegisterAsMember() {
		final Object testingData[][] = {
			{
				"testName", "testSurname", "testEmail@testemail.com", "testUser", "testPass", null
			}, {
				"", "testSurname", "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "", "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "test", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testUsertestUsertestUsertestUsertestUsertestUsertestUsertestUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "member1", "testPass", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerAsMemberTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2 (Acme-Madrugá)
	 *         Caso de uso: editar sus datos estando logeado
	 *         Tests positivos: 1
	 *         *** 1. Editar sus datos correctamente
	 *         Tests negativos: 9
	 *         *** 1. Intento de edición de datos de un actor que no es el logeado
	 *         *** 2. Intento de edición como "Member" con el nombre vacío
	 *         *** 3. Intento de edición como "Member" con el apellido vacío
	 *         *** 4. Intento de edición como "Member" con el email vacío
	 *         *** 5. Intento de edición como "Member" con el email sin cumplir el patrón adecuado
	 *         Analisis de cobertura de sentencias: 100% 211/211 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditData() {
		final Object testingData[][] = {
			{
				"member1", "member1", "testName", "testSurname", "testEmail@testemail.com", null
			}, {
				"member1", "member2", "testName", "testSurname", "testEmail@testemail.com", IllegalArgumentException.class
			}, {
				"member1", "member1", "", "testSurname", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"member1", "member1", "testName", "", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"member1", "member1", "testName", "testSurname", "", ConstraintViolationException.class
			}, {
				"member1", "member1", "testName", "testSurname", "testEmail", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.3 (Acme-Madrugá)
	 *         Caso de uso: autentificado como "Brotherhood", listar sus "Members"
	 *         Tests positivos: 1
	 *         *** 1. Listar sus "Members" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Members" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 23/23 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListMembersBrotherhood() {
		final Object testingData[][] = {
			{
				"brotherhood1", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listMembersBrotherhoodTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.3 (Acme-Madrugá)
	 *         Caso de uso: autentificado como "Brotherhood", eliminar un "Member" de su "Brotherhood"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Float" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "Member" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Member" que no es del "Brotherhood" logeado
	 *         Analisis de cobertura de sentencias: 90,2% 148/164 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRemoveMemberOfBrotherhood() {
		final Object testingData[][] = {
			{
				"brotherhood1", "member1", null
			}, {
				"chapter1", "member1", IllegalArgumentException.class
			}, {
				"brotherhood3", "member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.removeMemberOfBrotherhoodTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void registerAsMemberTemplate(final String name, final String surname, final String email, final String username, final String password, final Class<?> expected) {
		Class<?> caught = null;
		Member member;

		super.startTransaction();

		try {
			member = this.memberService.create();
			member.setName(name);
			member.setSurname(surname);
			member.setEmail(email);
			member.getUserAccount().setUsername(username);
			member.getUserAccount().setPassword(password);
			this.memberService.save(member);
			this.memberService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void editDataTemplate(final String username, final String actorData, final String name, final String surname, final String email, final Class<?> expected) {
		Class<?> caught = null;
		Member member;

		super.startTransaction();

		try {
			super.authenticate(username);
			member = this.memberService.findOne(super.getEntityId(actorData));
			member.setName(name);
			member.setSurname(surname);
			member.setEmail(email);
			this.memberService.save(member);
			this.memberService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void listMembersBrotherhoodTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Member> members;

		super.startTransaction();

		try {
			super.authenticate(username);
			members = this.memberService.findMembersByBrotherhoodLogged();
			Assert.notNull(members);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void removeMemberOfBrotherhoodTemplate(final String username, final String member, final Class<?> expected) {
		Class<?> caught = null;
		Member memberEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			memberEntity = this.memberService.findMemberBrotherhoodLogged(super.getEntityId(member));
			this.enrolmentService.removeMemberOfBrotherhood(memberEntity.getId());
			this.enrolmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
