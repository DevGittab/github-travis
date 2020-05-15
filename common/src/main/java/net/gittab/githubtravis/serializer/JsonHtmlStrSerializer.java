package net.gittab.githubtravis.serializer;

import java.io.IOException;

import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * JsonHtmlStrSerializer.
 *
 * @author xiaohua zhou
 **/
public class JsonHtmlStrSerializer extends JsonSerializer<String> {

	public JsonHtmlStrSerializer() {
	}

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException {
		if (StringUtils.isEmpty(value)) {
			jsonGenerator.writeString(value);
		}
		else {
			jsonGenerator.writeObject(HtmlUtils.htmlUnescape(value));
		}
	}

}
