package ru.flc.service.spmaster.model.data.entity;

/**
 * This class represents one stored procedure.
 */
public class StoredProc
{
	private int id;
	private String name;
	private String description;

	public StoredProc(int id, String name, String description)
	{
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}
}
