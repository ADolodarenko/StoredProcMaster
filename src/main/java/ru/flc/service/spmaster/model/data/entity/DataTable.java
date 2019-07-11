package ru.flc.service.spmaster.model.data.entity;

import java.util.List;

public class DataTable
{
	private DataTableType type;
	private List<DataElement> headers;
	private List<List<DataElement>> rows;

	public DataTable(DataTableType type, List<DataElement> headers, List<List<DataElement>> rows)
	{
		this.type = type;
		this.headers = headers;
		this.rows = rows;
	}

	public List<DataElement> getHeaders()
	{
		return headers;
	}

	public List<List<DataElement>> getRows()
	{
		return rows;
	}

	public DataTableType getType()
	{
		return type;
	}
}
