
package converters;

import java.net.URLEncoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.GPSCoordinates;

@Component
@Transactional
public class GPSCoordinatesToStringConverter implements Converter<GPSCoordinates, String> {

	@Override
	public String convert(final GPSCoordinates gPSCoordinates) {
		String result;
		StringBuilder builder;

		if (gPSCoordinates == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append("|");
				builder.append(URLEncoder.encode(Double.toString(gPSCoordinates.getLatitude()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Double.toString(gPSCoordinates.getLongitude()), "UTF-8"));
				builder.append("|");
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}

}
