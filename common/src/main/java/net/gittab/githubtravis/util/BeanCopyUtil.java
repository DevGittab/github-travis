package net.gittab.githubtravis.util;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * BeanCopyUtil.
 *
 * @author xiaohua zhou 2018-11-28 8:25 PM
 */
public class BeanCopyUtil {

	private static MapperFactory mapperFactory;

	private static MapperFactory ignoreNullMapperFactory;

	static {
		mapperFactory = new DefaultMapperFactory.Builder().build();
		ignoreNullMapperFactory = new DefaultMapperFactory.Builder().mapNulls(false)
				.build();
	}

	private static MapperFacade getMapperFacade() {
		return mapperFactory.getMapperFacade();
	}

	private static MapperFacade getIgnoreNullMapperFacade() {
		return ignoreNullMapperFactory.getMapperFacade();
	}

	public static <S, D> D copyProperties(S sourceObject, Class<D> target) {
		return getMapperFacade().map(sourceObject, target);
	}

	public static <S, T> void copyProperties(S sourceObject, T target) {
		getMapperFacade().map(sourceObject, target);
	}

	public static <S, T> void copyIgnoreNull(S sourceObject, T target) {
		getIgnoreNullMapperFacade().map(sourceObject, target);
	}

}
