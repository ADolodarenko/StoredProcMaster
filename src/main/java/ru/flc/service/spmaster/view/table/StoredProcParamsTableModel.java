package ru.flc.service.spmaster.view.table;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import ru.flc.service.spmaster.model.data.entity.StoredProcParameter;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StoredProcParamsTableModel extends AbstractTableModel
{
	private ResourceManager resourceManager;
	private List<StoredProcParameter> data;
	private Title[] titles;

	public StoredProcParamsTableModel(ResourceManager resourceManager,
									  String[] titleKeys, List<StoredProcParameter> data)
	{
		if (resourceManager == null)
			throw new IllegalArgumentException(Constants.EXCPT_RESOURCE_MANAGER_EMPTY);

		this.resourceManager = resourceManager;

		if (data != null)
			this.data = data;
		else
			this.data = new ArrayList<>();

		initColumnTitle(titleKeys);

		fireTableDataChanged();
	}

	private void initColumnTitle(String[] titleKeys)
	{
		if (titleKeys != null)
		{
			titles = new Title[titleKeys.length];

			for (int i = 0; i < titleKeys.length; i++)
				titles[i] = new Title(resourceManager, titleKeys[i]);
		}
	}

	@Override
	public int getRowCount()
	{
		return data.size();
	}

	@Override
	public int getColumnCount()
	{
		if (titles != null)
			return titles.length;
		else
			return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Object value = null;

		if (rowIndex < getRowCount())
		{
			StoredProcParameter row = data.get(rowIndex);

			if (row != null)
				switch (columnIndex)
				{
					case 0:
						value = row.getType();
						break;
					case 1:
						value = row.getName();
						break;
					case 2:
						value = row.isNullValue();
						break;
					case 3:
						value = row.getValue();
				}
		}

		return value;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		if (columnIndex < 2)
			return false;

		StoredProcParameter row = data.get(rowIndex);

		if (row != null && columnIndex == 3)
			return !row.isNullValue();

		return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		//JTable.editCellAt ensures that rowIndex and columnIndex pertain to appropriate ranges, so I don't need to do that.

		StoredProcParameter row = data.get(rowIndex);

		if (columnIndex == 2 && aValue != null &&
				aValue.getClass().getSimpleName().equals(AppConstants.CLASS_NAME_BOOLEAN))
			row.setNullValue((Boolean) aValue);
		else if (columnIndex == 3)
			try
			{
				row.setValue(aValue);
			}
			catch (IllegalArgumentException e)
			{}
	}

	@Override
	public String getColumnName(int column)
	{
		if (titles != null && titles.length > column)
			return titles[column].getText();
		else
			return "";
	}

	public void addRow(StoredProcParameter row)
	{
		int index = data.size();
		data.add(row);
		fireTableRowsInserted(index, index);
	}

	public void addAllRows(List<StoredProcParameter> rowList)
	{
		if (rowList != null && !rowList.isEmpty())
		{
			int firstIndex = data.size();
			int lastIndex = firstIndex + rowList.size() - 1;

			data.addAll(rowList);

			fireTableRowsInserted(firstIndex, lastIndex);
		}
	}

	public StoredProcParameter getRow(int rowIndex)
	{
		return data.get(rowIndex);
	}

	public void removeRow(StoredProcParameter row)
	{
		int index = data.indexOf(row);

		if (index >= 0)
		{
			data.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}

	public void clear()
	{
		int index = data.size() - 1;
		data.clear();

		if (index >= 0)
			fireTableRowsDeleted(0, index);
	}
}
