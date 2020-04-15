package net.zerodrive.natural.njx.controls.custom;

import java.util.function.Consumer;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.softwareag.cis.server.IDynamicAccess;

public class NdArrayConsumer implements Consumer<Object> {

	private JsonArray m_jarr;

	public NdArrayConsumer(JsonArray jarr) {
		super();
		this.m_jarr = jarr;
	}

	@Override
	public void accept(Object value) {
		if (value instanceof IDynamicAccess) {
			if (value instanceof ITEMSInfo) {
				NdArray narr = new NdArray((ITEMSInfo) value);
				JsonArray jarr = new JsonArray();
				m_jarr.add(jarr);
				narr.forEach(new NdArrayConsumer(jarr));
			} else {
				NdObject nobj = new NdObject((IDynamicAccess) value);
				if (nobj.size() == 1 && nobj.containsKey(CONST.DATAPROP)) {
//					Special case: An array of values.
					m_jarr.add(nobj.get(CONST.DATAPROP));
				} else {
//					An array of objects.
					JsonObject jobj = new JsonObject();
					m_jarr.add(jobj);
					nobj.forEach(new NdObjectConsumer(jobj));
				}
			}
		}
	}

}
