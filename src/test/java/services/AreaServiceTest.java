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
public class AreaServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private AreaService		areaService;

	@Autowired
	private ChapterService	chapterService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 8.1 (Acme-Parade)
	 *         Caso de uso: autoasignarse un "Area"
	 *         Tests positivos: 1
	 *         *** 1. Autoasignarse un "Area" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de autoasignación de un "Area" con una autoridad no permitida
	 *         *** 2. Intento de autoasignación de un "Area" teniendo ya una asignada
	 *         *** 3. Intento de autoasignación de un "Area" ya asignada a otro "Chapter"
	 */
	@Test
	public void driverSelfAssignArea() {

		final Object testingData[][] = {
			{
				"chapter5", "area6", null
			}, {
				"brotherhood1", "area6", IllegalArgumentException.class
			}, {
				"chapter1", "area6", IllegalArgumentException.class
			}, {
				"chapter5", "area1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.selfAssignAreaTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void selfAssignAreaTemplate(final String username, final String area, final Class<?> expected) {
		Class<?> caught = null;
		Area areaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			areaEntity = this.areaService.findOne(super.getEntityId(area));
			this.chapterService.selfAssign(areaEntity.getId());
			this.areaService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
