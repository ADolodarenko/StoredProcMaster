package ru.flc.service.spmaster.view.table;

import ru.flc.service.spmaster.model.data.entity.DataElement;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StoredProcResultTableModel extends AbstractTableModel
{
	private List<DataElement> headers;
	private List<List<DataElement>> rows;

	public StoredProcResultTableModel(DataTable data)
	{
		if (data == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_SP_RESULT_TABLE_EMPTY);

		this.headers = data.getHeaders();
		this.rows = data.getRows();
	}

	@Override
	public int getRowCount()
	{
		if (rows != null)
			return rows.size();
		else
			return 0;
	}

	@Override
	public int getColumnCount()
	{
		if (headers != null)
			return headers.size();
		else
			return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if (headers != null && headers.size() > columnIndex
			&& rows != null && rows.size() > rowIndex)
			return rows.get(rowIndex).get(columnIndex).getValue();
		else
			return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	@Override
	public String getColumnName(int column)
	{
		if (headers != null && headers.size() > column)
			return headers.get(column).getValue().toString();
		else
			return null;
	}
}
