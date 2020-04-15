package net.zerodrive.natural.njx.controls.custom;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.softwareag.cis.server.Adapter;
import com.softwareag.cis.server.DefaultAdapterListener;
import com.softwareag.cis.server.IDynamicAccess;

public class JSONAdapterListener extends DefaultAdapterListener {

	private Adapter m_adapter = null;
	private IDynamicAccess m_dynamic = null;

	@Override
	public void init(Adapter adapter) {
		super.init(adapter);
		m_adapter = adapter;
		if (adapter instanceof IDynamicAccess) {
			m_dynamic = (IDynamicAccess) m_adapter;
		}
	}

	@Override
	public void reactOnInvokePhaseStart() {
		super.reactOnInvokePhaseStart();
		String[] props = m_dynamic.findDynamicAccessProperties();
		for (int i = 0; i < props.length; i++) {
			if (props[i].endsWith(CONST.JSONSUFFIX)) {
				String value = (String) m_dynamic.getPropertyValue(props[i]);
				if (value != null && !value.isEmpty()) {
					JsonObject jobj = null;
					try {
						jobj = (JsonObject) Jsoner.deserialize(value);
					} catch (JsonException e) {
						e.printStackTrace();
					}
					String key = props[i].substring(0, props[i].lastIndexOf(CONST.JSONSUFFIX));
					IDynamicAccess dynamic = (IDynamicAccess) m_dynamic.getPropertyValue(key);
					jobj.forEach(new JsonObjectConsumer(dynamic));
				}
			}
		}
	}

	@Override
	public void reactOnInvokePhaseEnd() {
		super.reactOnInvokePhaseEnd();
		String[] props = m_dynamic.findDynamicAccessProperties();
		for (int i = 0; i < props.length; i++) {
			if (props[i].endsWith(CONST.JSONSUFFIX)) {
				String key = props[i].substring(0, props[i].lastIndexOf(CONST.JSONSUFFIX));
				IDynamicAccess dynamic = (IDynamicAccess) m_dynamic.getPropertyValue(key);
				NdObject nobj = new NdObject(dynamic);
				JsonObject jobj = new JsonObject();
				nobj.forEach(new NdObjectConsumer(jobj));
				String json = Jsoner.serialize(jobj);
				Object pretty = m_dynamic.getPropertyValue(key + CONST.PRETTYSUFFIX);
				if (pretty != null && pretty instanceof Boolean && ((Boolean) pretty).booleanValue() == true) {
					json = Jsoner.prettyPrint(json);
				}
				m_dynamic.setPropertyValue(props[i], json);
			}
		}
	}

}
