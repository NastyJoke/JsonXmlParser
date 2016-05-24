package com.company;
import org.json.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Renaud on 12/05/2016.
 */
public class XmlGenerator {
    private static String xml;
    private static String inputPath = "C:\\Users\\user\\Documents\\Cours\\JsonXmlParser\\src\\com\\company\\log.json";
    private static String outputPath = "C:\\Users\\user\\Documents\\Cours\\JsonXmlParser\\src\\com\\company\\output.xml";

    private static String readFile(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, "UTF-8");
    }

    private static void saveToXml() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
        writer.print(xml);
        writer.close();
    }


    public static void main(String args[]){


        try {

            JSONArray json = new JSONArray(readFile(inputPath));

            editXML(json);
            saveToXml();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){//L'excepction demandée est JSONException mais ça ne marche pas :(
            e.printStackTrace();
        }

    }

    private static void editXML(JSONArray json) throws  JSONException{
        xml = XML.toString(json);
        String log = "<?xml-stylesheet type=\"text/css\" href=\"island.css\" ?>\n<log>\n";
        xml = log.concat(xml);
        log = "</log>";
        xml = xml.concat(log);

        xml = xml.replaceAll("contracts", "contract");
        xml = xml.replaceAll("(<contract>.*</contract>)", "<contracts>$1</contracts>");
        xml = xml.replaceFirst("<array>", "<array id=\"init\">");
        xml = xml.replaceAll("<array><data><cost>", "<array id=\"from_server\"><data><cost>");
        xml = xml.replaceAll("<array><data><action>", "<array id=\"from_client\"><data><action>");

        xml = xml.replaceAll(">",">\n");
    }
}
