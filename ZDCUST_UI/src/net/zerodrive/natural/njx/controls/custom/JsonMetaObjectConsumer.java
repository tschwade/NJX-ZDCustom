package net.zerodrive.natural.njx.controls.custom;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.softwareag.cis.gui.generate.IXSDGenerationHandler;
import com.softwareag.cis.gui.protocol.ProtocolItem;

class JsonMetaObjectConsumer implements BiConsumer<String, Object> {
	private ArrayList<String> m_levels;
	private boolean m_inArray;
	private IXSDGenerationHandler m_xgh;
	private ProtocolItem m_pi;

	public JsonMetaObjectConsumer(ArrayList<String> levels, boolean inArray, IXSDGenerationHandler xgh,
			ProtocolItem pi) {
		super();
		this.m_levels = levels;
		this.m_inArray = inArray;
		this.m_xgh = xgh;
		this.m_pi = pi;
	}

	@Override
	public void accept(String key, Object value) {
		StringBuffer name = new StringBuffer();
		if (value instanceof JsonObject) {
			@SuppressWarnings(value = "unchecked")
			ArrayList<String> levels = (ArrayList<String>) m_levels.clone();
			levels.add(key);
			((JsonObject) value).forEach(new JsonMetaObjectConsumer(levels, false, m_xgh, m_pi));
		} else if (value instanceof JsonArray) {
			@SuppressWarnings(value = "unchecked")
			ArrayList<String> levels = (ArrayList<String>) m_levels.clone();
			levels.add(key);
			((JsonArray) value).forEach(new JsonMetaArrayConsumer(levels, m_xgh, m_pi));
		} else if (value instanceof String) {
			for (int i = 0; i < m_levels.size(); i++) {
				name.append(m_levels.get(i) + ".");
			}
			if (m_inArray) {
				m_xgh.addAdapterData(m_pi, name.toString() + key, (String) value);
				m_pi.addArrayItemProperty(name.toString() + "items[*]." + key, (String) value, name.toString() + "[*]");
			} else {
				name.append(key);
				m_xgh.addAdapterData(m_pi, name.toString(), (String) value);
				m_pi.addProperty(name.toString(), (String) value);
			}
		}
	}
}