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
import domain.Brotherhood;
import domain.LinkRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class LinkRecordServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private LinkRecordService	linkRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: crear un "LinkRecord"
	 *         Tests positivos: 1
	 *         *** 1. Crear de un "LinkRecord" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de creación de un "LinkRecord" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "LinkRecord" con título vacío
	 *         *** 3. Intento de creación de un "LinkRecord" con descripción vacía
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateLinkRecord() {

		final Object testingData[][] = {
			{
				"brotherhood1", "titleLinkRecord", "descriptionLinkRecord", "brotherhood2", null
			}, {
				"chapter1", "titleLinkRecord", "descriptionLinkRecord", "brotherhood2", IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionLinkRecord", "brotherhood2", ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLinkRecord", "", "brotherhood2", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.createLinkRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: editar un "LinkRecord"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "LinkRecord" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de edición de un "LinkRecord" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "LinkRecord" con título vacío
	 *         *** 3. Intento de edición de un "LinkRecord" con descripción vacía
	 *         Analisis de cobertura de sentencias: 98,96% 96/97 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditLinkRecord() {

		final Object testingData[][] = {
			{
				"brotherhood1", "titleLinkRecord", "descriptionLinkRecord", "brotherhood2", null
			}, {
				"chapter1", "titleLinkRecord", "descriptionLinkRecord", "brotherhood2", IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionLinkRecord", "brotherhood2", ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLinkRecord", "", "brotherhood2", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.editLinkRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: eliminar un "LinkRecord"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "LinkRecord" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de eliminación de un "LinkRecord" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 98% 48/49 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteLinkRecord() {
		final Object testingData[][] = {
			{
				"brotherhood1", "linkRecord1", null
			}, {
				"chapter1", "linkRecord1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteLinkRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void createLinkRecordTemplate(final String username, final String title, final String description, final String brotherhood, final Class<?> expected) {
		Class<?> caught = null;
		LinkRecord linkRecord;
		Brotherhood brotherhoodEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			linkRecord = this.linkRecordService.create();
			brotherhoodEntity = this.brotherhoodService.findOne(super.getEntityId(brotherhood));
			linkRecord.setTitle(title);
			linkRecord.setDescription(description);
			linkRecord.setBrotherhood(brotherhoodEntity);
			this.linkRecordService.save(linkRecord);
			this.linkRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editLinkRecordTemplate(final String username, final String title, final String description, final String brotherhood, final Class<?> expected) {
		Class<?> caught = null;
		LinkRecord linkRecord;
		Brotherhood brotherhoodEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			linkRecord = this.linkRecordService.findLinkRecordBrotherhoodLogged(super.getEntityId("linkRecord1"));
			brotherhoodEntity = this.brotherhoodService.findOne(super.getEntityId(brotherhood));
			linkRecord.setTitle(title);
			linkRecord.setDescription(description);
			linkRecord.setBrotherhood(brotherhoodEntity);
			this.linkRecordService.save(linkRecord);
			this.linkRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteLinkRecordTemplate(final String username, final String linkRecord, final Class<?> expected) {
		Class<?> caught = null;
		LinkRecord linkRecordEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			linkRecordEntity = this.linkRecordService.findLinkRecordBrotherhoodLogged(super.getEntityId(linkRecord));
			this.linkRecordService.delete(linkRecordEntity);
			this.linkRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
