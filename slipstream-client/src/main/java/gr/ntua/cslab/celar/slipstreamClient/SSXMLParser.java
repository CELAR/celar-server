package gr.ntua.cslab.celar.slipstreamClient;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sixsq.slipstream.statemachine.States;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Stack;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SSXMLParser {

    public static Map<String,String> parse(String document) throws Exception {
        SAXParserFactory parserFactor = SAXParserFactory.newInstance();
        SAXParser parser = parserFactor.newSAXParser();
        Map allInfo = new java.util.TreeMap<>();
        SSXMLHandler handler = new SSXMLHandler(allInfo);
        parser.parse(new ByteArrayInputStream(document.getBytes(StandardCharsets.UTF_8)), handler);
        return allInfo;
    }

    public static class SSXMLHandler extends DefaultHandler {

        Map allInfo;
        final private Stack<String> stack = new Stack();
        boolean breadCrumb = false;

        public SSXMLHandler(Map infather) {
            super();
            allInfo = infather;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {

            switch (qName) {
                case "run":
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if (attributes.getQName(i).equals("state")) {
                            String tmp = attributes.getValue(i);
                            for (States s : States.values()) {
                                if (tmp.equals(s.name())) {
                                    allInfo.put("state", s);
                                    break;
                                }
                            }
                            break;
                        }
                        //System.out.println(attributes.getQName(i)+" : "+attributes.getValue(i));
                    }
                    break;
                case "runtimeParameter":
                    // keep all runtime parameters
                    String key = attributes.getValue("key");
                    breadCrumb = true;
                    stack.push(key);
                    break;

                default:
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (!stack.isEmpty()) 
                stack.pop();
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {

            if (breadCrumb && !stack.isEmpty()) {
                String content = String.copyValueOf(ch, start, length).trim();
                String key = stack.peek();
                allInfo.put(key, content);
            }
        }

    }

}
