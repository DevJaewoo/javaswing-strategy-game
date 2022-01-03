package entity;

import javax.swing.ImageIcon;

import main.MainFrame;
import utility.Utils;

/*
 * 공중-근접
 */
public class EnemyBat extends Enemy {

	public static ImageIcon IdleImage = new ImageIcon(Utils.ImgPath + "/Enemy/Bat/Idle.png");
	public static ImageIcon MoveImage = new ImageIcon(Utils.ImgPath + "/Enemy/Bat/Move.png");
	public static ImageIcon AttackImage = new ImageIcon(Utils.ImgPath + "/Enemy/Bat/Attack.gif");
	public static ImageIcon DamagedImage = new ImageIcon(Utils.ImgPath + "/Enemy/Bat/Damaged.gif");
	public static ImageIcon DeadImage = new ImageIcon(Utils.ImgPath + "/Enemy/Bat/Dead.gif");
	
	private static String defaultName = "EnemyBat";
	private static int defaultMaxHealth = 150;
	private static int defaultMaxMana = 0;
	private static int defaultAttack = 200;
	private static int defaultDefense = 30;
	private static int defaultMoveSpeed = 4;
	
	private static int number = 1;
	
	public EnemyBat(int mapX, int mapY) {
		super(defaultName + number++, defaultMaxHealth, defaultMaxMana, defaultAttack, defaultDefense, defaultMoveSpeed, mapX, mapY);
		isSwimmable = true;
		isFlyable = false;
		
		// TODO Auto-generated constructor stub
		idleImage = IdleImage;
		moveImage = MoveImage;
		attackImage = AttackImage;
		damagedImage = DamagedImage;
		deadImage = DeadImage;
		
		attackRange = new boolean[][] {
			{false,	false,	false,	false,	false},
			{false,	false,	true,	false,	false},
			{false,	true,	true,	true,	false},
			{false,	false,	true,	false,	false},
			{false,	false,	false,	false,	false},
		};
		
		setImageIcon(idleImage);
		
		Utils.printDebugMsg(this.name + " character generated at (" + mapX + ", " + mapY + ")");
	}
	
	@Override
	public boolean moveCheck(int x, int y, boolean checkEntity)
	{
		if(x < 0 || y < 0 || x >= MainFrame.mapWidth || y >= MainFrame.mapHeight)
		{
			return false;
		}
		else if(MainFrame.groundMap[x][y] != 'G' && MainFrame.groundMap[x][y] != 'W') //공중유닛은 물 위도 다닐 수 있음
		{
			return false;
		}
		else if(MainFrame.entityMap[x][y] instanceof Enemy && checkEntity == true) //플레이어는 통과 가능으로 봐야 길찾기가 됨
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}

