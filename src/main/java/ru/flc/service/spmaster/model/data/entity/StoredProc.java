package ru.flc.service.spmaster.model.data.entity;

import ru.flc.service.spmaster.util.AppConstants;

/**
 * This class represents one stored procedure.
 */
public class StoredProc
{
	public static String[] getTitleKeys()
	{
		return new String[] { AppConstants.KEY_COLUMN_SP_STATUS,
							AppConstants.KEY_COLUMN_SP_DESCRIPT,
							AppConstants.KEY_COLUMN_SP_NAME};
	}

	private int id;
	private String name;
	private String description;
	private StoredProcStatus status;

	public StoredProc(int id, String name, String description, StoredProcStatus status)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
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

	public StoredProcStatus getStatus()
	{
		return status;
	}

	public void setStatus(StoredProcStatus status)
	{
		this.status = status;
	}

	public boolean isAvailable()
	{
		return status == StoredProcStatus.AVAILABLE;
	}
}
