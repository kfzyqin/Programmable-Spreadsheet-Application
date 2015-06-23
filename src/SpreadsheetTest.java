/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import javax.script.ScriptException;
import javax.swing.SwingUtilities;

import org.junit.Test;

/**
 * 
 * SpreadsheetTest - This is a simple integration test.  
 * We basically set some text within cells of the spread sheet and check they evaluate correctly.
 * 
 * @author Eric McCreath, Zhenyue Qin, u5505995
 * 
 */

public class SpreadsheetTest  {

	protected static final String sumandmaxfunctions = "sum(array values) {" +
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
										
	                                                   "max(array values) {\n" +
	                                                   "  double max; " +
	                                                   "  int i;" +
	                                                   "  max = values[0];" +
	                                                   "  i = 1;" +
	                                                   "  while (i < #values) {" +
	                                                   "     if (values[i] > max) {" +
	                                                   "        max = values[i];" +
	                                                   "		i++;" + 
	                                                   "     }" +
	                                                   "  }" +
	                                                   "  return max;" +
	                                                   "}" + '@';
	                                                   
	Spreadsheet gui;

	@Test
	public void testSimple() {
		gui = new Spreadsheet();
		try {
			
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					selectAndSet(1, 3, "Some Text");
					selectAndSet(4, 1, "5.12");
					gui.calculateButton.doClick();
				}
			});
			assertEquals(gui.worksheet.lookup(new CellIndex("C2")).show(),
					"Some Text");
			assertEquals(gui.worksheet.lookup(new CellIndex("A5")).show(),
					"5.12");
		} catch (InvocationTargetException e) {
			fail();
		} catch (InterruptedException e) {
			fail();
		}
	}

	@Test
	public void testExpressionCal() {
		gui = new Spreadsheet();
		try {
			
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					selectAndSet(2, 3, "Some Text");
					selectAndSet(3, 3, "23.4");
					selectAndSet(4, 3, "34.1");
					selectAndSet(5, 3, "=2.6+C4*C5");
					gui.calculateButton.doClick();
				}
			});
			assertEquals(gui.worksheet.lookup(new CellIndex("C3")).show(),
					"Some Text");
			assertEquals(gui.worksheet.lookup(new CellIndex("C4")).show(),
					"23.4");
			assertEquals(gui.worksheet.lookup(new CellIndex("C5")).show(),
					"34.1");
			assertEquals(gui.worksheet.lookup(new CellIndex("C6")).show(),
					"800.54");
			
		} catch (InvocationTargetException e) {
			fail();
		} catch (InterruptedException e) {
			fail();
		}
	}
	

	@Test
	public void testFunctionCal() {
		gui = new Spreadsheet();
		try {
			
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					selectAndSet(2, 3, "1.1");
					selectAndSet(3, 3, "2.2");
					selectAndSet(4, 3, "3.3");
					selectAndSet(5, 3, "=sum(C3:C5)");
					selectAndSet(6, 3, "=max(C3:C5)");
					gui.functioneditor.textarea.setText(sumandmaxfunctions);
					gui.functioneditor.updateWorksheet();
					gui.calculateButton.doClick();
				}
			});
			assertEquals(gui.worksheet.lookup(new CellIndex("C3")).show(),
					"1.1");
			assertEquals(gui.worksheet.lookup(new CellIndex("C4")).show(),
					"2.2");
			assertEquals(gui.worksheet.lookup(new CellIndex("C5")).show(),
					"3.3");
			assertEquals(gui.worksheet.lookup(new CellIndex("C6")).show(),
					"4.4");
			assertEquals(gui.worksheet.lookup(new CellIndex("C7")).show(),
					"3.3");
		} catch (InvocationTargetException e) {
			fail();
		} catch (InterruptedException e) {
			fail();
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testMathematicalGeneralExpressions() {
		gui = new Spreadsheet();
		try {
			
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					selectAndSet(2, 3, "= 2(4 + 5)");
					selectAndSet(3, 3, "= 2 + -1");
					selectAndSet(4, 3, "= -1 * 4");
					gui.calculateButton.doClick();
				}
			});
			assertEquals(gui.worksheet.lookup(new CellIndex("C3")).show(),
					"18.0");
			assertEquals(gui.worksheet.lookup(new CellIndex("C4")).show(),
					"1.0");
			assertEquals(gui.worksheet.lookup(new CellIndex("C5")).show(),
					"-4.0");
			
		} catch (InvocationTargetException e) {
			fail();
		} catch (InterruptedException e) {
			fail();
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void whiteTestParse() throws Exception {
		gui = new Spreadsheet();
		System.out.println("start");
		ParseToPostfixMachine machine = new ParseToPostfixMachine(" -(4 + 5 * 2)", gui.worksheet);
		machine.parse();
		machine.calculate();
		assertTrue(machine.result == -14);
	}

	private void selectAndSet(int r, int c, String text) {
		gui.worksheetview.addRowSelectionInterval(r, r);
		gui.worksheetview.addColumnSelectionInterval(c, c);
		gui.cellEditTextField.setText(text);
	}
}
