package parser;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;

import java.awt.Label;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.nio.charset.StandardCharsets;

public class Parser 
{
//	Liste von Nodes als Strings
	private static List<String> nodes;
//	Adjazenzliste
	private static Map<String, ArrayList<String>> edges;
//	The graph in elk format
	private static ElkNode parentNode;
	
//	In diese Funktion wird ein Graph der im Textformate vorliegt 
//	in ein von Elk verstandenes Format umgewandelt
//	filepath: Pfad zu der Datei aus der der Graph geladen werden soll 
	public static ElkNode parse(String filepath) throws IOException
	{
		nodes = new ArrayList<String>();
		edges = new HashMap<String, ArrayList<String>>();
//		Laden der Textdatei, jeder Zeile einspricht einem Listeneintrag
		List<String >lines = readSmallTextFile(filepath);
		parentNode = ElkGraphUtil.createNode(null);
		
		
//		Alle zeilen turchgehen
		for(String line : lines)
		{
//			Token werden durch Whitespaces getrennt z.B. node n1 bzw. edge n1 n2
			String[] tokens = line.split(" ");
			for(int pos = 0; pos < tokens.length; ++pos )
			{
				String currentToken = tokens[pos];
//				Es soll eine neue Node erstellt werden
				if(currentToken.equals("node"))
				{
//					Es wir gepeeked um den Name der Node zu erfahren die hinzugefügt werden soll
					nodes.add(peek(tokens, pos));
				}
//				Es soll eine neue Edge erstellt werden
				if(currentToken.equals("edge"))
				{
//					Name der Startnode
					String start = peek(tokens, pos);
//					Name der Endnode
					String end = peek(tokens, pos+1);
//					Fülle die Adjazenzliste
					if(edges.containsKey(start) == false)
					{
						edges.put(start, new ArrayList<String>());
						edges.get(start).add(end);
					}
					else
					{
						edges.get(start).add(end);
					}
				}
//				System.out.println("Length " + tokens.length);
//				String nextToken = peek(tokens, pos);
//				System.out.print(currentToken + " ");
//				System.out.print("next" + nextToken + " ");
				
			}
			
		}
		
		
		
	
		//		DIE KOMPLEXITÄT SUCKT
//		Alle Nodes zu ElkNodes machen
        for (int i = 0; i < nodes.size(); i++) {
            ElkNode newNode = ElkGraphUtil.createNode(parentNode);
            ElkLabel newLabel = ElkGraphUtil.createLabel(newNode);
            newLabel.setText(nodes.get(i));            
        }

        
        
        
        for (int i = 0; i < nodes.size(); i++) 
        {
//        	Iteator um über alle ElkNodes zu iterieren
        	Iterator<ElkNode> elkNodes = parentNode.getChildren().iterator();
            ElkNode currNode = elkNodes.next();
//          Speichere edges die zur jetzigen Node gehören
        	List<String> currentEdges = edges.get(i);
//        	Testen ueberhaupt Kanten zu diesem Knoten(nodes.get(i) existieren.
        	if(currentEdges != null)
        	{
//        		Uber alle kanten iterieren
        		for(int j = 0; j < currentEdges.size(); j++)
            	{
//        			Kante erzeugen
            		ElkEdge newEdge = ElkGraphUtil.createEdge(parentNode);
//            		Start der Kante initialiseren
        			newEdge.getSources().add(currNode);
//        			Ueber alle elkNodes iteriren
            		while(elkNodes.hasNext())
            		{
//            			Wenn die current elkNode den gleichen Namen wie einer der Nodes in der Adjazenzliste, ex. eine Kante zwischen ihnen
            			if(currNode.getLabels().get(0).getText() == currentEdges.get(j))
            			{
//            				Nun muss die EndNode gesetzt werden
            				newEdge.getTargets().add(currNode);
            			}
//            			Naechsten Node checken
            			currNode = elkNodes.next();
            		}
            		
            	}
        	}
        	
        }

//        Graphen der gelayered werden soll returnen
		return parentNode;
	}
	
	
	
//	Helperfunction for reading a small Textfile
	public static List<String> readSmallTextFile(String aFileName) throws IOException {
	    Path path = Paths.get(aFileName);
	    return Files.readAllLines(path, StandardCharsets.UTF_8);
	}
	
//	Return the next tokens without advancing the counter in the token stream
	public static String peek(String[] tokens, int currentPos) 
	{
		if(currentPos < tokens.length-1)
		{
			return tokens[currentPos+1];
		}
		else
		{
			return null;
		}
		   
	}
}
