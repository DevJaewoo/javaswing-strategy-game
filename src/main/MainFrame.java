package main;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

import java.awt.Color;

import entity.Enemy;
import entity.Entity;
import entity.Item;
import entity.Player;
import entity.Warrior;
import utility.Utils;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontMetrics;

public class MainFrame {

	private JFrame frame;
	
	private ImageIcon tileGroundIc = new ImageIcon(Utils.ImgPath + "/Tiles/Ground3_2.png");
	private ImageIcon tileWaterIc = new ImageIcon(Utils.ImgPath + "/Tiles/Water3_6.png");
	private ImageIcon tileBlockIc = new ImageIcon(Utils.ImgPath + "/Tiles/Block3_3.png");

	private ImageIcon buttonUpIc = new ImageIcon(Utils.ImgPath + "/UI/ButtonUp1.png");
	private ImageIcon buttonDownIc = new ImageIcon(Utils.ImgPath + "/UI/ButtonDown1.png");
	private ImageIcon buttonLeftIc = new ImageIcon(Utils.ImgPath + "/UI/ButtonLeft1.png");
	private ImageIcon buttonRightIc = new ImageIcon(Utils.ImgPath + "/UI/ButtonRight1.png");

	public static final int ScreenUpdatePeriod = 10;
	public static final int tileWidth = 75;
	public static final int tileHeight = 75;
	
	public static JPanel mainPanel;
	
	public static int mapWidth;
	public static int mapHeight;
	
	public static char[][] groundMap;
	public static Entity[][] entityMap;
	public static Player playerInstance;
	public static Thread playerThread;
	
	public static JLabel currentStageLabel;
	public static JLabel playerHealthLabel;
	public static JLabel playerManaLabel;
	public static JLabel playerDefenseLabel;
	public static JLabel playerMoveLabel;
	
	public static List<Item> itemList = new ArrayList<Item>();
	
	public static int gameSpeed = 100; //게임 속도. 칸 이동속도 기준
	
	private Image buffImage;
	private Graphics buffg;
	private Graphics2D buffg2;
	private Font font = new Font("Century Gothic", Font.BOLD, 15);
	private FontMetrics fontMetrics;

	class MainPanel extends JPanel
	{
		/**
		 * 안하면 워닝 뜸
		 */
		private static final long serialVersionUID = 2714770598583808666L;

		public MainPanel()
		{
			setFocusable(true); //키 입력 1순위
			
			//playerInstance = new Warrior(-1, -1);
			
			Thread repaintThread = new Thread(new Runnable() { //화면 갱신 쓰레드. 맵의 개체들 pixel 값만 바꿔주면 알아서 이동시켜서 띄워줌.
		
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true)
					{
						repaint();
						
						try
						{
							Thread.sleep(ScreenUpdatePeriod);
						}
						catch(InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
				
			});
			
			repaintThread.setPriority(5);
			repaintThread.start();
			
		}

		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			//System.out.println("Paint Component");
			Image tileImage = null;
			
			int baseX = (getWidth() - (mapWidth * tileWidth)) / 2;
			int baseY = (getHeight() - (mapHeight * tileHeight)) / 2;			
			
			if(buffg == null)
			{
				buffImage = createImage(this.getWidth(), this.getHeight());
				
				if(buffImage == null)
				{
					Utils.printDebugMsg("오프스크린 생성 실패");
				}
				else
				{
					buffg = buffImage.getGraphics();
					buffg.setFont(font);
					buffg2 = (Graphics2D)buffg;
					fontMetrics = buffg.getFontMetrics(font);
				}
			}
			
			super.paintComponent(buffg); //화면 비워주는 함수
			
			for(int y=0; y<mapHeight; y++)
			{
				for(int x=0; x<mapWidth; x++)
				{
					switch(groundMap[x][y])
					{
					case 'G': 	tileImage = tileGroundIc.getImage(); 	break;	
					case 'W': 	tileImage = tileWaterIc.getImage(); 	break;
					case 'B': 	tileImage = tileBlockIc.getImage(); 	break;	
					default:	break;
					}
					
					buffg.drawImage(tileImage, baseX + x * tileWidth, baseY + y * tileHeight, tileWidth, tileHeight, this);
				}
			}
			
			for(int y=0; y<mapHeight; y++)
			{
				for(int x=0; x<mapWidth; x++)
				{
					if(entityMap[x][y] != null)
					{
						try
						{
							buffg.drawImage(Utils.getRotatedImage(entityMap[x][y].getImageIcon(), entityMap[x][y].direction),
									baseX + entityMap[x][y].getMapX() * tileWidth + entityMap[x][y].getPixelX(),  //맵 기본 좌표 + 맵 타일 좌표 + 구조체 추가 거리
									baseY + entityMap[x][y].getMapY() * tileHeight + entityMap[x][y].getPixelY(),
									tileWidth, tileHeight, this);
							
							if(entityMap[x][y] instanceof Enemy)
							{
								String text = ((Enemy)entityMap[x][y]).getCurrentHealth() + "/" + ((Enemy)entityMap[x][y]).getDefense();
								
								buffg.drawString(text, 
										baseX + entityMap[x][y].getMapX() * tileWidth + entityMap[x][y].getPixelX() + ((tileWidth - fontMetrics.stringWidth(text)) / 2), 
										baseY + entityMap[x][y].getMapY() * tileHeight + entityMap[x][y].getPixelY() + (int)(tileHeight * 0.6));
							}
							
							
							if(entityMap[x][y].isDead == true)
							{
								entityMap[x][y] = null;
							}
						}
						catch(NullPointerException e) //적이 출력되는 도중 사망하는 경우
						{
							continue;
						}
					}
				}
			}
			
			Iterator<Item> iter = itemList.iterator();
			while(iter.hasNext())
			{
				Item item = iter.next();
				if(item.displayTime <= 0)
				{
					//itemList.remove(item);
					iter.remove();
					continue;
				}
				
				buffg2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)item.alpha/255));
				buffg.drawImage(item.image.getImage(), 
						baseX + item.mapX * tileWidth + item.pixelX, 
						baseY + item.mapY * tileHeight + item.pixelX, 
						item.width, item.height, this);
				
				item.displayTime -= ScreenUpdatePeriod;
			}
			/* Con무슨Exception 생김
			for(Item item : itemList)
			{
				if(item.displayTime <= 0)
				{
					itemList.remove(item);
					continue;
				}
				
				buffg2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)item.alpha/255));
				buffg.drawImage(item.image.getImage(), 
						baseX + item.mapX * tileWidth + item.pixelX, 
						baseY + item.mapY * tileHeight + item.pixelX, 
						item.width, item.height, this);
				
				item.displayTime -= ScreenUpdatePeriod;
			}
			*/
			buffg2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1)); //투명도 복구
			
			g.drawImage(buffImage, 0, 0, this);
			
		}
	}
	
	static class GameManager {
		
		private static final String mapFilePath = "maps";
		private static final int maxStage = 5;
		private static String currentStageFilePath;
		
		public GameManager()
		{
			
		}
		
		public static void start()
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for(int i=1; i<=maxStage; i++)
					{
						Utils.printDebugMsg("Start stage" + i);
						while(startStage(i) == false);
					}
					Utils.printDebugMsg("Game end");
					quit();
				}
			}).start();
		}
		
		public static void quit()
		{
			System.exit(0);
		}
		
		public static boolean startStage(int stage)
		{
			boolean isEnemyExist = false;
			
			currentStageFilePath = mapFilePath + "/Stage" + stage + ".txt";
			MainFrame.currentStageLabel.setText("Stage" + stage);
			
			playerInstance = new Warrior(-1, -1);
			
			mapHeight = Utils.getSize(currentStageFilePath).first(); //맵 크기 입력. 개인적으로 비효율적이라 생각함. 
			mapWidth = Utils.getSize(currentStageFilePath).second();
			
			groundMap = Utils.getStageMap(currentStageFilePath); //지형 맵 입력
			entityMap = Utils.getEntityMap(currentStageFilePath); //플레이어, 적 입력
			
			while(true)
			{
				Utils.printDebugMsg("Turn start");
				for(int y=0; y<mapHeight; y++)
				{
					for(int x=0; x<mapWidth; x++)
					{
						if(entityMap[x][y] instanceof Entity)
						{
							entityMap[x][y].turnInit();
						}
					}
				}
				
				playerInstance.setCurrentMana(playerInstance.getCurrentMana() + 10);
				playerInstance.updateStatus();
				
				Utils.printDebugMsg("Player turn");
				while(playerInstance.isTurnFinished() == false)
				{
					try {
						if(playerThread != null)
						{
							Utils.printDebugMsg("Player active skill");
							playerThread.start();
							playerThread.join();
							Utils.printDebugMsg("Player skill active complete");
							playerThread = null;
						}
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//플레이어 턴 안끝났는데 시작할 수도 있음
				Utils.printDebugMsg("Enemy turn");
				isEnemyExist = false;
				for(int y=0; y<mapHeight; y++)
				{
					for(int x=0; x<mapWidth; x++)
					{
						//System.out.println("x: " + x + " y: " + y + " Entity : " + (entityMap[x][y] != null));
						if(entityMap[x][y] instanceof Enemy)
						{
							isEnemyExist = true;
							if(entityMap[x][y].isTurnFinished() == false)
							{
								entityMap[x][y].active();
								
								if(playerInstance.isDead == true)
								{
									Utils.printDebugMsg("Player lose");
									return false;
								}
							}
						}
					}
				}
				
				Utils.printDebugMsg("Turn End");
				if(playerInstance.isDead == true)
				{
					Utils.printDebugMsg("Player lose");
					return false;
				}
				else if(isEnemyExist == false)
				{
					Utils.printDebugMsg("Player win");
					return true;
				}
			}
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.out.println("Start");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		Utils.printDebugMsg("Initialize");
		
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.setBounds(100, 100, 1160, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		/*
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		frame.setUndecorated(true);
		
		JPanel panel_main = new JPanel();
		panel_main.setBounds(0, 0, 1160, 720);
		panel_main.setBackground(new Color(0, 0, 139));
		panel_main.setForeground(new Color(0, 0, 139));
		frame.getContentPane().add(panel_main);
		panel_main.setLayout(null);
		
		JLabel lblLOGO = new JLabel("LOGO");
		lblLOGO.setBackground(Color.RED);
		lblLOGO.setBounds(48, 20, 225, 105);
		panel_main.add(lblLOGO);
		lblLOGO.setIcon(new ImageIcon(Utils.ImgPath + "\\UI\\LOGO.png"));
		
		JPanel panel = new MainPanel();
		panel.setBounds(320, 20, 680, 680);
		panel_main.add(panel);
		panel.setBackground(new Color(218, 165, 32));
		
		JButton btnActiveSkill1 = new JButton("Skill1");
		btnActiveSkill1.setBounds(1020, 20, 120, 120);
		btnActiveSkill1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playerThread != null)
				{
					return;
				}
				
				playerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerInstance.activeSkill1();
					}
				});
			}
		});
		panel_main.add(btnActiveSkill1);
		
		JButton btnActiveSkill2 = new JButton("Skill2");
		btnActiveSkill2.setBounds(1020, 160, 120, 120);
		btnActiveSkill2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playerThread != null)
				{
					return;
				}
				
				playerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerInstance.activeSkill2();
					}
				});
			}
		});
		panel_main.add(btnActiveSkill2);
		
		JButton btnActiveSkill3 = new JButton("Skill3");
		btnActiveSkill3.setBounds(1020, 300, 120, 120);
		btnActiveSkill3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playerThread != null)
				{
					return;
				}
				
				playerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerInstance.activeSkill3();
					}
				});
			}
		});
		panel_main.add(btnActiveSkill3);
		
		JButton btnActiveSkill4 = new JButton("Skill4");
		btnActiveSkill4.setBounds(1020, 440, 120, 120);
		btnActiveSkill4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playerThread != null)
				{
					return;
				}
				
				playerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerInstance.activeSkill4();
					}
				});
			}
		});
		panel_main.add(btnActiveSkill4);

		JButton btnTurnEnd = new JButton("Turn End");
		btnTurnEnd.setBounds(1020, 580, 120, 120);
		btnTurnEnd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playerThread != null)
				{
					return;
				}
				
				playerInstance.setTurnFinished(true);
			}
		});
		panel_main.add(btnTurnEnd);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(30, 420, 260, 260);
		panel_main.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnMoveUp = new JButton(buttonUpIc);
		btnMoveUp.setBounds(90, 10, 80, 80);
		
		btnMoveUp.setBorderPainted(false);
		btnMoveUp.setBorder(null);
		btnMoveUp.setContentAreaFilled(false);
		
		btnMoveUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playerThread != null)
				{
					return;
				}
				
				playerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerInstance.moveTile('U');
					}
				});
			}
		});
		panel_1.add(btnMoveUp);
		
		JButton btnMoveLeft = new JButton(buttonLeftIc);
		btnMoveLeft.setBounds(10, 90, 80, 80);
		
		btnMoveLeft.setBorderPainted(false);
		btnMoveLeft.setBorder(null);
		btnMoveLeft.setContentAreaFilled(false);
		
		btnMoveLeft.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playerThread != null)
				{
					return;
				}
				
				playerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerInstance.moveTile('L');
					}
				});
			}
		});
		panel_1.add(btnMoveLeft);
		
		JButton btnMoveDown = new JButton(buttonDownIc);
		btnMoveDown.setBounds(90, 170, 80, 80);
		
		btnMoveDown.setBorderPainted(false);
		btnMoveDown.setBorder(null);
		btnMoveDown.setContentAreaFilled(false);
		
		btnMoveDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playerThread != null)
				{
					return;
				}
				
				playerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerInstance.moveTile('D');
					}
				});
			}
		});
		panel_1.add(btnMoveDown);
		
		JButton btnMoveRight = new JButton(buttonRightIc);
		btnMoveRight.setBounds(170, 90, 80, 80);
		
		btnMoveRight.setBorderPainted(false);
		btnMoveRight.setBorder(null);
		btnMoveRight.setContentAreaFilled(false);
		
		btnMoveRight.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playerThread != null)
				{
					return;
				}
				
				playerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerInstance.moveTile('R');
					}
				});
			}
		});
		panel_1.add(btnMoveRight);
		
		mainPanel = panel;
		
		JLabel stageLabel = new JLabel("StageX");
		stageLabel.setForeground(Color.ORANGE);
		stageLabel.setFont(new Font("Consolas", Font.BOLD, 33));
		stageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stageLabel.setBounds(30, 171, 260, 72);
		panel_main.add(stageLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Health");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setForeground(Color.RED);
		lblNewLabel_2.setFont(new Font("Consolas", Font.BOLD, 22));
		lblNewLabel_2.setBounds(30, 253, 100, 30);
		panel_main.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Mana");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setForeground(Color.CYAN);
		lblNewLabel_2_1.setFont(new Font("Consolas", Font.BOLD, 22));
		lblNewLabel_2_1.setBounds(30, 293, 100, 30);
		panel_main.add(lblNewLabel_2_1);
		
		JLabel healthLabel = new JLabel("000/999");
		healthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		healthLabel.setForeground(Color.RED);
		healthLabel.setFont(new Font("Consolas", Font.BOLD, 22));
		healthLabel.setBounds(173, 253, 100, 30);
		panel_main.add(healthLabel);
		
		JLabel manaLabel = new JLabel("000/999");
		manaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		manaLabel.setForeground(Color.CYAN);
		manaLabel.setFont(new Font("Consolas", Font.BOLD, 22));
		manaLabel.setBounds(173, 293, 100, 30);
		panel_main.add(manaLabel);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("Defense");
		lblNewLabel_2_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1_1.setForeground(Color.GRAY);
		lblNewLabel_2_1_1.setFont(new Font("Consolas", Font.BOLD, 22));
		lblNewLabel_2_1_1.setBounds(30, 333, 100, 30);
		panel_main.add(lblNewLabel_2_1_1);
		
		JLabel defenseLabel = new JLabel("999");
		defenseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		defenseLabel.setForeground(Color.GRAY);
		defenseLabel.setFont(new Font("Consolas", Font.BOLD, 22));
		defenseLabel.setBounds(173, 333, 100, 30);
		panel_main.add(defenseLabel);
		
		JLabel lblNewLabel_2_1_1_1 = new JLabel("Move");
		lblNewLabel_2_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1_1_1.setForeground(Color.GREEN);
		lblNewLabel_2_1_1_1.setFont(new Font("Consolas", Font.BOLD, 22));
		lblNewLabel_2_1_1_1.setBounds(30, 373, 100, 30);
		panel_main.add(lblNewLabel_2_1_1_1);
		
		JLabel moveLabel = new JLabel("0/0");
		moveLabel.setHorizontalAlignment(SwingConstants.CENTER);
		moveLabel.setForeground(Color.GREEN);
		moveLabel.setFont(new Font("Consolas", Font.BOLD, 22));
		moveLabel.setBounds(173, 373, 100, 30);
		panel_main.add(moveLabel);


		currentStageLabel = stageLabel;
		playerHealthLabel = healthLabel;
		playerManaLabel = manaLabel;
		playerDefenseLabel = defenseLabel;
		playerMoveLabel = moveLabel;
		
		GameManager.start();
	}
}
