package net.zerodrive.natural.njx.controls.custom;

import java.util.function.Consumer;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.softwareag.cis.server.IDynamicAccess;

public class JsonArrayConsumer implements Consumer<Object> {

	private Object[] m_items;
	private int m_index;

	public JsonArrayConsumer(IDynamicAccess dynamic) {
		this.m_items = ((ITEMSInfo) dynamic).getItems();
		this.m_index = 0;
	}

	@Override
	public void accept(Object value) {
		if (m_items != null && m_index < m_items.length) {
			IDynamicAccess dynamic = (IDynamicAccess) m_items[m_index++];
			if (value instanceof JsonArray) {
				((JsonArray) value).forEach(new JsonArrayConsumer(dynamic));
			} else if (value instanceof JsonObject) {
				((JsonObject) value).forEach(new JsonObjectConsumer(dynamic));
			} else if (value instanceof String) {
				dynamic.setPropertyValue(CONST.DATAPROP, (String) value);
			}
		}
	}

}
