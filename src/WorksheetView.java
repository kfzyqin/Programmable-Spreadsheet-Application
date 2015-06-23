/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class WorksheetView extends JTable implements TableModel {

	private static final int NUMROWS = 20;
	private static final int NUMCOL = 26;
	private static final int FIRSTCOLUMNWIDTH = 20;
	private static final int COLUMNWIDTH = 50;

	/** 
	 * WorksheetView - This is the GUI view of a worksheet.  It builds on and modifies the JTable
	 * Eric McCreath 2015
	 */
	private static final long serialVersionUID = 1L;

	WorkSheet worksheet;

	ArrayList<TableModelListener> listeners;
	ArrayList<SelectionObserver> observers;

	public WorksheetView(WorkSheet worksheet) {
		this.worksheet = worksheet;
		listeners = new ArrayList<TableModelListener>();
		observers = new ArrayList<SelectionObserver>();
		this.setModel(this);
		this.setCellSelectionEnabled(true);
		this.setRowSelectionAllowed(false);
		this.setColumnSelectionAllowed(false);
		this.getColumnModel().getSelectionModel()
				.addListSelectionListener(this);
		(this.getSelectionModel()).addListSelectionListener(this);

		this.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int col = 0; col < NUMCOL + 1; col++) {
			this.getColumnModel().getColumn(col)
					.setPreferredWidth(col == 0 ? FIRSTCOLUMNWIDTH : COLUMNWIDTH);
		}
	}

	
	// getCellRenderer - provides the renderers for the cells.  Note the first column is just the index.
	public TableCellRenderer getCellRenderer(int row, int column) {
		if ((column == 0)) {
			return new TableCellRenderer() {

				@Override
				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					JLabel lab = new JLabel("" + (row + 1));
					lab.setOpaque(true);
					lab.setBackground(Color.gray);
					return lab;

				}

			};
		}
		return new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel lab = new JLabel("" + ((Cell) value).show());
				if (isSelected
						|| hasFocus
						|| (table.getSelectedColumn() == column && table
								.getSelectedRow() == row)) {
					lab.setOpaque(true);
					lab.setBackground(Color.lightGray);
				}
				return lab;

			}

		};
	}

	@Override
	public int getColumnCount() {
		return NUMCOL + 1;
	}

	@Override
	public int getRowCount() {
		return NUMROWS;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return worksheet.lookup(new CellIndex(columnIndex - 1, rowIndex));
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0)
			return "";
		return (char) ('A' + (char) (columnIndex - 1)) + "";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		informObservers();
	}

	public void addSelectionObserver(SelectionObserver o) {
		observers.add(o);
	}

	private void informObservers() {
		for (SelectionObserver o : observers)
			o.update();
	}

	public CellIndex getSelectedIndex() {
		return new CellIndex(this.getSelectedColumn() - 1,
				this.getSelectedRow());
	}

	public void setWorksheet(WorkSheet worksheet) {
		this.worksheet = worksheet;
		this.repaint();
	}

}
