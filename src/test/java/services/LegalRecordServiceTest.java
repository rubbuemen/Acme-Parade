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
import domain.LegalRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class LegalRecordServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private LegalRecordService	legalRecordService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: crear un "LegalRecord"
	 *         Tests positivos: 1
	 *         *** 1. Crear de un "LegalRecord" correctamente
	 *         Tests negativos: 6
	 *         *** 1. Intento de creación de un "LegalRecord" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "LegalRecord" con título vacío
	 *         *** 3. Intento de creación de un "LegalRecord" con descripción vacía
	 *         *** 4. Intento de creación de un "LegalRecord" con nombre legal vacío
	 *         *** 5. Intento de creación de un "LegalRecord" con número de IVA vacío
	 *         *** 6. Intento de creación de un "LegalRecord" con leyes aplicables vacía
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateLegalRecord() {
		final Collection<String> applicableLawsLegalRecord = this.applicableLawsLegalRecord();
		final Collection<String> emptyCollection = new HashSet<>();

		final Object testingData[][] = {
			{
				"brotherhood1", "titleLegalRecord", "descriptionLegalRecord", "legalNameLegalRecord", "VATNumberLegalRecord", applicableLawsLegalRecord, null
			}, {
				"chapter1", "titleLegalRecord", "descriptionLegalRecord", "legalNameLegalRecord", "VATNumberLegalRecord", applicableLawsLegalRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionLegalRecord", "legalNameLegalRecord", "VATNumberLegalRecord", applicableLawsLegalRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLegalRecord", "", "legalNameLegalRecord", "VATNumberLegalRecord", applicableLawsLegalRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLegalRecord", "descriptionLegalRecord", "", "VATNumberLegalRecord", applicableLawsLegalRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLegalRecord", "descriptionLegalRecord", "legalNameLegalRecord", "", applicableLawsLegalRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLegalRecord", "descriptionLegalRecord", "legalNameLegalRecord", "VATNumberLegalRecord", emptyCollection, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createLegalRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: editar un "LegalRecord"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "LegalRecord" correctamente
	 *         Tests negativos: 6
	 *         *** 1. Intento de edición de un "LegalRecord" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "LegalRecord" con título vacío
	 *         *** 3. Intento de edición de un "LegalRecord" con descripción vacía
	 *         *** 4. Intento de edición de un "LegalRecord" con nombre legal vacío
	 *         *** 5. Intento de edición de un "LegalRecord" con número de IVA vacío
	 *         *** 6. Intento de edición de un "LegalRecord" con leyes aplicables vacía
	 *         Analisis de cobertura de sentencias: 98,96% 96/97 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditLegalRecord() {
		final Collection<String> applicableLawsLegalRecord = this.applicableLawsLegalRecord();
		final Collection<String> emptyCollection = new HashSet<>();

		final Object testingData[][] = {
			{
				"brotherhood1", "titleLegalRecord", "descriptionLegalRecord", "legalNameLegalRecord", "VATNumberLegalRecord", applicableLawsLegalRecord, null
			}, {
				"chapter1", "titleLegalRecord", "descriptionLegalRecord", "legalNameLegalRecord", "VATNumberLegalRecord", applicableLawsLegalRecord, IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionLegalRecord", "legalNameLegalRecord", "VATNumberLegalRecord", applicableLawsLegalRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLegalRecord", "", "legalNameLegalRecord", "VATNumberLegalRecord", applicableLawsLegalRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLegalRecord", "descriptionLegalRecord", "", "VATNumberLegalRecord", applicableLawsLegalRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLegalRecord", "descriptionLegalRecord", "legalNameLegalRecord", "", applicableLawsLegalRecord, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleLegalRecord", "descriptionLegalRecord", "legalNameLegalRecord", "VATNumberLegalRecord", emptyCollection, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editLegalRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 3.1 (Acme-Parade)
	 *         Caso de uso: eliminar un "LegalRecord"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "LegalRecord" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de eliminación de un "LegalRecord" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 98% 48/49 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteLegalRecord() {
		final Object testingData[][] = {
			{
				"brotherhood1", "legalRecord1", null
			}, {
				"chapter1", "legalRecord1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteLegalRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void createLegalRecordTemplate(final String username, final String title, final String description, final String legalName, final String VATNumber, final Collection<String> applicableLaws, final Class<?> expected) {
		Class<?> caught = null;
		LegalRecord legalRecord;

		super.startTransaction();

		try {
			super.authenticate(username);
			legalRecord = this.legalRecordService.create();
			legalRecord.setTitle(title);
			legalRecord.setDescription(description);
			legalRecord.setLegalName(legalName);
			legalRecord.setVATNumber(VATNumber);
			legalRecord.setApplicableLaws(applicableLaws);
			this.legalRecordService.save(legalRecord);
			this.legalRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editLegalRecordTemplate(final String username, final String title, final String description, final String legalName, final String VATNumber, final Collection<String> applicableLaws, final Class<?> expected) {
		Class<?> caught = null;
		LegalRecord legalRecord;

		super.startTransaction();

		try {
			super.authenticate(username);
			legalRecord = this.legalRecordService.findLegalRecordBrotherhoodLogged(super.getEntityId("legalRecord1"));
			legalRecord.setTitle(title);
			legalRecord.setDescription(description);
			legalRecord.setLegalName(legalName);
			legalRecord.setVATNumber(VATNumber);
			legalRecord.setApplicableLaws(applicableLaws);
			this.legalRecordService.save(legalRecord);
			this.legalRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteLegalRecordTemplate(final String username, final String legalRecord, final Class<?> expected) {
		Class<?> caught = null;
		LegalRecord legalRecordEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			legalRecordEntity = this.legalRecordService.findLegalRecordBrotherhoodLogged(super.getEntityId(legalRecord));
			this.legalRecordService.delete(legalRecordEntity);
			this.legalRecordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	// Auxiliar methods ------------------------------------------------------

	private Collection<String> applicableLawsLegalRecord() {
		final Collection<String> applicableLawsLegalRecord = new HashSet<>();
		applicableLawsLegalRecord.add("test1");
		applicableLawsLegalRecord.add("test2");
		applicableLawsLegalRecord.add("test3");
		applicableLawsLegalRecord.add("test4");
		applicableLawsLegalRecord.add("test5");

		return applicableLawsLegalRecord;
	}
}
