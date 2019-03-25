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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
import domain.Brotherhood;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private BrotherhoodService	brotherhoodService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 8.1 (Acme-Madrugá)
	 *         Caso de uso: registrarse como "Brotherhood" en el sistema
	 *         Tests positivos: 1
	 *         *** 1. Registrarse como "Brotherhood" correctamente
	 *         Tests negativos: 12
	 *         *** 1. Intento de registro como "Brotherhood" con el nombre vacío
	 *         *** 2. Intento de registro como "Brotherhood" con el apellido vacío
	 *         *** 3. Intento de registro como "Brotherhood" con el email vacío
	 *         *** 4. Intento de registro como "Brotherhood" con el email sin cumplir el patrón adecuado
	 *         *** 5. Intento de registro como "Brotherhood" con el título vacío
	 *         *** 6. Intento de registro como "Brotherhood" con el fecha de establecimiento nula
	 *         *** 7. Intento de registro como "Brotherhood" con el fecha de establecimiento que no es pasada
	 *         *** 8. Intento de registro como "Brotherhood" con comentarios vacío
	 *         *** 9. Intento de registro como "Brotherhood" con el usuario vacío
	 *         *** 10. Intento de registro como "Brotherhood" con tamaño del usuario menor a 5 caracteres
	 *         *** 11. Intento de registro como "Brotherhood" con tamaño del usuario mayor a 32 caracteres
	 *         *** 12. Intento de registro como "Brotherhood" usuario ya usado
	 *         Analisis de cobertura de sentencias: 100% 250/250 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRegisterAsBrotherhood() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2000, 9, 30);
		final Date establishmentDatePast = cal.getTime();
		cal.set(2200, 9, 30);
		final Date establishmentDateFuture = cal.getTime();

		final Object testingData[][] = {
			{
				"testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", "testUser", "testPass", null
			}, {
				"", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "", "testTitle", establishmentDatePast, "testComments", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail", "testTitle", establishmentDatePast, "testComments", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "", establishmentDatePast, "testComments", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testTitle", null, "testComments", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDateFuture, "testComments", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", "", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", "test", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", "testUsertestUsertestUsertestUsertestUsertestUsertestUsertestUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", "brotherhood1", "testPass", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerAsBrotherhoodTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2 (Acme-Madrugá)
	 *         Caso de uso: editar sus datos estando logeado
	 *         Tests positivos: 1
	 *         *** 1. Editar sus datos correctamente
	 *         Tests negativos: 9
	 *         *** 1. Intento de edición de datos de un actor que no es el logeado
	 *         *** 2. Intento de edición como "Brotherhood" con el nombre vacío
	 *         *** 3. Intento de edición como "Brotherhood" con el apellido vacío
	 *         *** 4. Intento de edición como "Brotherhood" con el email vacío
	 *         *** 5. Intento de edición como "Brotherhood" con el email sin cumplir el patrón adecuado
	 *         *** 6. Intento de edición como "Brotherhood" con el título vacío
	 *         *** 7. Intento de edición como "Brotherhood" con el fecha de establecimiento nula
	 *         *** 8. Intento de edición como "Brotherhood" con el fecha de establecimiento que no es pasada
	 *         *** 9. Intento de edición como "Brotherhood" con comentarios vacío
	 *         Analisis de cobertura de sentencias: 100% 199/199 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditData() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2000, 9, 30);
		final Date establishmentDatePast = cal.getTime();
		cal.set(2200, 9, 30);
		final Date establishmentDateFuture = cal.getTime();

		final Object testingData[][] = {
			{
				"brotherhood1", "brotherhood1", "testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", null
			}, {
				"brotherhood1", "brotherhood2", "testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", IllegalArgumentException.class
			}, {
				"brotherhood1", "brotherhood1", "", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", ConstraintViolationException.class
			}, {
				"brotherhood1", "brotherhood1", "testName", "", "testEmail@testemail.com", "testTitle", establishmentDatePast, "testComments", ConstraintViolationException.class
			}, {
				"brotherhood1", "brotherhood1", "testName", "testSurname", "", "testTitle", establishmentDatePast, "testComments", ConstraintViolationException.class
			}, {
				"brotherhood1", "brotherhood1", "testName", "testSurname", "testEmail", "testTitle", establishmentDatePast, "testComments", ConstraintViolationException.class
			}, {
				"brotherhood1", "brotherhood1", "testName", "testSurname", "testEmail@testemail.com", "", establishmentDatePast, "testComments", ConstraintViolationException.class
			}, {
				"brotherhood1", "brotherhood1", "testName", "testSurname", "testEmail@testemail.com", "testTitle", null, "testComments", ConstraintViolationException.class
			}, {
				"brotherhood1", "brotherhood1", "testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDateFuture, "testComments", ConstraintViolationException.class
			}, {
				"brotherhood1", "brotherhood1", "testName", "testSurname", "testEmail@testemail.com", "testTitle", establishmentDatePast, "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Date) testingData[i][6], (String) testingData[i][7],
				(Class<?>) testingData[i][8]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.1 (Acme-Parade)
	 *         Caso de uso: navegar a "Brotherhoods" que coordinan un "Area" sin estar logeado
	 *         Tests positivos: 1
	 *         *** 1. Navegar a "Brotherhoods" que coordinan un "Area" sin estar logeado
	 *         Tests negativos: 1
	 *         *** 1. Intento de navegar a "Brotherhoods" que coordinan un "Area" inexistente
	 *         Analisis de cobertura de sentencias: 92,3% 12/13 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverNavegateBrotherhoodsArea() {

		final Object testingData[][] = {
			{
				"area1", null
			}, {
				"area10", AssertionError.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.navegateBrotherhoodsAreaTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Template methods ------------------------------------------------------

	protected void registerAsBrotherhoodTemplate(final String name, final String surname, final String email, final String title, final Date establishmentDate, final String comments, final String username, final String password, final Class<?> expected) {
		Class<?> caught = null;
		Brotherhood brotherhood;

		super.startTransaction();

		try {
			brotherhood = this.brotherhoodService.create();
			brotherhood.setName(name);
			brotherhood.setSurname(surname);
			brotherhood.setEmail(email);
			brotherhood.setTitle(title);
			brotherhood.setEstablishmentDate(establishmentDate);
			brotherhood.setComments(comments);
			brotherhood.getUserAccount().setUsername(username);
			brotherhood.getUserAccount().setPassword(password);
			this.brotherhoodService.save(brotherhood);
			this.brotherhoodService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void editDataTemplate(final String username, final String actorData, final String name, final String surname, final String email, final String title, final Date establishmentDate, final String comments, final Class<?> expected) {
		Class<?> caught = null;
		Brotherhood brotherhood;

		super.startTransaction();

		try {
			super.authenticate(username);
			brotherhood = this.brotherhoodService.findOne(super.getEntityId(actorData));
			brotherhood.setName(name);
			brotherhood.setSurname(surname);
			brotherhood.setEmail(email);
			brotherhood.setTitle(title);
			brotherhood.setEstablishmentDate(establishmentDate);
			brotherhood.setComments(comments);
			this.brotherhoodService.save(brotherhood);
			this.brotherhoodService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void navegateBrotherhoodsAreaTemplate(final String area, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Brotherhood> brotherhoods;

		super.startTransaction();

		try {
			brotherhoods = this.brotherhoodService.findBrotherhoodsByAreaId(super.getEntityId(area));
			Assert.notNull(brotherhoods);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}
}
