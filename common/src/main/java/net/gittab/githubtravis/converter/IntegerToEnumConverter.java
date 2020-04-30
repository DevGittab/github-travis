package net.gittab.githubtravis.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.convert.converter.Converter;

import net.gittab.githubtravis.enums.BaseEnum;

/**
 * IntegerToEnumConverter.
 *
 * @param <T> enum type.
 * @author xiaohua zhou
 **/
public class IntegerToEnumConverter<T extends BaseEnum> implements Converter<String, T> {

	private Map<Integer, T> enumMap = new HashMap<>();

	public IntegerToEnumConverter(Class<T> enumType) {
		T[] enums = enumType.getEnumConstants();
		for (T e : enums) {
			this.enumMap.put(e.getCode(), e);
		}
	}

	@Override
	public T convert(String source) {
		T t = this.enumMap.get(Integer.valueOf(source));
		if (Objects.isNull(t)) {
			throw new IllegalArgumentException("unable to match enum type");
		}
		return t;
	}

}
