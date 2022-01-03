package utility;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import entity.*;
import main.MainFrame;

public class Utils {

	public static final String ImgPath = "./img";
	public static final boolean DEBUG = false;
	
	public static final double IMGDIR_LEFT = -90;
	public static final double IMGDIR_RIGHT = 90;
	public static final double IMGDIR_UP = 0;
	public static final double IMGDIR_DOWN = 180;
	
	public static void printDebugMsg(String msg)
	{
		if(DEBUG)
		{
			System.out.println(msg);
		}
	}
	
	public static void delay(int second)
	{
		if(second < 0)
		{
			return;
		}
		
		try {
			Thread.sleep(second);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static double getDirection(int x, int y, int tx, int ty)
	{
		int diffX = Math.abs(x - tx);
		int diffY = Math.abs(y - ty);
		double retVal = 0;
		
		if(diffX > diffY)
		{
			if(tx < x)
			{
				retVal = IMGDIR_LEFT;
			}
			else
			{
				retVal = IMGDIR_RIGHT;
			}
		}
		else
		{
			if(ty < y)
			{
				retVal = IMGDIR_UP;
			}
			else
			{
				retVal = IMGDIR_DOWN;
			}
		}
		
		return retVal;
	}
	
	public static BufferedImage getRotatedImage(ImageIcon imageicon, double degree)
	{
		//Image image = imageicon.getImage();
		
		double radians = Math.toRadians(degree);
	    double sin = Math.abs(Math.sin(radians));
	    double cos = Math.abs(Math.cos(radians));
	    int newWidth = (int) Math.round(imageicon.getIconWidth() * cos + imageicon.getIconHeight() * sin);
	    int newHeight = (int) Math.round(imageicon.getIconWidth() * sin + imageicon.getIconHeight() * cos);

	    // Create a new image
	    BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = rotatedImage.createGraphics();
	    
	    // Calculate the "anchor" point around which the image will be rotated
	    int x = (newWidth - imageicon.getIconWidth()) / 2;
	    int y = (newHeight - imageicon.getIconHeight()) / 2;
	    
	    // Transform the origin point around the anchor point
	    AffineTransform at = new AffineTransform();
	    
	    at.setToRotation(radians, x + (imageicon.getIconWidth() / 2), y + (imageicon.getIconHeight() / 2));
	    at.translate(x, y);
	    g2d.setTransform(at);
	    
	    // Paint the original image
	    g2d.drawImage(imageicon.getImage(), 0, 0, null);
	    g2d.dispose();
	    
	    return rotatedImage;
	}
	
	public static Pair<Integer, Integer> getPlayerCoordinate()
	{
		/*
		for(int y = 0; y < MainFrame.mapHeight; y++)
		{
			for(int x = 0; x < MainFrame.mapWidth; x++)
			{
				if(MainFrame.entityMap[x][y] instanceof Player)
				{
					return new Pair<Integer, Integer>(x, y);
				}
			}
		}
		*/
		return new Pair<Integer, Integer>(MainFrame.playerInstance.getMapX(), MainFrame.playerInstance.getMapY());
		
		//return new Pair<Integer, Integer>(-1, -1);
	}
	
	public static Pair<Integer, Integer> getSize(String src)
	{
		try
		{
			File mapFile = new File(src);
			BufferedReader reader = new BufferedReader(new FileReader(mapFile));
			
			String str = reader.readLine();
			int height = Integer.parseInt(str.split(" ")[0]);
			int width = Integer.parseInt(str.split(" ")[1]);
			
			reader.close();
			return new Pair<Integer, Integer>(height, width);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new Pair<Integer, Integer>(0, 0);
		}
	}
	
	public static char[][] getStageMap(String src)
	{
		try
		{
			File mapFile = new File(src);
			BufferedReader reader = new BufferedReader(new FileReader(mapFile));
			
			String str = reader.readLine();
			int height = Integer.parseInt(str.split(" ")[0]);
			int width = Integer.parseInt(str.split(" ")[1]);
			
			char[][] map = new char[width][height];

			for(int y=0; y<height; y++)
			{
				for(int x=0; x<width; x++)
				{
					while(map[x][y] == '\0' || map[x][y] == '\r' || map[x][y] == '\n') map[x][y] = (char)reader.read();
				}
			}
			
			if(DEBUG)
			{
				for(int y=0; y<height; y++)
				{
					for(int x=0; x<width; x++)
					{
						System.out.print(map[x][y]);
					}
					System.out.println();
				}
			}
			
			reader.close();
			return map;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new char[0][0];
		}
	}
	
	public static Entity[][] getEntityMap(String src)
	{
		try
		{
			File mapFile = new File(src);
			BufferedReader reader = new BufferedReader(new FileReader(mapFile));
			
			String str = reader.readLine();
			int height = Integer.parseInt(str.split(" ")[0]);
			int width = Integer.parseInt(str.split(" ")[1]);
			
			char[][] map = new char[width][height];
			Entity[][] entityMap = new Entity[width][height];
			
			for(int i=0; i<height; i++)
			{
				reader.readLine();
			}

			for(int y=0; y<height; y++)
			{
				for(int x=0; x<width; x++)
				{
					while(map[x][y] == '\0' || map[x][y] == '\r' || map[x][y] == '\n') map[x][y] = (char)reader.read();
				}
			}
			
			if(DEBUG)
			{
				for(int y=0; y<height; y++)
				{
					for(int x=0; x<width; x++)
					{
						System.out.print(map[x][y]);
					}
					System.out.println();
				}
			}
			
			for(int y=0; y<height; y++)
			{
				for(int x=0; x<width; x++)
				{
					switch(map[x][y])
					{
					case 'P':
					case 'p': //player
						MainFrame.playerInstance.setMapX(x);
						MainFrame.playerInstance.setMapY(y);
						entityMap[x][y] = MainFrame.playerInstance;//new Warrior(x, y);
						break;
						
					case 'K':
					case 'k':
						entityMap[x][y] = new EnemyKnight(x, y);
						break;
						
					case 'B':
					case 'b':
						entityMap[x][y] = new EnemyBat(x, y);
						break;
						
					default:
						entityMap[x][y] = null;
					}
				}
			}
			
			reader.close();
			return entityMap;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new Entity[0][0];
		}
	}
	
	public static Pair<Integer, Integer> getSelectedTile(int mapX, int mapY, boolean[][] attackRange)
	{
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		Pair<Integer, Integer> selectedTile = new Pair<Integer, Integer>(-1, -1);

		int baseX = (MainFrame.mainPanel.getWidth() - (MainFrame.mapWidth * MainFrame.tileWidth)) / 2;
		int baseY = (MainFrame.mainPanel.getHeight() - (MainFrame.mapHeight * MainFrame.tileHeight)) / 2;		
		
		if(mapX != -1 && mapY != -1) //자기 클래스의 attackRange 배열 중심을 플레이어 위치에 덮어씌움
		{
			int offsetX = (attackRange[0].length / 2) * (-1);
			int offsetY = (attackRange.length / 2) * (-1);
			
			for(int rangeY = 0; rangeY < attackRange[0].length; rangeY++)
			{
				if(mapY + offsetY + rangeY < 0 || mapY + offsetY + rangeY >= MainFrame.mapHeight)
				{
					continue;
				}
				
				for(int rangeX = 0; rangeX < attackRange.length; rangeX++)
				{
					if(mapX + offsetX + rangeX < 0 || mapX + offsetX + rangeX >= MainFrame.mapWidth)
					{
						continue;
					}
					
					if(attackRange[rangeY][rangeX] == true)
					{
						JButton button = new JButton();
						button.setBounds(baseX + (mapX + offsetX + rangeX) * MainFrame.tileWidth
										, baseY + (mapY + offsetY + rangeY) * MainFrame.tileHeight
										, MainFrame.tileWidth, MainFrame.tileHeight);
						button.setOpaque(true);
						button.setBackground(new Color(255, 0, 0, 150));
						button.setContentAreaFilled(true);
						button.addActionListener(new RangeButtonActionListener(selectedTile, mapX + offsetX + rangeX, mapY + offsetY + rangeY));
						Utils.delay(5); //안하면 버튼이 가끔 이상한곳에 생성됨
						MainFrame.mainPanel.add(button);
						buttonList.add(button);
						
						//Utils.printDebugMsg(button.toString());
						Utils.printDebugMsg("Range button added at X : " + (mapX + offsetX + rangeX) 
								+ " Y : " + (mapY + offsetY + rangeY)
								+ " Bounds(" + (baseX + (mapX + offsetX + rangeX) * MainFrame.tileWidth) + 
								", " + (baseY + (mapY + offsetY + rangeY) * MainFrame.tileHeight)
								+ ", " + MainFrame.tileWidth + ", " + MainFrame.tileHeight + ")");
						
					}
				}
			}
		}
		
		while(selectedTile.x == -1 || selectedTile.y == -1)
		{
			Utils.delay(10);
		}
		
		Utils.printDebugMsg("Tile selected. X : " + selectedTile.x + " Y : " + selectedTile.y);
		
		while(buttonList.isEmpty() == false)
		{
			MainFrame.mainPanel.remove(buttonList.remove(0));
		}
		
		return selectedTile;
	}
}
