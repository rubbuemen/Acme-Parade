
package converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.PeriodRecordRepository;
import domain.PeriodRecord;

@Component
@Transactional
public class StringToPeriodRecordConverter implements Converter<String, PeriodRecord> {

	@Autowired
	PeriodRecordRepository	periodRecordRepository;


	@Override
	public PeriodRecord convert(final String text) {
		PeriodRecord result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.periodRecordRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
