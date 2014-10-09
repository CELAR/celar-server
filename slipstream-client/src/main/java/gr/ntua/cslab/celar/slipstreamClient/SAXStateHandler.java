package gr.ntua.cslab.celar.slipstreamClient;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXStateHandler extends DefaultHandler {
	
	public String state, currentKey;
	public HashMap<String,String> ips;
	private boolean print;

	public SAXStateHandler() {
		super();
		state = "NOT FOUND";
		print=false;
		ips = new HashMap<String, String>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
//		System.out.println(uri+" "+qName);
//		for (int i = 0; i < attributes.getLength(); i++) {
//			System.out.println(attributes.getQName(i)+" "+attributes.getValue(i));
//		}
		switch (qName) {
		case "run":
			for (int i = 0; i < attributes.getLength(); i++) {
				if(attributes.getQName(i).equals("state")){
					state = attributes.getValue(i);
					break;
				}
				//System.out.println(attributes.getQName(i)+" : "+attributes.getValue(i));
			}
			break;
		case "runtimeParameter":
			if(attributes.getValue("key").endsWith(":hostname")){
				currentKey = attributes.getValue("key");
				print=true;
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String content = String.copyValueOf(ch, start, length).trim();
		if(print){
			ips.put(currentKey, content);
			print=false;
		}
	}


}
