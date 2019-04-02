package ru.flc.service.spmaster.model.data.entity;

public class StoredProcParameter
{
	private StoredProcParamType type;
	private boolean nullable;
	private String name;
	private Class<?> valueClass;
	private Object value;

	public StoredProcParameter(StoredProcParamType type, boolean nullable,
							   String name, Class<?> valueClass, Object value)
	{
		this.type = type;
		this.nullable = nullable;
		this.name = name;
		this.valueClass = valueClass;
		this.value = value;
	}

	public StoredProcParamType getType()
	{
		return type;
	}

	public boolean isNullable()
	{
		return nullable;
	}

	public String getName()
	{
		return name;
	}

	public Class<?> getValueClass()
	{
		return valueClass;
	}

	public Object getValue()
	{
		return value;
	}
}
