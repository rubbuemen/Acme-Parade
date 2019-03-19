
package converters;

import java.sql.Time;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TimeSqlToStringConverter implements Converter<Time, String> {

	@Override
	public String convert(final Time time) {
		String result;
		StringBuilder builder;

		if (time == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(time.toString().substring(0, 5));
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}

}
