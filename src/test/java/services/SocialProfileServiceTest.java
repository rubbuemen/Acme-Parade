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
import domain.SocialProfile;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SocialProfileServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private SocialProfileService	socialProfileService;

	@PersistenceContext
	EntityManager					entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.1 (Acme-Madrugá)
	 *         Caso de uso: listar "SocialProfiles"
	 *         Tests positivos: 1
	 *         *** 1. Listar "SocialProfiles" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "SocialProfiles" sin estar logeado
	 *         Analisis de cobertura de sentencias: 100% 14/14 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListSocialProfilesBrotherhood() {
		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listSocialProfilesBrotherhoodTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.1 (Acme-Madrugá)
	 *         Caso de uso: crear un "SocialProfile"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "SocialProfile" correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de creación de un "SocialProfile" sin estar logeado
	 *         *** 2. Intento de creación de un "SocialProfile" con nick vacío
	 *         *** 3. Intento de creación de un "SocialProfile" con nombre de la red social vacío
	 *         *** 4. Intento de creación de un "SocialProfile" con link de perfil vacío
	 *         *** 5. Intento de creación de un "SocialProfile" con link de perfil que no es URL
	 *         Analisis de cobertura de sentencias: 100% 48/48 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateSocialProfile() {
		final Object testingData[][] = {
			{
				"admin", "nickTest", "socialNetworkNameTest", "http://www.test.com", null
			}, {
				null, "nickTest", "socialNetworkNameTest", "http://www.test.com", IllegalArgumentException.class
			}, {
				"admin", "", "socialNetworkNameTest", "http://www.test.com", ConstraintViolationException.class
			}, {
				"admin", "nickTest", "", "http://www.test.com", ConstraintViolationException.class
			}, {
				"admin", "nickTest", "socialNetworkNameTest", "", ConstraintViolationException.class
			}, {
				"admin", "nickTest", "socialNetworkNameTest", "test", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.createSocialProfileTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.1 (Acme-Madrugá)
	 *         Caso de uso: editar un "SocialProfile"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "SocialProfile" correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de edición de un "SocialProfile" sin estar logeado
	 *         *** 2. Intento de edición de un "SocialProfile" con nick vacío
	 *         *** 3. Intento de edición de un "SocialProfile" con nombre de la red social vacío
	 *         *** 4. Intento de edición de un "SocialProfile" con link de perfil vacío
	 *         *** 5. Intento de edición de un "SocialProfile" con link de perfil que no es URL
	 *         Analisis de cobertura de sentencias: 100% 33/33 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditSocialProfile() {
		final Object testingData[][] = {
			{
				"admin", "socialProfile1", "nickTest", "socialNetworkNameTest", "http://www.test.com", null
			}, {
				null, "socialProfile1", "nickTest", "socialNetworkNameTest", "http://www.test.com", IllegalArgumentException.class
			}, {
				"admin", "socialProfile1", "", "socialNetworkNameTest", "http://www.test.com", ConstraintViolationException.class
			}, {
				"admin", "socialProfile1", "nickTest", "", "http://www.test.com", ConstraintViolationException.class
			}, {
				"admin", "socialProfile1", "nickTest", "socialNetworkNameTest", "", ConstraintViolationException.class
			}, {
				"admin", "socialProfile1", "nickTest", "socialNetworkNameTest", "test", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.editSocialProfileTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 27.1 (Acme-Madrugá)
	 *         Caso de uso: eliminar un "SocialProfile"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "SocialProfile" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "SocialProfile" sin estar logeado
	 *         *** 2. Intento de eliminación de un "SocialProfile" que no es del "Actor" logeado
	 *         Analisis de cobertura de sentencias: 95,2% 20/21 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteSocialProfile() {
		final Object testingData[][] = {
			{
				"admin", "socialProfile1", null
			}, {
				null, "socialProfile1", IllegalArgumentException.class
			}, {
				"brotherhood1", "socialProfile1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteSocialProfileTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listSocialProfilesBrotherhoodTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<SocialProfile> socialProfiles;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			socialProfiles = this.socialProfileService.findSocialProfilesByActorLogged();
			Assert.notNull(socialProfiles);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createSocialProfileTemplate(final String username, final String nick, final String socialNetworkName, final String profileLink, final Class<?> expected) {
		Class<?> caught = null;
		SocialProfile socialProfileEntity;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			socialProfileEntity = this.socialProfileService.create();
			socialProfileEntity.setNick(nick);
			socialProfileEntity.setSocialNetworkName(socialNetworkName);
			socialProfileEntity.setProfileLink(profileLink);
			this.socialProfileService.save(socialProfileEntity);
			this.socialProfileService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editSocialProfileTemplate(final String username, final String socialProfile, final String nick, final String socialNetworkName, final String profileLink, final Class<?> expected) {
		Class<?> caught = null;
		SocialProfile socialProfileEntity;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			socialProfileEntity = this.socialProfileService.findSocialProfileActorLogged(super.getEntityId(socialProfile));
			socialProfileEntity.setNick(nick);
			socialProfileEntity.setSocialNetworkName(socialNetworkName);
			socialProfileEntity.setProfileLink(profileLink);
			this.socialProfileService.save(socialProfileEntity);
			this.socialProfileService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteSocialProfileTemplate(final String username, final String socialProfile, final Class<?> expected) {
		Class<?> caught = null;
		SocialProfile socialProfileEntity;

		super.startTransaction();

		try {
			if (username != null)
				super.authenticate(username);
			socialProfileEntity = this.socialProfileService.findSocialProfileActorLogged(super.getEntityId(socialProfile));
			this.socialProfileService.delete(socialProfileEntity);
			this.socialProfileService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
