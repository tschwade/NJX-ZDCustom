package net.zerodrive.natural.njx.controls.custom;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.softwareag.cis.server.IDynamicAccess;

public class NdObjectConsumer implements BiConsumer<String, Object> {

	private JsonObject m_jobj;

	public NdObjectConsumer(JsonObject jobj) {
		super();
		this.m_jobj = jobj;
	}

	@Override
	public void accept(String key, Object value) {
		if (value instanceof IDynamicAccess) {
			if (value instanceof ITEMSInfo) {
				NdArray narr = new NdArray((ITEMSInfo) value);
				JsonArray jarr = new JsonArray();
				m_jobj.put(Jsoner.mintJsonKey(key, null), jarr);
				narr.forEach(new NdArrayConsumer(jarr));
			} else {
				NdObject nobj = new NdObject((IDynamicAccess) value);
				JsonObject jobj = new JsonObject();
				m_jobj.put(Jsoner.mintJsonKey(key, null), jobj);
				nobj.forEach(new NdObjectConsumer(jobj));
			}
		} else if (value instanceof String || value instanceof Boolean || value instanceof Short
				|| value instanceof Integer || value instanceof Long || value instanceof BigDecimal
				|| value instanceof Float || value instanceof Double) {
			m_jobj.put(Jsoner.mintJsonKey(key, null), value);
		}
	}

}
