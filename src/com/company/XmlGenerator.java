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

    static String readFile(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, "UTF-8");
    }

    public static void main(String args[]){
        String inputPath = "C:\\Users\\user\\Documents\\Cours\\JsonXmlParser\\src\\com\\company\\log.json";
        String outputPath = "C:\\Users\\user\\Documents\\Cours\\JsonXmlParser\\src\\com\\company\\output.xml";

        try {

            JSONArray json = new JSONArray(readFile(inputPath));

            String xml = XML.toString(json);
            xml = xml.replaceAll(">",">\n");
            String log = "<log>\n";
            xml = log.concat(xml);
            log = "</log>";
            xml = xml.concat(log);

            PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
            writer.print(xml);
            writer.close();
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
}
