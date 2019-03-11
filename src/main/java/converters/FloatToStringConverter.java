
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Float;

@Component
@Transactional
public class FloatToStringConverter implements Converter<Float, String> {

	@Override
	public String convert(final Float floatE) {
		String result;

		if (floatE == null)
			result = null;
		else
			result = String.valueOf(floatE.getId());
		return result;
	}

}
