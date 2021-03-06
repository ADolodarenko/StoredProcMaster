package ru.flc.service.spmaster.model.data.source.database;

import com.sybase.jdbcx.SybDriver;
import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.Settings;
import org.dav.service.settings.type.Password;
import org.dav.service.util.Constants;
import ru.flc.service.spmaster.controller.executor.Executor;
import ru.flc.service.spmaster.model.DefaultValues;
import ru.flc.service.spmaster.model.data.entity.*;
import ru.flc.service.spmaster.model.settings.OperationalSettings;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppUtils;

import java.sql.*;
import java.util.*;

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

	private static String buildDatabaseUrl(DatabaseSettings dbSettings)
	{
		StringBuilder builder = new StringBuilder();
		fillDatabaseUrl(builder, dbSettings);

		return builder.toString();
	}

	private static void fillDatabaseUrl(StringBuilder builder, DatabaseSettings settings)
	{
		builder.append(settings.getConnectionPrefix());
		builder.append(':');
		builder.append(settings.getHost());
		builder.append(':');
		builder.append(settings.getPort());
		builder.append('/');
		builder.append(settings.getCatalog());
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

	private void transferStoredProcHeaders(List<StoredProc> sourceList, StoredProc destStoredProc)
	{
		if (sourceList == null || sourceList.isEmpty())
			destStoredProc.setStatus(StoredProcStatus.DEAD);
		else
		{
			StoredProc sourceStoredProc = sourceList.get(0);

			destStoredProc.setId(sourceStoredProc.getId());
			destStoredProc.setStatus(sourceStoredProc.getStatus());
			destStoredProc.setDescription(sourceStoredProc.getDescription());
			destStoredProc.setOccupant(sourceStoredProc.getOccupant());
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
			/* Checkout
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

			int columnsQuantity = resultSetMetaData.getColumnCount();

			for (int i = 1; i <= columnsQuantity; i++)
			{
				System.out.print(resultSetMetaData.getColumnName(i));
				System.out.print("\t");
			}

			System.out.println();
			*/

			int index = 1;

			while (resultSet.next())
			{
				/* Checkout
				for (int i = 1; i <= columnsQuantity; i++)
				{
					System.out.print(resultSet.getString(i));
					System.out.print("\t");
				}

				System.out.println();
				*/

				StoredProcParameter parameter = getParameterFromRow(resultSet, index);

				if (parameter != null)
				{
					parametersList.add(parameter);

					index++;
				}
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
				return StoredProcStatus.DEAD;
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

	private static StoredProcParameter getParameterFromRow(ResultSet record, int index) throws Exception
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

			if (initialValue == null)
				throw new Exception(AppConstants.EXCPT_SP_PARAM_INIT_VALUE_EMPTY);

			boolean nullableValue = false;

			if (parameterType != StoredProcParamType.IN)
				nullableValue = true;

			int precision = record.getInt(AppConstants.MESS_SP_PARAM_COL_NAME_PRECISION);
			short scale = record.getShort(AppConstants.MESS_SP_PARAM_COL_NAME_SCALE);
			String typeName = buildTypeName(record.getString(AppConstants.MESS_SP_PARAM_COL_NAME_TYPE_NAME),
					precision, scale);
			//int ordinalPosition = record.getInt(AppConstants.MESS_SP_PARAM_COL_NAME_ORDINAL_POSITION);

			parameter = new StoredProcParameter(parameterType, parameterName, databaseTypeId, valueClass,
					initialValue, nullableValue, precision, scale, typeName, index);
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

	private static void checkExecutionParameters(StoredProc storedProc, List<DataTable> resultTables, Executor executor)
	{
		if (storedProc == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_SP_EMPTY);

		if (resultTables == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_SP_RESULT_TABLES_EMPTY);

		if (executor == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_SP_EXECUTOR_EMPTY);
	}

	public static void setStoredProcParameters(StoredProc storedProc, CallableStatement statement, Executor executor) throws SQLException
	{
		StringBuilder builder = new StringBuilder();
		builder.append(storedProc.getName());
		builder.append(": ");

		List<StoredProcParameter> parameters = storedProc.getParameters();

		if (parameters != null)
			for (StoredProcParameter parameter : parameters)
			{
				StoredProcParamType paramType = parameter.getType();

				if (paramType != StoredProcParamType.IN)
				{
					statement.registerOutParameter(parameter.getName(), parameter.getSqlType(), parameter.getScale());

					if (paramType != StoredProcParamType.RETURN)
						builder.append(AppConstants.MESS_EXECUTOR_OUTPUT);
				}

				String paramName = parameter.getName();

				builder.append(paramName);
				builder.append(" = ");

				if (paramType == StoredProcParamType.IN)
				{
					if (parameter.isNullValue())
					{
						statement.setNull(paramName, parameter.getSqlType());
						builder.append(AppConstants.MESS_EXECUTOR_NULL);
					}
					else
					{
						Object paramValue = parameter.getValue();

						statement.setObject(paramName, paramValue,
								parameter.getSqlType(), parameter.getScale());

						builder.append(AppUtils.getQuotedStringValue(paramValue));
					}
				}
				else
					builder.append(AppConstants.MESS_EXECUTOR_NULL);

				builder.append(", ");

			}

		builder.delete(builder.length() - 2, builder.length());
		builder.append(AppConstants.MESS_EXECUTOR_SP_STARTED);

		executor.publishObjects(builder.toString());
	}

	private static void executeStatement(PreparedStatement statement,
										 List<DataTable> resultTables, Executor procExecutor) throws SQLException
	{
		boolean done = false;
		PreparedStatementExecutor executor = new PreparedStatementExecutor(statement);

		executor.execute();

		while (!done && !procExecutor.isInterrupted())
		{
			SQLException exception = executor.getException();

			if (exception != null)
				procExecutor.publishObjects(exception.toString());
			else if (executor.isResultSet())
				resultTables.add(getDataTable(executor.getResultSet()));
			else
			{
				int updateCount = executor.getUpdateCount();

				if (updateCount >= 0)
					procExecutor.publishObjects(String.format(Constants.MESS_ROWS_AFFECTED, updateCount));
				else
					done = true;
			}

			if (!done && !procExecutor.isInterrupted())
				executor.execute();
		}
	}

	private static void processWarnings(PreparedStatement statement, Executor procExecutor) throws SQLException
	{
		SQLWarning warning = statement.getWarnings();

		while (warning != null)
		{
			procExecutor.publishObjects(warning.getMessage());

			warning = warning.getNextWarning();
		}
	}

	private static void processOutputParameters(StoredProc storedProc, CallableStatement statement,
	                                            List<DataTable> resultTables, Executor executor)
			throws SQLException
	{
		StringBuilder builder = new StringBuilder();
		builder.append(storedProc.getName());
		builder.append(": ");

		List<StoredProcParameter> parameters = storedProc.getParameters();

		if (parameters != null)
		{
			List<DataElement> headers = new ArrayList<>();
			List<DataElement> oneRow = new ArrayList<>();

			for (StoredProcParameter parameter : parameters)
			{
				StoredProcParamType paramType = parameter.getType();
				String paramName = parameter.getName();
				Object paramValue;

				if (paramType == StoredProcParamType.IN)
					paramValue = parameter.getValue();
				else
				{
					if (paramType != StoredProcParamType.RETURN)
						builder.append(AppConstants.MESS_EXECUTOR_OUTPUT);

					paramValue = statement.getObject(paramName);

					headers.add(new DataElement(paramName, String.class));
					oneRow.add(new DataElement(paramValue, parameter.getValueClass()));
				}

				builder.append(paramName);
				builder.append(" = ");
				builder.append(AppUtils.getQuotedStringValue(paramValue));
				builder.append(", ");
			}

			if ( !oneRow.isEmpty() )
			{
				List<List<DataElement>> rows = new ArrayList<>();
				rows.add(oneRow);

				resultTables.add(new DataTable(DataTableType.OUTPUT_PARAMS, headers, rows));
			}
		}

		builder.delete(builder.length() - 2, builder.length());
		builder.append(AppConstants.MESS_EXECUTOR_SP_ENDED);
		executor.publishObjects(builder.toString());
	}

	private static void showInterruptedStatus(StoredProc storedProc, Executor executor)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(storedProc.getName());
		builder.append(": ");
		builder.append(AppConstants.MESS_EXECUTOR_SP_INTERRUPTED);

		executor.publishObjects(builder.toString());
	}

	private static DataTable getDataTable(ResultSet resultSet) throws SQLException
	{
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();

		List<DataElement> headers = new ArrayList<>();
		for (int i = 1; i <= columnCount; i++)
			headers.add(new DataElement(metaData.getColumnLabel(i), String.class));

		List<List<DataElement>> rows = new ArrayList<>();
		while (resultSet.next())
		{
			List<DataElement> row = new ArrayList<>();
			for (int i = 1; i <= columnCount; i++)
				row.add(new DataElement(resultSet.getObject(i),
						DatabaseTypes.getJavaClass(metaData.getColumnType(i))));

			rows.add(row);
		}

		return new DataTable(DataTableType.PLAIN_RESULT, headers, rows);
	}

	private static String getStoredProcCallString(StoredProc storedProc)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("{? = call ");
		builder.append(storedProc.getName());
		builder.append(" (");

		List<StoredProcParameter> parameters = storedProc.getParameters();
		if (parameters != null && !parameters.isEmpty())
		{
			for (StoredProcParameter parameter : parameters)
				if (parameter.getType() != StoredProcParamType.RETURN)
					builder.append("?, ");

			builder.delete(builder.length() - 2, builder.length());
		}

		builder.append(")}");

		return builder.toString();
	}


	private String url;
	private String dbName;
	private String user;
	private Password password;
	private String serviceDbName;
	private String clientHostName;
	private String clientHostProc;
	private String applicationName;
	private String storedProcListGetterName = AppConstants.MESS_SP_LIST_GETTER_NAME;
	private String storedProcTextGetterName = AppConstants.MESS_SP_TEXT_GETTER_NAME;

	private Connection connection;
	private DatabaseMetaData metaData;

	private boolean supportStoredProcedures;

	private AseDataSource(){}

	@Override
	public void open() throws SQLException
	{
		Properties connectionProperties = buildConnectionProperties();

		connection = DriverManager.getConnection(url, connectionProperties);

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

			checkSettings(dbSettings, operSettings);

			resetParameters(dbSettings, operSettings);
		}
		else
			throw new IllegalArgumentException(Constants.EXCPT_DATABASE_SETTINGS_EMPTY);
	}

	private void checkSettings(DatabaseSettings dbSettings, OperationalSettings operSettings) throws IllegalArgumentException
	{
		checkOneSetting(dbSettings, Constants.EXCPT_DATABASE_SETTINGS_EMPTY);
		checkOneSetting(dbSettings.getDriverName(), AppConstants.EXCPT_DATABASE_DRIVER_WRONG);
		checkOneSetting(dbSettings.getConnectionPrefix(), AppConstants.EXCPT_DATABASE_CONN_PREFIX_WRONG);
		checkOneSetting(dbSettings.getHost(), AppConstants.EXCPT_DATABASE_HOST_WRONG);
		checkOneSetting(dbSettings.getPort(), AppConstants.EXCPT_DATABASE_PORT_WRONG);
		checkOneSetting(dbSettings.getCatalog(), AppConstants.EXCPT_DATABASE_CATALOG_WRONG);
		checkOneSetting(dbSettings.getUserName(), AppConstants.EXCPT_DATABASE_USER_WRONG);
		checkOneSetting(dbSettings.getPassword(), AppConstants.EXCPT_DATABASE_PASSWORD_WRONG);

		if (operSettings != null)
			checkOneSetting(operSettings.getServiceCatalog(), AppConstants.EXCPT_DATABASE_SERVICE_CATALOG_WRONG);
	}

	private void checkOneSetting(Object parameter, String message) throws IllegalArgumentException
	{
		if (parameter == null)
			throw new IllegalArgumentException(message);

		String parameterClassName = parameter.getClass().getSimpleName();

		if (Constants.CLASS_NAME_STRING.equals(parameterClassName))
		{
			if ( ((String) parameter).isEmpty() )
				throw new IllegalArgumentException(message);
		}
	}

	private void resetParameters(DatabaseSettings dbSettings, OperationalSettings operSettings) throws Exception
	{
		SybDriver sybDriver = (SybDriver) Class.forName(dbSettings.getDriverName()).newInstance();
		DriverManager.registerDriver(sybDriver);

		url = buildDatabaseUrl(dbSettings);
		dbName = dbSettings.getCatalog();
		user = dbSettings.getUserName();
		password = dbSettings.getPassword();

		if (operSettings != null)
		{
			serviceDbName = operSettings.getServiceCatalog();
			clientHostName = operSettings.getClientHostName();
			clientHostProc = operSettings.getClientHostProc();
			applicationName = operSettings.getApplicationName();
		}
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
					procedureName + " (?, ?, ?)}"))
			{
				statement.setString(1, user);
				statement.setString(2, dbName);
				statement.setNull(3, Types.VARCHAR);

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

	@Override
	public void executeStoredProc(StoredProc storedProc, List<DataTable> resultTables, Executor executor) throws Exception
	{
		checkExecutionParameters(storedProc, resultTables, executor);

		if (supportStoredProcedures)
		{
			connection.setAutoCommit(true);

			try (CallableStatement statement = prepareSpStatement(storedProc, executor))
			{
				executeStatement(statement, resultTables, executor);

				if (!executor.isInterrupted())
				{
					processWarnings(statement, executor);
					processOutputParameters(storedProc, statement, resultTables, executor);
				}
				else
					showInterruptedStatus(storedProc, executor);
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
	public void updateStoredProcHeaders(StoredProc storedProc) throws Exception
	{
		if (supportStoredProcedures)
		{
			List<StoredProc> resultList = new LinkedList<>();

			connection.setAutoCommit(true);

			String procedureName = getServiceProcedureName(serviceDbName, storedProcListGetterName);

			try (CallableStatement statement = connection.prepareCall("{call " +
					procedureName + " (?, ?, ?)}"))
			{
				statement.setString(1, user);
				statement.setString(2, dbName);
				statement.setString(3, storedProc.getName());

				ResultSet resultSet = statement.executeQuery();
				transferResultToProcList(resultSet, resultList);

				transferStoredProcHeaders(resultList, storedProc);
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

	private CallableStatement prepareSpStatement(StoredProc storedProc, Executor executor) throws SQLException
	{
		String callString = getStoredProcCallString(storedProc);

		CallableStatement spStatement = connection.prepareCall(callString);
		setStoredProcParameters(storedProc, spStatement, executor);

		return spStatement;
	}

	private Properties buildConnectionProperties()
	{
		Properties properties = new Properties();
		properties.put(AppConstants.MESS_CONN_PARAM_USER, user);
		properties.put(AppConstants.MESS_CONN_PARAM_PASSWORD, new String(password.getKey()));

		if (clientHostName != null && !clientHostName.isEmpty())
			properties.put(AppConstants.MESS_CONN_PARAM_HOSTNAME, clientHostName);

		if (clientHostProc != null && !clientHostProc.isEmpty())
			properties.put(AppConstants.MESS_CONN_PARAM_HOSTPROC, clientHostProc);

		if (applicationName != null && !applicationName.isEmpty())
			properties.put(AppConstants.MESS_CONN_PARAM_APPLICATIONNAME, applicationName);

		return properties;
	}

	private static class SingletonHelper
	{
		private static final AseDataSource INSTANCE = new AseDataSource();
	}
}
