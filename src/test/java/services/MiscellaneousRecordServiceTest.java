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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.MiscellaneousRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@PersistenceContext
	EntityManager						entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: crear un "MiscellaneousRecord"
	 *         Tests positivos: 1
	 *         *** 1. Crear de un "MiscellaneousRecord" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de creación de un "MiscellaneousRecord" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "MiscellaneousRecord" con título vacío
	 *         *** 3. Intento de creación de un "MiscellaneousRecord" con descripción vacía
	 */
	@Test
	public void driverCreateMiscellaneousRecord() {

		final Object testingData[][] = {
			{
				"brotherhood1", "titleMiscellaneousRecord", "descriptionMiscellaneousRecord", null
			}, {
				"chapter1", "titleMiscellaneousRecord", "descriptionMiscellaneousRecord", IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionMiscellaneousRecord", ConstraintViolationException.class
			}, {
				"brotherhood1", "titleMiscellaneousRecord", "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.createMiscellaneousRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: editar un "MiscellaneousRecord"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "MiscellaneousRecord" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de edición de un "MiscellaneousRecord" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "MiscellaneousRecord" con título vacío
	 *         *** 3. Intento de edición de un "MiscellaneousRecord" con descripción vacía
	 */
	@Test
	public void driverEditMiscellaneousRecord() {

		final Object testingData[][] = {
			{
				"brotherhood1", "titleMiscellaneousRecord", "descriptionMiscellaneousRecord", null
			}, {
				"chapter1", "titleMiscellaneousRecord", "descriptionMiscellaneousRecord", IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionMiscellaneousRecord", ConstraintViolationException.class
			}, {
				"brotherhood1", "titleMiscellaneousRecord", "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.editMiscellaneousRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: eliminar un "MiscellaneousRecord"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar de un "MiscellaneousRecord" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de eliminación de un "MiscellaneousRecord" con una autoridad no permitida
	 */
	@Test
	public void driverDeleteMiscellaneousRecord() {
		final Object testingData[][] = {
			{
				"brotherhood1", "miscellaneousRecord1", null
			}, {
				"chapter1", "miscellaneousRecord1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteMiscellaneousRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void createMiscellaneousRecordTemplate(final String username, final String title, final String description, final Class<?> expected) {
		Class<?> caught = null;
		MiscellaneousRecord miscellaneousRecord;

		super.startTransaction();

		try {
			super.authenticate(username);
			miscellaneousRecord = this.miscellaneousRecordService.create();
			miscellaneousRecord.setTitle(title);
			miscellaneousRecord.setDescription(description);
			this.miscellaneousRecordService.save(miscellaneousRecord);
			this.miscellaneousRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editMiscellaneousRecordTemplate(final String username, final String title, final String description, final Class<?> expected) {
		Class<?> caught = null;
		MiscellaneousRecord miscellaneousRecord;

		super.startTransaction();

		try {
			super.authenticate(username);
			miscellaneousRecord = this.miscellaneousRecordService.findMiscellaneousRecordBrotherhoodLogged(super.getEntityId("miscellaneousRecord1"));
			miscellaneousRecord.setTitle(title);
			miscellaneousRecord.setDescription(description);
			this.miscellaneousRecordService.save(miscellaneousRecord);
			this.miscellaneousRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteMiscellaneousRecordTemplate(final String username, final String miscellaneousRecord, final Class<?> expected) {
		Class<?> caught = null;
		MiscellaneousRecord miscellaneousRecordEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			miscellaneousRecordEntity = this.miscellaneousRecordService.findMiscellaneousRecordBrotherhoodLogged(super.getEntityId(miscellaneousRecord));
			this.miscellaneousRecordService.delete(miscellaneousRecordEntity);
			this.miscellaneousRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
