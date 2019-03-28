package ru.flc.service.spmaster.model.data.entity;

public class StoredProcParameter
{
	private String name;
	private Class<?> type;
	private Object value;

	public StoredProcParameter(String name, Class<?> type, Object value)
	{
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public Class<?> getType()
	{
		return type;
	}

	public Object getValue()
	{
		return value;
	}
}
