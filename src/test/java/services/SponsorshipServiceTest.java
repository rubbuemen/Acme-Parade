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
import domain.CreditCard;
import domain.Sponsorship;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private SponsorService		sponsorService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 16.1 (Acme-Parade)
	 *         Caso de uso: listar "Sponsorships" logeado como Sponsor
	 *         Tests positivos: 1
	 *         *** 1. Listar "Sponsorships" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Sponsorships" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 20/20 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListSponsorships() {
		final Object testingData[][] = {
			{
				"sponsor1", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listSponsorshipsTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 16.1 (Acme-Parade)
	 *         Caso de uso: crear un "Sponsorship"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Sponsorship" correctamente
	 *         Tests negativos: 23
	 *         *** 1. Intento de creación de un "Sponsorship" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Sponsorship" con banner vacío
	 *         *** 3. Intento de creación de un "Sponsorship" con banner que no es URL
	 *         *** 4. Intento de creación de un "Sponsorship" con url de destino vacío
	 *         *** 5. Intento de creación de un "Sponsorship" con url de destino que no es URL
	 *         *** 6. Intento de creación de un "Sponsorship" con "isActived" nulo
	 *         *** 7. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el propietario vacío
	 *         *** 8. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene la marca vacío
	 *         *** 9. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el número vacío
	 *         *** 10. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un número que no es de tarjeta de crédito
	 *         *** 11. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el mes de caducidad nulo
	 *         *** 12. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un mes de caducidad menor a 1
	 *         *** 13. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un mes de caducidad mayor a 12
	 *         *** 14. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el año de caducidad nulo
	 *         *** 15. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un año de caducidad menor a 0
	 *         *** 16. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un año de caducidad mayor a 99
	 *         *** 17. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el CVV nulo
	 *         *** 18. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un CVV menor a 100
	 *         *** 19. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un CVV mayor a 999
	 *         *** 20. Intento de creación de un "Sponsorship" con un "Parade" que no está aceptado
	 *         *** 21. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un número no numérico
	 *         *** 22. Intento de creación de un "Sponsorship" cuya "CreditCard" está caducada
	 *         *** 23. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene una marca no incluida en la configuración del sistema
	 *         Analisis de cobertura de sentencias: 100% 154/154 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateSponsorship() {
		final Object testingData[][] = {
			{
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, null
			}, {
				"brotherhood1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "parade1", "", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "test", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "test", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", null, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "8534634734746", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", null, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 0, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 13, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, null, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, -1, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 100, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, null, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 99, ConstraintViolationException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 1000, ConstraintViolationException.class
			}, {
				"sponsor1", "parade6", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "testCreditCardNumber", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 2, 19, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "TEST", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createSponsorshipTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Integer) testingData[i][8], (Integer) testingData[i][9], (Integer) testingData[i][10], (Class<?>) testingData[i][11]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 16.1 (Acme-Parade)
	 *         Caso de uso: editar un "Sponsorship"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Sponsorship" correctamente
	 *         Tests negativos: 24
	 *         *** 1. Intento de edición de un "Sponsorship" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Sponsorship" que no es del "Sponsor" logeado
	 *         *** 3. Intento de edición de un "Sponsorship" con banner vacío
	 *         *** 4. Intento de edición de un "Sponsorship" con banner que no es URL
	 *         *** 5. Intento de edición de un "Sponsorship" con url de destino vacío
	 *         *** 6. Intento de edición de un "Sponsorship" con url de destino que no es URL
	 *         *** 7. Intento de edición de un "Sponsorship" con "isActived" nulo
	 *         *** 8. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el propietario vacío
	 *         *** 9. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene la marca vacío
	 *         *** 10. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el número vacío
	 *         *** 11. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un número que no es de tarjeta de crédito
	 *         *** 12. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el mes de caducidad nulo
	 *         *** 13. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un mes de caducidad menor a 1
	 *         *** 14. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un mes de caducidad mayor a 12
	 *         *** 15. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el año de caducidad nulo
	 *         *** 16. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un año de caducidad menor a 0
	 *         *** 17. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un año de caducidad mayor a 99
	 *         *** 18. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el CVV nulo
	 *         *** 19. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un CVV menor a 100
	 *         *** 20. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un CVV mayor a 999
	 *         *** 21. Intento de edición de un "Sponsorship" con un "Parade" que no está aceptado
	 *         *** 22. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un número no numérico
	 *         *** 23. Intento de edición de un "Sponsorship" cuya "CreditCard" está caducada
	 *         *** 24. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene una marca no incluida en la configuración del sistema
	 *         Analiis de cobertura de sentencias: 100% 130/130 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditSponsorship() {
		final Object testingData[][] = {
			{
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, null
			}, {
				"brotherhood1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor3", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "test", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "test", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", null, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "8534634734746", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", null, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 0, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 13, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, null, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, -1, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 100, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, null, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 99, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 1000, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "parade6", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "testCreditCardNumber", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "VISA", "4739158676192764", 2, 19, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "sponsorship1", "parade1", "http://www.testBanner.com", "http://www.testTargetUrl.com", true, "testCreditCardHolder", "TEST", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editSponsorshipTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Boolean) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (Integer) testingData[i][9], (Integer) testingData[i][10], (Integer) testingData[i][11], (Class<?>) testingData[i][12]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 16.1 (Acme-Parade)
	 *         Caso de uso: desactivar un "Sponsorship"
	 *         Tests positivos: 1
	 *         *** 1. Desactivar un "Sponsorship" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de desactivación de un "Sponsorship" con una autoridad no permitida
	 *         *** 2. Intento de desactivación de un "Sponsorship" que no es del "Sponsor" logeado
	 *         Analisis de cobertura de sentencias: 97,9% 46/47 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeactiveSponsorship() {
		final Object testingData[][] = {
			{
				"sponsor1", "sponsorship1", null
			}, {
				"member1", "sponsorship1", IllegalArgumentException.class
			}, {
				"sponsor3", "sponsorship1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deactiveSponsorshipTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listSponsorshipsTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Sponsorship> sponsorships;

		super.startTransaction();

		try {
			super.authenticate(username);
			sponsorships = this.sponsorshipService.findSponsorshipsBySponsorLogged();
			Assert.notNull(sponsorships);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createSponsorshipTemplate(final String username, final String parade, final String banner, final String targetURL, final Boolean isActivated, final String creditCardHolder, final String creditCardMake, final String creditCardNumber,
		final Integer creditCardExpirationMonth, final Integer creditCardExpirationYear, final Integer creditCardCVV, final Class<?> expected) {
		Class<?> caught = null;
		Sponsorship sponsorship;

		super.startTransaction();

		try {
			super.authenticate(username);
			sponsorship = this.sponsorshipService.create();
			final CreditCard creditCard = new CreditCard();
			creditCard.setHolder(creditCardHolder);
			creditCard.setMake(creditCardMake);
			creditCard.setNumber(creditCardNumber);
			creditCard.setExpirationMonth(creditCardExpirationMonth);
			creditCard.setExpirationYear(creditCardExpirationYear);
			creditCard.setCvv(creditCardCVV);
			sponsorship.setBanner(banner);
			sponsorship.setTargetURL(targetURL);
			sponsorship.setIsActivated(isActivated);
			sponsorship.setCreditCard(creditCard);
			sponsorship.setParade(this.paradeService.findOne(super.getEntityId(parade)));
			sponsorship.setSponsor(this.sponsorService.findOne(super.getEntityId(username)));
			this.sponsorshipService.save(sponsorship);
			this.sponsorshipService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editSponsorshipTemplate(final String username, final String sponsorship, final String parade, final String banner, final String targetURL, final Boolean isActivated, final String creditCardHolder, final String creditCardMake,
		final String creditCardNumber, final Integer creditCardExpirationMonth, final Integer creditCardExpirationYear, final Integer creditCardCVV, final Class<?> expected) {
		Class<?> caught = null;
		Sponsorship sponsorshipEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			sponsorshipEntity = this.sponsorshipService.findSponsorshipSponsorLogged(super.getEntityId(sponsorship));
			final CreditCard creditCard = new CreditCard();
			creditCard.setHolder(creditCardHolder);
			creditCard.setMake(creditCardMake);
			creditCard.setNumber(creditCardNumber);
			creditCard.setExpirationMonth(creditCardExpirationMonth);
			creditCard.setExpirationYear(creditCardExpirationYear);
			creditCard.setCvv(creditCardCVV);
			sponsorshipEntity.setBanner(banner);
			sponsorshipEntity.setTargetURL(targetURL);
			sponsorshipEntity.setIsActivated(isActivated);
			sponsorshipEntity.setCreditCard(creditCard);
			sponsorshipEntity.setParade(this.paradeService.findOne(super.getEntityId(parade)));
			sponsorshipEntity.setSponsor(this.sponsorService.findOne(super.getEntityId(username)));
			this.sponsorshipService.save(sponsorshipEntity);
			this.sponsorshipService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deactiveSponsorshipTemplate(final String username, final String sponsorship, final Class<?> expected) {
		Class<?> caught = null;
		Sponsorship sponsorshipEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			sponsorshipEntity = this.sponsorshipService.findSponsorshipSponsorLogged(super.getEntityId(sponsorship));
			this.sponsorshipService.deactivate(sponsorshipEntity);
			this.sponsorshipService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
