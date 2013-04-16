package za.co.luma.datastructures;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
	A general purpose QuadTree.
*/
public class QuadTree<T>
{
	private AbstractMeasure<T> measure;
	private double threshold;
	public Node root;
	public T defaultValue;
	
	/**
	 * The {@link AbstractMeasure} is used to measure detail, and to approximate 
	 * a quad node for whatever data type is stored in the tree. 
	 * @param data
	 * @param measure
	 * @param threshold
	 */
	public QuadTree(T data[][], AbstractMeasure<T> measure, double threshold)
	{
		this(data, measure, threshold, null);
	}
	
	public QuadTree(T data[][], AbstractMeasure<T> measure, double threshold, T defaultValue)
	{
		this.measure = measure;
		this.threshold = threshold;
		this.defaultValue = defaultValue;	
		root = new Node(data, 0, 0, data.length, data[0].length);
	}
	
	/**
		Gets the pixel data at the given coordinate.
	*/
	public T get(int i, int j)
	{
		return root.get(i, j);
	}	
	
	/**
		Counts the number of children.
	*/
	public int getChildCount(boolean countDataNodesOnly)
	{
		return root.getChildCount(countDataNodesOnly);		
	}
	
	public int getWidth()
	{
		return root.width;
	}
	
	public int getHeight()
	{
		return root.height;
	}
	
	/**
		A QuadTree node.
	*/
	public class Node
	{
		public T value;
		public Object children[];
		public int x, y;
		public int width, height;		
		
		public Node(T data[][], int x, int y, int width, int height)
		{
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			
			if((width == 1) || (height == 1) || (measure.measureDetail(data, x, y, width, height) <= threshold))
			{	
				value = measure.approximate(data, x, y, width, height);
			}
			else
			{				
				children = new Object[4];
				
				children[0] = new Node(data, x, y, width/2, height/2);
				children[1] = new Node(data, x + width/2, y, width - width/2, height/2);
				children[2] = new Node(data, x, y + height/2, width/2, height - height/2);
				children[3] = new Node(data, x + width/2, y + height / 2, width - width/2, height - height/2);
			}			
		}
		
		public T get(int i, int j)
		{
			if(value == null)//this is not a leaf
			{
				if(i < x + width/ 2)
				{
					if(j < y + height / 2)
					{
						return ((Node) children[0]).get(i, j);					
					}
					else
					{
						return ((Node) children[2]).get(i, j);
					}				
				}
				else
				{
					if(j < y + height / 2)
					{
						return ((Node) children[1]).get(i, j);					
					}
					else
					{
						return ((Node) children[3]).get(i, j);
					}	
				}
			}
			else
			{				
				if(((i == x) || (j == y)) && (defaultValue != null))
					return defaultValue;				
				else
					return value;
			}
		}
		
		public int getChildCount(boolean countDataNodesOnly)
		{
			int count = countDataNodesOnly ? 0 : 1; //self
			
			if(value == null)
			{
				count += ((Node) children[0]).getChildCount(countDataNodesOnly);
				count += ((Node) children[1]).getChildCount(countDataNodesOnly);
				count += ((Node) children[2]).getChildCount(countDataNodesOnly);
				count += ((Node) children[3]).getChildCount(countDataNodesOnly);
			}
			
			return count;
		}
	}
}
