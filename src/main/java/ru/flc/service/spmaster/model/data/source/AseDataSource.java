package ru.flc.service.spmaster.model.data.source;

import com.sybase.jdbcx.SybDriver;
import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.Settings;
import org.dav.service.settings.type.Password;
import org.dav.service.util.Constants;
import ru.flc.service.spmaster.model.DefaultValues;
import ru.flc.service.spmaster.model.data.entity.*;
import ru.flc.service.spmaster.model.settings.OperationalSettings;
import ru.flc.service.spmaster.util.AppConstants;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AseDataSource implements DataSource
{
	public static AseDataSource getInstance()
	{
		return SingletonHelper.INSTANCE;
	}

	private static String[] preciseTypeNames = {AppConstants.MESS_SP_PARAM_COL_VALUE_NUMERIC,
			AppConstants.MESS_SP_PARAM_COL_VALUE_DECIMAL,
			AppConstants.MESS_SP_PARAM_COL_VALUE_VARCHAR};

	static
	{
		Arrays.sort(preciseTypeNames);
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

	private static String getServiceProcedureName(String serviceDatabaseName, String procedureBaseName)
	{
		if (serviceDatabaseName != null && ( !serviceDatabaseName.isEmpty() ))
			return serviceDatabaseName + ".." + procedureBaseName;
		else
			return procedureBaseName;
	}

	private static void transferResultToProcList(ResultSet resultSet, List<StoredProc> storedProcList) throws SQLException
	{
		if (resultSet != null && storedProcList != null)
			while (resultSet.next())
			{
				int id = resultSet.getInt(AppConstants.MESS_SP_LIST_COL_NAME_PROC_ID);
				String name = resultSet.getString(AppConstants.MESS_SP_LIST_COL_NAME_PROC_NAME);
				String description = resultSet.getString(AppConstants.MESS_SP_LIST_COL_NAME_PROC_DESCR);
				StoredProcStatus procStatus = getProcStatusById(resultSet.getInt(AppConstants.MESS_SP_LIST_COL_NAME_PROC_STATUS_ID));
				User occupant = getProcOccupantByParams(procStatus,
						resultSet.getInt(AppConstants.MESS_SP_LIST_COL_NAME_OCCUPANT_ID),
						resultSet.getString(AppConstants.MESS_SP_LIST_COL_NAME_OCCUPANT_LOGIN),
						resultSet.getString(AppConstants.MESS_SP_LIST_COL_NAME_OCCUPANT_NAME));

				storedProcList.add(new StoredProc(id, name, description, procStatus, occupant));
			}
	}

	private static void transferResultToStringList(ResultSet resultSet, List<String> stringList) throws SQLException
	{
		if (resultSet != null && stringList != null)
			while (resultSet.next())
				stringList.add(resultSet.getString(1));
	}

	private static void transferResultToParamsList(ResultSet resultSet, List<StoredProcParameter> parametersList) throws Exception
	{
		if (resultSet != null && parametersList != null)
		{
			/*ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnsQuantity = resultSetMetaData.getColumnCount();

			for (int i = 1; i <= columnsQuantity; i++)
			{
				System.out.print(resultSetMetaData.getColumnName(i));
				System.out.print("\t");
			}

			System.out.println();*/

			while (resultSet.next())
			{
				/*for (int i = 1; i <= columnsQuantity; i++)
				{
					System.out.print(resultSet.getString(i));
					System.out.print("\t");
				}

				System.out.println();*/

				StoredProcParameter parameter = getParameterFromRow(resultSet);

				if (parameter != null)
					parametersList.add(parameter);
			}
		}
	}

	private static StoredProcStatus getProcStatusById(int statusId)
	{
		switch (statusId)
		{
			case 1:
				return StoredProcStatus.AVAILABLE;
			case 2:
				return StoredProcStatus.OCCUPIED;
			default:
				return StoredProcStatus.UNKNOWN;
		}
	}

	private static User getProcOccupantByParams(StoredProcStatus procStatus,
												int occupantId, String occupantLogin, String occupantName)
	{
		User occupant = null;

		if (procStatus == StoredProcStatus.OCCUPIED)
		{
			if (occupantLogin == null)
				occupantLogin = "";

			if (occupantName == null)
				occupantName = "";

			occupant = new User(occupantId, occupantLogin, occupantName);
		}

		return occupant;
	}

	private static StoredProcParameter getParameterFromRow(ResultSet record) throws Exception
	{
		StoredProcParameter parameter = null;

		short parameterTypeId = record.getShort(AppConstants.MESS_SP_PARAM_COL_NAME_COLUMN_TYPE);
		StoredProcParamType parameterType = StoredProcParamTypesMap.getType(parameterTypeId);

		if (parameterType != null)
		{
			String parameterName = record.getString(AppConstants.MESS_SP_PARAM_COL_NAME_COLUMN_NAME);

			int databaseTypeId = record.getInt(AppConstants.MESS_SP_PARAM_COL_NAME_DATA_TYPE);
			Class<?> valueClass = DatabaseTypes.getJavaClass(databaseTypeId);

			if (valueClass == null)
				throw new Exception(AppConstants.EXCPT_SP_PARAM_CLASS_EMPTY);

			Object initialValue = DefaultValues.getValue(valueClass);

			boolean nullableValue = false;

			if (initialValue == null)
				throw new Exception(AppConstants.EXCPT_SP_PARAM_INIT_VALUE_EMPTY);

			int precision = record.getInt(AppConstants.MESS_SP_PARAM_COL_NAME_PRECISION);
			short scale = record.getShort(AppConstants.MESS_SP_PARAM_COL_NAME_SCALE);
			String typeName = buildTypeName(record.getString(AppConstants.MESS_SP_PARAM_COL_NAME_TYPE_NAME),
					precision, scale);

			parameter = new StoredProcParameter(parameterType, parameterName, valueClass,
					initialValue, nullableValue, precision, scale, typeName);
		}

		return parameter;
	}

	private static String buildTypeName(String rawTypeName, int precision, short scale)
	{
		if (typeNameIsPrecise(rawTypeName))
		{
			StringBuilder builder = new StringBuilder(rawTypeName);
			builder.append('(');
			builder.append(precision);

			if (scale > 0)
			{
				builder.append(", ");
				builder.append(scale);
			}

			builder.append(')');

			return builder.toString();
		}
		else
			return rawTypeName;
	}

	private static boolean typeNameIsPrecise(String rawTypeName)
	{
		return Arrays.binarySearch(preciseTypeNames, rawTypeName.toUpperCase()) > 0;
	}

	private String url;
	private String dbName;
	private String user;
	private Password password;
	private String serviceDbName;
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
	public void tune(Settings... settingsArray) throws Exception
	{
		close();

		if (settingsArray != null && settingsArray.length > 0)
		{
			DatabaseSettings dbSettings = null;
			OperationalSettings operSettings = null;

			for (Settings settings : settingsArray)
			{
				String settingsClassName = settings.getClass().getSimpleName();

				if (Constants.CLASS_NAME_DATABASESETTINGS.equals(settingsClassName))
					dbSettings = (DatabaseSettings) settings;
				else if (AppConstants.CLASS_NAME_OPERATIONALSETTINGS.equals(settingsClassName))
					operSettings = (OperationalSettings) settings;
			}

			if ( dbSettings == null )
				throw new IllegalArgumentException(Constants.EXCPT_DATABASE_SETTINGS_EMPTY);

			resetParameters(dbSettings);

			if (operSettings != null)
				resetParameters(operSettings);
		}
		else
			throw new IllegalArgumentException(Constants.EXCPT_DATABASE_SETTINGS_EMPTY);
	}

	private void resetParameters(DatabaseSettings settings) throws Exception
	{
		SybDriver sybDriver = (SybDriver) Class.forName(settings.getDriverName()).newInstance();
		DriverManager.registerDriver(sybDriver);

		url = buildDatabaseUrl(settings);
		dbName = settings.getCatalog();
		user = settings.getUserName();
		password = settings.getPassword();
	}

	private void resetParameters(OperationalSettings settings)
	{
		serviceDbName = settings.getServiceCatalog();
	}

	@Override
	public List<StoredProc> getStoredProcList() throws Exception
	{
		if (supportStoredProcedures)
		{
			List<StoredProc> resultList = new LinkedList<>();

			connection.setAutoCommit(true);

			String procedureName = getServiceProcedureName(serviceDbName, storedProcListGetterName);

			try (CallableStatement statement = connection.prepareCall("{call " +
					procedureName + " (?, ?)}"))
			{
				statement.setString(1, user);
				statement.setString(2, dbName);

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

			String procedureName = getServiceProcedureName(serviceDbName, storedProcTextGetterName);

			try (CallableStatement statement = connection.prepareCall("{call " +
					procedureName + " (?, ?)}"))
			{
				statement.setString(1, dbName);
				statement.setInt(2, storedProc.getId());

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

	@Override
	public void attachStoredProcParams(StoredProc storedProc) throws Exception
	{
		List<StoredProcParameter> resultList = new LinkedList<>();

		ResultSet resultSet = metaData.getProcedureColumns(dbName, null, storedProc.getName(), null);
		transferResultToParamsList(resultSet, resultList);

		storedProc.setParameters(resultList);
	}

	private static class SingletonHelper
	{
		private static final AseDataSource INSTANCE = new AseDataSource();
	}
}
