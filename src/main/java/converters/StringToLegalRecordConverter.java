
package converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.LegalRecordRepository;
import domain.LegalRecord;

@Component
@Transactional
public class StringToLegalRecordConverter implements Converter<String, LegalRecord> {

	@Autowired
	LegalRecordRepository	legalRecordRepository;


	@Override
	public LegalRecord convert(final String text) {
		LegalRecord result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.legalRecordRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
