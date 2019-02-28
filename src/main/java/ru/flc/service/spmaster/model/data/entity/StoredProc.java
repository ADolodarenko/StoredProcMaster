package ru.flc.service.spmaster.model.data.entity;

import ru.flc.service.spmaster.util.AppConstants;

/**
 * This class represents one stored procedure.
 */
public class StoredProc
{
	public static String[] getTitleKeys()
	{
		return new String[] { AppConstants.KEY_COLUMN_SP_ID,
							AppConstants.KEY_COLUMN_SP_DESCRIPT,
							AppConstants.KEY_COLUMN_SP_NAME};
	}

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
