package ru.flc.service.spmaster.controller;

import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;

public class ActionsManager
{
	private AbstractAction connectDbAction;
	private AbstractAction disconnectDbAction;
	private AbstractAction execSpAction;
	private AbstractAction settingsAction;
	private AbstractAction helpAction;

	private ActionProcessor processor;
	private ResourceManager resourceManager;

	public ActionsManager(ActionProcessor processor, ResourceManager resourceManager)
	{
		this.processor = processor;
		this.resourceManager = resourceManager;

		initActions();
	}

	private void initActions()
	{

		resetActions();
	}

	public void resetActions()
	{
		resetAction(connectDbAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_CONNECTDB).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_CONNECTDB_DESCR).getText(),
				AppConstants.ICON_NAME_CONNECTDB);

		resetAction(disconnectDbAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_DISCONNECTDB).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_DISCONNECTDB_DESCR).getText(),
				AppConstants.ICON_NAME_DISCONNECTDB);

		resetAction(execSpAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_EXECSP).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_EXECSP_DESCR).getText(),
				AppConstants.ICON_NAME_EXECUTE);

		resetAction(settingsAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_SETTINGS).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_SETTINGS_DESCR).getText(),
				AppConstants.ICON_NAME_SETTINGS);

		resetAction(helpAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_HELP).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_HELP_DESCR).getText(),
				AppConstants.ICON_NAME_QUESTION);
	}

	public AbstractAction getConnectDbAction()
	{
		return connectDbAction;
	}

	public AbstractAction getDisconnectDbAction()
	{
		return disconnectDbAction;
	}

	public AbstractAction getExecSpAction()
	{
		return execSpAction;
	}

	public AbstractAction getSettingsAction()
	{
		return settingsAction;
	}

	public AbstractAction getAboutAction()
	{
		return helpAction;
	}

	private void resetAction(AbstractAction action,
							 String actionName,
							 String actionShortDescription,
							 String actionIconName)
	{
		action.putValue(Action.NAME, actionName);
		action.putValue(Action.SHORT_DESCRIPTION, actionShortDescription);
		action.putValue(Action.SMALL_ICON, resourceManager.getImageIcon(actionIconName));
	}
}
