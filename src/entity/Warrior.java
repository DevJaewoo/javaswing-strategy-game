package entity;

import javax.swing.ImageIcon;

import main.MainFrame;
import utility.Pair;
import utility.Utils;

public class Warrior extends Player {
	
	public static ImageIcon IdleImage = new ImageIcon(Utils.ImgPath + "/Player/Warrior/Idle3.png");
	public static ImageIcon MoveImage = new ImageIcon(Utils.ImgPath + "/Player/Warrior/Move2.png");
	public static ImageIcon AttackImage = new ImageIcon(Utils.ImgPath + "/Player/Warrior/Attack2.gif");
	public static ImageIcon DamagedImage = new ImageIcon(Utils.ImgPath + "/Player/Warrior/Damaged3.gif");
	public static ImageIcon DeadImage = new ImageIcon(Utils.ImgPath + "/Player/Warrior/Dead2.gif");

	public static ImageIcon Skill2EffectImage = new ImageIcon(Utils.ImgPath + "/Player/Warrior/Skill2.png");
	public static ImageIcon Skill3EffectImage = new ImageIcon(Utils.ImgPath + "/Player/Warrior/Skill3.gif");
	public static ImageIcon Skill4EffectImage = new ImageIcon(Utils.ImgPath + "/Player/Warrior/Skill4.gif");
	public static ImageIcon Skill4TileEffectImage = new ImageIcon(Utils.ImgPath + "/Player/Warrior/Skill4TileEffect2.png");
	
	private final static String defaultName = "Warrior";
	private final static int defaultMaxHealth = 500;
	private final static int defaultMaxMana = 100;
	private final static int defaultAttack = 100;
	private final static int defaultDefense = 25;
	private final static int defaultMoveSpeed = 4;
	
	private static final double skill1DamageRate = 1.0;
	private static final int skill1ManaCost = 0;
	private static boolean[][] skill1Range = 
		{
				{false,	true,	false},
				{true,	false,	true},
				{false,	true,	false},
		};
	
	private static final double skill2DamageRate = 1.5;
	private static final int skill2ManaCost = 20;
	private static boolean[][] skill2Range = 
		{
				{false,	true,	false},
				{true,	false,	true},
				{false,	true,	false},
		};
	
	private static final double skill3DamageRate = 1;
	private static final int skill3ManaCost = 50;
	private static boolean[][] skill3Range = 
		{
				{false,	false,	false,	true,	false,	false,	false},
				{false,	false,	true,	true,	true,	false,	false},
				{false,	true,	true,	true,	true,	true,	false},
				{true,	true,	true,	true,	true,	true,	true},
				{false,	true,	true,	true,	true,	true,	false},
				{false,	false,	true,	true,	true,	false,	false},
				{false,	false,	false,	true,	false,	false,	false},
		};
	
	private static final double skill4DamageRate = 2.0;
	private static final int skill4ManaCost = 60;
	private static boolean[][] skill4Range = 
		{
				{true},
		};
	
	public Warrior(String name, int maxHealth, int maxMana, int attack, int defense, int moveSpeed, int mapX, int mapY) {
		super(name, maxHealth, maxMana, attack, defense, moveSpeed, mapX, mapY);

		isSwimmable = false;
		isFlyable = false;
		
		// TODO Auto-generated constructor stub
		idleImage = IdleImage;
		moveImage = MoveImage;
		attackImage = AttackImage;
		damagedImage = DamagedImage;
		deadImage = DeadImage;
		
		setImageIcon(idleImage);
		updateStatus();
		
		Utils.printDebugMsg("Warrior character generated at (" + mapX + ", " + mapY + ")");
	}
	
	public Warrior(int mapX, int mapY) {
		this(defaultName, defaultMaxHealth, defaultMaxMana, defaultAttack, defaultDefense, defaultMoveSpeed, mapX, mapY);
		
		// TODO Auto-generated constructor stub
		//this.image = IdleImage.getImage();
		
		//Utils.printDebugMsg(this.name + " character generated at (" + mapX + ", " + mapY + ")");
	}

	@Override
	public void activeSkill1() {
		// TODO Auto-generated method stub
		Pair<Integer, Integer> target = null;
		Utils.printDebugMsg("Warrior active skill1");
		
		if(isActived == true) return;
			
		target = Utils.getSelectedTile(mapX, mapY, skill1Range);
		
		if(target.x == -1 || target.y == -1)
		{
			Utils.printDebugMsg("Warrior skill1 : Tile selected error.");
			return;
		}
		
		if(MainFrame.entityMap[target.x][target.y] instanceof Enemy == false)
		{
			Utils.printDebugMsg("Warrior skill1 : Enemy doesn't exist on selected tile.");
			return;
		}

		if(consumeMana(skill1ManaCost) == false)
		{
			Utils.printDebugMsg("Warrior skill1 : Not enough mana.");
			return;
		}
		
		direction = Utils.getDirection(mapX, mapY, target.x, target.y);
		/*
		if(target.x - mapX == -1) direction = Utils.IMGDIR_LEFT;
		else if(target.x - mapX == 1) direction = Utils.IMGDIR_RIGHT;
		else if(target.y - mapY == -1) direction = Utils.IMGDIR_UP;
		else if(target.y - mapY == 1) direction = Utils.IMGDIR_DOWN;*/
		
		setImageIcon(attackImage);
		Utils.delay(300);
		setImageIcon(IdleImage);
		
		attack(MainFrame.entityMap[target.x][target.y], (int)(attackDamage * skill1DamageRate));
		
		isActived = true;
		return;
	}

	@Override
	public void activeSkill2() {
		// TODO Auto-generated method stub
		Pair<Integer, Integer> target = null;
		
		final double tileProportion = 0.3; //밀치는 효과 범위
		final int frameDelay = 5;
		
		boolean enemyCaught = false;
		Enemy enemy = null;
		int dirX = 0, dirY = 0;
		
		Utils.printDebugMsg("Warrior active skill2");
		
		if(isActived == true) return;

		target = Utils.getSelectedTile(mapX, mapY, skill2Range);
		
		if(target.x == -1 || target.y == -1)
		{
			Utils.printDebugMsg("Warrior skill2 : Tile selected error.");
			return;
		}
		
		if((target.x - mapX) + (target.y - mapY) < -1 || (target.x - mapX) + (target.y - mapY) > 1)
		{
			Utils.printDebugMsg("Warrior skill2 : Tile selected error.");
			return;
		}
		
		if(consumeMana(skill2ManaCost) == false)
		{
			Utils.printDebugMsg("Warrior skill2 : Not enough mana.");
			return;
		}

		dirX = target.x - mapX;
		dirY = target.y - mapY;
		
		setImageIcon(Skill2EffectImage);

		direction = Utils.getDirection(mapX, mapY, target.x, target.y);
		/*
		if(dirX == -1)		direction = Utils.IMGDIR_LEFT;
		else if(dirX == 1)	direction = Utils.IMGDIR_RIGHT;
		else if(dirY == -1)	direction = Utils.IMGDIR_UP;
		else if(dirY == 1)	direction = Utils.IMGDIR_DOWN;
		*/
		while(true)
		{
			//어우 역겨워
			if((enemyCaught == false //혼자인 상태로
					&& ((mapX + dirX < 0 || mapX + dirX >= MainFrame.mapWidth) 		//X축 맵 벗어남
						|| (mapY + dirY < 0 || mapY + dirY >= MainFrame.mapHeight)  //Y축 맵 벗어남
						|| (MainFrame.groundMap[mapX + dirX][mapY + dirY] != 'G'))) //더 이상 못감
				|| (enemyCaught == true	//적 잡은 상태로
					&& ((mapX + dirX * 2 < 0 || mapX + dirX * 2 >= MainFrame.mapWidth) 	//X축 맵 벗어남
							|| (mapY + dirY * 2 < 0 || mapY + dirY * 2 >= MainFrame.mapHeight) //Y축 맵 벗어남
							|| (MainFrame.entityMap[mapX + dirX * 2][mapY + dirY * 2] instanceof Enemy) //다른 적이랑 충돌
							|| (MainFrame.groundMap[mapX + dirX][mapY + dirY] != 'G')	//물에 빠짐 (지상유닛 사망처리 해줘야됨)
							|| (MainFrame.groundMap[mapX + dirX * 2][mapY + dirY * 2] == 'B'))))		//벽에 충돌
			{
				if(enemyCaught == false)
				{
					for(int i=0; i<MainFrame.gameSpeed*tileProportion/10; i++)
					{
						setPixelX((int)(MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * dirX));
						setPixelY((int)(MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * dirY));
						
						Utils.delay(frameDelay);
					}
					
					for(int i=(int)(MainFrame.gameSpeed*tileProportion/10); i>=0; i--)
					{
						setPixelX((int)(MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * dirX));
						setPixelY((int)(MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * dirY));
						
						Utils.delay(frameDelay);
					}
					
					getDamage((int)(attackDamage * skill2DamageRate));
				}
				else
				{
					for(int i=0; i<MainFrame.gameSpeed*tileProportion/10; i++)
					{
						setPixelX((int)(MainFrame.tileWidth * tileProportion * dirX + MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * dirX));
						setPixelY((int)(MainFrame.tileWidth * tileProportion * dirY + MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * dirY));
						
						enemy.setPixelX((int)(MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * dirX));
						enemy.setPixelY((int)(MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * dirY));

						Utils.delay(frameDelay);
					}
					
					for(int i=(int)(MainFrame.gameSpeed*tileProportion/10); i>=0; i--)
					{
						setPixelX((int)(MainFrame.tileWidth * (i*2) / (MainFrame.gameSpeed/10) * dirX));
						setPixelY((int)(MainFrame.tileHeight * (i*2) / (MainFrame.gameSpeed/10) * dirY));
						
						enemy.setPixelX((int)(MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * dirX));
						enemy.setPixelY((int)(MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * dirY));

						Utils.delay(frameDelay);
					}
					
					if(MainFrame.groundMap[mapX + dirX][mapY + dirY] == 'W' && enemy.isSwimmable() == false)
					{
						Utils.printDebugMsg("Warrior skill2 : Enemy drown");
						enemy.getDamage(99999);
					}
					else
					{
						enemy.getDamage((int)(attackDamage * skill2DamageRate));
						if((mapX + dirX * 2 >= 0 && mapX + dirX * 2 < MainFrame.mapWidth)
								&& (mapY + dirY * 2 >= 0 && mapY + dirY * 2 < MainFrame.mapHeight)
								&& (MainFrame.entityMap[mapX + dirX * 2][mapY + dirY * 2] instanceof Enemy))
						{
							MainFrame.entityMap[mapX + dirX * 2][mapY + dirY * 2].getDamage((int)(attackDamage * skill2DamageRate));
						}
					}
				}
				
				break;
			}
			else if(enemyCaught == false && (MainFrame.entityMap[mapX + dirX][mapY + dirY] instanceof Enemy == true)) 
			{
				enemy = (Enemy)MainFrame.entityMap[mapX + dirX][mapY + dirY];
				enemyCaught = true;
				
				Utils.printDebugMsg("Warrior skill2 : Caught " + enemy.getName());
				
				for(int i=0; i<MainFrame.gameSpeed*tileProportion/10; i++)
				{
					setPixelX((int)(MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * dirX));
					setPixelY((int)(MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * dirY));

					Utils.delay(frameDelay);
				}
			}
			else
			{
				for(int i=0; i<(MainFrame.gameSpeed/10); i++)
				{
					if(enemyCaught == false)
					{
						setPixelX(MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * dirX);
						setPixelY(MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * dirY);
					}
					else
					{
						setPixelX((int)(MainFrame.tileWidth * tileProportion * dirX + MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * dirX));
						setPixelY((int)(MainFrame.tileHeight * tileProportion * dirY + MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * dirY));
						enemy.setPixelX(MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * dirX);
						enemy.setPixelY(MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * dirY);
					}

					Utils.delay(frameDelay);
				}
				
				if(enemyCaught == false)
				{
					MainFrame.entityMap[mapX][mapY] = null;
					setMapX(mapX + dirX);
					setMapY(mapY + dirY);
					MainFrame.entityMap[mapX][mapY] = getInstance();
					setPixelX(0);
					setPixelY(0);
				}
				else
				{
					MainFrame.entityMap[enemy.getMapX()][enemy.getMapY()] = null;
					enemy.setMapX(enemy.getMapX() + dirX);
					enemy.setMapY(enemy.getMapY() + dirY);
					MainFrame.entityMap[enemy.getMapX()][enemy.getMapY()] = enemy.getInstance();
					enemy.setPixelX(0);
					enemy.setPixelY(0);
					
					MainFrame.entityMap[mapX][mapY] = null;
					setMapX(mapX + dirX);
					setMapY(mapY + dirY);
					MainFrame.entityMap[mapX][mapY] = getInstance();
					setPixelX((int)(MainFrame.tileWidth * tileProportion * dirX));
					setPixelY((int)(MainFrame.tileHeight * tileProportion * dirY));
				}
			}
		}
		
		setImageIcon(idleImage);
		
		isActived = true;
		return;
	}

	@Override
	public void activeSkill3() {
		// TODO Auto-generated method stub
		Pair<Integer, Integer> target;
		Utils.printDebugMsg("Warrior active skill3");
		
		if(isActived == true) return;
		
		target = Utils.getSelectedTile(mapX, mapY, skill3Range);
		
		if(target.x == -1 || target.y == -1)
		{
			Utils.printDebugMsg("Warrior skill3 : Tile select error.");
			return;
		}
		
		if(MainFrame.groundMap[target.x][target.y] != 'G')
		{
			Utils.printDebugMsg("Warrior skill3 : Must select ground.");
			return;
		}
		
		if(MainFrame.entityMap[target.x][target.y] instanceof Enemy)
		{
			Utils.printDebugMsg("Warrior skill3 : Must select player or blank tile.");
			return;
		}
		
		if(consumeMana(skill3ManaCost) == false)
		{
			Utils.printDebugMsg("Warrior skill3 : Not enough mana.");
			return;
		}
		
		this.direction = Utils.IMGDIR_UP;
		
		setImageIcon(Skill3EffectImage);
		Utils.delay(1000);
		
		MainFrame.entityMap[mapX][mapY] = null;
		
		setMapX(target.x);
		setMapY(target.y);

		MainFrame.entityMap[mapX][mapY] = this.getInstance();
		
		this.defense += defaultDefense * skill3DamageRate;
		updateStatus();
		
		setImageIcon(idleImage);
		
		isActived = true;
	}

	@Override
	public void activeSkill4() {
		// TODO Auto-generated method stub
		Pair<Integer, Integer> target;
		
		Utils.printDebugMsg("Warrior active skill4");
		
		if(isActived == true) return;

		target = Utils.getSelectedTile(mapX, mapY, skill4Range);
		
		if(target.x == -1 || target.y == -1)
		{
			Utils.printDebugMsg("Warrior skill4 : Tile select error.");
			return;
		}
		
		if(MainFrame.entityMap[target.x][target.y] instanceof Player == false)
		{
			Utils.printDebugMsg("Warrior skill4 : Must select player.");
			return;
		}
		
		if(consumeMana(skill4ManaCost) == false)
		{
			Utils.printDebugMsg("Warrior skill4 : Not enough mana.");
			return;
		}
		
		setImageIcon(Skill4EffectImage);
		Utils.delay(500);
		
		for(int range=1; range<=3; range++)
		{
			for(int y = target.y - range; y <= target.y + range; y++)
			{
				for(int x = target.x - range; x <= target.x + range; x++)
				{
					if(x < 0 || x >= MainFrame.mapWidth || y < 0 || y >= MainFrame.mapHeight)
					{
						continue; //영역 밖
					}
					
					if((x != target.x - range && x != target.x + range) && (y != target.y - range && y != target.y + range))
					{
						continue; //사각형 안쪽
					}
					
					MainFrame.itemList.add(new Item(Skill4TileEffectImage, x, y, 0, 0, MainFrame.tileWidth, MainFrame.tileHeight, 128, 500));
				}
			}
			
			Utils.delay(300);
		}

		Utils.delay(500);
		setImageIcon(idleImage);
		
		for(int y = target.y - 3; y <= target.y + 3; y++)
		{
			for(int x = target.x - 3; x <= target.x + 3; x++)
			{
				if(x < 0 || x >= MainFrame.mapWidth || y < 0 || y >= MainFrame.mapHeight)
				{
					continue; //영역 밖
				}
				
				if(MainFrame.entityMap[x][y] instanceof Enemy)
				{
					((Enemy)MainFrame.entityMap[x][y]).getDamage((int)(attackDamage * skill4DamageRate));
				}
			}
		}
		
		isActived = true;
	}

	@Override
	public void attack(Entity target, int damage) {
		// TODO Auto-generated method stub
		//Utils.printDebugMsg("Warrior attack " + target.name);
		super.attack(target, damage);
	}

	@Override
	public void getDamage(int damage) {
		// TODO Auto-generated method stub
		super.getDamage(damage);
		//Utils.printDebugMsg("Warrior getDamage. Damage : " + damage + ", CurrentHealth : " + currentHealth);
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		Utils.printDebugMsg("Warrior move");
	}

	@Override
	public void active() {
		// TODO Auto-generated method stub
		Utils.printDebugMsg("Warrior turn start");
		
	}
}
