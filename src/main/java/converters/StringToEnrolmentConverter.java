
package converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.EnrolmentRepository;
import domain.Enrolment;

@Component
@Transactional
public class StringToEnrolmentConverter implements Converter<String, Enrolment> {

	@Autowired
	EnrolmentRepository	enrolmentRepository;


	@Override
	public Enrolment convert(final String text) {
		Enrolment result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.enrolmentRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
