/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

/**
 * Cell - an object of this class holds the data of a single cell. 
 * 
 * @author Eric McCreath
 * Zhenyue Qin modified
 */

public class Cell {

	private String text; // this is the text the person typed into the cell
	private Double calculatedValue; // this is the current calculated value for
									// the cell
	public String previousExpression; // this is the formula typed
	public boolean hasPreviousExpression = false; // this is whether the formula is stored
	
	private ParseToPostfixMachine toPostfix;

	public Cell(String text) {
		this.text = text;
		calculatedValue = null;
	}

	public Cell() {
		text = "";
		calculatedValue = null;
	}

	public Double value() {
		return calculatedValue;
	}

	/* Calcute the formula typed */
	public void calcuate(WorkSheet worksheet) throws Exception {
		try {
			if (calculatedValue != null) {
				System.out.println("text: " + text);
			}
			/* Test whether the value is a number or expression */ 
			calculatedValue = Double.parseDouble(text);
			text = Double.toString(calculatedValue);
			previousExpression = "";
			hasPreviousExpression = false;
			if (hasPreviousExpression) {
				reCalcuate(worksheet);
			}
		} catch (NumberFormatException nfe) {
			/* The formula is typed to avoid the exception*/
			if (!text.equals("")) {
				/* It is an expression */
				if (text.charAt(0) == '=') {
					System.out.println("haha: " + calculatedValue);
					previousExpression = text; // store the previous expression
					/* Parse the expression */
					toPostfix = new ParseToPostfixMachine(text.substring(1), worksheet);
					toPostfix.parse();
					toPostfix.calculate();
					calculatedValue = toPostfix.result;
					hasPreviousExpression = true;
				}
				
				
				
			}
		}
	}
	
	public void othersCalcuate(WorkSheet worksheet) throws Exception {
		try {
			if (calculatedValue != null) {
				System.out.println("text: " + text);
			}
			/* Test whether the value is a number or expression */ 
			calculatedValue = Double.parseDouble(text);
			text = Double.toString(calculatedValue);
			//previousExpression = "";
			//hasPreviousExpression = false;
			if (hasPreviousExpression) {
				reCalcuate(worksheet);
			}
		} catch (NumberFormatException nfe) {
			/* The formula is typed to avoid the exception*/
			if (!text.equals("")) {
				/* It is an expression */
				if (text.charAt(0) == '=') {
					System.out.println("haha: " + calculatedValue);
					previousExpression = text; // store the previous expression
					/* Parse the expression */
					toPostfix = new ParseToPostfixMachine(text.substring(1), worksheet);
					toPostfix.parse();
					toPostfix.calculate();
					calculatedValue = toPostfix.result;
					hasPreviousExpression = true;
				}
				
				
				
			}
		}
	}
	
	public void reCalcuate(WorkSheet worksheet) throws Exception {
		toPostfix = new ParseToPostfixMachine(previousExpression.substring(1), worksheet);
		toPostfix.parse();
		toPostfix.calculate();
		calculatedValue = toPostfix.result;
		System.out.println("here: " + calculatedValue);
	}

	public String show() { // this is what is viewed in each Cell
		return calculatedValue == null ? text : calculatedValue.toString();
	}

	public void setCalculatedValue(Double aValue) {
		this.calculatedValue = aValue;
	}
	
	public Double getCalculatedValue() {
		return calculatedValue;
	}
	
	@Override
	public String toString() {
		return text + "," + calculatedValue;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public boolean isEmpty() {
		return text.equals("");
	}
	
}
