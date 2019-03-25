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
import domain.PositionBrotherhood;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PositionBrotherhoodServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private PositionBrotherhoodService	positionBrotherhoodService;

	@PersistenceContext
	EntityManager						entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 12.2 (Acme-Madrugá)
	 *         Caso de uso: listar "PositionBrotherhoods"
	 *         Tests positivos: 1
	 *         *** 1. Listar "PositionBrotherhoods" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "PositionBrotherhoods" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 24/24 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListPositionBrotherhood() {
		final Object testingData[][] = {
			{
				"admin", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listPositionBrotherhoodTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 12.2 (Acme-Madrugá)
	 *         Caso de uso: crear un "PositionBrotherhood"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "PositionBrotherhood" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de creación de un "PositionBrotherhood" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "PositionBrotherhood" con nombre en inglés vacío
	 *         *** 3. Intento de creación de un "PositionBrotherhood" con nombre en español vacío
	 *         *** 4. Intento de creación de un "PositionBrotherhood" con nombres ya creados
	 *         Analisis de cobertura de sentencias: 88,63% 39/44 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreatePositionBrotherhood() {
		final Object testingData[][] = {
			{
				"admin", "nameENTest", "nameESTest", null
			}, {
				"member1", "nameENTest", "nameESTest", IllegalArgumentException.class
			}, {
				"admin", "", "nameESTest", ConstraintViolationException.class
			}, {
				"admin", "nameENTest", "", ConstraintViolationException.class
			}, {
				"admin", "President", "Presidente", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createPositionBrotherhoodTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 12.2 (Acme-Madrugá)
	 *         Caso de uso: editar un "PositionBrotherhood"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "PositionBrotherhood" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de edición de un "PositionBrotherhood" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "PositionBrotherhood" con nombre en inglés vacío
	 *         *** 3. Intento de edición de un "PositionBrotherhood" con nombre en español vacío
	 *         *** 4. Intento de edición de un "PositionBrotherhood" con nombres ya creados
	 *         Analisis de cobertura de sentencias: 82,1% 23/28 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditPositionBrotherhood() {
		final Object testingData[][] = {
			{
				"admin", "positionBrotherhood1", "nameENTest", "nameESTest", null
			}, {
				"member1", "positionBrotherhood1", "nameENTest", "nameESTest", IllegalArgumentException.class
			}, {
				"admin", "positionBrotherhood1", "", "nameESTest", ConstraintViolationException.class
			}, {
				"admin", "positionBrotherhood1", "nameENTest", "", ConstraintViolationException.class
			}, {
				"admin", "positionBrotherhood2", "President", "Presidente", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editPositionBrotherhoodTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 12.2 (Acme-Madrugá)
	 *         Caso de uso: eliminar un "PositionBrotherhood"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "PositionBrotherhood" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "PositionBrotherhood" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "PositionBrotherhood" que ya está en uso
	 *         Analisis de cobertura de sentencias: 97,7% 43/44 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeletePositionBrotherhood() {
		final Object testingData[][] = {
			{
				"admin", "positionBrotherhood4", null
			}, {
				"member1", "positionBrotherhood4", IllegalArgumentException.class
			}, {
				"admin", "positionBrotherhood1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deletePositionBrotherhoodTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listPositionBrotherhoodTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<PositionBrotherhood> positionBrotherhoods;

		super.startTransaction();

		try {
			super.authenticate(username);
			positionBrotherhoods = this.positionBrotherhoodService.findAll();
			this.positionBrotherhoodService.findPositionsBrotherhoodUsed();
			Assert.notNull(positionBrotherhoods);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createPositionBrotherhoodTemplate(final String username, final String nameEnglish, final String nameSpanish, final Class<?> expected) {
		Class<?> caught = null;
		PositionBrotherhood positionBrotherhoodEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			positionBrotherhoodEntity = this.positionBrotherhoodService.create();
			positionBrotherhoodEntity.setNameEnglish(nameEnglish);
			positionBrotherhoodEntity.setNameSpanish(nameSpanish);
			positionBrotherhoodEntity = this.positionBrotherhoodService.save(positionBrotherhoodEntity);
			this.positionBrotherhoodService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editPositionBrotherhoodTemplate(final String username, final String positionBrotherhood, final String nameEnglish, final String nameSpanish, final Class<?> expected) {
		Class<?> caught = null;
		PositionBrotherhood positionBrotherhoodEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			positionBrotherhoodEntity = this.positionBrotherhoodService.findOne(super.getEntityId(positionBrotherhood));
			positionBrotherhoodEntity.setNameEnglish(nameEnglish);
			positionBrotherhoodEntity.setNameSpanish(nameSpanish);
			positionBrotherhoodEntity = this.positionBrotherhoodService.save(positionBrotherhoodEntity);
			this.positionBrotherhoodService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deletePositionBrotherhoodTemplate(final String username, final String positionBrotherhood, final Class<?> expected) {
		Class<?> caught = null;
		PositionBrotherhood positionBrotherhoodEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			positionBrotherhoodEntity = this.positionBrotherhoodService.findOne(super.getEntityId(positionBrotherhood));
			this.positionBrotherhoodService.delete(positionBrotherhoodEntity);
			this.positionBrotherhoodService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
