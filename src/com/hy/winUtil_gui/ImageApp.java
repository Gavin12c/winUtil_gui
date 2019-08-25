package com.hy.winUtil_gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageApp extends JFrame {
	
	private static final long serialVersionUID = 1L;
	Point pressedPoint;
	private int x = 538/5;
	private int y = 897/5;
	
	//精灵显示
	private boolean flag = false;
	
    public ImageApp() {
    	this.setUndecorated(true);// 取消窗体修饰效果
    	this.setBackground(new Color(0,0,0,0));  //alpha 0 为透明
    	this.setAlwaysOnTop(true); //窗体最顶层显示
    	this.setType(JFrame.Type.UTILITY);//隐藏任务栏图标
    	/**
		 * 窗体鼠标移动事件
		 */
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) { //鼠标按下事件
				pressedPoint = e.getPoint(); //记录鼠标坐标
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) { // 鼠标拖拽事件
				Point point = e.getPoint();// 获取当前坐标
				Point locationPoint = getLocation();// 获取窗体坐标
				int x = locationPoint.x + point.x - pressedPoint.x;// 计算移动后的新坐标
				int y = locationPoint.y + point.y - pressedPoint.y;
				setLocation(x, y);// 改变窗体位置
			}
		});
        setLocationRelativeTo(null);// 窗体居中
        setSize(x, y);    //  设置窗体大小
        JPanel panel = new ImagePanel();
        panel.setOpaque(false);  //设置JPanel透明
        getContentPane().add(panel);
//        setVisible(true);    //显示
    }

//    public static void main(String[] args) {
//        ImageApp imageApp = new ImageApp();
////        imageApp.setVisible(false);
//    }

    class ImagePanel extends JPanel {
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D)g;
            ImageIcon icon = new ImageIcon("lib/men.png");
            g2.drawImage(icon.getImage(), 0, 0, x, y, null);
        }
    }

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
    
    
    
}