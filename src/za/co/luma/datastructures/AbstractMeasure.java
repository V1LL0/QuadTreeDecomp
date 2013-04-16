package za.co.luma.datastructures;

/**
	A class that contains the methods for doing implementation specific calculations for constructing a QuadTree.
*/
public abstract class AbstractMeasure<T>
{
	/**
		Measures the detail in a region.
	*/
	public abstract double measureDetail(T data[][], int x, int y, int width, int height);
	
	/**
		Calculates an approcimation for a region.
	*/
	public abstract T approximate(T data[][], int x, int y, int width, int height);
	
}
