package net.zerodrive.natural.njx.controls.custom;

import java.util.HashMap;

import com.softwareag.cis.server.IDynamicAccess;

public class NdObject extends HashMap<String, Object> {

	private static final long serialVersionUID = -4782610244242686318L;
	
	public NdObject() {
		super();
	}
	
	public NdObject(IDynamicAccess dynamic) {
		super();
		String[] props = dynamic.findDynamicAccessProperties();
		for (int i = 0; i < props.length; i++) {
			this.put(props[i], dynamic.getPropertyValue(props[i]));
		}
	}
	
}
