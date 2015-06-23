/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

/**
 * The general functions used
 * @author Zhenyue Qin, u5505995
 */

import javax.script.ScriptException;
import javax.swing.JOptionPane;

public class GeneralMethods {
	
	public static boolean isOperator(String theSign) {
		if (theSign.equals("+") || theSign.equals("-") || theSign.equals("*") || theSign.equals("/") || theSign.equals("^")) {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	public static boolean isOperator(char theSign) {
		if (theSign == '+' || theSign == '-' || theSign == '*' || theSign == '/' || theSign == '^') {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	public static boolean isLetter(char theChar) {
		if (theChar >= 'A' && theChar <= 'Z') {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	//http://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-a-numeric-type-in-java
	public static boolean isNumeric(String str) {  
		try {  
			double d = Double.parseDouble(str);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}
		
		return true;  
	}
	
	public static boolean isNumeric(char aChar) {  
		if (aChar >= '0' && aChar <= '9') {
		
			return true;  
		}
		
		else {
			return false;
		}
	}
	
	public static boolean isLowerLetter(char aChar) {
		if (aChar >= 'a' && aChar <= 'z') {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	public static void parseErrorDialogue(String unexpected) {
		JOptionPane.showMessageDialog(DialogueFrame.jframe,
			    "UNEXPECTED: " + unexpected,
			    "PARSE ERROR",
			    JOptionPane.ERROR_MESSAGE);
	}

	public static void parseErrorDialogueMorethings() {
		JOptionPane.showMessageDialog(DialogueFrame.jframe,
			    "YOU HAVE SOMETHING MORE IN THE END!",
			    "PARSE ERROR",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	public static void javaScriptSyntaxError(ScriptException e) {
		JOptionPane.showMessageDialog(DialogueFrame.jframe,
			    "" + e,
			    "SYNTAX ERROR",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	public static void javaScriptInputValue() {
		JOptionPane.showInputDialog(DialogueFrame.jframe, 
		        "Enter the value to continue", 
		        "INPUT THE VALUE", 
		        JOptionPane.WARNING_MESSAGE);
	}
}
