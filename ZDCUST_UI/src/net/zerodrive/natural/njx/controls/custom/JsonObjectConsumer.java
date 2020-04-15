package net.zerodrive.natural.njx.controls.custom;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.softwareag.cis.server.IDynamicAccess;

public class JsonObjectConsumer implements BiConsumer<String, Object> {

	private IDynamicAccess m_dynamic;

	public JsonObjectConsumer(IDynamicAccess dynamic) {
		this.m_dynamic = dynamic;
	}

	@Override
	public void accept(String key, Object value) {
		if (value instanceof JsonArray) {
			IDynamicAccess dynamic = (IDynamicAccess) m_dynamic.getPropertyValue(key);
			((JsonArray) value).forEach(new JsonArrayConsumer(dynamic));
		} else if (value instanceof JsonObject) {
			IDynamicAccess dynamic = (IDynamicAccess) m_dynamic.getPropertyValue(key);
			((JsonObject) value).forEach(new JsonObjectConsumer(dynamic));
		} else if (value instanceof String
				|| value instanceof Boolean
				|| value instanceof BigDecimal) {
			m_dynamic.setPropertyValue(key, value);
		}
	}

}
