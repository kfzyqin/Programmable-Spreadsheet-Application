/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

/**
 * ParseToPostfixMachine - Parse a string expression to the polish pattern
 * @author Zhenyue Qin, u5505995
 */

import java.util.ArrayList;
import java.util.LinkedList;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ParseToPostfixMachine {
	/* The parameters for C-Like function */
	ArrayList<String> parameters = new ArrayList<String>();
	
	/* The parsing result without the tree */ 
	Double redemResult;
	
	final String originalExpression;
	String tmpString;
	
	char currentCharacter;
	final int length; // The length of the original expression
	
	boolean isParsed = false;
	
	Double result;
	
	
	LinkedList<String> tempPostfix = new LinkedList<String>();
	LinkedList<String> originalPostfix = new LinkedList<String>();
	LinkedList<String> postfix = new LinkedList<String>();

	LinkedList<Character> parseStack = new LinkedList<Character>();
	
	String currentNumber = "";
	
	boolean negativeSignInFrontBracket = false;
	
	public ParseToPostfixMachine(String toConvert, WorkSheet worksheet) throws ScriptException, NoSuchMethodException {
		/* 
		 * Change tmpString to avoid the error of index of for loop
		 * We cannot change the length of the original 
		 */
		tmpString = toConvert;
		
		int leftBracket = 100;
		int rightBracket = 0;
		
		int indexLeft = 100;
		int indexRight = 0;
		String cellIndex = "";
		
		String parameters ="";
		
		/* Find the parameters of the C-Like functions */ 
		for (int i=0; i<toConvert.length(); i++) {
			/* The function names have to be lower case */
			if (GeneralMethods.isLowerLetter(toConvert.charAt(i))) {
				String personalFunction = "";
				String functionName;
				int functionLeft = i;
				int functionRight;
				
				int lefParameter = 0;
				
				while (toConvert.charAt(i) != ')') {
					if (toConvert.charAt(i) == '(') {
						lefParameter = i;
					}
					i++;
					continue;
				}
				
				functionRight = i;
				
				functionName = toConvert.substring(functionLeft, lefParameter);
				personalFunction = toConvert.substring(functionLeft, functionRight+1);
				
				PersonalFunctions functionLibrary = new PersonalFunctions(worksheet.getFuctions());
				
				parameters = toConvert.substring(lefParameter+1, functionRight);

				FunctionTransformer transformer = new FunctionTransformer(functionLibrary.functions.get(functionName), worksheet, parameters, functionName);

				transformer.transformToJavaScript();
				
				/* Run the functions to get the value */
				ScriptEngineManager mgr = new ScriptEngineManager();
			    ScriptEngine engine = mgr.getEngineByName("JavaScript");
			    
			    try {
				    engine.eval(transformer.function);
				    Invocable inv = (Invocable) engine;
				    double returnValue = (double) inv.invokeFunction(functionName);
				    
				    tmpString = tmpString.replaceFirst(functionName + "\\("+parameters+"\\)", Double.toString(returnValue));
		
			    } catch (ScriptException e) {
			    	GeneralMethods.javaScriptSyntaxError(e);
			    	throw new RuntimeException();
			    	
			    }
			}
		}
		
		toConvert = toConvert.replaceAll("\\s", "");
		toConvert = tmpString;
		
		/* Get the value stored in the cell */
		for (int i=0; i<toConvert.length(); i++) {
			if (GeneralMethods.isLetter(toConvert.charAt(i))) {
				cellIndex = "";
				indexLeft = i;
				i++;
				while(i < toConvert.length() && GeneralMethods.isNumeric(toConvert.charAt(i))) {
					cellIndex += toConvert.charAt(i);
					i++;
					if (i >= toConvert.length()) {
						break;
					}
				}
				
				indexRight = i - 1;
				
				int column = toConvert.charAt(indexLeft) - 'A';
				int row = Integer.parseInt(toConvert.substring(indexLeft+1, indexRight+1)) - 1;
				
				CellIndex tempIndex = new CellIndex(column, row);
				
				Cell tempCell = worksheet.lookup(tempIndex);
				
				String toReplace = toConvert.substring(indexLeft, indexRight+1);
				
				tmpString = tmpString.replaceAll(toReplace, tempCell.show());		
			}
		}
		
		toConvert = tmpString;
		toConvert = toConvert.replaceAll("\\s", "");
		
		if (toConvert.charAt(0) == '-') {
			negativeSignInFrontBracket = true;
			System.out.println("Cat: " + toConvert);
		}
		
		/* Replace all the negative numbers with '0 -' that number */
		/* Weakness: The negative numbers have to have parenthesis */
		if (toConvert.length() > 0) {
			
			if (toConvert.charAt(0) == '-') {
				String toReplace = "" + toConvert.charAt(0);
				String substitute = "0-";
				toConvert = toConvert.replaceFirst(toReplace, substitute);
				negativeSignInFrontBracket = true;
			}
		}
		
		for (int i=0; i<toConvert.length(); i++) {
			if (toConvert.charAt(i) == '(') {
				leftBracket = i;
			}
			
			if (toConvert.charAt(i) == ')') {
				rightBracket = i;
				
				if ((rightBracket - leftBracket) == 3) {
					String toReplace = toConvert.substring(leftBracket+1, rightBracket);
					String substitute = "(0-" + toReplace.charAt(1) + ")";
					toConvert = toConvert.replaceFirst(toReplace, substitute);
				}
			}
		}
		
		originalExpression = toConvert.replaceAll("\\s", "");
		length = originalExpression.length();
	}
	
	public boolean isOperator(char theSign) {
		if (theSign == '+' || theSign == '-' || theSign == '*' || theSign == '/' || theSign == '^') {
			return true;
		}
		else {
			return false;
		}
	}
	
	/* 
	 * Rules: 
	 * Exponential has the highest precedence
	 * Then multiplication and division
	 * Then addition and subtraction
	 * The operations in the same level, the one appearing earlier has the higher precedence
	 */
	public boolean comparePrecedence(char first, char second) {
		int value1 = 0;
		int value2 = 0;
		if (first == '+' || first == '-') {
			value1 = 1;
		}
		
		if (first == '*' || first == '/') {
			value1 = 2;
		}
		
		if (first == '^') {
			value1 = 3;
		}
		
		if (second == '+' || second == '-') {
			value2 = 1;
		}
		
		if (second == '*' || second == '/') {
			value2 = 2;
		}
		
		if (second == '^') {
			value2 = 3;
		}
		
		return (value1 >= value2);
		
	}

	/* Parse the ready expression */
	public void parse() {
		for (int i=0; i<length; i++) {
			currentCharacter = originalExpression.charAt(i);
			
			currentNumber = "";
			
			/* Handle the decimal numbers */
			while ((currentCharacter >= '0' && currentCharacter <= '9') || currentCharacter == '.' || (currentCharacter == '-' && i == 0) || 
					(currentCharacter == '-' && GeneralMethods.isOperator(originalExpression.charAt(i-1))) ) {
				currentNumber += currentCharacter;
				i++;
				if (i < length) {
					currentCharacter = originalExpression.charAt(i);
				}
				
				else {
					currentCharacter = 'E';
					if (currentNumber != "") {
						originalPostfix.add(currentNumber);
					}
					break;
				}
			}
			
			if (currentCharacter == 'E') {
				break;
			}
			
			if (!originalPostfix.equals("")) {
				originalPostfix.add(currentNumber);
			}
			
			if (isOperator(currentCharacter)) {
				while (!parseStack.isEmpty() && comparePrecedence(parseStack.getFirst(), currentCharacter) && parseStack.getFirst() != '(') {
					originalPostfix.add(Character.toString(parseStack.pop()));
				}
				parseStack.push(currentCharacter);
			}
			
			else if(currentCharacter == '(') {
				if (i > 0) {
					/* Is a negative number */
					if (GeneralMethods.isNumeric(originalExpression.charAt(i-1)) || originalExpression.charAt(i-1) == ')') {
						parseStack.push('*');
					}
				}
				parseStack.push(currentCharacter);
			}
			
			else if(currentCharacter == ')') {
				while (!parseStack.isEmpty() && parseStack.getFirst() != '(') {
					originalPostfix.add(Character.toString(parseStack.pop()));
				}
				parseStack.pop();
			}
			
		}
		
		/* When finished, pop everything */
		while (!parseStack.isEmpty()) {
			originalPostfix.add(Character.toString(parseStack.pop()));
		}
		
		isParsed = true;
	}
	
	/* Parse to a tree then calculate */
	public double calculate() throws Exception {
		PostfixToTree archer = new PostfixToTree(this.tempPostfix);
		Double rtn;
		if (!isParsed) {
			throw new Exception("You have not parsed yet");
		}
		
		else {
			this.tempPostfix = originalPostfix;
			archer = new PostfixToTree(this.tempPostfix);
			archer.insert();
			rtn = archer.finalTree.calculate();
			result = rtn;
		}
		result = Math.round(result*100.0) / 100.0;
		return Math.round(result*100.0) / 100.0;
	}

	
	public static void main(String[] args) throws Exception {
		ParseToPostfixMachine machine = new ParseToPostfixMachine("3(4 + 6)", null);
		machine.parse();
		machine.calculate();
		System.out.println(machine.result);
	}
}