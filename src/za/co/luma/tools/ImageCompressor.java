package za.co.luma.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import za.co.iocom.image.ColorUtil;
import za.co.iocom.math.MathUtil;
import za.co.luma.datastructures.AbstractMeasure;
import za.co.luma.datastructures.QuadTree;
import za.co.luma.geom.Vector3DDouble;

/**
	Illustrates the use of QuadTrees for images.
 */
public class ImageCompressor
{
	enum Channel
	{
		RED,
		GREEN,
		BLUE
	}

	static Color[][] makeColorArray(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();

		Color colors[][] = new Color[width][height];		

		for (int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				colors[i][j] = new Color(image.getRGB(i, j));
			}
		}

		return colors;		
	}

	static Vector3DDouble[][] makeGradientArray(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();

		Vector3DDouble colors[][] = new Vector3DDouble[width - 1][height - 1];	

		for (int i = 1; i < width; i++)
		{
			for(int j = 1; j < height; j++)
			{
				int color1 = image.getRGB(i, j);
				int color0 = image.getRGB(i - 1, j);
				int colorUp = image.getRGB(i, j - 1);

				int red1 = ColorUtil.red(color1);
				int green1 = ColorUtil.green(color1);
				int blue1 = ColorUtil.blue(color1);

				int red0 = ColorUtil.red(color0);
				int green0 = ColorUtil.green(color0);
				int blue0 = ColorUtil.blue(color0);

				int redUp = ColorUtil.red(colorUp);
				int greenUp = ColorUtil.green(colorUp);
				int blueUp = ColorUtil.blue(colorUp);				

				colors[i-1][j - 1] = new Vector3DDouble((2*red1 - red0 - redUp) / 1.0, (2*green1 - green0 - greenUp)/ 1.0, (2*blue1 - blue0 - blueUp)/ 1.0);
			}
		}

		return colors;
	}

	static int clampColor(double col)
	{
		if(col < 0)
			return 0;
		if (col > 255)
			return 255;
		else
			return (int) col;
	}

	static BufferedImage makeImage(QuadTree<Vector3DDouble> gradientGrid, BufferedImage firstColumn)
	{
		BufferedImage image = new BufferedImage(gradientGrid.getWidth() + 1, gradientGrid.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);

		//copy first column
		for(int j = 0; j < gradientGrid.getHeight(); j++)
			image.setRGB(0, j, /*ColorUtil.greyToRGB(128)*/firstColumn.getRGB(0, j));

		for(int i = 0; i < gradientGrid.getWidth(); i++)
			image.setRGB(i, 0, firstColumn.getRGB(i, 0));

		for(int i = 1; i < gradientGrid.getWidth() + 1; i++)
			for(int j = 1; j < gradientGrid.getHeight() + 1; j++)
			{
				int color0 = image.getRGB(i-1, j);
				int colorUp = image.getRGB(i, j-1);

				int red0 = ColorUtil.red(color0);
				int green0 = ColorUtil.green(color0);
				int blue0 = ColorUtil.blue(color0);

				int redUp = ColorUtil.red(colorUp);
				int greenUp = ColorUtil.green(colorUp);
				int blueUp = ColorUtil.blue(colorUp);		

				int newColor = ColorUtil.rgb(
						clampColor((gradientGrid.get(i-1,j-1).x + red0 + redUp)/2), 
						clampColor((gradientGrid.get(i-1,j-1).y + green0 + greenUp)/2), 
						clampColor((gradientGrid.get(i-1,j-1).z + blue0 + blueUp)/2));

				image.setRGB(i, j, newColor);
			}

		return image;

	}

	@SuppressWarnings("boxing")
	static Double[][] makeChannelArray(BufferedImage image, Channel channel)
	{
		int width = image.getWidth();
		int height = image.getHeight();

		Double colors[][] = new Double[width][height];		

		switch(channel)
		{
		case RED:
			for (int i = 0; i < width; i++)
			{
				for(int j = 0; j < height; j++)
				{
					colors[i][j] = (double) ColorUtil.red(image.getRGB(i, j));
				}
			}
			break;
		case GREEN:
			for (int i = 0; i < width; i++)
			{
				for(int j = 0; j < height; j++)
				{
					colors[i][j] = (double) ColorUtil.green(image.getRGB(i, j));
				}
			}
			break;
		case BLUE:
			for (int i = 0; i < width; i++)
			{
				for(int j = 0; j < height; j++)
				{
					colors[i][j] = (double) ColorUtil.blue(image.getRGB(i, j));
				}
			}
			break;
		}

		return colors;		
	}

	/***************************/
	public void launchCompression(String imagePath, ProgressBar pb){

		try{
			/** Original file:  "quadtree_original.png" **/
			BufferedImage image = ImageIO.read(new File(imagePath));

			normalQuadtreeCompress(image, pb);			
			//channalSeparatedQuadtreeCompress(image);
			//gradientQuadtreeCompress(image);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	/**************************/


	public static void main(String[] args){
		ImageCompressor ic = new ImageCompressor();
		ProgressBar pb = new ProgressBar();
		
		WindowChooseFile window = WindowChooseFile.getInstance(ic, pb);
		window.setVisible(true);
	}


	private static void normalQuadtreeCompress(BufferedImage image, final ProgressBar pb) throws IOException{
		
		Color[][] colors = makeColorArray(image);	

		int width = image.getWidth();
		int height = image.getHeight();

		for(int k = 1; k < 20; k++)	{
			QuadTree<Color> quadTree = new QuadTree<Color>(colors, new ImageMeasure(), k / 300.0, new Color(0, 0, 0));

			BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			for(int i = 0; i < width; i++){
				for(int j = 0; j < height; j++){
					outImage.setRGB(i, j, quadTree.get(i, j).getRGB());								
				}
			} 
			
			SwingUtilities.invokeLater(new Runnable() {
		          public void run() {
		             pb.updateBar((k*100)/19);
		            }
		          });
			
			WindowChooseFile.getInstance(null, null).invalidate();
			WindowChooseFile.getInstance(null, null).repaint();
			WindowChooseFile.getInstance(null, null).validate();
			ImageIO.write(outImage, "png", new File("normal_quad" + k + ".png"));
		}
	}

	private static void channalSeparatedQuadtreeCompress(BufferedImage image) throws IOException
	{
		Double[][] red = makeChannelArray(image, Channel.RED);
		Double[][] green = makeChannelArray(image, Channel.GREEN);
		Double[][] blue = makeChannelArray(image, Channel.BLUE);

		int width = image.getWidth();
		int height = image.getHeight();

		for(int k = 1; k < 20; k++)
		{
			QuadTree<Double> redTree = new QuadTree<Double>(red, new ChannelMeasure(), k / 900.0/*, new Double(0)*/);
			QuadTree<Double> greenTree = new QuadTree<Double>(green, new ChannelMeasure(), k / 900.0/*, new Double(0)*/);
			QuadTree<Double> blueTree = new QuadTree<Double>(blue, new ChannelMeasure(), k / 900.0/*, new Double(0)*/);

			BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			for(int i = 0; i < width; i++)
			{
				for(int j = 0; j < height; j++)
				{
					int rgb = ColorUtil.rgb(
							(int) (double)redTree.get(i, j), 
							(int) (double)greenTree.get(i, j), 
							(int) (double)blueTree.get(i, j));

					outImage.setRGB(i, j, rgb);					
				}
			}		

			ImageIO.write(outImage, "png", new File("channel_separated_quad" + k + ".png"));				
		}
	}

	private static void gradientQuadtreeCompress(BufferedImage image) throws IOException
	{

		Vector3DDouble[][] gradient = makeGradientArray(image);

		for(int k = 1; k < 20; k++)
		{
			QuadTree<Vector3DDouble> gradientTree = new QuadTree<Vector3DDouble>(gradient, new GradientMeasure(), k / 300.0);

			BufferedImage outImage = makeImage(gradientTree, image);
			ImageIO.write(outImage, "png", new File("gradient_quad" + k + ".png"));		

		}
	}

	public static class ChannelMeasure extends AbstractMeasure<Double>
	{

		@SuppressWarnings("boxing")
		@Override
		public Double approximate(Double[][] data, int x, int y, int width, int height)
		{
			double sum = 0;

			for(int i = x; i < x + width; i++)
			{
				for(int j = y; j < y + height; j++)
				{
					sum += data[i][j];
				}
			}

			return sum / (width * height);
		}

		@Override
		public double measureDetail(Double[][] data, int x, int y, int width, int height)
		{
			double average = approximate(data, x, y, width, height);
			double sum = 0;

			for(int i = x; i < x + width; i++)
			{
				for(int j = y; j < y + height; j++)
				{
					sum += MathUtil.sqr(average - data[i][j]) / (255*255);
				}
			}

			return sum / (width * height);
		}

	}

	public static class GradientMeasure extends AbstractMeasure<Vector3DDouble>
	{

		@Override
		public Vector3DDouble approximate(Vector3DDouble[][] data, int x, int y, int width, int height)
		{
			double redSum = 0;
			double greenSum = 0;
			double blueSum = 0;

			for(int i = x; i < x + width; i++)
			{
				for(int j = y; j < y + height; j++)
				{
					redSum += data[i][j].x;
					greenSum += data[i][j].y;
					blueSum += data[i][j].z;
				}
			}

			int pixelCount = width * height;

			return new Vector3DDouble(redSum / pixelCount, greenSum / pixelCount, blueSum / pixelCount);			
		}

		@Override
		public double measureDetail(Vector3DDouble[][] data, int x, int y, int width, int height)
		{
			int redSum = 0;
			int greenSum = 0;
			int blueSum = 0;

			for(int i = x; i < x + width; i++)
			{
				for(int j = y; j < y + height; j++)
				{
					redSum += data[i][j].x;
					greenSum += data[i][j].y;
					blueSum += data[i][j].z;
				}
			}

			double pixelCount = width * height;

			double redAvg = redSum / pixelCount;
			double greenAvg = greenSum / pixelCount;
			double blueAvg = blueSum / pixelCount;

			redSum = 0;
			greenSum = 0;
			blueSum = 0;

			for(int i = x; i < x + width; i++)
			{
				for(int j = y; j < y + height; j++)
				{
					double red = data[i][j].x;
					double green = data[i][j].y;
					double blue = data[i][j].z;

					redSum += MathUtil.sqr(red - redAvg);
					greenSum += MathUtil.sqr(green - greenAvg);
					blueSum += MathUtil.sqr(blue - blueAvg);					
				}
			}

			return redSum / (pixelCount * 255 * 255) + greenSum / (pixelCount * 255 * 255) + blueSum / (pixelCount * 255 * 255);
		}		
	}

	public static class ImageMeasure extends AbstractMeasure<Color>
	{

		@Override
		public Color approximate(Color[][] data, int x, int y, int width, int height)
		{
			int redSum = 0;
			int greenSum = 0;
			int blueSum = 0;

			for(int i = x; i < x + width; i++)
			{
				for(int j = y; j < y + height; j++)
				{
					redSum += data[i][j].getRed();
					greenSum += data[i][j].getGreen();
					blueSum += data[i][j].getBlue();
				}
			}

			int pixelCount = width * height;

			return new Color(redSum / pixelCount, greenSum / pixelCount, blueSum / pixelCount);			
		}

		@Override
		public double measureDetail(Color[][] data, int x, int y, int width, int height)
		{
			int redSum = 0;
			int greenSum = 0;
			int blueSum = 0;

			for(int i = x; i < x + width; i++)
			{
				for(int j = y; j < y + height; j++)
				{
					redSum += data[i][j].getRed();
					greenSum += data[i][j].getGreen();
					blueSum += data[i][j].getBlue();
				}
			}

			double pixelCount = width * height;

			double redAvg = redSum / pixelCount;
			double greenAvg = greenSum / pixelCount;
			double blueAvg = blueSum / pixelCount;

			redSum = 0;
			greenSum = 0;
			blueSum = 0;

			for(int i = x; i < x + width; i++)
			{
				for(int j = y; j < y + height; j++)
				{
					int red = data[i][j].getRed();
					int green = data[i][j].getGreen();
					int blue = data[i][j].getBlue();

					redSum += MathUtil.sqr(red - redAvg);
					greenSum += MathUtil.sqr(green - greenAvg);
					blueSum += MathUtil.sqr(blue - blueAvg);					
				}
			}
			return redSum / (pixelCount * 255 * 255) + greenSum / (pixelCount * 255 * 255) + blueSum / (pixelCount * 255 * 255);
		}		
	}
}
