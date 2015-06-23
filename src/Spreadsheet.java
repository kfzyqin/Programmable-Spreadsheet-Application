/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.sun.glass.events.MouseEvent;

public class Spreadsheet implements Runnable, ActionListener, KeyListener,
		SelectionObserver, DocumentListener {

	private static final Dimension PREFEREDDIM = new Dimension(500, 400);
	/**
	 * Spreadsheet - a simple spreadsheet program. Eric McCreath 2015
	 */
	
	private static final String EXITCOMMAND = "exitcommand";
	private static final String CLEARCOMMAND = "clearcommand";
	private static final String SAVECOMMAND = "savecommand";
	private static final String OPENCOMMAND = "opencommand";
	private static final String EDITFUNCTIONCOMMAND = "editfunctioncommand";
	
	private static final String SHEET = System.getProperty("user.dir") + "//"  + "sheet";
	private static final String SHEET1 = System.getProperty("user.dir") + "//" + "sheet1.XML";
	private static final String SHEET2 = System.getProperty("user.dir") + "//" + "sheet2.XML";
	private static final String SHEET3 = System.getProperty("user.dir") + "//" + "sheet3.XML";

	JFrame jframe;
	WorksheetView worksheetview;
	FunctionEditor functioneditor;
	WorkSheet worksheet;
	JButton calculateButton;
	JTextField cellEditTextField;
	JLabel selectedCellLabel;
	JFileChooser filechooser = new JFileChooser();
	Integer whichSheet = 1;

	public Spreadsheet() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new Spreadsheet();
	}

	public void run() {
		
		
		jframe = new JFrame("Spreadsheet");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set up the menu bar
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("File");
		bar.add(menu);
		makeMenuItem(menu, "New", CLEARCOMMAND);
		makeMenuItem(menu, "Open", OPENCOMMAND);
		makeMenuItem(menu, "Save", SAVECOMMAND);
		makeMenuItem(menu, "Exit", EXITCOMMAND);
		menu = new JMenu("Edit");
		bar.add(menu);
		makeMenuItem(menu, "EditFunction", EDITFUNCTIONCOMMAND);

		jframe.setJMenuBar(bar);
		worksheet = new WorkSheet();
		worksheetview = new WorksheetView(worksheet);
		worksheetview.addSelectionObserver(this);
		worksheetview.addKeyListener(this);
		worksheetview.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					String input = JOptionPane.showInputDialog(DialogueFrame.jframe, 
					        "Enter the value to continue", 
					        "INPUT THE VALUE", 
					        JOptionPane.WARNING_MESSAGE);
					CellIndex index = worksheetview.getSelectedIndex();
					Cell current = worksheet.lookup(index);
					if (input != null && input != "") {
						current.setText(input);
					}
					update();
				}
				
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		// set up the tool area
		JPanel toolarea = new JPanel();
		calculateButton = new JButton("Calculate");
		calculateButton.addActionListener(this);
		calculateButton.setActionCommand("CALCULATE");
		toolarea.add(calculateButton);
		selectedCellLabel = new JLabel("--");
		selectedCellLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		toolarea.add(selectedCellLabel);
		cellEditTextField = new JTextField(20);
		cellEditTextField.getDocument().addDocumentListener(this);
		
		cellEditTextField.addKeyListener(this);
		
		toolarea.add(cellEditTextField);

		functioneditor = new FunctionEditor(worksheet);

		jframe.getContentPane().add(new JScrollPane(worksheetview),
				BorderLayout.CENTER);
		jframe.getContentPane().add(toolarea, BorderLayout.PAGE_START);
		
		JPanel shiftButtons = new JPanel(new GridLayout(0, 3));
		JButton sheet1 = new JButton("Sheet 1");
		JButton sheet2 = new JButton("Sheet 2");
		JButton sheet3 = new JButton("Sheet 3");
		
		sheet1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String toSaveSheet = SHEET + whichSheet + ".XML";
				File f = new File(toSaveSheet);
				f.getParentFile().mkdirs(); 
				try {
					f.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				worksheet.save(f);
				
				File toOpen = new File(SHEET1);
				try {
					worksheet = WorkSheet.load(toOpen);
					whichSheet = 1;
					worksheetChange();
				} catch (Exception exception) {
					worksheet = new WorkSheet();
					worksheetChange();
				}
				
			}
			
		});
		
		sheet2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String toSaveSheet = SHEET + whichSheet + ".XML";
				File f = new File(toSaveSheet);
				f.getParentFile().mkdirs(); 
				try {
					f.createNewFile();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				worksheet.save(f);
				
				File toOpen = new File(SHEET2);
				try {
					worksheet = WorkSheet.load(toOpen);
					whichSheet = 2;
					worksheetChange();
				} catch (Exception exception) {
					worksheet = new WorkSheet();
					
					worksheetChange();
				}
			}
			
		});
		
		sheet3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String toSaveSheet = SHEET + whichSheet + ".XML";
				File f = new File(toSaveSheet);
				f.getParentFile().mkdirs(); 
				try {
					f.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				worksheet.save(f);
				
				File toOpen = new File(SHEET3);
				try {
					worksheet = WorkSheet.load(toOpen);
					whichSheet = 3;
					worksheetChange();
				} catch (Exception exception) {
					worksheet = new WorkSheet();
					
					worksheetChange();
				}
			}
			
		});
		shiftButtons.add(sheet1);
		shiftButtons.add(sheet2);
		shiftButtons.add(sheet3);
		
		
		jframe.add(shiftButtons, BorderLayout.PAGE_END);
		
		jframe.setVisible(true);
		jframe.setPreferredSize(PREFEREDDIM);
		jframe.pack();
	}

	private void makeMenuItem(JMenu menu, String name, String command) {
		JMenuItem menuitem = new JMenuItem(name);
		menu.add(menuitem);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(command);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals(EXITCOMMAND)) {
			exit();
		} else if (ae.getActionCommand().equals(SAVECOMMAND)) {
			int res = filechooser.showSaveDialog(jframe);
			if (res == JFileChooser.APPROVE_OPTION) {
				worksheet.save(filechooser.getSelectedFile());
			}
		} else if (ae.getActionCommand().equals(OPENCOMMAND)) {
			int res = filechooser.showOpenDialog(jframe);
			if (res == JFileChooser.APPROVE_OPTION) {
				worksheet = WorkSheet.load(filechooser.getSelectedFile());
				worksheetChange();
			}
		} else if (ae.getActionCommand().equals(CLEARCOMMAND)) {
			worksheet = new WorkSheet();
			worksheetChange();
		} else if (ae.getActionCommand().equals(EDITFUNCTIONCOMMAND)) {
			functioneditor.setVisible(true);
		} else if (ae.getActionCommand().equals("CALCULATE")) {
			CellIndex index = worksheetview.getSelectedIndex();
			System.out.println(index.show());
			Cell current = worksheet.lookup(index);
			try {
				current.calcuate(worksheet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			for (int i=0; i<26; i++) {
				for (int j=0; j<20; j++) {
					CellIndex tempIndex = new CellIndex(i, j);
					Cell tempCell = worksheet.lookup(tempIndex);
					if (current != tempCell) {
					
						try {
							tempCell.othersCalcuate(worksheet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			update();
		}
		
	}
	
	
	
	private void worksheetChange() {
		worksheetview.setWorksheet(worksheet);
		functioneditor.setWorksheet(worksheet);
		worksheetview.repaint();
	}

	private void exit() {
		System.exit(0);
	}

	@Override
	public void update() {
		CellIndex index = worksheetview.getSelectedIndex();
		selectedCellLabel.setText(index.show());
		cellEditTextField.setText(worksheet.lookup(index).show());
		jframe.repaint();
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		textChanged();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		textChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		textChanged();
	}

	private void textChanged() {
		CellIndex index = worksheetview.getSelectedIndex();
		Cell current = worksheet.lookup(index);
		current.setText(cellEditTextField.getText());
		worksheetview.repaint();
	}
	
	/* Control + C to copy and Control + V to paste */
	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			CellIndex index = worksheetview.getSelectedIndex();
			Cell current = worksheet.lookup(index);
			ClipBoard.tempCell = current;
        }
		
		if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			CellIndex index = worksheetview.getSelectedIndex();
			Cell current = worksheet.lookup(index);
			
			if (ClipBoard.tempCell.getText() != "") {
				current.setText(ClipBoard.tempCell.getText());
				current.hasPreviousExpression = ClipBoard.tempCell.hasPreviousExpression;
				current.previousExpression = ClipBoard.tempCell.previousExpression;
				if (ClipBoard.tempCell.getCalculatedValue() != null) {
					current.setCalculatedValue(ClipBoard.tempCell.getCalculatedValue());
				}
				else {
					current.setCalculatedValue(null);
				}
				
			}
			System.out.println(current);
			System.out.println("deploy");
			update();
        }
		
		if (e.getKeyCode()==KeyEvent.VK_ENTER) {
			calculateButton.doClick();
		}
	}

	public void mouseClicked(MouseEvent event) {
		
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	
}
