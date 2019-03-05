package ru.flc.service.spmaster.model.data.source;

import com.sybase.jdbcx.SybDriver;
import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.Settings;
import org.dav.service.settings.type.Password;
import org.dav.service.util.Constants;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcStatus;
import ru.flc.service.spmaster.util.AppConstants;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AseDataSource implements DataSource
{
	public static AseDataSource getInstance()
	{
		return SingletonHelper.INSTANCE;
	}

	private static String buildDatabaseUrl(DatabaseSettings settings)
	{
		StringBuilder builder = new StringBuilder(settings.getConnectionPrefix());
		builder.append(':');
		builder.append(settings.getHost());
		builder.append(':');
		builder.append(settings.getPort());
		builder.append('/');
		builder.append(settings.getCatalog());

		/*
		ClientHostName=computerName;
		ClientHostProc=localProcessName;
		ApplicationName=myAppName;
		*/

		return builder.toString();
	}

	private static void transferResultToProcList(ResultSet resultSet, List<StoredProc> storedProcList) throws SQLException
	{
		if (resultSet != null && storedProcList != null)
			while (resultSet.next())
			{
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String description = resultSet.getString(3);

				storedProcList.add(new StoredProc(id, name, description, StoredProcStatus.AVAILABLE));
			}
	}

	private static void transferResultToStringList(ResultSet resultSet, List<String> stringList) throws SQLException
	{
		if (resultSet != null && stringList != null)
			while (resultSet.next())
				stringList.add(resultSet.getString(1));
	}


	private String url;
	private String user;
	private Password password;
	private String storedProcListGetterName = AppConstants.MESS_SP_LIST_GETTER_NAME;
	private String storedProcTextGetterName = AppConstants.MESS_SP_TEXT_GETTER_NAME;

	private Connection connection;
	private DatabaseMetaData metaData;

	private boolean supportStoredProcedures;

	private AseDataSource(){}

	@Override
	public void open() throws SQLException
	{
		connection = DriverManager.getConnection(url, user, new String(password.getKey()));

		SQLWarning warning = connection.getWarnings();
		if (warning != null)
			throw warning;

		connection.setAutoCommit(false);

		metaData = connection.getMetaData();
		supportStoredProcedures = metaData.supportsStoredProcedures();
	}

	@Override
	public void close() throws SQLException
	{
		if (connection != null && !connection.isClosed())
			connection.close();
	}

	@Override
	public void tune(Settings settings) throws Exception
	{
		close();

		if (settings != null)
		{
			String settingsClassName = settings.getClass().getSimpleName();

			if (Constants.CLASS_NAME_DATABASESETTINGS.equals(settingsClassName))
				resetParameters((DatabaseSettings)settings);
			else
				throw new IllegalArgumentException(Constants.EXCPT_DATABASE_SETTINGS_WRONG);
		}
		else
			throw new IllegalArgumentException(Constants.EXCPT_DATABASE_SETTINGS_EMPTY);
	}

	private void resetParameters(DatabaseSettings settings) throws Exception
	{
		SybDriver sybDriver = (SybDriver) Class.forName(settings.getDriverName()).newInstance();
		DriverManager.registerDriver(sybDriver);

		url = buildDatabaseUrl(settings);
		user = settings.getUserName();
		password = settings.getPassword();
	}

	@Override
	public List<StoredProc> getStoredProcList() throws Exception
	{
		if (supportStoredProcedures)
		{
			List<StoredProc> resultList = new LinkedList<>();

			connection.setAutoCommit(true);

			try (CallableStatement statement = connection.prepareCall("{call " +
					storedProcListGetterName + " (?)}"))
			{
				statement.setString(1, user);

				ResultSet resultSet = statement.executeQuery();
				transferResultToProcList(resultSet, resultList);

				return resultList;
			}
			catch (SQLException e)
			{
				throw e;
			}
			finally
			{
				connection.setAutoCommit(false);
			}
		}
		else
			throw new Exception(Constants.EXCPT_DATABASE_WITHOUT_SP_SUPPORT);
	}

	@Override
	public List<String> getStoredProcText(StoredProc storedProc) throws Exception
	{
		if (supportStoredProcedures)
		{
			List<String> resultList = new LinkedList<>();

			connection.setAutoCommit(true);

			try (CallableStatement statement = connection.prepareCall("{call " +
					storedProcTextGetterName + " (?)}"))
			{
				statement.setInt(1, storedProc.getId());

				ResultSet resultSet = statement.executeQuery();
				transferResultToStringList(resultSet, resultList);

				return resultList;
			}
			catch (SQLException e)
			{
				throw e;
			}
			finally
			{
				connection.setAutoCommit(false);
			}
		}
		else
			throw new Exception(Constants.EXCPT_DATABASE_WITHOUT_SP_SUPPORT);
	}

	private static class SingletonHelper
	{
		private static final AseDataSource INSTANCE = new AseDataSource();
	}
}
