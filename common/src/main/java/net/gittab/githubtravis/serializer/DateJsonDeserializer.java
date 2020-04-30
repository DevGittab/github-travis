package net.gittab.githubtravis.serializer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * DateJsonDeserializer.
 *
 * @author xiaohua zhou
 **/
public class DateJsonDeserializer extends JsonDeserializer<Date> {

	private static ThreadLocal<SimpleDateFormat> SIMPLE_SDF_LOCAL = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt) {
		try {
			if (jsonParser != null && !StringUtils.isEmpty(jsonParser.getText())) {
				SimpleDateFormat dateFormat = SIMPLE_SDF_LOCAL.get();
				return dateFormat.parse(jsonParser.getText());
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
