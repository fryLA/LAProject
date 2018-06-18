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
//	adjazenzliste
	private static Map<String, ArrayList<String>> edges;
//	The graph in elk format
	private static ElkNode parentNode;
	
	
	public static ElkNode parse(String filename) throws IOException
	{
		nodes = new ArrayList<String>();
		edges = new HashMap<String, ArrayList<String>>();
		
		List<String >lines = readSmallTextFile(filename);
	   parentNode = ElkGraphUtil.createNode(null);
		
		
		
		for(String line : lines)
		{
			String[] tokens = line.split(" ");
			for(int pos = 0; pos < tokens.length; ++pos )
			{
				String currentToken = tokens[pos];
				if(currentToken.equals("node"))
				{
					nodes.add(peek(tokens, pos));
				}
				
				if(currentToken.equals("edge"))
				{
					String start = peek(tokens, pos);
					String end = peek(tokens, pos+1);
//					System.out.print("Start:" + start + " End:" + end + "\n");
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
        for (int i = 0; i < nodes.size(); i++) {
            ElkNode newNode = ElkGraphUtil.createNode(parentNode);
            ElkLabel newLabel = ElkGraphUtil.createLabel(newNode);
            newLabel.setText(nodes.get(i));
            
            
        }
//        
        
        
        
        for (int i = 0; i < nodes.size(); i++) 
        {
        	Iterator<ElkNode> elkNodes = parentNode.getChildren().iterator();
            ElkNode currNode = elkNodes.next();
        
        	List<String> currentEdges = edges.get(i);
        	if(currentEdges != null)
        	{
        		for(int j = 0; j < currentEdges.size(); j++)
            	{
            		ElkEdge newEdge = ElkGraphUtil.createEdge(parentNode);
        			newEdge.getSources().add(currNode);
            		while(elkNodes.hasNext())
            		{
            			if(currNode.getLabels().get(0).getText() == currentEdges.get(j))
            			{
            				newEdge.getTargets().add(currNode);
            			}
            			currNode = elkNodes.next();
            		}
            		
            	}
        	}
        	
        }

        
		return parentNode;
		
		
//		Erste alle nodes aus der Lsiste in elknodes, dann ieelk nodes durchgehen und fuer alles nodes in der liste die liste durchgehen udn die passenden edges hinzufügen
		
		
	}
	
	
	
	
	public static List<String> readSmallTextFile(String aFileName) throws IOException {
	    Path path = Paths.get(aFileName);
	    return Files.readAllLines(path, StandardCharsets.UTF_8);
	}
	
//	Return the next tokens without advancing the coutner in the token stream
	public static String peek(String[] tokens, int currentPos) 
	{
//		System.out.println("Length curr" + currentPos);
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
