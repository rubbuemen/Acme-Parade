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
import domain.Proclaim;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProclaimServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private ProclaimService	proclaimService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1 (Acme-Parade)
	 *         Caso de uso: crear un "Proclaim"
	 *         Tests positivos: 1
	 *         *** 1. Crear de un "Proclaim" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de creación de un "Proclaim" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Proclaim" con texto vacío
	 *         *** 3. Intento de creación de un "Proclaim" con tamaño de texto mayor a 250 caracteres
	 *         Analisis de cobertura de sentencias: 98,52% 67/68 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateProclaim() {
		final Object testingData[][] = {
			{
				"chapter1", "testText", null
			},
			{
				"brotherhood1", "testText", IllegalArgumentException.class
			},
			{
				"chapter1", "", ConstraintViolationException.class
			},
			{
				"chapter1",
				"testTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttestTexttes",
				ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.createProclaimTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void createProclaimTemplate(final String username, final String text, final Class<?> expected) {
		Class<?> caught = null;
		Proclaim proclaim;

		super.startTransaction();

		try {
			super.authenticate(username);
			proclaim = this.proclaimService.create();
			proclaim.setText(text);
			this.proclaimService.save(proclaim);
			this.proclaimService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
