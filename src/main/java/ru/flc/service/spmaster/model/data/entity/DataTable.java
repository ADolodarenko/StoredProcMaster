package ru.flc.service.spmaster.model.data.entity;

import java.util.List;

public class DataTable
{
	private String name;
	private List<DataElement> headers;
	private List<List<DataElement>> rows;

	public DataTable(String name, List<DataElement> headers, List<List<DataElement>> rows)
	{
		this.name = name;
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

	public String getName()
	{
		return name;
	}
}
