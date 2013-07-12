package snd_master;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Map {

	static final String levelNames[]={"Newbie","Master","Expert"};
	final String mapName = "soundmaster.map";
	String path;
	private ArrayList theMap[] = {new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>()};
	
	public Map(String path) {
		this.path = path+"/"+mapName;
//		try to load map
		if (new File(this.path).exists() && load())
			;
		else
			for (int complexity=0;complexity<3;++complexity){
				addLvl(complexity);
			}
	}

	
	public void updateLvl(int complexity, int index, String newValue){
		theMap[complexity].set(index, newValue);
	}
	
	public void addLvl(int complexity){
		String emptyLvlMap="ZZ";
		for (int i=0;i<99;++i){
			emptyLvlMap+=",ZZ";
		}
		theMap[complexity].add(emptyLvlMap);
	}
	
	public int getLvlMax(int complexity){
		return theMap[complexity].size()-1; 
	}
	
	public String getLvlMap(int complexity, int mapIndex){
		return (String)theMap[complexity].get(mapIndex);
	}
	
	
	public void save() throws ParserConfigurationException, TransformerException{
		/*
		временно
		*/
		
		for (int i=0;i<3;i++){
			System.out.println(levelNames[i]);
			for (int j=0;j<theMap[i].size();j++){
				String str = theMap[i].get(j).toString();
				System.out.print("[");
				for (String s : str.split(",")){
					System.out.print("'"+s+"',");
				}
				System.out.println("]");				
			}
		}
		
		if (true) return;
		
		
//create elements
		Document mapDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element rootElement = mapDoc.createElement("map");
		mapDoc.appendChild(rootElement);
		for (int complex=0;complex<theMap.length;complex++){
			Element complexityMap= mapDoc.createElement("complexity");
			complexityMap.setAttribute("name", levelNames[complex]);
			rootElement.appendChild(complexityMap);
			for (int i=0;i<=getLvlMax(0);i++){
				Element el = mapDoc.createElement("level");
				el.setAttribute("num", ""+i);
				el.appendChild(mapDoc.createTextNode(theMap[complex].get(i).toString()));
				complexityMap.appendChild(el);
			}
		}
//write the content into xml file
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
		DOMSource source = new DOMSource(mapDoc);

		StreamResult result =  new StreamResult(new File(this.path));
		transformer.transform(source, result);

	}
	
	private boolean load(){
		try {
			Document mapDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
//
//			make validation here ....
//			
			
			NodeList mapNodes = mapDoc.getDocumentElement().getElementsByTagName("complexity");
			for (int complex=0; complex < mapNodes.getLength(); complex++){
				NodeList lvlNodes = ((Element) mapNodes.item(complex)).getElementsByTagName("level");
				for (int i=0; i < lvlNodes.getLength(); i++){
					Element el = (Element) lvlNodes.item(i);
					theMap[complex].add(el.getTextContent());
				}
			}
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
