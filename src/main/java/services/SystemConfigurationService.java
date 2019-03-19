
package services;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SystemConfigurationRepository;
import domain.Actor;
import domain.Message;
import domain.Parade;
import domain.RequestMarch;
import domain.Sponsorship;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	// Managed repository
	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	// Supporting services
	@Autowired
	private ParadeService				paradeService;

	@Autowired
	private MessageService					messageService;

	@Autowired
	private ActorService					actorService;
	
	@Autowired
	private SponsorshipService					sponsorshipService;;


	// Simple CRUD methods
	public SystemConfiguration create() {
		SystemConfiguration result;

		result = new SystemConfiguration();

		return result;
	}

	public Collection<SystemConfiguration> findAll() {
		Collection<SystemConfiguration> result;

		result = this.systemConfigurationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public SystemConfiguration findOne(final int systemConfigurationId) {
		Assert.isTrue(systemConfigurationId != 0);

		SystemConfiguration result;

		result = this.systemConfigurationRepository.findOne(systemConfigurationId);
		Assert.notNull(result);

		return result;
	}

	public SystemConfiguration save(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration);

		SystemConfiguration result;

		result = this.systemConfigurationRepository.save(systemConfiguration);

		return result;
	}

	public void delete(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration);
		Assert.isTrue(systemConfiguration.getId() != 0);
		Assert.isTrue(this.systemConfigurationRepository.exists(systemConfiguration.getId()));

		this.systemConfigurationRepository.delete(systemConfiguration);
	}

	// Other business methods
	public SystemConfiguration getConfiguration() {
		SystemConfiguration result;

		result = this.systemConfigurationRepository.getConfiguration();
		Assert.notNull(result);

		return result;
	}

	// R10.6: The system must suggest a good position automatically
	public Map<Integer, Integer> suggestedRowColumn(final int requestMarchId) {
		final Map<Integer, Integer> result = new HashMap<>();

		final Parade parade = this.paradeService.findParadeByRequestMarchId(requestMarchId);
		final Collection<RequestMarch> requestsMarchParade = parade.getRequestsMarch();

		Integer suggestedRow = 0;
		Integer suggestedColumn = 0;

		final boolean[][] position = new boolean[parade.getMaxRows()][parade.getMaxColumns()];

		for (final RequestMarch rm : requestsMarchParade)
			if (rm.getStatus().equals("APPROVED") && rm.getPositionRow() != null && rm.getPositionColumn() != null) {
				final Integer r = rm.getPositionRow() - 1;
				final Integer c = rm.getPositionColumn() - 1;
				position[r][c] = true;
			}

		if (parade.getMaxRows() > parade.getMaxColumns()) {
			breakloop: for (int r = 0; r < position.length; r++)
				for (int c = 0; c < position[r].length; c++)
					if (position[r][c] == false) {
						suggestedRow = r;
						suggestedColumn = c;
						break breakloop;
					}
		} else{
			breakloop: for (int c = 0; c < position.length; c++)
				for (int r = 0; r < position[c].length; r++)
					if (position[r][c] == false) {
						suggestedRow = r;
						suggestedColumn = c;
						break breakloop;
					}
		}
		

		result.put(suggestedRow + 1, suggestedColumn + 1);

		return result;
	}

	private Pattern patternSpamWords() {
		final Collection<String> spamWords = this.getConfiguration().getSpamWords();
		String pattern = "";
		for (final String sw : spamWords)
			pattern = pattern + (sw + "|");
		pattern = pattern.substring(0, pattern.length() - 1);
		pattern = pattern + "$";
		final Pattern result = Pattern.compile(pattern);
		return result;
	}

	private Pattern patternPositiveWords() {
		final Collection<String> positiveWords = this.getConfiguration().getPositiveWords();
		String pattern = "";
		for (final String pw : positiveWords)
			pattern = pattern + (pw + "|");
		pattern = pattern.substring(0, pattern.length() - 1);
		pattern = pattern + "$";
		final Pattern result = Pattern.compile(pattern);
		return result;
	}

	private Pattern patternNegativeWords() {
		final Collection<String> negativeWords = this.getConfiguration().getNegativeWords();
		String pattern = "";
		for (final String nw : negativeWords)
			pattern = pattern + (nw + "|");
		pattern = pattern.substring(0, pattern.length() - 1);
		pattern = pattern + "$";
		final Pattern result = Pattern.compile(pattern);
		return result;
	}

	// R28.2
	public Map<Actor, Boolean> mapActorSpammer() {
		final Map<Actor, Boolean> result = new HashMap<>();

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		Matcher spamWordsSubject, spamWordsBody;
		Integer hasSpamCount = 0;

		final Collection<Actor> actors = this.actorService.findAll();
		final Actor system = this.actorService.getSystemActor();
		actors.remove(system);

		for (final Actor a : actors) {
			boolean isSpammer = false;
			final Collection<Message> messagesActor = this.messageService.findMessagesSentByActorId(a.getId());
			for (final Message m : messagesActor) {
				spamWordsSubject = this.patternSpamWords().matcher(m.getSubject().toLowerCase());
				spamWordsBody = this.patternSpamWords().matcher(m.getBody().toLowerCase());
				if (spamWordsSubject.find() || spamWordsBody.find())
					hasSpamCount++;
			}
			final Integer sizeMessages = messagesActor.size();
			Double spamPorcentMessages = 0.0;
			if (sizeMessages != 0)
				spamPorcentMessages = (double) (hasSpamCount / sizeMessages) * 100;
			if (spamPorcentMessages >= 10)
				isSpammer = true;
			result.put(a, isSpammer);
		}
		return result;
	}

	// R28.2
	public void computeSpammers() {
		Boolean res = null;

		final Map<Actor, Boolean> spammerMap = this.mapActorSpammer();

		final Collection<Actor> actors = this.actorService.findAll();
		final Actor system = this.actorService.getSystemActor();
		actors.remove(system);

		for (final Actor a : actors) {
			res = spammerMap.get(a);
			a.setIsSpammer(res);
			this.actorService.saveForComputes(a);
		}
	}

	public Map<Actor, Double> mapActorPolarityScore() {
		final Map<Actor, Double> result = new HashMap<>();

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		final Collection<Actor> actors = this.actorService.findAll();
		final Actor system = this.actorService.getSystemActor();
		actors.remove(system);

		final Map<Actor, Integer> scoreWordsActors = new HashMap<>();
		Matcher positiveWordsSubject, positiveWordsBody, negativeWordsSubject, negativeWordsBody;

		for (final Actor a : actors) {
			final Collection<Message> messagesActor = this.messageService.findMessagesSentByActorId(a.getId());
			Integer countScoreWords = 0;
			for (final Message m : messagesActor) {
				positiveWordsSubject = this.patternPositiveWords().matcher(m.getSubject().toLowerCase());
				positiveWordsBody = this.patternPositiveWords().matcher(m.getBody().toLowerCase());
				negativeWordsSubject = this.patternNegativeWords().matcher(m.getSubject().toLowerCase());
				negativeWordsBody = this.patternNegativeWords().matcher(m.getBody().toLowerCase());
				while (positiveWordsSubject.find())
					countScoreWords++;
				while (positiveWordsBody.find())
					countScoreWords++;
				while (negativeWordsSubject.find())
					countScoreWords--;
				while (negativeWordsBody.find())
					countScoreWords--;
				if (scoreWordsActors.containsKey(m.getSender())) {
					final Integer scoreWordsActor = scoreWordsActors.get(m.getSender());
					scoreWordsActors.put(m.getSender(), scoreWordsActor + countScoreWords);
				} else
					scoreWordsActors.put(m.getSender(), countScoreWords);
			}
		}

		Double maxScoreActors = 0.0;
		Double minScoreActors = 0.0;

		if (!scoreWordsActors.values().isEmpty()) {
			maxScoreActors = Collections.max(scoreWordsActors.values()).doubleValue();
			minScoreActors = Collections.min(scoreWordsActors.values()).doubleValue();
		}

		for (final Actor a : scoreWordsActors.keySet()) {
			final Double scoreActor = scoreWordsActors.get(a).doubleValue();
			Double lineaHomotheticTransformation = 0.0;
			if ((maxScoreActors - minScoreActors) != 0)
				lineaHomotheticTransformation = -1 + ((scoreActor - minScoreActors) * (1 - -1) / (maxScoreActors - minScoreActors));
			final DecimalFormat formatDecimals = new DecimalFormat(".##");
			lineaHomotheticTransformation = Double.valueOf(formatDecimals.format(lineaHomotheticTransformation));
			result.put(a, lineaHomotheticTransformation);
		}

		return result;
	}

	// R28.3
	public void computePolarityScore() {
		Double res = null;

		final Map<Actor, Double> scoresMap = this.mapActorPolarityScore();

		final Collection<Actor> actors = this.actorService.findAll();
		final Actor system = this.actorService.getSystemActor();
		actors.remove(system);

		for (final Actor a : actors) {
			res = scoresMap.get(a);
			a.setPolarityScore(res);
			this.actorService.saveForComputes(a);
		}
	}
	
	// R18.1 (Acme-Parade)
	public void deactivatesSponsorships() {
		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAll();
		for (Sponsorship ss : sponsorships) {
			if (!sponsorshipService.checkCreditCard(ss.getCreditCard())) {
				sponsorshipService.deactivateByAdmin(ss);
			}
		}		
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public SystemConfiguration reconstruct(final SystemConfiguration systemConfiguration, final BindingResult binding) {
		SystemConfiguration result;

		result = this.systemConfigurationRepository.findOne(systemConfiguration.getId());
		Assert.notNull(result, "This entity does not exist");
		result.setNameSystem(systemConfiguration.getNameSystem());
		result.setBannerUrl(systemConfiguration.getBannerUrl());
		result.setWelcomeMessageEnglish(systemConfiguration.getWelcomeMessageEnglish());
		result.setWelcomeMessageSpanish(systemConfiguration.getWelcomeMessageSpanish());
		result.setPhoneCountryCode(systemConfiguration.getPhoneCountryCode());
		result.setPeriodFinder(systemConfiguration.getPeriodFinder());
		result.setMaxResultsFinder(systemConfiguration.getMaxResultsFinder());
		result.setPrioritiesMessagesList(systemConfiguration.getPrioritiesMessagesList());
		result.setPositiveWords(systemConfiguration.getPositiveWords());
		result.setNegativeWords(systemConfiguration.getNegativeWords());
		result.setSpamWords(systemConfiguration.getSpamWords());
		result.setCreditCardMakes(systemConfiguration.getCreditCardMakes());
		result.setFare(systemConfiguration.getFare());
		result.setVATPercentage(systemConfiguration.getVATPercentage());

		this.validator.validate(result, binding);

		return result;
	}

}
