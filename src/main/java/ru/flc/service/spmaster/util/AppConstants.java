package ru.flc.service.spmaster.util;

/**
 * The application constants store.
 */
public final class AppConstants
{
	//Exceptions
	public static final String EXCPT_CONTROLLER_EMPTY = "Controller is empty.";
	public static final String EXCPT_SP_LIST_TABLE_EMPTY = "The table that lists stored procedures is empty.";
	public static final String EXCPT_ARRAY_EMPTY = "The array is empty.";
	public static final String EXCPT_SP_PARAM_CLASS_EMPTY = "The class of a stored procedure parameter is empty.";
	public static final String EXCPT_SP_PARAM_INIT_VALUE_EMPTY = "The initial value of a stored procedure parameter is empty.";
	public static final String EXCPT_SP_EMPTY = "The stored procedure is empty.";
	public static final String EXCPT_SP_RESULT_TABLES_EMPTY = "The list that is meant for stored procedure results is empty.";
	public static final String EXCPT_SP_EXECUTOR_EMPTY = "The stored procedure executor is empty.";
	public static final String EXCPT_SP_RESULT_TABLE_EMPTY = "The stored procedure result is empty.";
	public static final String EXCPT_ACTIONS_MANAGER_EMPTY = "The actions manager is empty.";

	//Class names
	public static final String CLASS_NAME_LONG = "Long";
	public static final String CLASS_NAME_FLOAT = "Float";
	public static final String CLASS_NAME_BIGDECIMAL = "BigDecimal";
	public static final String CLASS_NAME_TIMESTAMP = "Timestamp";
	public static final String CLASS_NAME_NUMBEREDITOR = "NumberEditor";
	public static final String CLASS_NAME_JFORMATTEDTEXTFIELD = "JFormattedTextField";
	public static final String CLASS_NAME_STOREDPROCLISTTABLEMODEL = "StoredProcListTableModel";
	public static final String CLASS_NAME_STOREDPROCLISTTABLE = "StoredProcListTable";
	public static final String CLASS_NAME_STOREDPROCSTATUS = "StoredProcStatus";
	public static final String CLASS_NAME_OPERATIONALSETTINGS = "OperationalSettings";
	public static final String CLASS_NAME_STOREDPROCPARAMSTABLEMODEL = "StoredProcParamsTableModel";
	public static final String CLASS_NAME_STOREDPROCPARAMSTABLE = "StoredProcParamsTable";
	public static final String CLASS_NAME_STOREDPROCRESULTTABLEMODEL = "StoredProcResultTableModel";

	//Actions
	public static final String KEY_ACTION_CONNECTDB = "ActionConnectDb";
	public static final String KEY_ACTION_CONNECTDB_DESCR = "ActionConnectDbDescript";
	public static final String KEY_ACTION_DISCONNECTDB = "ActionDisconnectDb";
	public static final String KEY_ACTION_DISCONNECTDB_DESCR = "ActionDisconnectDbDescr";
	public static final String KEY_ACTION_REFRESHSP = "ActionRefreshSpList";
	public static final String KEY_ACTION_REFRESHSP_DESCR = "ActionRefreshSpListDescr";
	public static final String KEY_ACTION_EXECSP = "ActionExecuteSp";
	public static final String KEY_ACTION_EXECSP_DESCR = "ActionExecuteSpDescr";
	public static final String KEY_ACTION_CANCELSP = "ActionCancelSp";
	public static final String KEY_ACTION_CANCELSP_DESCR = "ActionCancelSpDescr";
	public static final String KEY_ACTION_SHOW_SETTINGS = "ActionShowSettings";
	public static final String KEY_ACTION_SHOW_SETTINGS_DESCR = "ActionShowSettingsDescr";
	public static final String KEY_ACTION_SHOW_HELP = "ActionShowHelp";
	public static final String KEY_ACTION_SHOW_HELP_DESCR = "ActionShowHelpDescr";

	public static final String KEY_PARAM_SCRIPT_FILE_PATH = "ScriptFilePath";
	public static final String KEY_PARAM_DB_SERVICE_CATALOG = "DatabaseServiceCatalog";

	//Titles for main frame
	public static final String KEY_PANEL_PROC_LIST = "PanelStoredProcList";
	public static final String KEY_PANEL_PROC_TEXT = "PanelStoredProcText";
	public static final String KEY_PANEL_PROC_RESULT = "PanelStoredProcResult";
	public static final String KEY_PANEL_LOG = "PanelLog";
	public static final String KEY_PANEL_STATUS_CONNECTED = "PanelStatusConnected";
	public static final String KEY_PANEL_STATUS_DISCONNECTED = "PanelStatusDisconnected";
	public static final String KEY_COLUMN_SP_STATUS = "ColumnStoredProcStatus";
	public static final String KEY_COLUMN_SP_DESCRIPT = "ColumnStoredProcDescript";
	public static final String KEY_COLUMN_SP_NAME = "ColumnStoredProcName";
	public static final String KEY_TAB_RESULT_REGULAR = "TabResultRegular";
	public static final String KEY_TAB_RESULT_OUTPUT_PARAMS = "TabResultOutputParams";
	public static final String KEY_TEXTFIELD_SEARCH_HINT = "TextFieldSearchHint";

	//Titles for dialogs
	public static final String KEY_EXECUTION_DIALOG = "ExecutionDialog";
	public static final String KEY_BUTTON_EXECUTE = "ExecuteButton";
	public static final String KEY_PANEL_PROC_PARAMETERS = "PanelStoredProcParamList";
	public static final String KEY_COLUMN_SP_PARAM_TYPE = "ColumnStoredProcParamType";
	public static final String KEY_COLUMN_SP_PARAM_NAME = "ColumnStoredProcParamName";
	public static final String KEY_COLUMN_SP_PARAM_VALUETYPE = "ColumnStoredProcParamValueType";
	public static final String KEY_COLUMN_SP_PARAM_NULL = "ColumnStoredProcParamNull";
	public static final String KEY_COLUMN_SP_PARAM_VALUE = "ColumnStoredProcParamValue";

	//Names
	public static final String MESS_LOGGING_PROPERTIES_FILE_NAME = "sp_master_logging.conf";
	public static final String MESS_CONFIG_FILE_NAME = "sp_master.conf";
	public static final String MESS_RESOURCE_BUNDLE_NAME = "StoredProcMaster";
	public static final String MESS_SP_LIST_GETTER_NAME = "spm_get_available_proc";
	public static final String MESS_SP_TEXT_GETTER_NAME = "spm_get_proc_text";
	public static final String MESS_SP_LIST_COL_NAME_PROC_ID = "proc_id";
	public static final String MESS_SP_LIST_COL_NAME_PROC_NAME = "proc_name";
	public static final String MESS_SP_LIST_COL_NAME_PROC_DESCR = "proc_description";
	public static final String MESS_SP_LIST_COL_NAME_PROC_STATUS_ID = "proc_status_id";
	public static final String MESS_SP_LIST_COL_NAME_OCCUPANT_ID = "occupant_id";
	public static final String MESS_SP_LIST_COL_NAME_OCCUPANT_LOGIN = "occupant_login";
	public static final String MESS_SP_LIST_COL_NAME_OCCUPANT_NAME = "occupant_name";
	public static final String MESS_SP_PARAM_COL_NAME_COLUMN_TYPE = "COLUMN_TYPE";
	public static final String MESS_SP_PARAM_COL_NAME_COLUMN_NAME = "COLUMN_NAME";
	public static final String MESS_SP_PARAM_COL_NAME_DATA_TYPE = "DATA_TYPE";
	public static final String MESS_SP_PARAM_COL_NAME_TYPE_NAME = "TYPE_NAME";
	public static final String MESS_SP_PARAM_COL_NAME_PRECISION = "PRECISION";
	public static final String MESS_SP_PARAM_COL_NAME_SCALE = "SCALE";
	public static final String MESS_SP_PARAM_COL_NAME_ORDINAL_POSITION = "ORDINAL_POSITION";
	public static final String MESS_SP_PARAM_COL_VALUE_NUMERIC = "NUMERIC";
	public static final String MESS_SP_PARAM_COL_VALUE_DECIMAL = "DECIMAL";
	public static final String MESS_SP_PARAM_COL_VALUE_VARCHAR = "VARCHAR";
	public static final String MESS_WORKER_PROPERTY_NAME_STATE = "state";

	//Messages
	public static final String MESS_EXECUTOR_SP_STARTED = " - started.";
	public static final String MESS_EXECUTOR_SP_ENDED = " - ended.";
	public static final String MESS_EXECUTOR_SP_INTERRUPTED = "interrupted.";
	public static final String MESS_EXECUTOR_NULL = "null";
	public static final String MESS_EXECUTOR_OUTPUT = "OUT ";
	public static final String MESS_FILENAME_EXT_KEY_CSV = "CSV";
	public static final String MESS_FILENAME_EXT_KEY_TXT = "CSV";
	public static final String MESS_FILENAME_EXT_KEY_XLS = "XLS";
	public static final String MESS_FILENAME_EXT_KEY_XLSX = "XLSX";
	public static final String MESS_FILENAME_EXT_VALUE_CSV = "CSV";
	public static final String MESS_FILENAME_EXT_VALUE_TXT = "CSV";
	public static final String MESS_FILENAME_EXT_VALUE_XLS = "XLS";
	public static final String MESS_FILENAME_EXT_VALUE_XLSX = "XLSX";

	//Patterns
	public static final String PATTERN_DATETIME = "dd.MM.yyyy HH:mm:ss";

	//Images
	public static final String ICON_NAME_ABOUT = "hedgehogs.png";
	public static final String ICON_NAME_MAIN = "fire.png";
	public static final String ICON_NAME_CONNECTDB = "y_db_connect.png";
	public static final String ICON_NAME_DISCONNECTDB = "y_db_disconnect.png";
	public static final String ICON_NAME_REFRESH = "y_refresh.png";
	public static final String ICON_NAME_EXECUTE = "y_exec.png";
	public static final String ICON_NAME_SETTINGS = "y_settings.png";
	public static final String ICON_NAME_QUESTION = "y_question.png";
	public static final String ICON_NAME_STORED_PROCEDURE = "hardwarechip.png";
	public static final String ICON_NAME_EXECUTING = "loading_mod_green.gif";
	public static final String ICON_NAME_PROC_DEAD = "proc_unknown.png";
	public static final String ICON_NAME_PROC_AVAILABLE = "proc_available.png";
	public static final String ICON_NAME_PROC_OCCUPIED = "proc_occupied.png";
	public static final String ICON_NAME_DATA_TABLE = "table01.png";
	public static final String ICON_NAME_SEARCH = "search01.png";

	private AppConstants(){}
}
