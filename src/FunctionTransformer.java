/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

/**
 * Translate the C-Like code to the JavaScript code
 */

/*
 * The syntax of the C-Like function:
 * function_name(array values()) {
 *   while (i < #values)
 * }
 * All the function names cannot be upper case
 * All the parameters are separated with ':'
 * The other parts are the same with C language
 */

import java.util.ArrayList;

public class FunctionTransformer {
	String function; // The original C-Like functions
	String functionName; // The name of the function
	
	WorkSheet worksheet; 
	
	int parameterStart;
	int parameterEnd;
	
	String parameterString;
	
	int numberOfParameters = 0;
	
	int firstBrace = 0;
	int lastBrace = 0;
	
	ArrayList<String> parameters = new ArrayList<String>();
	
	public FunctionTransformer(String aFunction, WorkSheet worksheet, String theParameters, String functionName) {
		this.function = aFunction;
		this.worksheet = worksheet;
		this.functionName = functionName;
		
		/* Count how many parameters */
		for (int i=0; i<theParameters.length(); i++) {
			if (theParameters.charAt(i) == ':') {
				numberOfParameters++;
			}
		}
		
		parameterString = theParameters;
		numberOfParameters++;
		
		parameterString = parameterString.replaceAll("\\s", "");
		parameterString = parameterString.replace("\n", "");
		
		/* Get all the parameters */
		int leftNumber = 0;
		int rightNumber = 0;
		for (int i=0; i<parameterString.length(); i++) {
			if (parameterString.charAt(i) == ':') {
				rightNumber = i;
				if (leftNumber == 0) {
					String oneParameter = parameterString.substring(leftNumber, rightNumber);
					parameters.add(oneParameter);
				}
				
				else {
					String oneParameter = parameterString.substring(leftNumber+1, rightNumber);
					parameters.add(oneParameter);
				}
				
				leftNumber = i;
			}
		}
		
		String lastParameter = parameterString.substring(leftNumber+1);
		parameters.add(lastParameter);
		
		/* Get the corresponding parameter values */ 
		for (int i=0; i<parameters.size(); i++) {
			int column = parameters.get(i).charAt(0) - 'A';
			int row = Integer.parseInt(parameters.get(i).substring(1)) -1;
			CellIndex tempIndex = new CellIndex(column, row);
			Cell tempCell = worksheet.lookup(tempIndex);
			parameters.set(i, tempCell.show());
		}
	}
	
	/* Transform the function to JavaScript function */
	public void transformToJavaScript() {
		
		function = function.replaceAll("int", "var");
		function = function.replaceAll("float", "var");
		function = function.replaceAll("double", "var");
		function = function.replaceAll("prvar", "print");
		
		function = function.replaceAll("#values", Integer.toString(parameters.size()));
		
		for (int i=0; i<function.length(); i++) {
			if (function.charAt(i) == '{') {
				firstBrace = i;
				break;
			}
		}
		
		function = function.substring(firstBrace+1);
		
		String values = "var values = [";
		
		for (int i=0; i<parameters.size() ;i++) {
			if (i != parameters.size() - 1) {
				values = values + parameters.get(i) + ", ";
			}
			else {
				values = values + parameters.get(i) + "];";
			}
		}
		
		function = values + function;
		function = "function " + functionName + "()" + "{" + function;
	}
}
