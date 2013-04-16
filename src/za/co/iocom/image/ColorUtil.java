package za.co.iocom.image;

import java.awt.Color;

public class ColorUtil
{

	public static int grey(int rgb)
	{
		return (red(rgb) + green(rgb) + blue(rgb)) / 3;
	}

	/**
	 * Returns the blue component of a integer presentation of a color. The
	 * component is some value between 0 and 255.
	 * 
	 * @param c
	 *            The color to analize
	 * @return The blue component of the color.
	 */
	public static int blue(int c)
	{
		return c & 0xFF;
	}

	/**
	 * Returns the green component of a integer presentation of a color. The
	 * component is some value between 0 and 255.
	 * 
	 * @param c
	 *            The color to analyze
	 * @return The green component of the color.
	 */
	public static int green(int c)
	{
		return (c >> 8) & 0xFF;
	}

	/**
	 * Constructs a grey color with the intensity specified. Convenience for
	 * rgb(grey, grey, grey)
	 * 
	 * @param grey
	 *            intensity of grey, between 0 and 255
	 * @return an integer that represents a grey with the specified intensity
	 */
	public static int greyToRGB(int grey)
	{
		return rgb(grey, grey, grey);
	}

	/**
	 * Returns the red component of a integer presentation of a color. The
	 * component is some value between 0 and 255.
	 * 
	 * @param c
	 *            The color to analyze
	 * @return The red component of the color.
	 */
	public static final int red(int c)
	{
		return (c >> 16) & 0xFF;
	}

	/**
	 * Returns the alpha component of a integer presentation of a color. The
	 * component is some value between 0 and 255.
	 * 
	 * @param c
	 *            The color to analyze
	 * @return The red component of the color.
	 */
	public static final int alpha(int c)
	{
		return (c >> 24) & 0xFF;
	}

	/**
	 * Constructs an integer representing the color with the given components,
	 * and no alpha. Each of the components should be between 0 and 255.
	 * 
	 * @param red
	 *            The red component of the color
	 * @param green
	 *            The blue component of the color
	 * @param blue
	 *            The blue component of the color
	 * @return integer presenting the color with the given components
	 */
	public static final int rgb(int red, int green, int blue)
	{
		return rgba(red, green, blue, 0xFF);
	}

	/**
	 * Constructs an integer representing the given Color.
	 * 
	 * @param c
	 *            The Color from which to compute the integer presentation.
	 * @return integer presenting the given Color
	 */
	public static final int rgba(Color c)
	{
		return rgba(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	/**
	 * Constructs an integer representing the color with the given components,
	 * and the given alpha. Each of the components should be between 0 and 255.
	 * 
	 * @param red
	 *            The red component of the color
	 * @param green
	 *            The blue component of the color
	 * @param blue
	 *            The blue component of the color
	 * @param alpha
	 *            The alpha component of the color
	 * @return integer presenting the color with the given components
	 */
	public static final int rgba(int red, int green, int blue, int alpha)
	{
		return (alpha << 24) + (red << 16) + (green << 8) + blue;
	}

	/**
	 * Returns (int) (255 * r).
	 */
	public static int normalToByte(double r)
	{
		return (int) (255 * r);
	}

}
