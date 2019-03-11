
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.PositionBrotherhood;

@Component
@Transactional
public class PositionBrotherhoodToStringConverter implements Converter<PositionBrotherhood, String> {

	@Override
	public String convert(final PositionBrotherhood positionBrotherhood) {
		String result;

		if (positionBrotherhood == null)
			result = null;
		else
			result = String.valueOf(positionBrotherhood.getId());
		return result;
	}

}
