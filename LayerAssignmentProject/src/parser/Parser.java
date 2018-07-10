package parser;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;

import src.tdojlafry.layered.layerAssignment.gui.LayerAssignmentComputationManager;

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
//	aehnlich. einer Adjazenzliste enhaelt allerdings nur Knoten die auch min. einen adjazenten Knoten besitzen.
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
			}	
		}
		
		//Falls ein zyklische Graph  vorliegt, kommt der layering Algo. damit nicht klar,
		//dies wird hier abgefangen
		if(LayerAssignmentComputationManager.isCyclic(nodes, edges) == true)
		{
			return null;
		}
		
	
		
//		Alle Nodes zu ElkNodes machen
        for (int i = 0; i < nodes.size(); i++) {
            ElkNode newNode = ElkGraphUtil.createNode(parentNode);
            ElkLabel newLabel = ElkGraphUtil.createLabel(newNode);
            newLabel.setText(nodes.get(i));            
        }

       
        for (int i = 0; i < parentNode.getChildren().size(); i++) 
        {
//        	Iteator um über alle ElkNodes zu iterieren
        	Iterator<ElkNode> elkNodes = parentNode.getChildren().iterator();

            ElkNode currNode = elkNodes.next();
//          Speichere edges die zur jetzigen Node gehören
        	List<String> currentEdges = edges.get(nodes.get(i));
        	if(currentEdges != null)
        	{

//        		Uber alle kanten iterieren
        		for(int j = 0; j < currentEdges.size(); j++)
            	{
//        			Kante erzeugen
            		ElkEdge newEdge = ElkGraphUtil.createEdge(parentNode);
//            		Start der Kante initialiseren
            		for(ElkNode child : parentNode.getChildren())
            		{
            			if(child.getLabels().get(0).getText().equals(nodes.get(i)))
            			{
            				newEdge.getSources().add(child);
            			}
            		}
        		
//        			Ueber alle elkNodes iterieren
        		
            		do
            		{      
            			try {
            				currNode = elkNodes.next();
            			} catch (Exception e)
            			{
            				elkNodes = parentNode.getChildren().iterator();
            	            currNode = elkNodes.next();
            			}
            			
            			
//            			Wenn die current elkNode den gleichens Namen wie einer der Nodes in der Adjazenzliste, ex. eine Kante zwischen ihnen
            			if(currNode.getLabels().get(0).getText().equals(currentEdges.get(j)))
            			{
//            			    System.out.println("Kannte hinzugefuegt");
            				newEdge.getTargets().add(currNode);
            			}
//            			Naechsten Node checken    

            		} while(elkNodes.hasNext());
            	}
        	}
   		 
        }



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
