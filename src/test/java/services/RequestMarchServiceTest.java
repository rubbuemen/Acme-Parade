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
import domain.RequestMarch;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RequestMarchServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private RequestMarchService	requestMarchService;

	@Autowired
	private ParadeService		paradeService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.6 (Acme-Madrugá)
	 *         Caso de uso: listar "RequestMarchs" de un "Parade" de la "Brotherhood" logeada
	 *         Tests positivos: 1
	 *         *** 1. Listar "RequestMarchs" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "RequestMarchs" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListRequestMarchsBrotherhood() {
		final Object testingData[][] = {
			{
				"brotherhood1", "parade1", null
			}, {
				"chapter1", "parade1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listRequestMarchsBrotherhoodTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.6 (Acme-Madrugá)
	 *         Caso de uso: aceptar o rchazar una "RequestMarch" de un "Member" para una "Parade"
	 *         Tests positivos: 2
	 *         *** 1. Aprobar una "RequestMarch" de un "Member" correctamente
	 *         *** 2. Rechazar una "RequestMarch" de un "Member" correctamente
	 *         Tests negativos: 10
	 *         *** 1. Intento de aprobar una "RequestMarch" de un "Member" con una autoridad no permitida
	 *         *** 2. Intento de aprobar una "RequestMarch" de un "Member" que no es del "Brotherhood" logeado
	 *         *** 3. Intento de aprobar una "RequestMarch" de un "Member" sin seleccionar fila en la que marchará
	 *         *** 4. Intento de aprobar una "RequestMarch" de un "Member" sin seleccionar columna en la que marchará
	 *         *** 5. Intento de aprobar una "RequestMarch" de un "Member" seleccionando una fila menor a 1
	 *         *** 6. Intento de aprobar una "RequestMarch" de un "Member" seleccionando una columna menor a 1
	 *         *** 7. Intento de aprobar una "RequestMarch" de un "Member" seleccionando una fila mayor al máximo de filas del "Parade"
	 *         *** 8. Intento de aprobar una "RequestMarch" de un "Member" seleccionando una fila mayor al máximo de columnas del "Parade"
	 *         *** 9. Intento de aprobar una "RequestMarch" de un "Member" seleccionando una fila y columna ya ocupada por otro "Member"
	 *         *** 10. Intento de rechazar una "RequestMarch" de un "Member" con motivo de rechazo vacío
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverAcceptOrRejectRequestMarchMember() {
		final Object testingData[][] = {
			{
				"brotherhood1", "requestMarch1", "APPROVED", 4, 1, null, null
			}, {
				"brotherhood1", "requestMarch1", "REJECTED", 4, 1, "testRejectReason", null
			}, {
				"chapter1", "requestMarch1", "APPROVED", 4, 1, null, IllegalArgumentException.class
			}, {
				"brotherhood5", "requestMarch1", "APPROVED", 4, 1, null, IllegalArgumentException.class
			}, {
				"brotherhood1", "requestMarch1", "APPROVED", null, 1, null, IllegalArgumentException.class
			}, {
				"brotherhood1", "requestMarch1", "APPROVED", 4, null, null, IllegalArgumentException.class
			}, {
				"brotherhood1", "requestMarch1", "APPROVED", 0, 1, null, IllegalArgumentException.class
			}, {
				"brotherhood1", "requestMarch1", "APPROVED", 4, 0, null, IllegalArgumentException.class
			}, {
				"brotherhood1", "requestMarch1", "APPROVED", 501, 1, null, IllegalArgumentException.class
			}, {
				"brotherhood1", "requestMarch1", "APPROVED", 4, 3, null, IllegalArgumentException.class
			}, {
				"brotherhood1", "requestMarch1", "APPROVED", 1, 1, null, IllegalArgumentException.class
			}, {
				"brotherhood1", "requestMarch1", "REJECTED", 4, 1, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptOrRejectRequestMarchMemberTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (String) testingData[i][5],
				(Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 11.1 (Acme-Madrugá)
	 *         Caso de uso: crear una "RequestMarch" para un "Parade"
	 *         Tests positivos: 1
	 *         *** 1. Crear una "RequestMarch" para un "Parade" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de creación de una "RequestMarch" para un "Parade" con una autoridad no permitida
	 *         *** 2. Intento de creación de una "RequestMarch" para un "Parade" de una "Brotherhood" a la que no pertenece
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateRequestMarch() {
		final Object testingData[][] = {
			{
				"member1", "parade1", null
			}, {
				"chapter1", "parade1", IllegalArgumentException.class
			}, {
				"member1", "parade4", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createRequestMarchTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 11.1 (Acme-Madrugá)
	 *         Caso de uso: eliminar una "RequestMarch" pendiente
	 *         Tests positivos: 1
	 *         *** 1. Eliminar una "RequestMarch" pendiente correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de una "RequestMarch" pendiente con una autoridad no permitida
	 *         *** 2. Intento de eliminación de una "RequestMarch" pendiente que no le pertenece
	 *         *** 3. Intento de eliminación de una "RequestMarch" ya aceptada
	 *         Analisis de cobertura de sentencias: 99,11% 112/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteRequestMarch() {
		final Object testingData[][] = {
			{
				"member1", "requestMarch1", null
			}, {
				"chapter1", "requestMarch1", IllegalArgumentException.class
			}, {
				"member1", "requestMarch13", IllegalArgumentException.class
			}, {
				"member1", "requestMarch2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteRequestMarchTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listRequestMarchsBrotherhoodTemplate(final String username, final String parade, final Class<?> expected) {
		Class<?> caught = null;
		Collection<RequestMarch> requestMarchs;

		super.startTransaction();

		try {
			super.authenticate(username);
			requestMarchs = this.requestMarchService.findRequestsMarchByParade(super.getEntityId(parade));
			Assert.notNull(requestMarchs);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void acceptOrRejectRequestMarchMemberTemplate(final String username, final String requestMarch, final String status, final Integer positionRow, final Integer positionColumn, final String rejectReason, final Class<?> expected) {
		Class<?> caught = null;
		RequestMarch requestMarchEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			requestMarchEntity = this.requestMarchService.findRequestMarchPenddingBrotherhoodLogged(super.getEntityId(requestMarch));
			requestMarchEntity.setStatus(status);
			requestMarchEntity.setPositionRow(positionRow);
			requestMarchEntity.setPositionColumn(positionColumn);
			requestMarchEntity.setRejectReason(rejectReason);
			requestMarchEntity = this.requestMarchService.save(requestMarchEntity);
			this.requestMarchService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createRequestMarchTemplate(final String username, final String parade, final Class<?> expected) {
		Class<?> caught = null;
		RequestMarch requestMarchEntity;
		Parade paradeEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			requestMarchEntity = this.requestMarchService.create();
			paradeEntity = this.paradeService.findOne(super.getEntityId(parade));
			this.requestMarchService.save(requestMarchEntity, paradeEntity);
			this.requestMarchService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteRequestMarchTemplate(final String username, final String requestMarch, final Class<?> expected) {
		Class<?> caught = null;
		RequestMarch requestMarchEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			requestMarchEntity = this.requestMarchService.findOne(super.getEntityId(requestMarch));
			this.requestMarchService.delete(requestMarchEntity);
			this.requestMarchService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
