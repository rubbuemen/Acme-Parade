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
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Area;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FinderServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private FinderService	finderService;

	@Autowired
	private AreaService		areaService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 21.2 (Acme-Madrugá)
	 *         Caso de uso: usar "Finder"
	 *         Tests positivos: 1
	 *         *** 1. Usar "Finder" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de uso de un "Finder" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 63% 97/154 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverManageFinder() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2018, 9, 30);
		final Date minDateTest = cal.getTime();
		cal.set(2029, 10, 30);
		final Date maxDateTest = cal.getTime();
		final Object testingData[][] = {
			{
				"member1", "keyWordTest", minDateTest, maxDateTest, "area1", null
			}, {
				"brotherhood1", "keyWordTest", minDateTest, maxDateTest, "area1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.manageFinderTemplate((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	// Template methods ------------------------------------------------------

	protected void manageFinderTemplate(final String username, final String keyWord, final Date minDate, final Date maxDate, final String area, final Class<?> expected) {
		Class<?> caught = null;
		Area areaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			areaEntity = this.areaService.findOne(super.getEntityId(area));
			this.finderService.updateCriteria(keyWord, minDate, maxDate, areaEntity);
			this.finderService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
