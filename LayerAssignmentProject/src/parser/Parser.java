package parser;
import org.eclipse.elk.graph.ElkNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.nio.charset.StandardCharsets;

public class Parser 
{
	public static List<String> nodes;
	public static Map<String, ArrayList<String>> edges;
	public static ElkNode parse(String filename) throws IOException
	{
		nodes = new ArrayList<String>();
		edges = new HashMap<String, ArrayList<String>>();
		List<String >lines = readSmallTextFile(filename);

		
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
		return null;
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
