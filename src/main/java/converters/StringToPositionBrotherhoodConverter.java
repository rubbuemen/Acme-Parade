
package converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.PositionBrotherhoodRepository;
import domain.PositionBrotherhood;

@Component
@Transactional
public class StringToPositionBrotherhoodConverter implements Converter<String, PositionBrotherhood> {

	@Autowired
	PositionBrotherhoodRepository	positionBrotherhoodRepository;


	@Override
	public PositionBrotherhood convert(final String text) {
		PositionBrotherhood result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.positionBrotherhoodRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
