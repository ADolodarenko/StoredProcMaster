package ru.flc.service.spmaster.model.data.entity;

public class DataPage
{
	private int index;
	private String name;
	private DataTable dataTable;

	public DataPage(int index, String name, DataTable dataTable)
	{
		this.index = index;
		this.name = name;
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
