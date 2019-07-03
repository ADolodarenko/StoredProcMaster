package ru.flc.service.spmaster.model.data.source;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreparedStatementExecutor
{
	private PreparedStatement statement;

	private boolean firstExecution;
	private boolean resultSet;
	private SQLException exception;

	public PreparedStatementExecutor(PreparedStatement statement)
	{
		this.statement = statement;
		this.firstExecution = true;
	}

	public void execute()
	{
		exception = null;

		try
		{
			if (firstExecution)
				resultSet = statement.execute();
			else
				resultSet = statement.getMoreResults();
		}
		catch (SQLException e)
		{
			exception = e;
		}

		firstExecution = false;
	}

	public boolean isResultSet()
	{
		return resultSet;
	}

	public SQLException getException()
	{
		return exception;
	}

	public ResultSet getResultSet() throws SQLException
	{
		return statement.getResultSet();
	}

	public int getUpdateCount() throws SQLException
	{
		return statement.getUpdateCount();
	}
}
