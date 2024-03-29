package exam;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * 
* Copyright: Copyright (c) 2019 ChongYang
* @ClassName: MyListener.java
* @Description: 数据核心层
* @version: v1.0.0
* @date: 2019年6月18日 下午5:15:42 
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年6月18日     YancyChong           v1.0.0               修改原因
 */
public class MyListener extends KeyAdapter implements ActionListener {

	private Game UI;// 界面对象
	private int Numbers[][];// 存放数据的数组
	private Random rand = new Random();
	private int BackUp[][]= new int[4][4];//用于备份数组，供回退时使用
	private int BackUp2[][]= new int[4][4];//用于备份数组，供起死回生时使用
	public JLabel lb;
	int score = 0;
	int tempscore,tempscore2;//记录回退的分数值
	public JButton bt,about,back;
	public JCheckBox isSoundBox;
	private boolean isWin=false,relive=false,hasBack=false,isSound=true;
	


	public MyListener(Game UI, int Numbers[][], JLabel lb,JButton bt,JButton about,JButton back,JCheckBox isSoundBox) {
		this.UI = UI;
		this.Numbers = Numbers;
		this.lb = lb;
		this.bt=bt;
		this.about=about;
		this.back=back;
		this.isSoundBox=isSoundBox;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() ==bt ){
			isWin=false;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				Numbers[i][j] = 0;

		score = 0;// 初始化，保证每次重置游戏都是0分开始
		lb.setText("分数：" + score);
		int r1 = rand.nextInt(4);
		int r2 = rand.nextInt(4);
		int c1 = rand.nextInt(4);
		int c2 = rand.nextInt(4);

		while (r1 == r2 && c1 == c2) {
			r2 = rand.nextInt(4);
			c2 = rand.nextInt(4);
		}
        
		// 生成数字（2或者4）
		int value1 = rand.nextInt(2) * 2 + 2;
		int value2 = rand.nextInt(2) * 2 + 2;

		// 把数字存进对应的位置
		Numbers[r1][c1] = value1;
		Numbers[r2][c2] = value2;
		UI.paint(UI.getGraphics());
		}
		else if(e.getSource()==about){
			JOptionPane.showMessageDialog(UI, "游戏规则：\n"
					+ "开始时棋盘内随机出现两个数字，出现的数字仅可能为2或4\n"
					+ "玩家可以选择上下左右四个方向键（就是那个↑↓←→懂？），要是棋盘里面的数字出现位移或合并，就视他们为有效移动\n"
					+ "玩家选择的方向上若有相同的数字则合并，每次有效移动可以同时合并，但不可以连续合并\n"
					+ "合并所得的所有新生成数字想加即为该步的有效得分\n"
					+ "玩家选择的方向行或列前方有空格则出现位移\n"
					+ "每有效移动一步，棋盘的空位(无数字处)随机出现一个数字(依然可能为2或4)\n"
					+ "棋盘被数字填满，无法进行有效移动，判负，游戏结束\n"
					+ "棋盘上出现2048，判胜，游戏结束。\n"
					+ "相关人员如下\n"
					+ "项目规划、技术文档、代码编写：李崇阳\n"
					+ "说明文档、演示文档：李崇阳");
		}
		else if(e.getSource()==back&&hasBack==false){
			hasBack=true;
			if(relive==false){
			score=tempscore;
			lb.setText("分数：" + score);
			for(int i=0;i<BackUp.length;i++){
				Numbers[i]=Arrays.copyOf(BackUp[i], BackUp[i].length);
			}
			}
			else{
					score=tempscore2;
					lb.setText("分数：" + score);
					for(int i=0;i<BackUp2.length;i++){
						Numbers[i]=Arrays.copyOf(BackUp2[i], BackUp2[i].length);
					}
					relive=false;
			}
			UI.paint(UI.getGraphics());	
		}
		else if(e.getSource().equals(isSoundBox)){
			 if (isSoundBox.isSelected())
				 isSound=false;
		       else
		    	   isSound=true;
		    }
		}
	

	

	    
	// 键盘监听
	public void keyPressed(KeyEvent event) {

		int Counter = 0;// 计算器，判断是否移动了
		int NumCounter = 0;// 用于统计整个大方框中数字的个数，判断是否已满
		int NumNearCounter = 0;// 用于统计相邻格子数字相同的个数
		/*
		 * 方向键键值：左：37上：38右：39下：40
		 */

		hasBack = false;

		if (BackUp != null || BackUp.length != 0) {
			tempscore2 = tempscore;// 先把分数备份好
			// 下面的for循环调用java.util.Arrays.copyOf()方法复制数组，实现备份
			for (int i = 0; i < BackUp.length; i++) {
				BackUp2[i] = Arrays.copyOf(BackUp[i], BackUp[i].length);
			}
		}

		tempscore = score;// 先把分数备份好
		// 下面的for循环调用java.util.Arrays.copyOf()方法复制数组，实现备份
		for (int i = 0; i < Numbers.length; i++) {
			BackUp[i] = Arrays.copyOf(Numbers[i], Numbers[i].length);
		}

		if (isWin == false) {
			switch (event.getKeyCode()) {

			case 37:
				// 向左移动
				if (isSound == true)
					new PlaySound("move.wav").start();
				for (int h = 0; h < 4; h++)
					for (int l = 0; l < 4; l++)
						if (Numbers[h][l] != 0) {
							int temp = Numbers[h][l];
							int pre = l - 1;
							while (pre >= 0 && Numbers[h][pre] == 0) {
								Numbers[h][pre] = temp;
								Numbers[h][pre + 1] = 0;
								pre--;
								Counter++;
							}
						}
				for (int h = 0; h < 4; h++)
					for (int l = 0; l < 4; l++)
						if (l + 1 < 4
								&& (Numbers[h][l] == Numbers[h][l + 1])
								&& (Numbers[h][l] != 0 || Numbers[h][l + 1] != 0)) {
							if (isSound == true)
								new PlaySound("merge.wav").start();
							Numbers[h][l] = Numbers[h][l] + Numbers[h][l + 1];
							Numbers[h][l + 1] = 0;
							Counter++;
							score += Numbers[h][l];
							if (Numbers[h][l] == 2048) {
								isWin = true;
							}
						}

				for (int h = 0; h < 4; h++)
					for (int l = 0; l < 4; l++)
						if (Numbers[h][l] != 0) {
							int temp = Numbers[h][l];
							int pre = l - 1;
							while (pre >= 0 && Numbers[h][pre] == 0) {
								Numbers[h][pre] = temp;
								Numbers[h][pre + 1] = 0;
								pre--;
								Counter++;
							}
						}
				break;

			case 39:// 向右移动
				if (isSound == true)
					new PlaySound("move.wav").start();
				for (int h = 3; h >= 0; h--)
					for (int l = 3; l >= 0; l--)
						if (Numbers[h][l] != 0) {
							int temp = Numbers[h][l];
							int pre = l + 1;
							while (pre <= 3 && Numbers[h][pre] == 0) {
								Numbers[h][pre] = temp;
								Numbers[h][pre - 1] = 0;
								pre++;
								Counter++;
							}
						}

				for (int h = 3; h >= 0; h--)
					for (int l = 3; l >= 0; l--)
						if (l + 1 < 4
								&& (Numbers[h][l] == Numbers[h][l + 1])
								&& (Numbers[h][l] != 0 || Numbers[h][l + 1] != 0)) {
							if (isSound == true)
								new PlaySound("merge.wav").start();
							Numbers[h][l + 1] = Numbers[h][l]
									+ Numbers[h][l + 1];
							Numbers[h][l] = 0;
							Counter++;
							score += Numbers[h][l + 1];
							if (Numbers[h][l + 1] == 2048) {
								isWin = true;
							}
						}
				for (int h = 3; h >= 0; h--)
					for (int l = 3; l >= 0; l--)
						if (Numbers[h][l] != 0) {
							int temp = Numbers[h][l];
							int pre = l + 1;
							while (pre <= 3 && Numbers[h][pre] == 0) {
								Numbers[h][pre] = temp;
								Numbers[h][pre - 1] = 0;
								pre++;
								Counter++;
							}
						}
				break;

			case 38:
				// 向上移动
				if (isSound == true)
					new PlaySound("move.wav").start();
				for (int l = 0; l < 4; l++)
					for (int h = 0; h < 4; h++)
						if (Numbers[h][l] != 0) {
							int temp = Numbers[h][l];
							int pre = h - 1;
							while (pre >= 0 && Numbers[pre][l] == 0) {
								Numbers[pre][l] = temp;
								Numbers[pre + 1][l] = 0;
								pre--;
								Counter++;
							}
						}
				for (int l = 0; l < 4; l++)
					for (int h = 0; h < 4; h++)
						if (h + 1 < 4
								&& (Numbers[h][l] == Numbers[h + 1][l])
								&& (Numbers[h][l] != 0 || Numbers[h + 1][l] != 0)) {
							if (isSound == true)
								new PlaySound("merge.wav").start();
							Numbers[h][l] = Numbers[h][l] + Numbers[h + 1][l];
							Numbers[h + 1][l] = 0;
							Counter++;
							score += Numbers[h][l];
							if (Numbers[h][l] == 2048) {
								isWin = true;
							}
						}

				for (int l = 0; l < 4; l++)
					for (int h = 0; h < 4; h++)
						if (Numbers[h][l] != 0) {
							int temp = Numbers[h][l];
							int pre = h - 1;
							while (pre >= 0 && Numbers[pre][l] == 0) {
								Numbers[pre][l] = temp;
								Numbers[pre + 1][l] = 0;
								pre--;
								Counter++;
							}
						}
				break;

			case 40:
				// 向下移动
				if (isSound == true)
					new PlaySound("move.wav").start();
				for (int l = 3; l >= 0; l--)
					for (int h = 3; h >= 0; h--)
						if (Numbers[h][l] != 0) {
							int temp = Numbers[h][l];
							int pre = h + 1;
							while (pre <= 3 && Numbers[pre][l] == 0) {
								Numbers[pre][l] = temp;
								Numbers[pre - 1][l] = 0;
								pre++;
								Counter++;
							}
						}
				for (int l = 3; l >= 0; l--)
					for (int h = 3; h >= 0; h--)
						if (h + 1 < 4
								&& (Numbers[h][l] == Numbers[h + 1][l])
								&& (Numbers[h][l] != 0 || Numbers[h + 1][l] != 0)) {
							if (isSound == true)
								new PlaySound("merge.wav").start();
							Numbers[h + 1][l] = Numbers[h][l]
									+ Numbers[h + 1][l];
							Numbers[h][l] = 0;
							Counter++;
							score += Numbers[h + 1][l];
							if (Numbers[h + 1][l] == 2048) {
								isWin = true;
							}
						}

				for (int l = 3; l >= 0; l--)
					for (int h = 3; h >= 0; h--)
						if (Numbers[h][l] != 0) {
							int temp = Numbers[h][l];
							int pre = h + 1;
							while (pre <= 3 && Numbers[pre][l] == 0) {
								Numbers[pre][l] = temp;
								Numbers[pre - 1][l] = 0;
								pre++;
								Counter++;
							}
						}
				break;

			}

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (Numbers[i][j] == Numbers[i][j + 1]
							&& Numbers[i][j] != 0) {
						NumNearCounter++;
					}
					if (Numbers[i][j] == Numbers[i + 1][j]
							&& Numbers[i][j] != 0) {
						NumNearCounter++;
					}
					if (Numbers[3][j] == Numbers[3][j + 1]
							&& Numbers[3][j] != 0) {
						NumNearCounter++;
					}
					if (Numbers[i][3] == Numbers[i + 1][3]
							&& Numbers[i][3] != 0) {
						NumNearCounter++;
					}
				}
			}
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (Numbers[i][j] != 0) {
						NumCounter++;
					}
				}
			}
			if (Counter > 0) {

				lb.setText("分数：" + score);
				int r1 = rand.nextInt(4);
				int c1 = rand.nextInt(4);
				while (Numbers[r1][c1] != 0) {
					r1 = rand.nextInt(4);
					c1 = rand.nextInt(4);
				}
				int value1 = rand.nextInt(2) * 2 + 2;
				Numbers[r1][c1] = value1;
			}

			if (isWin == true){
				UI.paint(UI.getGraphics());
				JOptionPane.showMessageDialog(UI, "好了你赢了!\n你娃儿的最终得分为：" + score);
			}
				
			if (NumCounter == 16 && NumNearCounter == 0) {
				relive = true;
				JOptionPane.showMessageDialog(UI, "没路了，你凉了！"
						+ "\n你输了，别玩了，快去看书" + "\n饿，忘记了，这个游戏还有一个起死回生的功能，“退一步”试试？"
						+ "\n说不定这把凉的要慢一点");
			}
			UI.paint(UI.getGraphics());
		}
	}
}
