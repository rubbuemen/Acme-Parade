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

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@PersistenceContext
	EntityManager						entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 18.1 (Acme-Parade)
	 *         Caso de uso: desactivar "Sponsorships" con tarjeta de crédito caducada lanzando un proceso
	 *         Tests positivos: 1
	 *         *** 1. Desactivar "Sponsorships" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de desactivación de "Sponsorships" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeactivatesSponsorshipsProcess() {
		final Object testingData[][] = {
			{
				"admin", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deactivesSponsorshipsProcessTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Template methods ------------------------------------------------------

	protected void deactivesSponsorshipsProcessTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.systemConfigurationService.deactivatesSponsorships();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
