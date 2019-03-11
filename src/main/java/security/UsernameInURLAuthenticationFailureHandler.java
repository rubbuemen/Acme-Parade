
package security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class UsernameInURLAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	UserAccountRepository		userRepository;

	private String				urlPrefix;
	private RedirectStrategy	redirectStrategy	= new DefaultRedirectStrategy();
	private String				formUsernameKey		= "username";


	public UsernameInURLAuthenticationFailureHandler(final String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	//Failure logic:
	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException {
		//We inherited that method:
		this.saveException(request, exception);

		//Prepare URL:
		final String username = request.getParameter(this.formUsernameKey);

		final UserAccount userAccount = this.userRepository.findByUsername(username);
		boolean banned = false;

		if (userAccount != null)
			if (!userAccount.getStatusAccount())
				banned = true;

		final String redirectUrl = this.urlPrefix + "?banned=" + banned;

		//Redirect:
		this.redirectStrategy.sendRedirect(request, response, redirectUrl);
	}
	//Getters and setters:
	public String getUrlPrefix() {
		return this.urlPrefix;
	}

	public void setUrlPrefix(final String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public String getFormUsernameKey() {
		return this.formUsernameKey;
	}

	public void setFormUsernameKey(final String formUsernameKey) {
		this.formUsernameKey = formUsernameKey;
	}

	@Override
	public RedirectStrategy getRedirectStrategy() {
		return this.redirectStrategy;
	}

	@Override
	public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
}
