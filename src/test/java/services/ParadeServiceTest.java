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
import java.util.Collection;
import java.util.Date;
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
import domain.Float;
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

	@Autowired
	private FloatService	floatService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.2 (Acme-Madrugá)
	 *         Caso de uso: listar "Parades" logeado como "Brotherhood"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Parades" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Parades" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListParadesBrotherhood() {
		final Object testingData[][] = {
			{
				"brotherhood1", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listParadesBrotherhoodTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.2 (Acme-Madrugá)
	 *         Caso de uso: crear un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Parade" correctamente
	 *         Tests negativos: 11
	 *         *** 1. Intento de creación de un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Parade" con título vacío
	 *         *** 3. Intento de creación de un "Parade" con descripción vacía
	 *         *** 4. Intento de creación de un "Parade" con momento para organizar nulo
	 *         *** 5. Intento de creación de un "Parade" con momento para organizar pasado
	 *         *** 6. Intento de creación de un "Parade" con máximo de filas nulo
	 *         *** 7. Intento de creación de un "Parade" con máximo de filas menor a 1
	 *         *** 8. Intento de creación de un "Parade" con máximo de columnas nulo
	 *         *** 9. Intento de creación de un "Parade" con máximo de columnas menor a 1
	 *         *** 10. Intento de creación de un "Parade" sin "Floats"
	 *         *** 11. Intento de creación de un "Parade" sin tener la "Brotherhood" ningún "Area" seleccionada aún
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateParade() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2200, 9, 30);
		final Date momentOrganizeDateFuture = cal.getTime();
		cal.set(2000, 9, 30);
		final Date momentOrganizeDatePast = cal.getTime();
		final Collection<Float> floatsTest = this.floatsParadeBrotherhood1();
		final Collection<Float> emptyFloats = new HashSet<>();

		final Object testingData[][] = {
			{
				"brotherhood1", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 50, floatsTest, null
			}, {
				"member1", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 50, floatsTest, IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionTest", momentOrganizeDateFuture, 40, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleTest", "", momentOrganizeDateFuture, 40, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleTest", "descriptionTest", null, 40, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleTest", "descriptionTest", momentOrganizeDatePast, 40, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleTest", "descriptionTest", momentOrganizeDateFuture, null, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleTest", "descriptionTest", momentOrganizeDateFuture, 0, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, null, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 0, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood1", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 50, emptyFloats, ConstraintViolationException.class
			}, {
				"brotherhood8", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 50, floatsTest, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createParadeTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Collection<Float>) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.2 (Acme-Madrugá)
	 *         Caso de uso: editar un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Parade" correctamente
	 *         Tests negativos: 12
	 *         *** 1. Intento de edición de un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Parade" que no es del "Brotherhood" logeado
	 *         *** 3. Intento de edición de un "Parade" con título vacío
	 *         *** 4. Intento de edición de un "Parade" con descripción vacía
	 *         *** 5. Intento de edición de un "Parade" con momento para organizar nulo
	 *         *** 6. Intento de edición de un "Parade" con momento para organizar pasado
	 *         *** 7. Intento de edición de un "Parade" con máximo de filas nulo
	 *         *** 8. Intento de edición de un "Parade" con máximo de filas menor a 1
	 *         *** 9. Intento de edición de un "Parade" con máximo de columnas nulo
	 *         *** 10. Intento de edición de un "Parade" con máximo de columnas menor a 1
	 *         *** 11. Intento de edición de un "Parade" sin "Floats"
	 *         *** 12. Intento de edición de un "Parade" en modo final
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditParade() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2200, 9, 30);
		final Date momentOrganizeDateFuture = cal.getTime();
		cal.set(2000, 9, 30);
		final Date momentOrganizeDatePast = cal.getTime();
		final Collection<Float> floatsTest = this.floatsParadeBrotherhood3();
		final Collection<Float> floatsTest2 = this.floatsParadeBrotherhood1();
		final Collection<Float> emptyFloats = new HashSet<>();

		final Object testingData[][] = {
			{
				"brotherhood3", "parade4", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 50, floatsTest, null
			}, {
				"member1", "parade1", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 50, floatsTest, IllegalArgumentException.class
			}, {
				"brotherhood1", "parade4", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 50, floatsTest2, IllegalArgumentException.class
			}, {
				"brotherhood3", "parade4", "", "descriptionTest", momentOrganizeDateFuture, 40, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood3", "parade4", "titleTest", "", momentOrganizeDateFuture, 40, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood3", "parade4", "titleTest", "descriptionTest", null, 40, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood3", "parade4", "titleTest", "descriptionTest", momentOrganizeDatePast, 40, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood3", "parade4", "titleTest", "descriptionTest", momentOrganizeDateFuture, null, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood3", "parade4", "titleTest", "descriptionTest", momentOrganizeDateFuture, 0, 50, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood3", "parade4", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, null, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood3", "parade4", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 0, floatsTest, ConstraintViolationException.class
			}, {
				"brotherhood3", "parade4", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 50, emptyFloats, ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "titleTest", "descriptionTest", momentOrganizeDateFuture, 40, 50, floatsTest2, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editParadeTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
				(Collection<Float>) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.2 (Acme-Madrugá)
	 *         Caso de uso: eliminar un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Parade" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Parade" que no es del "Brotherhood" logeado
	 *         *** 3. Intento de eliminación de un "Parade" que está en modo final
	 *         Analisis de cobertura de sentencias: 98% 48/49 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteParade() {
		final Object testingData[][] = {
			{
				"brotherhood3", "parade4", null
			}, {
				"member1", "parade4", IllegalArgumentException.class
			}, {
				"brotherhood1", "parade4", IllegalArgumentException.class
			}, {
				"brotherhood1", "parade1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteParadeTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.2 (Acme-Madrugá)
	 *         Caso de uso: cambiar a modo final un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Cambiar a modo final un "Parade" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de cambiar a modo final de un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de cambiar a modo final de un "Parade" que no es del "Brotherhood" logeado
	 *         *** 3. Intento de cambiar a modo final de un "Parade" que ya está en modo final
	 *         Analisis de cobertura de sentencias: 98% 48/49 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverChangeFinalModeParade() {
		final Object testingData[][] = {
			{
				"brotherhood3", "parade4", null
			}, {
				"member1", "parade4", IllegalArgumentException.class
			}, {
				"brotherhood1", "parade4", IllegalArgumentException.class
			}, {
				"brotherhood1", "parade1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.changeFinalModeParadeTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

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

	protected void listParadesBrotherhoodTemplate(final String username, final Class<?> expected) {
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

	protected void createParadeTemplate(final String username, final String title, final String description, final Date momentOrganize, final Integer maxRows, final Integer maxColumns, final Collection<Float> floats, final Class<?> expected) {
		Class<?> caught = null;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			paradeEntity = this.paradeService.create();
			paradeEntity.setTitle(title);
			paradeEntity.setDescription(description);
			paradeEntity.setMomentOrganise(momentOrganize);
			paradeEntity.setMaxRows(maxRows);
			paradeEntity.setMaxColumns(maxColumns);
			paradeEntity.setFloats(floats);
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

	protected void editParadeTemplate(final String username, final String parade, final String title, final String description, final Date momentOrganize, final Integer maxRows, final Integer maxColumns, final Collection<Float> floats,
		final Class<?> expected) {
		Class<?> caught = null;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			paradeEntity = this.paradeService.findParadeBrotherhoodLogged(super.getEntityId(parade));
			paradeEntity.setTitle(title);
			paradeEntity.setDescription(description);
			paradeEntity.setMomentOrganise(momentOrganize);
			paradeEntity.setMaxRows(maxRows);
			paradeEntity.setMaxColumns(maxColumns);
			paradeEntity.setFloats(floats);
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

	protected void deleteParadeTemplate(final String username, final String parade, final Class<?> expected) {
		Class<?> caught = null;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			paradeEntity = this.paradeService.findParadeBrotherhoodLogged(super.getEntityId(parade));
			this.paradeService.delete(paradeEntity);
			this.paradeService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void changeFinalModeParadeTemplate(final String username, final String parade, final Class<?> expected) {
		Class<?> caught = null;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			paradeEntity = this.paradeService.findParadeBrotherhoodLogged(super.getEntityId(parade));
			this.paradeService.changeFinalMode(paradeEntity);
			this.paradeService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

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

	// Auxiliar methods ------------------------------------------------------

	private Collection<Float> floatsParadeBrotherhood1() {
		final Collection<Float> floatsParade = new HashSet<>();
		floatsParade.add(this.floatService.findOne(super.getEntityId("float1")));
		floatsParade.add(this.floatService.findOne(super.getEntityId("float2")));

		return floatsParade;
	}

	private Collection<Float> floatsParadeBrotherhood3() {
		final Collection<Float> floatsParade = new HashSet<>();
		floatsParade.add(this.floatService.findOne(super.getEntityId("float4")));

		return floatsParade;
	}
}
