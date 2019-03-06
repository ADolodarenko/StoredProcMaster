package ru.flc.service.spmaster.model.data.entity;

/**
 * This class represents one database user.
 * It might be used for storing info about the login that executing a stored procedure (so called "Occupant").
 */
public class User
{
	private int id;
	private String login;
	private String name;

	public User(int id, String login, String name)
	{
		this.id = id;
		this.login = login;
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public String getLogin()
	{
		return login;
	}

	public String getName()
	{
		return name;
	}
}
