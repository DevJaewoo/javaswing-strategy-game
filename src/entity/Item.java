package entity;

import javax.swing.ImageIcon;

public class Item {
	
	public ImageIcon image;
	
	public int mapX;
	public int mapY;
	
	public int pixelX;
	public int pixelY;
	
	public int width;
	public int height;
	
	public int alpha;
	
	public int displayTime;

	/**
	 * @param image
	 * @param mapX
	 * @param mapY
	 * @param pixelX
	 * @param pixelY
	 * @param width
	 * @param height
	 * @param displayTime
	 */
	public Item(ImageIcon image, int mapX, int mapY, int pixelX, int pixelY, int width, int height, int alpha, int displayTime) {
		super();
		this.image = image;
		this.mapX = mapX;
		this.mapY = mapY;
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		this.width = width;
		this.height = height;
		this.alpha = alpha;
		this.displayTime = displayTime;
	}
	
}
