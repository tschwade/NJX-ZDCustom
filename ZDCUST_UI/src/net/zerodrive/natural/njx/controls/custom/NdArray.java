package net.zerodrive.natural.njx.controls.custom;

import java.util.ArrayList;

public class NdArray extends ArrayList<Object> {

	private static final long serialVersionUID = -795553828430103281L;

	public NdArray(ITEMSInfo info) {
		super();
		Object[] items = info.getItems();
		for (int i = 0; i < items.length; i++) {
			this.add(items[i]);
		}
	}
	
}
