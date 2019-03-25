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
import domain.Chapter;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChapterServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private ChapterService	chapterService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 7.1 (Acme-Parade)
	 *         Caso de uso: registrarse como "Chapter" en el sistema
	 *         Tests positivos: 1
	 *         *** 1. Registrarse como "Chapter" correctamente
	 *         Tests negativos: 9
	 *         *** 1. Intento de registro como "Chapter" con el nombre vacío
	 *         *** 2. Intento de registro como "Chapter" con el apellido vacío
	 *         *** 3. Intento de registro como "Chapter" con el email vacío
	 *         *** 4. Intento de registro como "Chapter" con el email sin cumplir el patrón adecuado
	 *         *** 5. Intento de registro como "Chapter" con el título vacío
	 *         *** 6. Intento de registro como "Chapter" con el usuario vacío
	 *         *** 7. Intento de registro como "Chapter" con tamaño del usuario menor a 5 caracteres
	 *         *** 8. Intento de registro como "Chapter" con tamaño del usuario mayor a 32 caracteres
	 *         *** 9. Intento de registro como "Chapter" usuario ya usado
	 *         Analisis de cobertura de sentencias: 100% 250/250 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRegisterAsChapter() {
		final Object testingData[][] = {
			{
				"testName", "testSurname", "testEmail@testemail.com", "titleTest", "testUser", "testPass", null
			}, {
				"", "testSurname", "testEmail@testemail.com", "titleTest", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "", "testEmail@testemail.com", "titleTest", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "", "titleTest", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail", "titleTest", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "titleTest", "", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "titleTest", "test", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "titleTest", "testUsertestUsertestUsertestUsertestUsertestUsertestUsertestUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "titleTest", "chapter1", "testPass", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerAsChapterTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2 (Acme-Madrugá)
	 *         Caso de uso: editar sus datos estando logeado
	 *         Tests positivos: 1
	 *         *** 1. Editar sus datos correctamente
	 *         Tests negativos: 9
	 *         *** 1. Intento de edición de datos de un actor que no es el logeado
	 *         *** 2. Intento de edición como "Chapter" con el nombre vacío
	 *         *** 3. Intento de edición como "Chapter" con el apellido vacío
	 *         *** 4. Intento de edición como "Chapter" con el email vacío
	 *         *** 5. Intento de edición como "Chapter" con el email sin cumplir el patrón adecuado
	 *         *** 6. Intento de edición como "Chapter" con el título vacío
	 *         Analisis de cobertura de sentencias: 100% 199/199 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditData() {
		final Object testingData[][] = {
			{
				"chapter1", "chapter1", "testName", "testSurname", "testEmail@testemail.com", "testTitle", null
			}, {
				"chapter1", "chapter2", "testName", "testSurname", "testEmail@testemail.com", "testTitle", IllegalArgumentException.class
			}, {
				"chapter1", "chapter1", "", "testSurname", "testEmail@testemail.com", "testTitle", ConstraintViolationException.class
			}, {
				"chapter1", "chapter1", "testName", "", "testEmail@testemail.com", "testTitle", ConstraintViolationException.class
			}, {
				"chapter1", "chapter1", "testName", "testSurname", "", "testTitle", ConstraintViolationException.class
			}, {
				"chapter1", "chapter1", "testName", "testSurname", "testEmail", "testTitle", ConstraintViolationException.class
			}, {
				"chapter1", "chapter1", "testName", "testSurname", "testEmail@testemail.com", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	// Template methods ------------------------------------------------------

	protected void registerAsChapterTemplate(final String name, final String surname, final String email, final String title, final String username, final String password, final Class<?> expected) {
		Class<?> caught = null;
		Chapter chapter;

		super.startTransaction();

		try {
			chapter = this.chapterService.create();
			chapter.setName(name);
			chapter.setSurname(surname);
			chapter.setEmail(email);
			chapter.setTitle(title);
			chapter.getUserAccount().setUsername(username);
			chapter.getUserAccount().setPassword(password);
			this.chapterService.save(chapter);
			this.chapterService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void editDataTemplate(final String username, final String actorData, final String name, final String surname, final String email, final String title, final Class<?> expected) {
		Class<?> caught = null;
		Chapter chapter;

		super.startTransaction();

		try {
			super.authenticate(username);
			chapter = this.chapterService.findOne(super.getEntityId(actorData));
			chapter.setName(name);
			chapter.setSurname(surname);
			chapter.setEmail(email);
			chapter.setTitle(title);
			this.chapterService.save(chapter);
			this.chapterService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}
}
