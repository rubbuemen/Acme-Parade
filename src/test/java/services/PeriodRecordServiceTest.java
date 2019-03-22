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

import utilities.AbstractTest;
import domain.PeriodRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PeriodRecordServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private PeriodRecordService	periodRecordService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: crear un "PeriodRecord"
	 *         Tests positivos: 1
	 *         *** 1. Crear de un "PeriodRecord" correctamente
	 *         Tests negativos: 10
	 *         *** 1. Intento de creación de un "PeriodRecord" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "PeriodRecord" con título vacío
	 *         *** 3. Intento de creación de un "PeriodRecord" con descripción vacía
	 *         *** 4. Intento de creación de un "PeriodRecord" con año de comienzo nulo
	 *         *** 5. Intento de creación de un "PeriodRecord" con año de finalización nulo
	 *         *** 6. Intento de creación de un "PeriodRecord" con año de comienzo futuro
	 *         *** 7. Intento de creación de un "PeriodRecord" con año de finalización futuro
	 *         *** 8. Intento de creación de un "PeriodRecord" con año de comienzo posterior al de finalización
	 *         *** 9. Intento de creación de un "PeriodRecord" con fotos vacía
	 *         *** 10. Intento de creación de un "PeriodRecord" con fotos que no son URL
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreatePeriodRecord() {
		final Collection<String> photosPeriodRecord = this.photosPeriodRecord();
		final Collection<String> emptyCollection = new HashSet<>();
		final Collection<String> stringsNoUrl = new HashSet<>();
		stringsNoUrl.add("test");

		final Object testingData[][] = {
			{
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 2001, photosPeriodRecord, null
			}, {
				"chapter1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 2001, photosPeriodRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionPeriodRecord", 2000, 2001, photosPeriodRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "", 2000, 2001, photosPeriodRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", null, 2001, photosPeriodRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, null, photosPeriodRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 3000, 2001, photosPeriodRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 3001, photosPeriodRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2001, 2000, photosPeriodRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 2001, emptyCollection, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 2001, stringsNoUrl, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createPeriodRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: editar un "PeriodRecord"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "PeriodRecord" correctamente
	 *         Tests negativos: 10
	 *         *** 1. Intento de edición de un "PeriodRecord" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "PeriodRecord" con título vacío
	 *         *** 3. Intento de edición de un "PeriodRecord" con descripción vacía
	 *         *** 4. Intento de edición de un "PeriodRecord" con año de comienzo nulo
	 *         *** 5. Intento de edición de un "PeriodRecord" con año de finalización nulo
	 *         *** 6. Intento de edición de un "PeriodRecord" con año de comienzo futuro
	 *         *** 7. Intento de edición de un "PeriodRecord" con año de finalización futuro
	 *         *** 8. Intento de edición de un "PeriodRecord" con año de comienzo posterior al de finalización
	 *         *** 9. Intento de edición de un "PeriodRecord" con fotos vacía
	 *         *** 10. Intento de edición de un "PeriodRecord" con fotos que no son URL
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditPeriodRecord() {
		final Collection<String> photosPeriodRecord = this.photosPeriodRecord();
		final Collection<String> emptyCollection = new HashSet<>();
		final Collection<String> stringsNoUrl = new HashSet<>();
		stringsNoUrl.add("test");

		final Object testingData[][] = {
			{
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 2001, photosPeriodRecord, null
			}, {
				"chapter1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 2001, photosPeriodRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionPeriodRecord", 2000, 2001, photosPeriodRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "", 2000, 2001, photosPeriodRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", null, 2001, photosPeriodRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, null, photosPeriodRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 3000, 2001, photosPeriodRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 3001, photosPeriodRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2001, 2000, photosPeriodRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 2001, emptyCollection, ConstraintViolationException.class
			}, {
				"brotherhood1", "titlePeriodRecord", "descriptionPeriodRecord", 2000, 2001, stringsNoUrl, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editPeriodRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: eliminar un "PeriodRecord"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar de un "PeriodRecord" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de eliminación de un "PeriodRecord" con una autoridad no permitida
	 */
	@Test
	public void driverDeletePeriodRecord() {
		final Object testingData[][] = {
			{
				"brotherhood1", "periodRecord1", null
			}, {
				"chapter1", "periodRecord1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deletePeriodRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void createPeriodRecordTemplate(final String username, final String title, final String description, final Integer startYear, final Integer endYear, final Collection<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		PeriodRecord periodRecord;

		super.startTransaction();

		try {
			super.authenticate(username);
			periodRecord = this.periodRecordService.create();
			periodRecord.setTitle(title);
			periodRecord.setDescription(description);
			periodRecord.setStartYear(startYear);
			periodRecord.setEndYear(endYear);
			periodRecord.setPhotos(photos);
			this.periodRecordService.save(periodRecord);
			this.periodRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editPeriodRecordTemplate(final String username, final String title, final String description, final Integer startYear, final Integer endYear, final Collection<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		PeriodRecord periodRecord;

		super.startTransaction();

		try {
			super.authenticate(username);
			periodRecord = this.periodRecordService.findPeriodRecordBrotherhoodLogged(super.getEntityId("periodRecord1"));
			periodRecord.setTitle(title);
			periodRecord.setDescription(description);
			periodRecord.setStartYear(startYear);
			periodRecord.setEndYear(endYear);
			periodRecord.setPhotos(photos);
			this.periodRecordService.save(periodRecord);
			this.periodRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deletePeriodRecordTemplate(final String username, final String periodRecord, final Class<?> expected) {
		Class<?> caught = null;
		PeriodRecord periodRecordEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			periodRecordEntity = this.periodRecordService.findPeriodRecordBrotherhoodLogged(super.getEntityId(periodRecord));
			this.periodRecordService.delete(periodRecordEntity);
			this.periodRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	// Auxiliar methods ------------------------------------------------------

	private Collection<String> photosPeriodRecord() {
		final Collection<String> photosPeriodRecord = new HashSet<>();
		photosPeriodRecord.add("http://www.test1.com");
		photosPeriodRecord.add("http://www.test2.com");
		photosPeriodRecord.add("http://www.test3.com");
		photosPeriodRecord.add("http://www.test4.com");
		photosPeriodRecord.add("http://www.test5.com");

		return photosPeriodRecord;
	}
}
