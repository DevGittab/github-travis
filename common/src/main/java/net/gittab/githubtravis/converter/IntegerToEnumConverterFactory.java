package net.gittab.githubtravis.converter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import net.gittab.githubtravis.enums.BaseEnum;

/**
 * IntegerToEnumConverterFactory.
 *
 * @author xiaohua zhou
 **/
public class IntegerToEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

	private static final Map<Class, Converter> CONVERTERS = new HashMap<>();

	@Override
	public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
		Converter<String, T> converter = CONVERTERS.get(targetType);
		if (converter == null) {
			converter = new IntegerToEnumConverter<>(targetType);
			CONVERTERS.put(targetType, converter);
		}
		return converter;
	}

}
