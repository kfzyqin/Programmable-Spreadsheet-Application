/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * This class is for generating dialogues
 * @author Zhenyue Qin, u5505995
 *
 */

//https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html

public class DialogueFrame implements Runnable, ActionListener{
	private static final Object EXITCOMMAND = null;
	public static JFrame jframe;
	
	public static void main(String[] args) {
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals(EXITCOMMAND)) {
			System.exit(0);
		}
		
	}

	@Override
	public void run() {

	}

}