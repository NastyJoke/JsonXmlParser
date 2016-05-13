package com.company;
import org.json.*;

import javax.crypto.BadPaddingException;
import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.*;
import org.w3c.dom.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.IllegalFormatFlagsException;
import java.util.Stack;

/**
 * Created by Renaud on 12/05/2016.
 */
public class XmlGenerator {
    public String file;

    public XmlGenerator(String jsonObject){
        file = jsonObject;
    }

    public void saveToXML(String xml) {//Path of the output file
        Document dom;
        Element e = null;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();

            // create the root element
            Element rootEle = dom.createElement("log");

            parseJson(dom, e, rootEle);
            // create data elements and place them under root
           /* e = dom.createElement("role1");
            e.appendChild(dom.createTextNode(role1));
            rootEle.appendChild(e);

            e = dom.createElement("role2");
            e.appendChild(dom.createTextNode(role2));
            rootEle.appendChild(e);

            e = dom.createElement("role3");
            e.appendChild(dom.createTextNode(role3));
            rootEle.appendChild(e);

            e = dom.createElement("role4");
            e.appendChild(dom.createTextNode(role4));
            rootEle.appendChild(e);*/

            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "isla.dtd");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(xml)));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private void parseJson(Document dom, Element e, Element rootEle) throws IllegalFormatFlagsException {
        String label = "log";
        String value = "";
        String label_id = "";
        boolean label_flag = false;//Dans quelle string j'ecrit?
        boolean string_flag = false;//Sommes nous dans une string?
        Stack stack = new Stack();
        stack.add(new Node(true, label));
        for(int i = 0; i<file.length();i++){
            char current = file.charAt(i);
            if(string_flag){
                if(current == '"') {
                    string_flag = false;
                    e = dom.createElement(label);
                    e.appendChild(dom.createTextNode(value));
                    rootEle.appendChild(e);
                    label = "";
                    value = "";
                    label_id = "";
                }else if(!label_flag)
                    label = label+current;
                else
                    value = value+current;


            }else{
                if(current == '{') {
                    Node n = (Node) stack.peek();
                    stack.add(new Node(false, n.getCurrentFatherNode()));
                    label_flag = false;
                }

                if(current == '[') {
                    Node n = (Node) stack.peek();
                    stack.add(new Node(true, n.getCurrentFatherNode()));
                    label_flag = false;
                }

                if(current == '}'){
                    Node node = (Node) stack.pop();
                    if (node.isParenthesis())
                        throw new IllegalFormatFlagsException("Missing '}' ");
                }

                if(current == ']'){
                    Node node = (Node) stack.pop();
                    if (!node.isParenthesis())
                        throw new IllegalFormatFlagsException("Missing ']' ");
                }

                if(current == '"')
                    string_flag = true;
            }


        }
    }


    private String getTextValue(String def, Element doc, String tag) {
        String value = def;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }


    public static void main(String args[]){
        String js = " [{\n" +
                "  \"data\": {\n" +
                "    \"heading\": \"N\",\n" +
                "    \"men\": 15,\n" +
                "    \"contracts\": [\n" +
                "      {\n" +
                "        \"amount\": 50,\n" +
                "        \"resource\": \"RUM\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"amount\": 300,\n" +
                "        \"resource\": \"LEATHER\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"amount\": 500,\n" +
                "        \"resource\": \"QUARTZ\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"amount\": 10000,\n" +
                "        \"resource\": \"WOOD\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"budget\": 20000\n" +
                "  },\n" +
                "  \"part\": \"Engine\",\n" +
                "  \"time\": 1458550337338,\n" +
                "  \"meth\": \"initialize\"\n" +
                "},{\n" +
                "  \"data\": {\n" +
                "    \"action\": \"echo\",\n" +
                "    \"parameters\": {\n" +
                "      \"message\": \"Calibrating Coords\",\n" +
                "      \"direction\": \"W\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"part\": \"Explorer\",\n" +
                "  \"time\": 1458550337345,\n" +
                "  \"meth\": \"takeDecision\"\n" +
                "},{\n" +
                "  \"data\": {\n" +
                "    \"cost\": 6,\n" +
                "    \"extras\": {\n" +
                "      \"found\": \"OUT_OF_RANGE\",\n" +
                "      \"range\": 52\n" +
                "    },\n" +
                "    \"status\": \"OK\"\n" +
                "  },\n" +
                "  \"part\": \"Engine\",\n" +
                "  \"time\": 1458550337346,\n" +
                "  \"meth\": \"takeDecision\"\n" +
                "},{\n" +
                "  \"data\": {\n" +
                "    \"action\": \"echo\",\n" +
                "    \"parameters\": {\n" +
                "      \"message\": \"Determine Width\",\n" +
                "      \"direction\": \"E\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"part\": \"Explorer\",\n" +
                "  \"time\": 1458550337346,\n" +
                "  \"meth\": \"takeDecision\"\n" +
                "} ] ";

        String path = "C:\\Users\\user\\Documents\\Cours\\output.xml";

        XmlGenerator gen = new XmlGenerator(js);
        gen.saveToXML(path);

    }
}
