
package converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.RequestMarchRepository;
import domain.RequestMarch;

@Component
@Transactional
public class StringToRequestMarchConverter implements Converter<String, RequestMarch> {

	@Autowired
	RequestMarchRepository	requestMarchRepository;


	@Override
	public RequestMarch convert(final String text) {
		RequestMarch result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.requestMarchRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
