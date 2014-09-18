package gr.ntua.cslab.celar.SlipstreamClient;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXStateHandler extends DefaultHandler {
	
	public String state;

	public SAXStateHandler() {
		super();
		state = "Not Found";
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
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

			//System.out.println(content);
	}


}
