
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.Authority;
import security.UserAccount;
import security.UserAccountRepository;

@Service
@Transactional
public class UserAccountService {

	// Managed repository
	@Autowired
	private UserAccountRepository	userAccountRepository;


	// Supporting services

	// Simple CRUD methods
	public UserAccount create() {
		UserAccount result;

		result = new UserAccount();
		final Collection<Authority> authorities = new HashSet<>();

		result.setAuthorities(authorities);
		result.setStatusAccount(true);

		return result;
	}

	public Collection<UserAccount> findAll() {
		Collection<UserAccount> result;

		result = this.userAccountRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public UserAccount findOne(final int userAccountId) {
		Assert.isTrue(userAccountId != 0);

		UserAccount result;

		result = this.userAccountRepository.findOne(userAccountId);
		Assert.notNull(result);

		return result;
	}

	public UserAccount save(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		UserAccount result;

		if (userAccount.getId() == 0) {
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			String password = userAccount.getPassword();
			final String passwordEncoded = encoder.encodePassword(password, null);
			Assert.isTrue(encoder.isPasswordValid(passwordEncoded, password, null));
			password = passwordEncoded;
			userAccount.setPassword(password);
		}

		result = this.userAccountRepository.save(userAccount);

		return result;
	}

	public void delete(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Assert.isTrue(userAccount.getId() != 0);
		Assert.isTrue(this.userAccountRepository.exists(userAccount.getId()));

		this.userAccountRepository.delete(userAccount);
	}

	// Other business methods

	// Reconstruct methods

}
