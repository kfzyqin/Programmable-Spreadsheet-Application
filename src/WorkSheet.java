/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

import java.io.File;
import java.util.HashMap;

/**
 * WorkSheet - this stores the information for the worksheet. This is made up of
 * all the cells. all the cells of the worksheet.
 * 
 * @author Eric McCreath
 * 
 */

public class WorkSheet {

	private static final String SPREAD_SHEET = "SpreadSheet";
	private static final String INDEXED_CELL = "IndexedCell";
	private static final String CELL = "Cell";
	private static final String CELL_INDEX = "CellIndex";
	private static final String FUNCTIONTEXT = "FunctionText";
	private static final String FUNCTIONS = "Functions";

	private HashMap<CellIndex, Cell> tabledata;
	
	private HashMap<String, CellIndex> StringToCellIndex;
	// For simplicity the table data is just stored as a hashmap from a cell's
	// index to the
	// cells data. Cells that are not yet part of this mapping are assumed
	// empty.
	// Once a cell is constructed the object is not replaced in this mapping,
	// rather
	// the data of the cell can be modified.

	private String functions;

	public WorkSheet() {
		StringToCellIndex = new HashMap<String, CellIndex>();
		tabledata = new HashMap<CellIndex, Cell>();
		functions = "";
	}

	public Cell lookup(CellIndex index) {
		Cell cell = tabledata.get(index);
		if (cell == null) { // if the cell is not there then create it and add
							// it to the mapping
			cell = new Cell();
			tabledata.put(index, cell);
		}
		return cell;
	}

	// calculate all the values for each cell that makes up the worksheet
	public void calculate() throws Exception {
		for (CellIndex i : tabledata.keySet()) {
			tabledata.get(i).calcuate(this);
		}
	}

	public void save(File file) {
		StoreFacade sf = new StoreFacade(file, SPREAD_SHEET);
		for (CellIndex i : tabledata.keySet()) {
			if (!tabledata.get(i).isEmpty()) {
				sf.start(INDEXED_CELL);
				sf.addText(CELL_INDEX, i.show());
				sf.addText(CELL, tabledata.get(i).getText());
			}
		}
		sf.start(FUNCTIONTEXT);
		sf.addText(FUNCTIONS, functions);
		sf.close();
	}

	public static WorkSheet load(File file) {
		LoadFacade lf = LoadFacade.load(file);
		WorkSheet worksheet = new WorkSheet();
		String name;
		while ((name = lf.nextElement()) != null) {
			if (name.equals(INDEXED_CELL)) {
				String index = lf.getText(CELL_INDEX);
				String text = lf.getText(CELL);
				worksheet.put(index, text);
			} else if (name.equals(FUNCTIONTEXT)) {
				worksheet.functions = lf.getText(FUNCTIONS);
			}
		}
		return worksheet;
	}

	public String getFuctions() {
		return functions;
	}

	public void setFunctions(String fun) {
		functions = fun;
	}

	private void put(String index, String text) {
		tabledata.put(new CellIndex(index), new Cell(text));
	}
	
	public HashMap<CellIndex, Cell> getTableData() {
		return this.tabledata;
	}
}
