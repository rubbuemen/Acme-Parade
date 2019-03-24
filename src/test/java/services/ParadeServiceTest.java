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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Parade;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ParadeServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private ParadeService	paradeService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 8.2 (Acme-Parade)
	 *         Caso de uso: listar los "Parades" publicadas de un "Brotherhood"
	 *         Tests positivos: 1
	 *         *** 1. Listar los "Parades" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar los "Parades" con una autoridad no permitida
	 *         *** 2. Intento de listar los "Parades" sin tener relación entre e "Brotherhood" y el "Chapter"
	 *         Analisis de cobertura de sentencias: 97,4% 37/38 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListParadesByBrotherhoodChapter() {

		final Object testingData[][] = {
			{
				"chapter1", "brotherhood1", null
			}, {
				"member1", "brotherhood1", IllegalArgumentException.class
			}, {
				"chapter4", "brotherhood1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listParadesByBrotherhoodChapterTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 8.2 (Acme-Parade)
	 *         Caso de uso: aprobar un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Aceptar un "Parade" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de aceptar un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de aceptar un "Parade" sin tener permisos para ello
	 *         *** 3. Intento de aceptar un "Parade" que no tiene el estado "submitted"
	 *         Analisis de cobertura de sentencias: 60,71% 170/280 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverAcceptParadesChapter() {

		final Object testingData[][] = {
			{
				"chapter1", "parade8", null
			}, {
				"member1", "parade8", IllegalArgumentException.class
			}, {
				"chapter5", "parade8", IllegalArgumentException.class
			}, {
				"chapter1", "parade1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptParadeChapterTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 8.2 (Acme-Parade)
	 *         Caso de uso: rechazar un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Rechazar un "Parade" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de rechazar un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de rechazar un "Parade" sin tener permisos para ello
	 *         *** 3. Intento de rechazar un "Parade" que no tiene el estado "submitted"
	 *         *** 4. Intento de rechazar un "Parade" sin dejar una razón de por qué
	 *         Analisis de cobertura de sentencias: 56,3% 136/243 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRejectParadesChapter() {

		final Object testingData[][] = {
			{
				"chapter1", "parade8", "Reject reason test", null
			}, {
				"member1", "parade8", "Reject reason test", IllegalArgumentException.class
			}, {
				"chapter5", "parade8", "Reject reason test", IllegalArgumentException.class
			}, {
				"chapter1", "parade1", "Reject reason test", IllegalArgumentException.class
			}, {
				"chapter1", "parade8", "", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.rejectParadeChapterTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.1 (Acme-Parade)
	 *         Caso de uso: listar los "Parades" del "Brotherhood" logeado
	 *         Tests positivos: 1
	 *         *** 1. Listar los "Parades" del "Brotherhood" logeado correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar los "Parades" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 97,4% 37/38 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListParadesByBrotherhood() {

		final Object testingData[][] = {
			{
				"brotherhood1", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listParadesByBrotherhoodTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2 (Acme-Parade)
	 *         Caso de uso: copiar un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Copiar un "Parade" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de copiar un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de copiar un "Parade" que no le pertenece
	 *         Analisis de cobertura de sentencias: 97,4% 37/38 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCopyParade() {

		final Object testingData[][] = {
			{
				"brotherhood1", "parade1", null
			}, {
				"member1", "parade1", IllegalArgumentException.class
			}, {
				"brotherhood1", "parade3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.copyParadeTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.1 (Acme-Parade)
	 *         Caso de uso: navegar a "Parades" de un "Brotherhood" sin estar logeado
	 *         Tests positivos: 1
	 *         *** 1. Navegar a "Parades"de un "Brotherhood" sin estar logeado
	 *         Tests negativos: 1
	 *         *** 1. Intento de navegar a "Parades" de un "Brotherhood" inexistente
	 *         Analisis de cobertura de sentencias: 92,3% 12/13 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverNavegateParadesBrotherhood() {

		final Object testingData[][] = {
			{
				"brotherhood1", null
			}, {
				"brotherhood2423", AssertionError.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.navegateParadesBrotherhoodTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Template methods ------------------------------------------------------

	protected void listParadesByBrotherhoodChapterTemplate(final String username, final String brotherhood, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Parade> parades;

		super.startTransaction();

		try {
			super.authenticate(username);
			parades = this.paradeService.findParadesFinalModeOrderByStatusByBrotherhoodId(super.getEntityId(brotherhood));
			Assert.notNull(parades);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void acceptParadeChapterTemplate(final String username, final String parade, final Class<?> expected) {
		Class<?> caught = null;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			paradeEntity = this.paradeService.findParadeSubmittedChapterLogged(super.getEntityId(parade));
			this.paradeService.acceptParade(paradeEntity);
			this.paradeService.save(paradeEntity);
			this.paradeService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void rejectParadeChapterTemplate(final String username, final String parade, final String rejectReason, final Class<?> expected) {
		Class<?> caught = null;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			paradeEntity = this.paradeService.findParadeSubmittedChapterLogged(super.getEntityId(parade));
			paradeEntity.setStatus("REJECTED");
			paradeEntity.setRejectReason(rejectReason);
			this.paradeService.save(paradeEntity);
			this.paradeService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void listParadesByBrotherhoodTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Parade> parades;

		super.startTransaction();

		try {
			super.authenticate(username);
			parades = this.paradeService.findParadesByBrotherhoodLogged();
			Assert.notNull(parades);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void copyParadeTemplate(final String username, final String parade, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.paradeService.copyParade(super.getEntityId(parade));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void navegateParadesBrotherhoodTemplate(final String brotherhood, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Parade> parades;

		super.startTransaction();

		try {
			parades = this.paradeService.findParadesAcceptedByBrotherhoodId(super.getEntityId(brotherhood));
			Assert.notNull(parades);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}
}
