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

import utilities.AbstractTest;
import domain.Administrator;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private AdministratorService	administratorService;

	@PersistenceContext
	EntityManager					entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 12.1 (Acme-Madrugá)
	 *         Caso de uso: registrar un "Administrator" en el sistema
	 *         Tests positivos: 1
	 *         *** 1. Registrar un "Administrator" correctamente
	 *         Tests negativos: 9
	 *         *** 1. Intento de registro de un "Administrator" con el nombre vacío
	 *         *** 2. Intento de registro de un "Administrator" con una autoridad no permitida
	 *         *** 3. Intento de registro de un "Administrator" con el apellido vacío
	 *         *** 4. Intento de registro de un "Administrator" con el email vacío
	 *         *** 5. Intento de registro de un "Administrator" con el email sin cumplir el patrón adecuado
	 *         *** 6. Intento de registro de un "Administrator" con el usuario vacío
	 *         *** 7. Intento de registro de un "Administrator" con tamaño del usuario menor a 5 caracteres
	 *         *** 8. Intento de registro de un "Administrator" con tamaño del usuario mayor a 32 caracteres
	 *         *** 9. Intento de registro de un "Administrator" con usuario ya usado
	 *         Analisis de cobertura de sentencias: 92,3% 12/13 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRegisterAdministrator() {
		final Object testingData[][] = {
			{
				"admin", "testName", "testSurname", "testEmail@testemail.com", "testUser", "testPass", null
			}, {
				"brotherhood1", "testName", "testSurname", "testEmail@testemail.com", "testUser", "testPass", IllegalArgumentException.class
			}, {
				"admin", "", "testSurname", "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"admin", "testName", "", "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"admin", "testName", "testSurname", "", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"admin", "testName", "testSurname", "testEmail", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"admin", "testName", "testSurname", "testEmail@testemail.com", "", "testPass", ConstraintViolationException.class
			}, {
				"admin", "testName", "testSurname", "testEmail@testemail.com", "test", "testPass", ConstraintViolationException.class
			}, {
				"admin", "testName", "testSurname", "testEmail@testemail.com", "testUsertestUsertestUsertestUsertestUsertestUsertestUsertestUser", "testPass", ConstraintViolationException.class
			}, {
				"admin", "testName", "testSurname", "testEmail@testemail.com", "admin", "testPass", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerAdministratorTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2 (Acme-Madrugá)
	 *         Caso de uso: editar sus datos estando logeado
	 *         Tests positivos: 1
	 *         *** 1. Editar sus datos correctamente
	 *         Tests negativos: 9
	 *         *** 1. Intento de edición de datos de un actor que no es el logeado
	 *         *** 2. Intento de edición como "Administrator" con el nombre vacío
	 *         *** 3. Intento de edición como "Administrator" con el apellido vacío
	 *         *** 4. Intento de edición como "Administrator" con el email vacío
	 *         *** 5. Intento de edición como "Administrator" con el email sin cumplir el patrón adecuado
	 *         Analisis de cobertura de sentencias: 92,3% 12/13 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditData() {
		final Object testingData[][] = {
			{
				"admin", "administrator1", "testName", "testSurname", "testEmail@testemail.com", null
			}, {
				"admin", "administrator2", "testName", "testSurname", "testEmail@testemail.com", IllegalArgumentException.class
			}, {
				"admin", "administrator1", "", "testSurname", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurname", "", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurname", "testEmail", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	// Template methods ------------------------------------------------------

	protected void registerAdministratorTemplate(final String username, final String name, final String surname, final String email, final String user, final String password, final Class<?> expected) {
		Class<?> caught = null;
		Administrator administrator;

		super.startTransaction();

		try {
			super.authenticate(username);
			administrator = this.administratorService.create();
			administrator.setName(name);
			administrator.setSurname(surname);
			administrator.setEmail(email);
			administrator.getUserAccount().setUsername(user);
			administrator.getUserAccount().setPassword(password);
			this.administratorService.save(administrator);
			this.administratorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void editDataTemplate(final String username, final String actorData, final String name, final String surname, final String email, final Class<?> expected) {
		Class<?> caught = null;
		Administrator administrator;

		super.startTransaction();

		try {
			super.authenticate(username);
			administrator = this.administratorService.findOne(super.getEntityId(actorData));
			administrator.setName(name);
			administrator.setSurname(surname);
			administrator.setEmail(email);
			this.administratorService.save(administrator);
			this.administratorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}
}
