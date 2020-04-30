package net.gittab.githubtravis.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * DateRangeValidator.
 *
 * @author xiaohua zhou
 **/
public class DateRangeValidator implements ConstraintValidator<CheckDateRange, Object> {

	private String startTime;

	private String endTime;

	@Override
	public void initialize(CheckDateRange constraintAnnotation) {
		this.startTime = constraintAnnotation.startTime();
		this.endTime = constraintAnnotation.endTime();
	}

	@Override
	public boolean isValid(Object object,
			ConstraintValidatorContext constraintValidatorContext) {
		if (object == null) {
			return true;
		}
		BeanWrapper beanWrapper = new BeanWrapperImpl(object);
		Object start = beanWrapper.getPropertyValue(this.startTime);
		Object end = beanWrapper.getPropertyValue(this.endTime);
		if (null == start || null == end) {
			return true;
		}
		return ((Date) end).after((Date) start);
	}

}
