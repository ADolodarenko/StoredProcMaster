package ru.flc.service.spmaster.view.entity;

import ru.flc.service.spmaster.model.data.entity.DataTable;

public class DataPage
{
	private int index;
	private DataTable dataTable;

	public DataPage(int index, DataTable dataTable)
	{
		this.index = index;
		this.dataTable = dataTable;
	}

	public int getIndex()
	{
		return index;
	}

	public DataTable getDataTable()
	{
		return dataTable;
	}
}
