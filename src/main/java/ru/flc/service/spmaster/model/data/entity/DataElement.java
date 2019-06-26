package ru.flc.service.spmaster.model.data.entity;

public class DataElement
{
	private Object value;
	private Class<?> type;

	public DataElement(Object value, Class<?> type)
	{
		this.value = value;
		this.type = type;
	}

	public Object getValue()
	{
		return value;
	}

	public Class<?> getType()
	{
		return type;
	}
}
