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
import domain.InceptionRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InceptionRecordServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@PersistenceContext
	EntityManager					entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: editar un "InceptionRecord"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "InceptionRecord" correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de edición de un "InceptionRecord" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "InceptionRecord" con título vacío
	 *         *** 3. Intento de edición de un "InceptionRecord" con descripción vacío
	 *         *** 4. Intento de edición de un "InceptionRecord" con fotos vacía
	 *         *** 5. Intento de edición de un "InceptionRecord" con fotos que no son URL
	 *         Analisis de cobertura de sentencias: 98,24% 56/57 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditInceptionRecord() {
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
			this.editInceptionRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	// Template methods ------------------------------------------------------

	protected void editInceptionRecordTemplate(final String username, final String title, final String description, final Collection<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		InceptionRecord inceptionRecord;

		super.startTransaction();

		try {
			super.authenticate(username);
			inceptionRecord = this.inceptionRecordService.findInceptionRecordBrotherhoodLogged(super.getEntityId("inceptionRecord1"));
			inceptionRecord.setTitle(title);
			inceptionRecord.setDescription(description);
			inceptionRecord.setPhotos(photos);
			inceptionRecord = this.inceptionRecordService.save(inceptionRecord);
			this.inceptionRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
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
