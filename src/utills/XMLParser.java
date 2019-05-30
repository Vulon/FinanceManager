package utills;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import dataStructure.Category;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class XMLParser{
    private Document doc;
    public XMLParser()throws Exception{
        File file = new File(Paths.get("src", "config", "cat.xml").toUri());
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = builder.parse(file);
        doc.getDocumentElement().normalize();
    }
    public ArrayList<Category> getNesIncomes(){
        ArrayList<Category> arrayList = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName("nes_income");
        for(int i = 0; i < nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element e = (Element)node;
                String name = e.getAttribute("id");
                String line = node.getTextContent().trim();
                line = line.replaceAll(" ", "");
                String values[] = line.split("\n");//0 - number, 1 - color
                values[1] = values[1].trim();
                String image = values[0];
                int iconId = Integer.parseInt(image);
                int id = Integer.parseInt(values[0]);

                Category category = new Category(id, name, values[1], iconId, true);
                arrayList.add(category);
            }
        }

        return  arrayList;
    }
    public ArrayList<Category> getNesExpenses(){
        ArrayList<Category> arrayList = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName("nes_expense");
        for(int i = 0; i < nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element e = (Element)node;
                String name = e.getAttribute("id");
                String line = node.getTextContent().trim();
                line = line.replaceAll(" ", "");
                String values[] = line.split("\n");//0 - number, 1 - color
                values[1] = values[1].trim();
                String image = values[0];
                int id = Integer.parseInt(values[0]);

                Category category = new Category(id, name, values[1], id, false);
                arrayList.add(category);
            }
        }
        return  arrayList;
    }


}