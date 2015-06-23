/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

/**
 * Parse a postfix to a tree
 * @author Zhenyue Qin, u5505995
 * 
 */

/*
 * Basic ideas: 
 * Push the numbers to the stack and when come across operators, 
 * pop the top two numbers and the second (do) the first
 */

import java.util.LinkedList;

public class PostfixToTree {
	LinkedList<String> tempPostfix = new LinkedList<String>();
	LinkedList<String> postfix = new LinkedList<String>();
	
	LinkedList<ExpressionTree> tree = new LinkedList<ExpressionTree>();
	
	ExpressionTree finalTree;
	
	public PostfixToTree(LinkedList<String> aPostfix) {
		this.tempPostfix = aPostfix;
		
		for (int i=0; i<tempPostfix.size(); i++) {
			if (GeneralMethods.isOperator(tempPostfix.get(i)) || GeneralMethods.isNumeric(tempPostfix.get(i))) {
				this.postfix.add(tempPostfix.get(i));
			}
		}
	}
	
	public void insert() {
		System.out.println("post: " + postfix);
		while (!postfix.isEmpty()) {
			
			if (!GeneralMethods.isOperator(postfix.getFirst())) {
				if (GeneralMethods.isNumeric(postfix.getFirst())) {
					ExpressionTree tmpNumber = new ExpressionTree(false);
					tmpNumber.setNumber(Double.parseDouble(postfix.pop()));
					tree.push(tmpNumber);
				}
				
			}
			
			else {
				try {
					ExpressionTree right = tree.pop();
					ExpressionTree left = tree.pop();
					
					ExpressionTree node = new ExpressionTree(true);
					node.setOperator(postfix.pop());
					node.setLeft(left);
					node.setRight(right);
					
					tree.push(node);
				} catch (Exception e) {
					GeneralMethods.parseErrorDialogue(postfix.pop());
					throw new RuntimeException("Here");
				}
			}
		}
		finalTree = tree.getFirst();
	}
}
