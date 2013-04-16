package za.co.luma.datastructures;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
	A class that can write a QuadTee to XML.
	
	@author Herman Tulleken
*/
public class QuadTreeWriter
{
	/**
		Writes a QuadTree to XML.
	*/
	static <T> void writeQuadTree(QuadTree<T> tree, OutputStreamWriter writer) throws IOException
	{
		writer.write("<QuadTree>");
		writeQuadTreeNode(tree.root, writer);
		writer.write("</QuadTree>");
	}
	
	/**
		Wrties a QuadTree.Node to XML.
	*/
	static <T> void writeQuadTreeNode(QuadTree<T>.Node node, OutputStreamWriter writer) throws IOException
	{
		writer.write("<Node>");
		
		if (node.children == null)
		{
			writer.write(node.value.toString());
		}
		else
		{
			for(Object o : node.children)
			{
				writeQuadTreeNode((QuadTree<T>.Node) o, writer); 
				
			}
		}
		
		writer.write("</Node>");
	}

}
