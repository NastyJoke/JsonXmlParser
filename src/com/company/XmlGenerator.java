package com.company;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.json.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Renaud on 12/05/2016.
 */
public class XmlGenerator {
    private static String xml;
    private static String[] resources = {"FISH", "FLOWER", "FRUITS", "FUR", "ORE", "QUARTZ", "SUGAR_CANE", "WOOD"};
    private static String inputPath = "C:\\Users\\user\\Documents\\Cours\\JsonXmlParser\\src\\com\\company\\log.json";
    private static String outputPath = "C:\\Users\\user\\Documents\\Cours\\JsonXmlParser\\src\\com\\company\\output.xml";
    private static Map<String, Integer> balisesMap = new HashMap<>();


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

    private static void statistiques(){
        String balises = "";
        boolean flag = false;
        for (int i = 0; i < xml.length(); i++) {
            if(flag && xml.charAt(i) != '>' && xml.charAt(i) != '/' && xml.charAt(i)!='<')
                balises += xml.charAt(i);
            else if(xml.charAt(i)=='<') {
                flag = true;
            }
            else {
                flag = false;
                if(!balises.equals("")) {
                    if (balisesMap.containsKey(balises)) {
                        balisesMap.put(balises, balisesMap.get(balises) + 1);
                    } else {
                        balisesMap.put(balises, 1);
                    }
                    balises = "";
                }
            }
        }
        String statXml = "<statistiques><stat>Statistiques</stat><liste>";
        int incr = 0;
        for (String str: balisesMap.keySet()) {
            if(str.contains("array"))
                statXml+=("<"+str+">"+balisesMap.get(str)+"</array>");
            else
                statXml+=("<"+str+">"+balisesMap.get(str)+"</"+str+">");

            incr+=balisesMap.get(str);
        }
        statXml+="</liste><total>Nombre total de balises : "+incr+"</total>";
        statXml+=distance();
        double tab[] = paInfo();
        statXml+="<restant>"+tab[0]+"</restant><cout_moyen>"+tab[1]+"</cout_moyen>";
        statXml += "</statistiques>";
        xml = statXml.concat(xml);
    }

    private static String distance(){
        int distance = decompte("fly", 3) + decompte("move_to");
        String dist = "<distance> Distance Parcourue : "+ distance+"</distance>";
        return dist;
    }

    private static int decompte(String action, int val){
        int act = 0;
        int incr = 0;
        while(act != -1){
            act = xml.indexOf(action,act+1);
            incr+=val;
        }
        return incr;
    }

    private static int decompte(String action){
        return decompte(action, 1);
    }

    private static void editXML(JSONArray json) throws  JSONException{
        xml = XML.toString(json);


        xml = xml.replaceAll("contracts", "contract");
        xml = xml.replaceAll("(<contract>.*</contract>)", "<contracts>$1</contracts>");
        xml = xml.replaceFirst("<array>", "<array id=\"init\">");
        xml = xml.replaceAll("<array><data><cost>", "<array id=\"from_server\"><data><cost>");
        xml = xml.replaceAll("<array><data><action>", "<array id=\"from_client\"><data><action>");
        xml = xml.replaceAll("<extras><resources>", "<extras><explored_resources><resources>");
        xml = xml.replaceAll("</altitude><resources>", "</altitude><explored_resources><resources>");
        xml = xml.replaceAll("</resources></extras>", "</resources></explored_resources></extras>");
        xml = xml.replaceAll("</resources><pois>", "</resources></explored_resources><pois>");
        String biomes = "<biomes>";
        int i = 0;
        System.out.print("Counting biomes");
        while(xml.contains(biomes)){
            xml = xml.replaceFirst("<biomes>", "<biome>");
            xml = xml.replaceFirst("</biomes>", "</biome>");
            xml = xml.replaceFirst("<extras><biome>", "<extras><found_biomes><biome>");
            xml = xml.replaceFirst("</biome></extras>", "</biome></found_biomes></extras>");
        }
        System.out.println("Done!");
        xml = xml.replaceAll("</creeks><biome>","</creeks><found_biomes><biome>");
        xml = xml.replaceAll("transform</action><parameters>", "transform</action><parameters><craft_resources>");
        for (String res: resources) {
            xml = xml.replaceAll("</"+res+"></parameters>", "</"+res+"></craft_resources></parameters>");
        }

    }
    private static void finalTouch(){
        String log = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/css\" href=\"../../../island.css\" ?><log>";
        xml = log.concat(xml);
        log = "</log>";
        xml = xml.concat(log);
        xml = xml.replaceAll(">",">\n");
    }


    public static double[] paInfo()
    {
        int budget = Integer.parseInt(xml.substring(xml.indexOf("<budget>")+8,xml.indexOf("</budget>")));
        int costSum = 0;
        int nbActions = 0;
        int cost;
        int offset = 0;
        while (xml.indexOf("<cost>",offset) != -1)
        {
            nbActions++;
            offset = xml.indexOf("<cost>",offset)+6;
            cost = Integer.parseInt(xml.substring(offset,xml.indexOf("</cost>",offset)));
            budget -= cost;
            costSum += cost;
        }
        double average = costSum/nbActions;
        return new double[] {budget,average};
    }


    public static void main(String args[]){


        try {

            JSONArray json = new JSONArray(readFile(inputPath));

            editXML(json);
            statistiques();
            finalTouch();
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


}
