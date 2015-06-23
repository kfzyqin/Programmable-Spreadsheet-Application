/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FunctionEditor extends JDialog implements ActionListener {
/**
 * FunctionEditor - a simple modal dialog to edit the text of the functions. 
 * 
 * @author Eric McCreath
 * 
 */
	
	
	WorkSheet worksheet;
	JTextArea textarea;

	public FunctionEditor(WorkSheet worksheet) {
		this.worksheet = worksheet;
		textarea = new JTextArea(20, 50);
		JButton closebutton = new JButton("Close");
		closebutton.addActionListener(this);
		this.setModal(true);
		this.getContentPane().add(new JScrollPane(textarea), BorderLayout.CENTER);
		this.getContentPane().add(closebutton, BorderLayout.PAGE_END);
        this.pack();
	}

	public void setWorksheet(WorkSheet ws) {
		worksheet = ws;
		textarea.setText(worksheet.getFuctions());
	}
	public void actionPerformed(ActionEvent ae) { // it is always the close button
		updateWorksheet();
		dispose();
	}
	public void updateWorksheet() {
		worksheet.setFunctions(textarea.getText());
	}
}
