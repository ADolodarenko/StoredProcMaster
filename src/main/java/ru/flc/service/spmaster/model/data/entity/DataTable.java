package ru.flc.service.spmaster.model.data.entity;

import java.util.List;

public class DataTable
{
	private List<DataElement> headers;
	private List<List<DataElement>> rows;

	public DataTable(List<DataElement> headers, List<List<DataElement>> rows)
	{
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
}
