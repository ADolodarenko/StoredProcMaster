package ru.flc.service.spmaster.model.data.entity;

import ru.flc.service.spmaster.util.AppConstants;

import java.util.List;
import java.util.Objects;

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
	private User occupant;
	private List<StoredProcParameter> parameters;

	public StoredProc(int id, String name, String description,
					  StoredProcStatus status, User occupant)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.occupant = occupant;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StoredProc that = (StoredProc) o;
		return id == that.id &&
				name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name);
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

	public boolean isAvailable()
	{
		return status == StoredProcStatus.AVAILABLE;
	}

	public User getOccupant()
	{
		return occupant;
	}

	public List<StoredProcParameter> getParameters()
	{
		return parameters;
	}

	public void setParameters(List<StoredProcParameter> parameters)
	{
		this.parameters = parameters;
	}

	public void setStatus(StoredProcStatus status)
	{
		this.status = status;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setOccupant(User occupant)
	{
		this.occupant = occupant;
	}
}
