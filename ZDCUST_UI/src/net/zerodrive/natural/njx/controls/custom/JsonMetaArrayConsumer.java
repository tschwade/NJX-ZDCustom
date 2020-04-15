package net.zerodrive.natural.njx.controls.custom;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.softwareag.cis.gui.generate.IXSDGenerationHandler;
import com.softwareag.cis.gui.protocol.ProtocolItem;

class JsonMetaArrayConsumer implements Consumer<Object> {
	private ArrayList<String> m_levels;
	private IXSDGenerationHandler m_xgh;
	private ProtocolItem m_pi;

	public JsonMetaArrayConsumer(ArrayList<String> levels, IXSDGenerationHandler xgh, ProtocolItem pi) {
		super();
		this.m_levels = levels;
		this.m_xgh = xgh;
		this.m_pi = pi;
	}

	@Override
	public void accept(Object obj) {
		if (obj instanceof JsonObject) {
			StringBuffer name = new StringBuffer();
			for (int i = 0; i < m_levels.size(); i++) {
				name.append(m_levels.get(i));
				if (i < m_levels.size() - 1) {
					name.append(".");
				}
			}
			m_xgh.addControlInfoClass(m_pi, name.toString(), ITEMSInfo.class);
			m_xgh.addAdapterData(m_pi, name.toString(), IXSDGenerationHandler.TYPE_ARRAY);
			m_pi.addProperty(name.toString(), "ITEMSInfo");
			m_pi.addArrayProperty(name.toString() + ".items[*]");
			@SuppressWarnings(value = "unchecked")
			ArrayList<String> levels = (ArrayList<String>) m_levels.clone();
			((JsonObject) obj).forEach(new JsonMetaObjectConsumer(levels, true, m_xgh, m_pi));
		}
	}
}