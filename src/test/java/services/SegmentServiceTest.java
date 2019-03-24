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

import java.sql.Time;
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
import domain.GPSCoordinates;
import domain.Parade;
import domain.Segment;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SegmentServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private SegmentService	segmentService;

	@Autowired
	private ParadeService	paradeService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.3 (Acme-Parade)
	 *         Caso de uso: listar "Segments" de un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Segments" de un "Parade" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Segments" con una autoridad no permitida
	 *         *** 2. Intento de listar "Segments" que no pertenecen al "Brotherhood" logeado
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListSegments() {
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
			this.listSegmentsTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.3 (Acme-Parade)
	 *         Caso de uso: crear un "Segment" en un "Parade"
	 *         Tests positivos: 2
	 *         *** 1. Crear un "Segment" en un "Parade" que no tiene segments correctamente
	 *         *** 2. Crear un "Segment" en un "Parade" que tiene segments correctamente
	 *         Tests negativos: 17
	 *         *** 1. Intento de creación de un "Segment" en un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Segment" en un "Parade" que no es del "Brotherhood" logeado
	 *         *** 3. Intento de creación de un "Segment" cuya latitud de origen es nula
	 *         *** 4. Intento de creación de un "Segment" cuya latitud de origen es mayor a 90
	 *         *** 5. Intento de creación de un "Segment" cuya latitud de origen es menor a -90
	 *         *** 6. Intento de creación de un "Segment" cuya longitud de origen es nula
	 *         *** 7. Intento de creación de un "Segment" cuya longitud de origen es mayor a 180
	 *         *** 8. Intento de creación de un "Segment" cuya longitud de origen es menor a -180
	 *         *** 9. Intento de creación de un "Segment" cuya latitud de destino es nula
	 *         *** 10. Intento de creación de un "Segment" cuya latitud de destino es mayor a 90
	 *         *** 11. Intento de creación de un "Segment" cuya latitud de destino es menor a -90
	 *         *** 12. Intento de creación de un "Segment" cuya longitud de destino es nula
	 *         *** 13. Intento de creación de un "Segment" cuya longitud de destino es mayor a 180
	 *         *** 14. Intento de creación de un "Segment" cuya longitud de destino es menor a -180
	 *         *** 15. Intento de creación de un "Segment" cuyo tiempo de salida es nulo
	 *         *** 16. Intento de creación de un "Segment" cuyo tiempo de llegada es nulo
	 *         *** 17. Intento de creación de un "Segment" cuyo tiempo de salida es posterior al tiempo de llegada
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateSegment() {
		final Object testingData[][] = {
			{
				"brotherhood1", "parade5", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), null
			}, {
				"brotherhood1", "parade1", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), null
			}, {
				"member1", "parade5", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), IllegalArgumentException.class
			}, {
				"brotherhood2", "parade5", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), IllegalArgumentException.class
			}, {
				"brotherhood1", "parade5", null, 62.6245, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", 91.00, 62.6245, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -91.00, 62.6245, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, null, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 181.0, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, -181.0, -70.4253, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 62.6245, null, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 62.6245, 91.00, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 62.6245, -91.00, 62.6245, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 62.6245, -70.4253, null, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 62.6245, -70.4253, 181.0, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 62.6245, -70.4253, -181.0, Time.valueOf("10:30:00"), Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 62.6245, -70.4253, 62.6245, null, Time.valueOf("11:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("10:30:00"), null, ConstraintViolationException.class
			}, {
				"brotherhood1", "parade5", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("12:30:00"), Time.valueOf("11:30:00"), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createSegmentTemplate((String) testingData[i][0], (String) testingData[i][1], (Double) testingData[i][2], (Double) testingData[i][3], (Double) testingData[i][4], (Double) testingData[i][5], (Time) testingData[i][6],
				(Time) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.3 (Acme-Parade)
	 *         Caso de uso: editar un "Segment" en un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Segment" intermedio en el conjunto de un "Parade" correctamente
	 *         Tests negativos: 19
	 *         *** 1. Intento de edición de un "Segment" en un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Segment" en un "Parade" que no es del "Brotherhood" logeado
	 *         *** 3. Intento de edición de un "Segment" cuya latitud de origen es nula
	 *         *** 4. Intento de edición de un "Segment" cuya latitud de origen es mayor a 90
	 *         *** 5. Intento de edición de un "Segment" cuya latitud de origen es menor a -90
	 *         *** 6. Intento de edición de un "Segment" cuya longitud de origen es nula
	 *         *** 7. Intento de edición de un "Segment" cuya longitud de origen es mayor a 180
	 *         *** 8. Intento de edición de un "Segment" cuya longitud de origen es menor a -180
	 *         *** 9. Intento de edición de un "Segment" cuya latitud de destino es nula
	 *         *** 10. Intento de edición de un "Segment" cuya latitud de destino es mayor a 90
	 *         *** 11. Intento de edición de un "Segment" cuya latitud de destino es menor a -90
	 *         *** 12. Intento de edición de un "Segment" cuya longitud de destino es nula
	 *         *** 13. Intento de edición de un "Segment" cuya longitud de destino es mayor a 180
	 *         *** 14. Intento de edición de un "Segment" cuya longitud de destino es menor a -180
	 *         *** 15. Intento de edición de un "Segment" cuyo tiempo de salida es nulo
	 *         *** 16. Intento de edición de un "Segment" cuyo tiempo de llegada es nulo
	 *         *** 17. Intento de edición de un "Segment" cuyo tiempo de salida es posterior al tiempo de llegada
	 *         *** 18. Intento de edición de un "Segment" cuyo tiempo de salida es anterior al del anterior segmento
	 *         *** 19. Intento de edición de un "Segment" cuyo tiempo de llegada es posterior al del siguiente segmento
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditSegment() {
		final Object testingData[][] = {
			{
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), null
			}, {
				"member1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), IllegalArgumentException.class
			}, {
				"brotherhood2", "parade1", "segment2", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), IllegalArgumentException.class
			}, {
				"brotherhood1", "parade1", "segment2", null, 62.6245, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", 91.00, 62.6245, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -91.00, 62.6245, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, null, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 181.0, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, -181.0, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, null, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, 91.00, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -91.00, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, null, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, 181.0, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, -181.0, Time.valueOf("13:30:00"), Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, 62.6245, null, Time.valueOf("16:30:00"), ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("13:30:00"), null, ConstraintViolationException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("16:30:00"), Time.valueOf("13:30:00"), IllegalArgumentException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("09:30:00"), Time.valueOf("16:30:00"), IllegalArgumentException.class
			}, {
				"brotherhood1", "parade1", "segment2", -20.4253, 62.6245, -70.4253, 62.6245, Time.valueOf("13:30:00"), Time.valueOf("21:30:00"), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editSegmentTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (Double) testingData[i][4], (Double) testingData[i][5], (Double) testingData[i][6],
				(Time) testingData[i][7], (Time) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.3 (Acme-Parade)
	 *         Caso de uso: eliminar un "Segment"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Segment" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "Segment" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Segment" en un "Parade" que no es del "Brotherhood" logeado
	 *         Analisis de cobertura de sentencias: 98% 48/49 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteSegment() {
		final Object testingData[][] = {
			{
				"brotherhood1", "parade1", "segment2", null
			}, {
				"member1", "parade1", "segment2", IllegalArgumentException.class
			}, {
				"brotherhood2", "parade1", "segment2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteSegmentTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	// Template methods ------------------------------------------------------

	protected void listSegmentsTemplate(final String username, final String parade, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Segment> segments;

		super.startTransaction();

		try {
			super.authenticate(username);
			segments = this.segmentService.findSegmentsByParade(super.getEntityId(parade));
			Assert.notNull(segments);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createSegmentTemplate(final String username, final String parade, final Double latitudeOrigin, final Double longitudeOrigin, final Double latitudeDestination, final Double longitudeDestination, final Time timeReachOrigin,
		final Time timeReachDestination, final Class<?> expected) {
		Class<?> caught = null;
		Segment segment;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			Boolean contiguouTest = false;
			paradeEntity = this.paradeService.findOne(super.getEntityId(parade));
			if (paradeEntity.getSegments().size() > 0)
				contiguouTest = true;
			segment = this.segmentService.create();
			final GPSCoordinates origin = new GPSCoordinates();
			final GPSCoordinates destination = new GPSCoordinates();
			origin.setLatitude(latitudeOrigin);
			origin.setLongitude(longitudeOrigin);
			destination.setLatitude(latitudeDestination);
			destination.setLongitude(longitudeDestination);
			segment.setOrigin(origin);
			segment.setDestination(destination);
			segment.setTimeReachOrigin(timeReachOrigin);
			segment.setTimeReachDestination(timeReachDestination);
			segment = this.segmentService.save(segment, paradeEntity);
			if (contiguouTest)
				Assert.isTrue(segment.getOrigin().getLatitude() != latitudeOrigin && segment.getOrigin().getLongitude() != longitudeOrigin && segment.getTimeReachOrigin() != timeReachOrigin);
			this.segmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editSegmentTemplate(final String username, final String parade, final String segment, final Double latitudeOrigin, final Double longitudeOrigin, final Double latitudeDestination, final Double longitudeDestination,
		final Time timeReachOrigin, final Time timeReachDestination, final Class<?> expected) {
		Class<?> caught = null;
		Segment segmentEntity;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			paradeEntity = this.paradeService.findOne(super.getEntityId(parade));
			segmentEntity = this.segmentService.findOne(super.getEntityId(segment));
			final GPSCoordinates origin = new GPSCoordinates();
			final GPSCoordinates destination = new GPSCoordinates();
			origin.setLatitude(latitudeOrigin);
			origin.setLongitude(longitudeOrigin);
			destination.setLatitude(latitudeDestination);
			destination.setLongitude(longitudeDestination);
			segmentEntity.setOrigin(origin);
			segmentEntity.setDestination(destination);
			segmentEntity.setTimeReachOrigin(timeReachOrigin);
			segmentEntity.setTimeReachDestination(timeReachDestination);
			this.segmentService.save(segmentEntity, paradeEntity);
			this.segmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteSegmentTemplate(final String username, final String parade, final String segment, final Class<?> expected) {
		Class<?> caught = null;
		Segment segmentEntity;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			paradeEntity = this.paradeService.findOne(super.getEntityId(parade));
			this.segmentService.findSegmentsByParade(super.getEntityId(parade));
			segmentEntity = this.segmentService.findOne(super.getEntityId(segment));
			this.segmentService.delete(segmentEntity, paradeEntity);
			this.segmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
