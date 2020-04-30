package net.gittab.githubtravis.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * DateJsonSerializer.
 *
 * @author xiaohua zhou
 **/
public class DateJsonSerializer extends JsonSerializer<Date> {

	private static ThreadLocal<SimpleDateFormat> SHORT_SDF_LOCAL = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException {
		if (!Objects.isNull(value)) {
			SimpleDateFormat dateFormat = SHORT_SDF_LOCAL.get();
			gen.writeString(dateFormat.format(value));
		}
		else {
			gen.writeString("");
		}
	}

}
