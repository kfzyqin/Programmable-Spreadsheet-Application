/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

/** 
 * The tree recursive data structure to store the parsed result
 * @author Zhenyue Qin
 * 
 */

/* 
 * The tree structure
 * 		+
 * 	   / \
 *    1	  *
 *    	 / \
 *      4   5
 */

public class ExpressionTree {
	
	/* Define the tree structure */
	public char operator; // the node
	public boolean isOperator;
	public ExpressionTree left; // the left tree
	public ExpressionTree right; // the right tree
	
	private Double number;
	
	public char getOperator() {
		return this.operator;
	}
	
	public boolean getIsOperator() {
		return this.isOperator;
	}
	
	public ExpressionTree(boolean aIsOperator) {
		this.isOperator = aIsOperator;
	}
	
	public void setNumber(double aNumber) {
		this.number = aNumber;
	}
	
	public void setOperator(String aOperator) {
		this.operator = aOperator.charAt(0);
	}
	
	public void setLeft(ExpressionTree aLeft) {
		this.left = aLeft;
	}
	
	public void setRight(ExpressionTree aRight) {
		this.right = aRight;
	}
	
	/* 
	 * Calculate the expression tree 
	 * Inverse the polish algorithm
	 */
	public double calculate() {
		if (!this.isOperator) {
			return this.number;
		}
		
		else {
			if (this.operator == '+') {
				return this.left.calculate() + this.right.calculate();
			}
			
			else if (this.operator == '-') {
				return this.left.calculate() - this.right.calculate();
			}
			
			else if (this.operator == '*') {
				return this.left.calculate() * this.right.calculate();
			}
			
			else if (this.operator == '/'){
				return this.left.calculate() / this.right.calculate();
			}
			
			else {
				return Math.pow(this.left.calculate(), this.right.calculate());
			}
		}
	}
	
	@Override
	public String toString() {
		if (!this.isOperator) {
			return " " + this.number + " ";
		}
		
		else {
			return left.toString() + "\n " + this.operator + "\n " + right.toString();
		}
		
	}
	
}
