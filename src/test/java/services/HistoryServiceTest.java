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
import domain.History;
import domain.InceptionRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class HistoryServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private HistoryService			historyService;

	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@PersistenceContext
	EntityManager					entityManager;


	/**
	 * @author Rubén Bueno
	 *         Caso de uso: crear un "History"
	 *         Tests positivos: 1
	 *         *** 1. Creación de un "History" correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de creación de un "History" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "History" con título de "InceptionRecord Record" vacío
	 *         *** 3. Intento de creación de un "History" con descripción de "InceptionRecord Record" vacío
	 *         *** 4. Intento de creación de un "History" con fotos de "InceptionRecord Record" vacía
	 *         *** 5. Intento de creación de un "History" con fotos de "InceptionRecord Record" que no son URL
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateHistory() {
		final Collection<String> photosInceptionRecord = this.photosInceptionRecord();
		final Collection<String> emptyCollection = new HashSet<>();
		final Collection<String> stringsNoUrl = new HashSet<>();
		stringsNoUrl.add("test");

		final Object testingData[][] = {
			{
				"brotherhood1", "titleInceptionRecord", "descriptionInceptionRecord", photosInceptionRecord, null
			}, {
				"chapter1", "titleInceptionRecord", "descriptionInceptionRecord", photosInceptionRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionInceptionRecord", photosInceptionRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleInceptionRecord", "", photosInceptionRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleInceptionRecord", "descriptionInceptionRecord", emptyCollection, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleInceptionRecord", "descriptionInceptionRecord", stringsNoUrl, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createHistoryTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Caso de uso: editar un "History"
	 *         Tests positivos: 1
	 *         *** 1. Creación de un "History" correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de creación de un "History" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "History" con título de "InceptionRecord Record" vacío
	 *         *** 3. Intento de creación de un "History" con descripción de "InceptionRecord Record" vacío
	 *         *** 4. Intento de creación de un "History" con fotos de "InceptionRecord Record" vacía
	 *         *** 5. Intento de creación de un "History" con fotos de "InceptionRecord Record" que no son URL
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditHistory() {
		final Collection<String> photosInceptionRecord = this.photosInceptionRecord();
		final Collection<String> emptyCollection = new HashSet<>();
		final Collection<String> stringsNoUrl = new HashSet<>();
		stringsNoUrl.add("test");

		final Object testingData[][] = {
			{
				"brotherhood1", "titleInceptionRecord", "descriptionInceptionRecord", photosInceptionRecord, null
			}, {
				"chapter1", "titleInceptionRecord", "descriptionInceptionRecord", photosInceptionRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionInceptionRecord", photosInceptionRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleInceptionRecord", "", photosInceptionRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleInceptionRecord", "descriptionInceptionRecord", emptyCollection, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleInceptionRecord", "descriptionInceptionRecord", stringsNoUrl, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editHistoryInceptionRecordRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void createHistoryTemplate(final String username, final String title, final String description, final Collection<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		History history;
		InceptionRecord inceptionRecord;

		try {
			super.authenticate(username);
			history = this.historyService.create();
			inceptionRecord = history.getInceptionRecord();
			inceptionRecord.setTitle(title);
			inceptionRecord.setDescription(description);
			inceptionRecord.setPhotos(photos);
			inceptionRecord = this.inceptionRecordService.save(inceptionRecord);
			this.inceptionRecordService.flush();
			history = this.historyService.save(history);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		this.entityManager.clear();
		super.unauthenticate();
	}

	protected void editHistoryInceptionRecordRecordTemplate(final String username, final String title, final String description, final Collection<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		History history;

		InceptionRecord inceptionRecord;

		try {
			super.authenticate(username);
			history = this.historyService.findOne(super.getEntityId("history1"));
			inceptionRecord = history.getInceptionRecord();
			inceptionRecord.setTitle(title);
			inceptionRecord.setDescription(description);
			inceptionRecord.setPhotos(photos);
			inceptionRecord.setTitle(title);
			inceptionRecord.setDescription(description);
			inceptionRecord.setPhotos(photos);
			inceptionRecord = this.inceptionRecordService.save(inceptionRecord);
			this.inceptionRecordService.flush();
			this.historyService.save(history);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

	// Auxiliar methods ------------------------------------------------------

	private Collection<String> photosInceptionRecord() {
		final Collection<String> photosInceptionRecord = new HashSet<>();
		photosInceptionRecord.add("http://www.test1.com");
		photosInceptionRecord.add("http://www.test2.com");
		photosInceptionRecord.add("http://www.test3.com");
		photosInceptionRecord.add("http://www.test4.com");
		photosInceptionRecord.add("http://www.test5.com");

		return photosInceptionRecord;
	}
}
