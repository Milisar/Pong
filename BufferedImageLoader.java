package Pong;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageLoader {

	private BufferedImage image;
	
	public BufferedImage loadimage(String path) throws IOException{
		
		image = ImageIO.read(getClass().getResource(path));
		return image;
	}
}
