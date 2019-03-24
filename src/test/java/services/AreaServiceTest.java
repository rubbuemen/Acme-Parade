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
import org.springframework.util.Assert;

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
	private AreaService			areaService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ChapterService		chapterService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 20.1 (Acme-Madrugá)
	 *         Caso de uso: seleccionar un "Area" como "Brotherhood"
	 *         Tests positivos: 1
	 *         *** 1. Seleccionar un "Area" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de selección de un "Area" con una autoridad no permitida
	 *         *** 2. Intento de selección de un "Area" teniendo ya una asignada
	 *         Analisis de cobertura de sentencias: 97,8% 45/46 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverSelectAreaBrotherhood() {

		final Object testingData[][] = {
			{
				"brotherhood8", "area1", null
			}, {
				"member1", "area1", IllegalArgumentException.class
			}, {
				"brotherhood1", "area4", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.selectAreaBrotherhoodTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 22.1 (Acme-Madrugá)
	 *         Caso de uso: listar "Areas"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Areas" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Areas" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListArea() {
		final Object testingData[][] = {
			{
				"admin", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listAreaTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 22.1 (Acme-Madrugá)
	 *         Caso de uso: crear un "Area"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Area" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de creación de un "Area" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Area" con nombre vacío
	 *         *** 3. Intento de creación de un "Area" con pictures vacío
	 *         *** 4. Intento de creación de un "Area" con pictures que no son URLs
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateArea() {
		final Collection<String> picturesArea = this.picturesArea();
		final Collection<String> emptyCollection = new HashSet<>();
		final Collection<String> stringsNoUrl = new HashSet<>();
		stringsNoUrl.add("test");
		final Object testingData[][] = {
			{
				"admin", "nameTest", picturesArea, null
			}, {
				"member1", "nameTest", picturesArea, IllegalArgumentException.class
			}, {
				"admin", "", picturesArea, ConstraintViolationException.class
			}, {
				"admin", "nameTest", emptyCollection, ConstraintViolationException.class
			}, {
				"admin", "nameTest", stringsNoUrl, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createAreaTemplate((String) testingData[i][0], (String) testingData[i][1], (Collection<String>) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 22.1 (Acme-Madrugá)
	 *         Caso de uso: editar un "Area"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Area" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de edición de un "Area" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Area" con nombre vacío
	 *         *** 3. Intento de edición de un "Area" con pictures vacío
	 *         *** 4. Intento de edición de un "Area" con pictures que no son URLs
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditArea() {
		final Collection<String> picturesArea = this.picturesArea();
		final Collection<String> emptyCollection = new HashSet<>();
		final Collection<String> stringsNoUrl = new HashSet<>();
		stringsNoUrl.add("test");
		final Object testingData[][] = {
			{
				"admin", "area1", "nameTest", picturesArea, null
			}, {
				"member1", "area1", "nameTest", picturesArea, IllegalArgumentException.class
			}, {
				"admin", "area1", "", picturesArea, ConstraintViolationException.class
			}, {
				"admin", "area1", "nameTest", emptyCollection, ConstraintViolationException.class
			}, {
				"admin", "area1", "nameTest", stringsNoUrl, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editAreaTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 22.1 (Acme-Madrugá)
	 *         Caso de uso: eliminar un "Area"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Area" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "Area" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Area" que ya está seleccionada por un "Brotherhood"
	 *         Analisis de cobertura de sentencias: 98% 48/49 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteArea() {
		final Object testingData[][] = {
			{
				"admin", "area7", null
			}, {
				"member1", "area7", IllegalArgumentException.class
			}, {
				"admin", "area1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteAreaTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 8.1 (Acme-Parade)
	 *         Caso de uso: autoasignarse un "Area" como "Chapter"
	 *         Tests positivos: 1
	 *         *** 1. Autoasignarse un "Area" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de autoasignación de un "Area" con una autoridad no permitida
	 *         *** 2. Intento de autoasignación de un "Area" teniendo ya una asignada
	 *         *** 3. Intento de autoasignación de un "Area" ya asignada a otro "Chapter"
	 *         Analisis de cobertura de sentencias: 97,8% 45/46 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverSelfAssignAreaChapter() {

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
			this.selfAssignAreaChapterTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.1 (Acme-Parade)
	 *         Caso de uso: navegar al "Area" que coordina un "Chapter" sin estar logeado
	 *         Tests positivos: 1
	 *         *** 1. Navegar al "Area" que coordina un "Chapter" sin estar logeado
	 *         Tests negativos: 1
	 *         *** 1. Intento de navegar al "Area" que coordina un "Chapter" que no tiene área
	 *         Analisis de cobertura de sentencias: 92,3% 12/13 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverNavegateAreaChapter() {

		final Object testingData[][] = {
			{
				"chapter1", null
			}, {
				"chapter5", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.navegateAreaChapterTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Template methods ------------------------------------------------------

	protected void selectAreaBrotherhoodTemplate(final String username, final String area, final Class<?> expected) {
		Class<?> caught = null;
		Area areaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			areaEntity = this.areaService.findOne(super.getEntityId(area));
			this.brotherhoodService.selectArea(areaEntity.getId());
			this.areaService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void listAreaTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Area> areas;

		super.startTransaction();

		try {
			super.authenticate(username);
			areas = this.areaService.findAll();
			this.areaService.findAreasBrotherhoodUsed();
			Assert.notNull(areas);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createAreaTemplate(final String username, final String name, final Collection<String> pictures, final Class<?> expected) {
		Class<?> caught = null;
		Area areaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			areaEntity = this.areaService.create();
			areaEntity.setName(name);
			areaEntity.setPictures(pictures);
			this.areaService.save(areaEntity);
			this.areaService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editAreaTemplate(final String username, final String area, final String name, final Collection<String> pictures, final Class<?> expected) {
		Class<?> caught = null;
		Area areaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			areaEntity = this.areaService.findOne(super.getEntityId(area));
			areaEntity.setName(name);
			areaEntity.setPictures(pictures);
			areaEntity = this.areaService.save(areaEntity);
			this.areaService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteAreaTemplate(final String username, final String area, final Class<?> expected) {
		Class<?> caught = null;
		Area areaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			areaEntity = this.areaService.findOne(super.getEntityId(area));
			this.areaService.delete(areaEntity);
			this.areaService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void selfAssignAreaChapterTemplate(final String username, final String area, final Class<?> expected) {
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

	protected void navegateAreaChapterTemplate(final String chapter, final Class<?> expected) {
		Class<?> caught = null;
		Area area;

		super.startTransaction();

		try {
			area = this.areaService.findAreaByChapterId(super.getEntityId(chapter));
			Assert.notNull(area);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	// Auxiliar methods ------------------------------------------------------

	private Collection<String> picturesArea() {
		final Collection<String> picturesArea = new HashSet<>();
		picturesArea.add("http://www.test1.com");
		picturesArea.add("http://www.test2.com");
		picturesArea.add("http://www.test3.com");
		picturesArea.add("http://www.test4.com");
		picturesArea.add("http://www.test5.com");

		return picturesArea;
	}
}
