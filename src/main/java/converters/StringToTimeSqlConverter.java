
package converters;

import java.sql.Time;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StringToTimeSqlConverter implements Converter<String, Time> {

	@Override
	public Time convert(final String text) {
		Time result;

		if (text == null)
			result = null;
		else
			try {
				result = Time.valueOf(text + ":00");
			} catch (final Throwable oops) {
				throw new IllegalArgumentException(oops);
			}
		return result;
	}

}
