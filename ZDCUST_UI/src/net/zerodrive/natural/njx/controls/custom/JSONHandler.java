package net.zerodrive.natural.njx.controls.custom;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.xml.sax.AttributeList;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.softwareag.cis.gui.generate.ITagHandler;
import com.softwareag.cis.gui.generate.IXSDGenerationHandler;
import com.softwareag.cis.gui.protocol.Message;
import com.softwareag.cis.gui.protocol.ProtocolItem;

public class JSONHandler implements ITagHandler {
	private String m_jsonobject = null;
	private String m_jsonmetadata = null;
	private boolean m_jsonpretty = false;

	public void generateHTMLForStartTag(int id, String tagName, AttributeList attrlist, ITagHandler[] handlersAbove,
			StringBuffer html, ProtocolItem pi) {
		readAttributes(attrlist);
		checkAttributes(pi);
		addDataBinding(pi);
	}

	public void generateHTMLForEndTag(String tagName, StringBuffer html) {
	}

	public void generateJavaScriptForInit(int id, String tagName, StringBuffer html) {
	}

	private void readAttributes(AttributeList attrlist) {
		for (int i = 0; i < attrlist.getLength(); i++) {
			if (attrlist.getName(i).equals("jsonobject"))
				m_jsonobject = attrlist.getValue(i);
			if (attrlist.getName(i).equals("jsonmetadata"))
				m_jsonmetadata = attrlist.getValue(i);
			if (attrlist.getName(i).equals("jsonpretty"))
				m_jsonpretty = attrlist.getValue(i).contentEquals("true");
		}
	}

	private void checkAttributes(ProtocolItem pi) {
		if (m_jsonobject == null)
			pi.addMessage(new Message(Message.TYPE_ERROR, "Attribute jsonobject is not set."));
		if (m_jsonmetadata == null)
			pi.addMessage(new Message(Message.TYPE_ERROR, "Attribute jsonmetadata is not set."));
	}

	private void addDataBinding(ProtocolItem pi) {
		IXSDGenerationHandler xgh = pi.findXSDGenerationHandler();
		if (xgh == null) {
			pi.addMessage(new Message(Message.TYPE_ERROR, "XSDGenerationHandler not found"));
		} else {
			xgh.addAdapterData(pi, m_jsonobject + CONST.JSONSUFFIX, "xs:string");
			pi.addProperty(m_jsonobject + CONST.JSONSUFFIX, "xs:string");
			if (m_jsonpretty == true) {
				xgh.addAdapterData(pi, m_jsonobject + CONST.PRETTYSUFFIX, "xs:boolean");
				pi.addProperty( m_jsonobject + CONST.PRETTYSUFFIX, "xs:boolean");
			}
			ArrayList<String> levels = new ArrayList<String>();
			levels.add(m_jsonobject);
			getJsonObject(pi).forEach(new JsonMetaObjectConsumer(levels, false, xgh, pi));
		}
	}

	private JsonObject getJsonObject(ProtocolItem pi) {
		URL myUrl = this.getClass().getResource(this.getClass().getSimpleName() + ".class");
		String myPath = myUrl.getPath();
		int i = myPath.indexOf("appclasses");
		Path path = Paths.get(myPath.substring(0, i), CONST.JSONDIR, m_jsonmetadata);
		JsonObject obj = null;
		try (FileReader reader = new FileReader(path.toString())) {
			obj = (JsonObject) Jsoner.deserialize(reader);
		} catch (JsonException e) {
			pi.addMessage(new Message(Message.TYPE_ERROR, "File " + path + " contains no valid JSON data."));
		} catch (FileNotFoundException e) {
			pi.addMessage(new Message(Message.TYPE_ERROR, "File " + path + " not found."));
		} catch (IOException e) {
			pi.addMessage(new Message(Message.TYPE_ERROR, "File " + path + " could not be opened."));
		}
		return obj;
	}
}