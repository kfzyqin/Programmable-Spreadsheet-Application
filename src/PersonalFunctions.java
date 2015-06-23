/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

/**
 * Split the functions according to '@' and store functions in the map
 * @author Zhenyue Qin, u5505995
 */

import java.util.ArrayList;
import java.util.HashMap;


public class PersonalFunctions {
	static HashMap<String, String> functions = new HashMap<String, String>();
	static ArrayList<String> functionContents = new ArrayList<String>();
	
	PersonalFunctions(String allFunctions) {
		/* Function contents */
		String tempFunction = "";
		int lastAtSign = 0;
		for (int i=0; i<allFunctions.length(); i++) {
			if (allFunctions.charAt(i) == '@') {
				if (lastAtSign == 0) {
					functionContents.add(allFunctions.substring(lastAtSign, i));
					lastAtSign = i;
				}
				
				else {
					functionContents.add(allFunctions.substring(lastAtSign+1, i));
					lastAtSign = i;
				}
			}
		}
		
		/* Function names */
		for (int i=0; i<functionContents.size(); i++) {
			String aFunction = functionContents.get(i);
			String functionName = "";
			
			for (int j=0; j<aFunction.length(); j++) {
				if (aFunction.charAt(j) == '(') {
					functionName = aFunction.substring(0, j);
					break;
				}
			}
			
			functions.put(functionName, aFunction);
		}
	}
	
	public static void main(String[] args) {
		String sumandmaxfunctions = "sum(array values) {" +
                "  double sum;" +
                "  int i;" +
                "  sum = 0.0;" +
                "  i = 0;" +
                "  while (i < #values) {" +
                "     sum = sum + values[i];" +
                "	i++;" +
                "  }" +
                "  return sum;" +
                "}" + '@' + 

                "MAX(array values) {" +
                "  double max; " +
                "  int i;" +
                "  max = values[0];" +
                "  i = 1;" +
                "  while (i < #values) {" +
                "     if (values[i] > max) {" +
                "        max = values[i];" +
                "     }" +
                "  }" +
                "  return max;" +
                "}" + '@';
		
		PersonalFunctions test = new PersonalFunctions(sumandmaxfunctions);
		System.out.println(test.functions);
	}
	
}
