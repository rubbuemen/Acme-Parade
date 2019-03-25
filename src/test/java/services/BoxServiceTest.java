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
import domain.Box;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BoxServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private BoxService	boxService;

	@PersistenceContext
	EntityManager		entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.2 (Acme-Madrugá)
	 *         Caso de uso: listar "Boxes"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Boxes" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Boxes" sin estar logeado
	 *         Analisis de cobertura de sentencias: 100% 14/14 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListBoxes() {
		final Object testingData[][] = {
			{
				"brotherhood1", null
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listBoxesTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.2 (Acme-Madrugá)
	 *         Caso de uso: crear un "Box"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Box" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de creación de un "Box" sin estar logeado
	 *         *** 2. Intento de creación de un "Box" con nombre vacío
	 *         Analisis de cobertura de sentencias: 87,3% 110/126 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateBox() {
		final Object testingData[][] = {
			{
				"admin", "nameTest", null
			}, {
				null, "nameTest", IllegalArgumentException.class
			}, {
				"admin", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createBoxTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.2 (Acme-Madrugá)
	 *         Caso de uso: editar un "Box"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Box" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de edición de un "Box" sin estar logeado
	 *         *** 2. Intento de edición de un "Box" que no es del "Actor" logeado
	 *         *** 3. Intento de edición de un "Box" con nombre vacío
	 *         *** 3. Intento de edición de un "Box" que es del sistema
	 *         Analisis de cobertura de sentencias: 83,3% 80/96 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditBox() {
		final Object testingData[][] = {
			{
				"admin", "box6", "nameTest", null
			}, {
				null, "box6", "nameTest", IllegalArgumentException.class
			}, {
				"brotherhood1", "box6", "nameTest", IllegalArgumentException.class
			}, {
				"admin", "box6", "", ConstraintViolationException.class
			}, {
				"admin", "box1", "nameTest", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editBoxTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.2 (Acme-Madrugá)
	 *         Caso de uso: eliminar un "Box"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Box" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "Box" sin estar logeado
	 *         *** 2. Intento de eliminación de un "Box" que no es del "Actor" logeado
	 *         *** 3. Intento de eliminación de un "Box" que es del sistema
	 *         Analisis de cobertura de sentencias: 99,1% 110/111 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteBox() {
		final Object testingData[][] = {
			{
				"admin", "box6", null
			}, {
				null, "box6", IllegalArgumentException.class
			}, {
				"brotherhood1", "box6", IllegalArgumentException.class
			}, {
				"admin", "box1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteBoxTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listBoxesTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Box> boxs;

		super.startTransaction();

		try {
			super.authenticate(username);
			boxs = this.boxService.findRootBoxesByActorLogged();
			Assert.notNull(boxs);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createBoxTemplate(final String username, final String name, final Class<?> expected) {
		Class<?> caught = null;
		Box boxEntity;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			boxEntity = this.boxService.create();
			boxEntity.setName(name);
			this.boxService.save(boxEntity, null);
			this.boxService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editBoxTemplate(final String username, final String box, final String name, final Class<?> expected) {
		Class<?> caught = null;
		Box boxEntity;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			boxEntity = this.boxService.findBoxActorLogged(super.getEntityId(box));
			boxEntity.setName(name);
			this.boxService.save(boxEntity, null);
			this.boxService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
	protected void deleteBoxTemplate(final String username, final String box, final Class<?> expected) {
		Class<?> caught = null;
		Box boxEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			boxEntity = this.boxService.findBoxActorLogged(super.getEntityId(box));
			this.boxService.delete(boxEntity);
			this.boxService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
