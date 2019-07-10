package ru.flc.service.spmaster.view.table.filter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableFilterListener implements DocumentListener
{
	private JTextField filterTextField;
	private TableRowSorter<TableModel> rowSorter;
	private TableModel tableModel;

	public TableFilterListener(JTextField filterTextField, TableRowSorter<TableModel> rowSorter)
	{
		this.filterTextField = filterTextField;
		this.rowSorter = rowSorter;
		this.tableModel = rowSorter.getModel();
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		if (tableModel.getRowCount() > 0)
		{
			String text = filterTextField.getText();

			if (text.trim().length() == 0)
				rowSorter.setRowFilter(null);
			else
				rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		if (tableModel.getRowCount() > 0)
		{
			String text = filterTextField.getText();

			if (text.trim().length() == 0)
				rowSorter.setRowFilter(null);
			else
				rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
