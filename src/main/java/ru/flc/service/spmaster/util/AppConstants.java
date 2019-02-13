package ru.flc.service.spmaster.util;

public class AppConstants
{
	private AppConstants(){}

	//Actions
	public static final String KEY_ACTION_CONNECTDB = "ActionConnectDb";
	public static final String KEY_ACTION_CONNECTDB_DESCR = "ActionConnectDbDescript";
	public static final String KEY_ACTION_DISCONNECTDB = "ActionDisconnectDb";
	public static final String KEY_ACTION_DISCONNECTDB_DESCR = "ActionDisconnectDbDescr";
	public static final String KEY_ACTION_REFRESHSP = "ActionRefreshSpList";
	public static final String KEY_ACTION_REFRESHSP_DESCR = "ActionRefreshSpListDescr";
	public static final String KEY_ACTION_EXECSP = "ActionExecuteSp";
	public static final String KEY_ACTION_EXECSP_DESCR = "ActionExecuteSpDescr";
	public static final String KEY_ACTION_SHOW_SETTINGS = "ActionShowSettings";
	public static final String KEY_ACTION_SHOW_SETTINGS_DESCR = "ActionShowSettingsDescr";
	public static final String KEY_ACTION_SHOW_HELP = "ActionShowHelp";
	public static final String KEY_ACTION_SHOW_HELP_DESCR = "ActionShowHelpDescr";

	public static final String KEY_PARAM_SCRIPT_FILE_PATH = "ScriptFilePath";

	//Titles for main frame
	public static final String KEY_PANEL_PROC_LIST = "PanelStoredProcList";
	public static final String KEY_PANEL_PROC_TEXT = "PanelStoredProcText";
	public static final String KEY_PANEL_PROC_RESULT = "PanelStoredProcResult";
	public static final String KEY_PANEL_LOG = "PanelLog";

	//Names
	public static final String MESS_LOGGING_PROPERTIES_FILE_NAME = "sp_master_logging.conf";
	public static final String MESS_CONFIG_FILE_NAME = "sp_master.conf";
	public static final String MESS_RESOURCE_BUNDLE_NAME = "StoredProcMaster";

	//Images
	public static final String ICON_NAME_ABOUT = "hedgehogs.png";
	public static final String ICON_NAME_MAIN = "fire.png";
	public static final String ICON_NAME_CONNECTDB = "y_db_connect.png";
	public static final String ICON_NAME_DISCONNECTDB = "y_db_disconnect.png";
	public static final String ICON_NAME_REFRESH = "y_refresh.png";
	public static final String ICON_NAME_EXECUTE = "y_exec.png";
	public static final String ICON_NAME_SETTINGS = "y_settings.png";
	public static final String ICON_NAME_QUESTION = "y_question.png";
}
