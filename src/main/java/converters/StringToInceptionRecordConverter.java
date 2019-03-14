
package converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.InceptionRecordRepository;
import domain.InceptionRecord;

@Component
@Transactional
public class StringToInceptionRecordConverter implements Converter<String, InceptionRecord> {

	@Autowired
	InceptionRecordRepository	inceptionRecordRepository;


	@Override
	public InceptionRecord convert(final String text) {
		InceptionRecord result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.inceptionRecordRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
