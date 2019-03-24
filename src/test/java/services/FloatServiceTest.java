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
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Float;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FloatServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private FloatService	floatService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.1 (Acme-Madrugá)
	 *         Caso de uso: listar "Floats"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Floats" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Floats" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListFloatsBrotherhood() {
		final Object testingData[][] = {
			{
				"brotherhood1", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listFloatsBrotherhoodTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.1 (Acme-Madrugá)
	 *         Caso de uso: crear un "Float"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Float" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de creación de un "Float" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Float" con título vacío
	 *         *** 3. Intento de creación de un "Float" con descripción vacía
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateFloat() {
		final Object testingData[][] = {
			{
				"brotherhood1", "titleTest", "descriptionTest", null
			}, {
				"member1", "titleTest", "descriptionTest", IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionTest", ConstraintViolationException.class
			}, {
				"brotherhood1", "titleTest", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createFloatTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.1 (Acme-Madrugá)
	 *         Caso de uso: editar un "Float"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Float" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de edición de un "Float" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Float" que no es del "Brotherhood" logeado
	 *         *** 3. Intento de edición de un "Float" con título vacío
	 *         *** 4. Intento de edición de un "Float" con descripción vacía
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditFloat() {
		final Object testingData[][] = {
			{
				"brotherhood1", "float1", "titleTest", "descriptionTest", null
			}, {
				"member1", "float1", "titleTest", "descriptionTest", IllegalArgumentException.class
			}, {
				"brotherhood2", "float1", "titleTest", "descriptionTest", IllegalArgumentException.class
			}, {
				"brotherhood1", "float1", "", "descriptionTest", ConstraintViolationException.class
			}, {
				"brotherhood1", "float1", "titleTest", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editFloatTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.1 (Acme-Madrugá)
	 *         Caso de uso: eliminar un "Float"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Float" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "Float" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Float" que no es del "Brotherhood" logeado
	 *         *** 3. Intento de eliminación de un "Float" que haría que se quedara un "Parade" sin "Floats"
	 *         Analisis de cobertura de sentencias: 98% 48/49 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteFloat() {
		final Object testingData[][] = {
			{
				"brotherhood3", "float5", null
			}, {
				"member1", "float5", IllegalArgumentException.class
			}, {
				"brotherhood2", "float5", IllegalArgumentException.class
			}, {
				"brotherhood1", "float1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteFloatTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listFloatsBrotherhoodTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Float> floats;

		super.startTransaction();

		try {
			super.authenticate(username);
			floats = this.floatService.findFloatsByBrotherhoodLogged();
			Assert.notNull(floats);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createFloatTemplate(final String username, final String title, final String description, final Class<?> expected) {
		Class<?> caught = null;
		Float floatEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			floatEntity = this.floatService.create();
			floatEntity.setTitle(title);
			floatEntity.setDescription(description);
			floatEntity = this.floatService.save(floatEntity);
			this.floatService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editFloatTemplate(final String username, final String floatt, final String title, final String description, final Class<?> expected) {
		Class<?> caught = null;
		Float floatEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			floatEntity = this.floatService.findFloatBrotherhoodLogged(super.getEntityId(floatt));
			floatEntity.setTitle(title);
			floatEntity.setDescription(description);
			floatEntity = this.floatService.save(floatEntity);
			this.floatService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteFloatTemplate(final String username, final String floatt, final Class<?> expected) {
		Class<?> caught = null;
		Float floatEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			floatEntity = this.floatService.findFloatBrotherhoodLogged(super.getEntityId(floatt));
			this.floatService.delete(floatEntity);
			this.floatService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
