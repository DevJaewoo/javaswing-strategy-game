package entity;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import main.MainFrame;
import utility.Pair;
import utility.Utils;

public abstract class Enemy extends Entity {
	
	protected boolean[][] attackRange; //�׻� ����, ���� ��� Ȧ�����ߵ�
	protected boolean playerInRange;

	public Enemy(String name, int maxHealth, int maxMana, int attack, int defense, int moveSpeed, int mapX, int mapY) {
		super(name, maxHealth, maxMana, attack, defense, moveSpeed, mapX, mapY);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void active()
	{
		Utils.printDebugMsg(this.name + " active");
		
		move();
		if(getPlayerInRange() == true)
		{
			attack(MainFrame.playerInstance, attackDamage);
			isActived = true;
		}
		
		isTurnFinished = true;
	}
	
	@Override
	public void attack(Entity target, int damage) {
		// TODO Auto-generated method stub
		setImageIcon(attackImage);

		direction = Utils.getDirection(mapX, mapY, target.mapX, target.mapY);
		
		/*
		if(target.mapX - mapX == -1) direction = Utils.IMGDIR_LEFT;
		else if(target.mapX - mapX == 1) direction = Utils.IMGDIR_RIGHT;
		else if(target.mapY - mapY == -1) direction = Utils.IMGDIR_UP;
		else if(target.mapY - mapY == 1) direction = Utils.IMGDIR_DOWN;
		*/
		
		for(int i=0; i<30; i++)
		{
			//this.image = Utils.getRotatedImage(attackImage, direction);
			Utils.delay(10);
		}

		setImageIcon(idleImage);
		
		super.attack(target, damage);
	}

	@Override
	public void move() //�˰��� : �÷��̾�� ���� �� �� �ո� �� �켱, �� ������ ���� ������ ���� ��
	{
		char[] path;
		
		if(this.isMoved == true)
		{
			return;
		}
		
		path = getMovePath(true); //�ո� �� �˻�
		
		if(path == null)
		{
			path = getMovePath(false); //����� �� �˻�
		}
		
		if(path == null)
		{
			Utils.printDebugMsg(this.name + " can't move");
			return;
		}
		
		//playerInRange = getPlayerInRange();
		
		//�̵� ����Ʈ �� ��ǥ ����
		if(Utils.DEBUG)
		{
			System.out.print("Move length : " + path.length + " Path : ");
			for(char c : path)
			{
				System.out.print(c);
			}
			System.out.println();
		}
		
		for(char c : path)
		{
			moveTile(c);
		}
		
		this.isMoved = true;
	}
	
	private boolean getPlayerInRange()
	{
		boolean rangeMap[][] = new boolean[MainFrame.mapWidth][MainFrame.mapHeight];
		Pair<Integer, Integer> playerCoord = Utils.getPlayerCoordinate();
		
		if(playerCoord.x != -1 && playerCoord.y != -1) //�ڱ� Ŭ������ attackRange �迭 �߽��� �÷��̾� ��ġ�� �����
		{
			int offsetX = (attackRange.length / 2) * (-1);
			int offsetY = (attackRange[0].length / 2) * (-1);
			
			for(int rangeY = 0; rangeY < attackRange[0].length; rangeY++)
			{
				if(playerCoord.y + offsetY + rangeY < 0 || playerCoord.y + offsetY + rangeY >= MainFrame.mapHeight)
				{
					continue;
				}
				
				for(int rangeX = 0; rangeX < attackRange.length; rangeX++)
				{
					if(playerCoord.x + offsetX + rangeX < 0 || playerCoord.x + offsetX + rangeX >= MainFrame.mapWidth)
					{
						continue;
					}
					
					rangeMap[playerCoord.x + offsetX + rangeX][playerCoord.y + offsetY + rangeY] = attackRange[rangeY][rangeX];
				}
			}
		}
		
		return rangeMap[mapX][mapY];
	}

	/*
	 * 1. �÷��̾� ���� ���� �Ÿ����� ������ ������ �� ��� �����Ѵ�.
	 * 2. �÷��̾�� ���� ������ ���� ���� �ִٸ� �� ��� �̵��ӵ��� �ִ��� �Ҹ��Ͽ� �̵��Ѵ�.
	 * 3. �÷��̾�� ���� ���� �����ٸ� �ִܰŸ��� ������ ���� �̵��Ѵ�.
	 */
	private char[] getMovePath(boolean checkEntity)
	{
		Queue<Pair<Integer, Integer>> queue = new LinkedList<Pair<Integer, Integer>>();
		
		//Pair<Integer, Integer> maskMap[][] = (Pair<Integer, Integer>[][]) new Pair[MainFrame.mapWidth][MainFrame.mapHeight];
		char maskMap[][] = new char[MainFrame.mapWidth][MainFrame.mapHeight];
		boolean rangeMap[][] = new boolean[MainFrame.mapWidth][MainFrame.mapHeight];
		char[] movePath;// = new char[MainFrame.mapWidth + MainFrame.mapHeight];
		Pair<Integer, Integer> playerCoord = Utils.getPlayerCoordinate();
		Pair<Integer, Integer> coord;
		
		Utils.printDebugMsg(this.name + " move");
		
		if(checkEntity == true)
		{
			if(playerCoord.x != -1 && playerCoord.y != -1) //�ڱ� Ŭ������ attackRange �迭 �߽��� �÷��̾� ��ġ�� �����
			{
				int offsetX = (attackRange.length / 2) * (-1);
				int offsetY = (attackRange[0].length / 2) * (-1);
				
				for(int rangeY = 0; rangeY < attackRange[0].length; rangeY++)
				{
					if(playerCoord.y + offsetY + rangeY < 0 || playerCoord.y + offsetY + rangeY >= MainFrame.mapHeight)
					{
						continue;
					}
					
					for(int rangeX = 0; rangeX < attackRange.length; rangeX++)
					{
						if(playerCoord.x + offsetX + rangeX < 0 || playerCoord.x + offsetX + rangeX >= MainFrame.mapWidth)
						{
							continue;
						}
						
						rangeMap[playerCoord.x + offsetX + rangeX][playerCoord.y + offsetY + rangeY] = attackRange[rangeY][rangeX];
					}
				}
			}
			
			if(Utils.DEBUG)
			{
				for(int y=0; y<MainFrame.mapHeight; y++)
				{
					for(int x=0; x<MainFrame.mapWidth; x++)
					{
						if(rangeMap[x][y] == true) System.out.print('T');
						else System.out.print('F');
					}
					System.out.println();
				}
			}
			
			queue.offer(new Pair<Integer, Integer>(mapX, mapY)); //�� ��ġ���� ����
			maskMap[mapX][mapY] = 'O';
		}
		else
		{
			queue.offer(playerCoord); //���� �� ã�� �� �÷��̾� ����
			maskMap[playerCoord.x][playerCoord.y] = 'O';
		}
		
		while(queue.isEmpty() == false)
		{
			coord = queue.poll();
			
			if(checkEntity == true)
			{
				if(rangeMap[coord.x][coord.y] == true) //�� -> ��Ÿ�
				{
					if(Utils.DEBUG)
					{
						for(int y=0; y<MainFrame.mapHeight; y++)
						{
							for(int x=0; x<MainFrame.mapWidth; x++)
							{
								if(maskMap[x][y] == 0)
								{
									System.out.print(' ');
								}
								else
								{
									System.out.print(maskMap[x][y]);
								}
							}
							System.out.println();
						}
					}
					
					//��� ����
					Pair<Integer, Integer> tmpCoord = new Pair<Integer, Integer>(coord.x, coord.y);
					Stack<Character> tmpStack = new Stack<Character>();
					
					while(maskMap[tmpCoord.x][tmpCoord.y] != 'O')
					{
						if(tmpCoord.x != playerCoord.x || tmpCoord.y != playerCoord.y)
						{
							tmpStack.push(maskMap[tmpCoord.x][tmpCoord.y]);
						}
						
						switch(maskMap[tmpCoord.x][tmpCoord.y])
						{
						case 'U':	tmpCoord.y++;	break;
						case 'D':	tmpCoord.y--;	break;
						case 'L':	tmpCoord.x++;	break;
						case 'R':	tmpCoord.x--;	break;
						default:	break;
						}
					}
					
					if(tmpStack.size() < moveSpeed)
					{
						movePath = new char[tmpStack.size()];
						for(int i=0; !tmpStack.isEmpty(); i++)
						{
							movePath[i] = tmpStack.pop();
						}
					}
					else
					{
						movePath = new char[moveSpeed];
						for(int i=0; i<moveSpeed; i++)
						{
							movePath[i] = tmpStack.pop();
						}
					}
					
					return movePath;
				}
			}
			else
			{
				if(mapX == coord.x && mapY == coord.y) //�÷��̾� -> ��
				{
					if(Utils.DEBUG)
					{
						for(int y=0; y<MainFrame.mapHeight; y++)
						{
							for(int x=0; x<MainFrame.mapWidth; x++)
							{
								if(maskMap[x][y] == 0)
								{
									System.out.print(' ');
								}
								else
								{
									System.out.print(maskMap[x][y]);
								}
							}
							System.out.println();
						}
					}
					
					Pair<Integer, Integer> tmpCoord = new Pair<Integer, Integer>(coord.x, coord.y); 
					Stack<Character> tmpStack = new Stack<Character>();
					
					for(int index = 0; index < moveSpeed; index++)
					{
						switch(maskMap[tmpCoord.x][tmpCoord.y])
						{
						case 'U':	tmpStack.push('D');	tmpCoord.y++;	break;
						case 'D':	tmpStack.push('U');	tmpCoord.y--;	break;
						case 'L':	tmpStack.push('R');	tmpCoord.x++;	break;
						case 'R':	tmpStack.push('L');	tmpCoord.x--;	break;
						default:	break;
						}
						
						if(moveCheck(tmpCoord.x, tmpCoord.y, true) == false)
						{
							tmpStack.pop();
							break;
						}
					}
					
					movePath = new char[tmpStack.size()];
					for(int index=tmpStack.size() - 1; index>=0; index--)
					{
						movePath[index] = tmpStack.pop();
					}
					
					return movePath;
				}
			}
			
			if(moveCheck(coord.x - 1, coord.y, checkEntity) == true && maskMap[coord.x - 1][coord.y] == '\0')
			{
				maskMap[coord.x - 1][coord.y] = 'L';
				queue.offer(new Pair<Integer, Integer>(coord.x - 1, coord.y));
			}
			if(moveCheck(coord.x + 1, coord.y, checkEntity) == true && maskMap[coord.x + 1][coord.y] == '\0')
			{
				maskMap[coord.x + 1][coord.y] = 'R';
				queue.offer(new Pair<Integer, Integer>(coord.x + 1, coord.y));
			}
			if(moveCheck(coord.x, coord.y - 1, checkEntity) == true && maskMap[coord.x][coord.y - 1] == '\0')
			{
				maskMap[coord.x][coord.y - 1] = 'U';
				queue.offer(new Pair<Integer, Integer>(coord.x, coord.y - 1));
			}
			if(moveCheck(coord.x, coord.y + 1, checkEntity) == true && maskMap[coord.x][coord.y + 1] == '\0')
			{
				maskMap[coord.x][coord.y + 1] = 'D';
				queue.offer(new Pair<Integer, Integer>(coord.x, coord.y + 1));
			}
		}
		
		return null;
	}
}
