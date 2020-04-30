package net.gittab.githubtravis.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.gittab.githubtravis.converter.IntegerToEnumConverterFactory;

/**
 * InterceptorConfig.
 *
 * @author xiaohua zhou
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(new IntegerToEnumConverterFactory());
	}

}
