package snd_master;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.TreeMap;

import javax.swing.JComponent;

public class Cell extends JComponent implements MouseListener, MouseWheelListener{
	static TreeMap<String, Image> cellCards = new TreeMap<>();
	static String currentCard="ZZ";

	private String cellType;
	private boolean active=false;
	
	
	public Cell(String cellType, boolean active) {
		this.cellType = cellType;
		this.active = active;		
		if (active){
			addMouseListener(this);
			addMouseWheelListener(this);
		}
	}
	
	public String getCellType(){
		return(this.cellType);
	}

	public void setCellType(String type){
		this.cellType = type;
	}

	public boolean isCellActive(){
		return(this.active);
	}
	
	public void setCellActive(boolean active){
		this.active = active;
		if (active){
			if (getMouseListeners().length == 0) addMouseListener(this);
			if (getMouseWheelListeners().length == 0) addMouseWheelListener(this);			
		}
	}
	
	
	public void paintComponent(Graphics g){
		g.drawImage(cellCards.get(cellType), 0, 0, getWidth(), getHeight(), null);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!active) return;
		double dir = e.getPreciseWheelRotation();
		String oldCellType=cellType;
		switch (cellType) {
			case "ZZ":	
				break;
			case "A0":	cellType=(dir>0) ? "A1" : "A3";
				break;
			case "A1":	cellType=(dir>0) ? "A2" : "A0";
				break;
			case "A2":	cellType=(dir>0) ? "A3" : "A1";
				break;
			case "A3":	cellType=(dir>0) ? "A0" : "A2";
				break;
			case "B0":	cellType="B1";
				break;
			case "B1":	cellType="B0";
				break;
			case "B2":	cellType="B3";
				break;
			case "B3":	cellType="B2";
				break;
			case "W0":	cellType="W1";
				break;
			case "W1":	cellType="W0";
				break;
			case "W2":	cellType=(dir>0) ? "W3" : "W5";
				break;
			case "W3":	cellType=(dir>0) ? "W4" : "W2";
				break;
			case "W4":	cellType=(dir>0) ? "W5" : "W3";
				break;
			case "W5":	cellType=(dir>0) ? "W2" : "W4";
				break;
			case "W6":
				break;

			default:
				break;
		}
		if (oldCellType != cellType)
			repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!active) return;
		if (cellType != currentCard){
			cellType = currentCard;
			repaint();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
