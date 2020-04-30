package net.gittab.githubtravis.configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.hibernate.validator.HibernateValidator;

/**
 * ValidatorConfig.
 *
 * @author xiaohua zhou
 **/
@Configuration
public class ValidatorConfig {

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation
				.byProvider(HibernateValidator.class).configure().failFast(true)
				.buildValidatorFactory();
		return validatorFactory.getValidator();
	}

}
