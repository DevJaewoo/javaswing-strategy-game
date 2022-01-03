package entity;

import main.MainFrame;

public abstract class Player extends Entity {
	
	public Player(String name, int maxHealth, int maxMana, int attack, int defense, int moveSpeed, int mapX, int mapY) {
		super(name, maxHealth, maxMana, attack, defense, moveSpeed, mapX, mapY);
		// TODO Auto-generated constructor stub
	}
	
	public abstract void activeSkill1();
	public abstract void activeSkill2();
	public abstract void activeSkill3();
	public abstract void activeSkill4();
	
	
	
	@Override
	public void getDamage(int damage) {
		// TODO Auto-generated method stub
		super.getDamage(damage);
		updateStatus();
	}
	
	protected boolean consumeMana(int mana)
	{
		if(currentMana < mana) return false;
		
		currentMana -= mana;
		updateStatus();
		
		return true;
	}
	
	public void updateStatus()
	{
		MainFrame.playerHealthLabel.setText(currentHealth + "/" + maxHealth);
		MainFrame.playerManaLabel.setText(currentMana + "/" + maxMana);
		MainFrame.playerDefenseLabel.setText(defense + "");
		MainFrame.playerMoveLabel.setText(moveLeft + "/" + moveSpeed);
	}
	
	@Override
	public void moveTile(char dir)
	{
		super.moveTile(dir);
		updateStatus();
	}
}
