package entity;

import javax.swing.ImageIcon;

import utility.Utils;

/*
 * 지상-근접
 */
public class EnemyKnight extends Enemy {

	public static ImageIcon IdleImage = new ImageIcon(Utils.ImgPath + "/Enemy/Knight/Idle2.png");
	public static ImageIcon MoveImage = new ImageIcon(Utils.ImgPath + "/Enemy/Knight/Move2.png");
	public static ImageIcon AttackImage = new ImageIcon(Utils.ImgPath + "/Enemy/Knight/Attack2.gif");
	public static ImageIcon DamagedImage = new ImageIcon(Utils.ImgPath + "/Enemy/Knight/Damaged2.gif");
	public static ImageIcon DeadImage = new ImageIcon(Utils.ImgPath + "/Enemy/Knight/Dead2.gif");
	
	private static String defaultName = "EnemyKnight";
	private static int defaultMaxHealth = 200;
	private static int defaultMaxMana = 0;
	private static int defaultAttack = 100;
	private static int defaultDefense = 30;
	private static int defaultMoveSpeed = 2;
	
	private static int number = 1;
	
	public EnemyKnight(int mapX, int mapY) {
		super(defaultName + number++, defaultMaxHealth, defaultMaxMana, defaultAttack, defaultDefense, defaultMoveSpeed, mapX, mapY);
		isSwimmable = false;
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
}
