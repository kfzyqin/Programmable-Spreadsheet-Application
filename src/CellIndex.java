/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

/**
 * CellIndex - 
 * 
 * @author Eric McCreath
 */

public class CellIndex {
   int row, column; // these values stored in row and column start at 0.  Although when
   // they are displayed the column starts with 'A' and the row starts with '1'. 
   // e.g. cell index A1 will be row 0 and column 0.
   
   public CellIndex(int column, int row) {
	   this.row = row;
	   this.column = column;
   }
   
   
   public CellIndex(String index) {
	char letter = index.charAt(0);
	column = letter - 'A';
	row  = Integer.parseInt(index.substring(1)) -1;
   }

@Override
	public boolean equals(Object obj) {
	   CellIndex o = (CellIndex) obj;
		return row == o.row && column == o.column;
	}
   
   @Override
	public int hashCode() {
		return show().hashCode(); 
	}
   
   public String show() {
	   return ((char) ('A' + (char) column)) + ""  + (row+1);
   }
   
}
