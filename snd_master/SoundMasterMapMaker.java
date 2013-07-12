package snd_master;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

@SuppressWarnings("serial")
public class SoundMasterMapMaker extends JFrame{

	static final String imgNames[]={"ZZ","B0","B1","B2","B3","A1","A0","A2","A3","W0","W1","W2","W3","W4","W5","W6"}; 

	JPanel panelGameArea = new JPanel();
	JPanel panelMenu = new JPanel();
	JPanel panelMenuTools = new JPanel();
	TreeMap<String, JPanel> menuTools = new TreeMap<>();
	String menuToolsCurrent="ZZ";
	JComboBox<String> comboComplexity = new JComboBox<String>(Map.levelNames);
	JLabel labelLevel = new JLabel("Level:");
	JSpinner spinnerLevel = new JSpinner();
	JButton buttonLevelNew = new JButton();
	JButton buttonLevelClear = new JButton();
	JButton buttonSaveMap = new JButton("Save");
	int currComplexity=0;
	int curLevelIndex=0;
	ArrayList<Cell> currMap = new ArrayList<Cell>();
	Map theMap;

	
	void setCurrMap(String lvlMap){
		String arrayLvlMap[]=lvlMap.split(",");
		int k=0;

		for (int i=0;i<10;i++)
			for (int j=0;j<10;j++){
				boolean active=true;
				if ((currComplexity == 0) && ((i<2)||(j<2)||(i>7)||(j>7)))
					active=false;
				else if ((currComplexity == 1) && ((i<1)||(j<1)||(i>8)||(j>8)))
					active=false;
				currMap.get(k).setCellType(arrayLvlMap[k]);
				currMap.get(k).setCellActive(active);
				k++;
			}
	}
	
	String currMapToString(){
		String s=currMap.get(0).getCellType();
		for (int i=1;i<100;++i){
			s+=","+currMap.get(i).getCellType();
		}
		return s;
	}

	
	
	public SoundMasterMapMaker(String pathToGameFolder) {
		super("The Map Maker");
		setBounds(300, 200, 1100, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		SpringLayout layoutMain = new SpringLayout();
		Container containt = getContentPane();
		layoutMain.putConstraint(SpringLayout.NORTH, panelMenu, 0, SpringLayout.NORTH, containt);
		layoutMain.putConstraint(SpringLayout.EAST, panelMenu, 0, SpringLayout.EAST, containt);
		layoutMain.putConstraint(SpringLayout.SOUTH, panelMenu, 0, SpringLayout.SOUTH, containt);
		layoutMain.putConstraint(SpringLayout.WEST, panelMenu, -300, SpringLayout.EAST, containt);
		layoutMain.putConstraint(SpringLayout.NORTH, panelGameArea, 0, SpringLayout.NORTH, containt);
		layoutMain.putConstraint(SpringLayout.WEST, panelGameArea, 0, SpringLayout.WEST, containt);
		layoutMain.putConstraint(SpringLayout.SOUTH, panelGameArea, 0, SpringLayout.SOUTH, containt);
		layoutMain.putConstraint(SpringLayout.EAST, panelGameArea, 0, SpringLayout.WEST, panelMenu);
		setLayout(layoutMain);
		add(panelMenu,BorderLayout.EAST);
		add(panelGameArea,BorderLayout.CENTER);

		
		panelGameArea.setBorder(new EmptyBorder(50, 50, 50, 50));
		panelMenu.setBorder(BorderFactory.createEtchedBorder());
		panelGameArea.setLayout(new GridLayout(10,10));
//		for (int i=0;i<100;++i) panelGameArea.add(new Cell("ZZ",true));
		
		theMap = new Map(pathToGameFolder);
		for (int i=0;i<100;++i){
			currMap.add(new Cell("ZZ",false));
			panelGameArea.add(currMap.get(i));
		}
		setCurrMap(theMap.getLvlMap(currComplexity, 0));		
				
		panelMenu.setLayout(null);
		comboComplexity.setBounds(50,50,200,20);
		panelMenu.add(comboComplexity);
		comboComplexity.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currComplexity != ((JComboBox)e.getSource()).getSelectedIndex()){
//					save current lvl into theMap
					theMap.updateLvl(currComplexity, curLevelIndex, currMapToString());

//					update currComplexity
					currComplexity=((JComboBox)e.getSource()).getSelectedIndex();

//					update spinner
					int maxLevelIndex=theMap.getLvlMax(currComplexity);
					curLevelIndex=maxLevelIndex;
					spinnerLevel.setModel(new SpinnerNumberModel(curLevelIndex,0,maxLevelIndex,1));

//					set currMap to new lvl
					setCurrMap(theMap.getLvlMap(currComplexity, curLevelIndex));
					panelGameArea.repaint();

				}				
			}
		});
		
		labelLevel.setBounds(50, 80, 50, 20);
		panelMenu.add(labelLevel);
		spinnerLevel.setBounds(100, 80, 70, 20);
		spinnerLevel.setModel(new SpinnerNumberModel(0,0,theMap.getLvlMax(currComplexity),1));
		panelMenu.add(spinnerLevel);
//		TODO: повесить обработчик на спиннер
		spinnerLevel.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
//				save current lvl into theMap
				theMap.updateLvl(currComplexity, curLevelIndex, currMapToString());

//				set currMap
				curLevelIndex=(int)spinnerLevel.getValue();
				setCurrMap(theMap.getLvlMap(currComplexity, curLevelIndex));

				panelGameArea.repaint();
			}
		});
		
		buttonLevelNew.setBounds(175, 80, 30, 20);		
		buttonLevelNew.setIcon(UIManager.getIcon("FileView.fileIcon"));
		panelMenu.add(buttonLevelNew);
		buttonLevelNew.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
//				save current lvl into theMap
				theMap.updateLvl(currComplexity, curLevelIndex, currMapToString());
				
//				add lvl to theMap
				theMap.addLvl(currComplexity);
				int maxLevelIndex = theMap.getLvlMax(currComplexity);
				curLevelIndex = maxLevelIndex;
				spinnerLevel.setModel(new SpinnerNumberModel(curLevelIndex,0,maxLevelIndex,1));

//				set currMap to new lvl
				setCurrMap(theMap.getLvlMap(currComplexity, curLevelIndex));
				panelGameArea.repaint();
			}
		});
		
		buttonLevelClear.setBounds(210, 80, 30, 20);
		buttonLevelClear.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
		panelMenu.add(buttonLevelClear);
		buttonLevelClear.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Cell c : currMap){
					c.setCellType("ZZ");
				}
				panelGameArea.repaint();
			}
		});

		buttonSaveMap.setBounds(50,320,200,20);
		panelMenu.add(buttonSaveMap);
		buttonSaveMap.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					theMap.save();
				} catch (ParserConfigurationException | TransformerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		panelMenuTools.setLayout(new GridLayout(4,4,10,10));		
		panelMenuTools.setBounds(50,110,200,200);
		panelMenu.add(panelMenuTools);
		for (String cellCardType : Cell.cellCards.keySet()) {
			JPanel panel4tool = new JPanel(new BorderLayout());
			panel4tool.add(new Cell(cellCardType,false));
			panel4tool.setName(cellCardType);
			panel4tool.addMouseListener(new MouseListener() {				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub				
					menuTools.get(menuToolsCurrent).setBorder(null);
					menuToolsCurrent=((Component) e.getSource()).getName().toString();
					menuTools.get(menuToolsCurrent).setBorder(new EtchedBorder(Color.RED, Color.MAGENTA));
					Cell.currentCard=menuToolsCurrent;
				}				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub					
				}				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub					
				}				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub					
				}				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
				}
			});

			menuTools.put(cellCardType,panel4tool);
			panelMenuTools.add(panel4tool);
		}
		menuTools.get(menuToolsCurrent).setBorder(new EtchedBorder(Color.RED, Color.MAGENTA));
	}

	
	public static void main(String[] args) {
//		check the args
		if (args.length < 1){
			System.err.println("usage: SoundMasterMapMaker pathToSoundMasterFolder");
			System.exit(1);
		}
//		check the correctness of arg
		String path = args[0];
		if (!new File(path).isDirectory()){
			System.err.println("Error: argument mast be a path");
			System.exit(2);			
		}		
//		load the images into the map
		for (String name : SoundMasterMapMaker.imgNames){
			String fileName=path+"/img/"+name+".png";
			if (!new File(fileName).exists()){
				System.err.println("Error: file "+fileName+" is not exists!");
				System.exit(3);
			}
			Cell.cellCards.put(name, new ImageIcon(fileName).getImage());
		}
		
//		let's start
		new SoundMasterMapMaker(path).setVisible(true);
	}

}
