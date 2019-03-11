
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.RequestMarch;

@Component
@Transactional
public class RequestMarchToStringConverter implements Converter<RequestMarch, String> {

	@Override
	public String convert(final RequestMarch requestMarch) {
		String result;

		if (requestMarch == null)
			result = null;
		else
			result = String.valueOf(requestMarch.getId());
		return result;
	}

}
