package entity;

import java.awt.Image;

import javax.swing.ImageIcon;

import main.MainFrame;
import utility.Utils;

public abstract class Entity {
	
	//public static Image IdleImage;
	protected Entity instance;
	
	protected String name;
	
	protected int maxHealth;
	protected int maxMana;
	
	protected int currentHealth;
	protected int currentMana;
	
	protected int attackDamage;
	protected int defense;
	
	protected int moveSpeed;
	protected int moveLeft;
	
	protected boolean isSwimmable;
	protected boolean isFlyable;
	
	protected boolean isTurnFinished;
	protected boolean isActived;
	protected boolean isMoved;
	
	protected int mapX;
	protected int mapY;
	
	protected int pixelX; //relative pixel from map[x][y]
	protected int pixelY;
	
	protected ImageIcon imageIcon;
	protected Image image;
	
	public double direction; //0:up 1:right 2:down 3:left
	
	protected boolean isIdle = true;
	protected ImageIcon idleImage;
	
	protected boolean isMoving = false;
	protected ImageIcon moveImage;

	protected boolean isAttacking = false;
	protected ImageIcon attackImage;
	
	protected boolean isDamaged = false;
	protected ImageIcon damagedImage;

	public boolean isDead = false;
	protected ImageIcon deadImage;
	
	/**
	 * @param name
	 * @param maxHealth
	 * @param maxMana
	 * @param attack
	 * @param defense
	 * @param moveSpeed
	 * @param mapX
	 * @param mapY
	 * @param image
	 */
	public Entity(String name, int maxHealth, int maxMana, int attack, int defense, int moveSpeed, int mapX, int mapY) {
		
		super();
		this.name = name;
		this.maxHealth = maxHealth;
		this.maxMana = maxMana;
		this.attackDamage = attack;
		this.defense = defense;
		this.moveSpeed = moveSpeed;
		this.mapX = mapX;
		this.mapY = mapY;
		this.pixelX = 0;
		this.pixelY = 0;
		
		this.currentHealth = maxHealth;
		this.currentMana = maxMana;
		this.moveLeft = moveSpeed;
		this.isTurnFinished = false;
		this.isMoved = false;
		
		this.instance = this;
	}

	
	/**
	 * @return the instance
	 */
	public Entity getInstance() {
		return instance;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the maxHealth
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * @param maxHealth the maxHealth to set
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	/**
	 * @return the maxMana
	 */
	public int getMaxMana() {
		return maxMana;
	}

	/**
	 * @param maxMana the maxMana to set
	 */
	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	/**
	 * @return the currentHealth
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * @param currentHealth the currentHealth to set
	 */
	public void setCurrentHealth(int currentHealth) {
		if(currentHealth > maxHealth)
		{
			this.currentHealth = maxHealth;
		}
		else 
		{
			this.currentHealth = currentHealth;
		}
	}

	/**
	 * @return the currentMana
	 */
	public int getCurrentMana() {
		return currentMana;
	}

	/**
	 * @param currentMana the currentMana to set
	 */
	public void setCurrentMana(int currentMana) {
		if(currentMana > maxMana)
		{
			this.currentMana = maxMana;
		}
		else
		{
			this.currentMana = currentMana;
		}
	}

	/**
	 * @return the attack
	 */
	public int getAttackDamage() {
		return attackDamage;
	}

	/**
	 * @param attack the attack to set
	 */
	public void setAttack(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	/**
	 * @return the defense
	 */
	public int getDefense() {
		return defense;
	}

	/**
	 * @param defense the defense to set
	 */
	public void setDefense(int defense) {
		this.defense = defense;
	}

	/**
	 * @return the moveSpeed
	 */
	public int getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * @param moveSpeed the moveSpeed to set
	 */
	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * @return the isSwimmable
	 */
	public boolean isSwimmable() {
		return isSwimmable;
	}


	/**
	 * @param isSwimmable the isSwimmable to set
	 */
	public void setSwimmable(boolean isSwimmable) {
		this.isSwimmable = isSwimmable;
	}


	/**
	 * @return the isFlyable
	 */
	public boolean isFlyable() {
		return isFlyable;
	}


	/**
	 * @param isFlyable the isFlyable to set
	 */
	public void setFlyable(boolean isFlyable) {
		this.isFlyable = isFlyable;
	}


	/**
	 * @return the isTurnFinished
	 */
	public boolean isTurnFinished() {
		return isTurnFinished;
	}

	/**
	 * @param isTurnFinished the isTurnFinished to set
	 */
	public void setTurnFinished(boolean isTurnFinished) {
		this.isTurnFinished = isTurnFinished;
	}

	/**
	 * @return the isMoved
	 */
	public boolean isMoved() {
		return isMoved;
	}

	/**
	 * @param isMoved the isMoved to set
	 */
	public void setMoved(boolean isMoved) {
		this.isMoved = isMoved;
	}

	/**
	 * @return the mapX
	 */
	public int getMapX() {
		return mapX;
	}

	/**
	 * @param mapX the mapX to set
	 */
	public void setMapX(int mapX) {
		if(mapX < 0)
		{
			mapX = 0;
		}
		if(mapX >= MainFrame.mapWidth)
		{
			mapX = MainFrame.mapWidth - 1;
		}
		this.mapX = mapX;
	}

	/**
	 * @return the mapY
	 */
	public int getMapY() {
		return mapY;
	}

	/**
	 * @param mapY the mapY to set
	 */
	public void setMapY(int mapY) {
		if(mapY < 0)
		{
			mapY = 0;
		}
		if(mapY >= MainFrame.mapHeight)
		{
			mapY = MainFrame.mapHeight - 1;
		}
		this.mapY = mapY;
	}

	/**
	 * @return the pixelX
	 */
	public int getPixelX() {
		return pixelX;
	}

	/**
	 * @param pixelX the pixelX to set
	 */
	public void setPixelX(int pixelX) {
		this.pixelX = pixelX;
	}

	/**
	 * @return the pixelY
	 */
	public int getPixelY() {
		return pixelY;
	}

	/**
	 * @param pixelY the pixelY to set
	 */
	public void setPixelY(int pixelY) {
		this.pixelY = pixelY;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	
	/**
	 * @return the imageicon
	 */
	public ImageIcon getImageIcon() {
		return imageIcon;
	}


	/**
	 * @param imageicon the imageicon to set
	 */
	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
		this.imageIcon.getImage().flush();
	}


	/*
	 * 공격 시 처리되는 함수
	 */
	public void attack(Entity target, int damage)
	{
		if(target == null)
		{
			return;
		}
		
		Utils.printDebugMsg(this.name + " attack. Target : " + target.getName() + " Damage : " + damage);
		
		target.getDamage(damage);
	}
	
	/*
	 * 데미지를 받을 시 처리되는 함수
	 */
	public void getDamage(int damage)
	{
		if(damage > defense)
		{
			damage -= defense;
		}
		else damage = 1;
		
		if(damage > currentHealth)
		{
			damage = currentHealth;
		}
		currentHealth -= damage;
		
		Utils.printDebugMsg(this.name + " getDamage. Damage : " + damage + " CurrentHealth : " + currentHealth);
		
		setImageIcon(damagedImage);
		
		Utils.delay(500);
		/*
		for(int i=0; i<30; i++)
		{
			//this.image = damagedImage.getImage();
			Utils.delay(10);
		}*/
		
		if(currentHealth <= 0)
		{
			dead();
		}
		else
		{
			setImageIcon(idleImage);
		}
	}
	
	public void dead()
	{
		setImageIcon(deadImage);

		Utils.delay(500);
		
		Utils.printDebugMsg(this.name + " dead.");
		isDead = true;
	}
	
	/*
	 * 맵에서 움직이는 함수
	 */
	public abstract void move();
	
	/*
	 * 턴 진행 함수
	 */
	public abstract void active();
	
	public void turnInit()
	{
		this.isMoved = false;
		this.isActived = false;
		this.isTurnFinished = false;
		this.moveLeft = moveSpeed;
	}
	
	protected boolean moveCheck(int x, int y, boolean checkEntity)
	{
		if(x < 0 || y < 0 || x >= MainFrame.mapWidth || y >= MainFrame.mapHeight)
		{
			return false;
		}
		else if((MainFrame.groundMap[x][y] == 'W' && isSwimmable == false) || (MainFrame.groundMap[x][y] == 'B' && isFlyable == false))
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
	
	public void moveTile(char dir)
	{
		if(this.isMoved == true || this.moveLeft == 0)
		{
			return;
		}
		
		try
		{	
			this.image = moveImage.getImage();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int originX = 0;//getPixelX();
				int originY = 0;//getPixelY();
				
				switch(dir)
				{
				case 'U':
					//if(mapY <= 0) return;
					if(moveCheck(mapX, mapY - 1, true) == false) 
					{
						return;
					}
					
					setImageIcon(moveImage);
					direction = Utils.IMGDIR_UP;
					
					for(int i=0; i<(MainFrame.gameSpeed/10); i++) //500 : msec 	10 : 화면 갱신 주기 
					{
						setPixelY(MainFrame.tileHeight * i / (MainFrame.gameSpeed/10) * (-1));
						Utils.delay(10);
					}

					MainFrame.entityMap[getMapX()][getMapY()] = null;
					setMapY(getMapY() - 1);
					break;

				case 'D':
					//if(mapY >= MainFrame.mapHeight - 1) return;
					if(moveCheck(mapX, mapY + 1, true) == false)
					{
						return;
					}

					setImageIcon(moveImage);
					direction = Utils.IMGDIR_DOWN;
					
					for(int i=0; i<(MainFrame.gameSpeed/10); i++) //500 : msec 	10 : 화면 갱신 주기 
					{
						setPixelY(MainFrame.tileHeight * i / (MainFrame.gameSpeed/10));
						Utils.delay(10);
					}

					MainFrame.entityMap[getMapX()][getMapY()] = null;
					setMapY(getMapY() + 1);
					break;

				case 'L':
					//if(mapX <= 0) return;
					if(moveCheck(mapX - 1, mapY, true) == false) 
					{
						return;
					}

					setImageIcon(moveImage);
					direction = Utils.IMGDIR_LEFT;
					
					for(int i=0; i<(MainFrame.gameSpeed/10); i++) //MainFrame.gameSpeed : 움직이는 속도 	10 : 화면 갱신 주기 
					{
						setPixelX(MainFrame.tileWidth * i / (MainFrame.gameSpeed/10) * (-1));
						Utils.delay(10);
					}

					MainFrame.entityMap[getMapX()][getMapY()] = null;
					setMapX(getMapX() - 1);
					break;

				case 'R':
					//if(mapX >= MainFrame.mapWidth - 1) return;
					if(moveCheck(mapX + 1, mapY, true) == false) 
					{
						return;	
					}

					setImageIcon(moveImage);
					direction = Utils.IMGDIR_RIGHT;
					
					for(int i=0; i<(MainFrame.gameSpeed/10); i++) //500 : msec 	10 : 화면 갱신 주기 
					{
						setPixelX(MainFrame.tileWidth * i / (MainFrame.gameSpeed/10));
						Utils.delay(10);
					}

					MainFrame.entityMap[getMapX()][getMapY()] = null;
					setMapX(getMapX() + 1);
					break;
					
				default:
					break;
				}
				
				moveLeft--;
				if(moveLeft == 0)
				{
					isMoved = true;
				}
				
				setPixelX(originX);
				setPixelY(originY);
				setImageIcon(idleImage);
				MainFrame.entityMap[getMapX()][getMapY()] = getInstance();
			}
			
		});
		
		t.start();
		
		try
		{
			t.join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		this.image = idleImage.getImage();
		return;
	}
}
