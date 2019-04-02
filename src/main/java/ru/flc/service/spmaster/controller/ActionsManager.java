package ru.flc.service.spmaster.controller;

import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppStatus;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionsManager
{
	private AbstractAction connectDbAction;
	private AbstractAction disconnectDbAction;
	private AbstractAction refreshSpListAction;
	private AbstractAction execSpAction;
	private AbstractAction showSettingsAction;
	private AbstractAction showHelpAction;

	private Controller controller;
	private ResourceManager resourceManager;

	public ActionsManager(Controller controller, ResourceManager resourceManager)
	{
		this.controller = controller;
		this.resourceManager = resourceManager;

		initActions();
	}

	private void initActions()
	{
		connectDbAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.connectToDatabase();
				controller.refreshStoredProcedureList();
			}
		};

		disconnectDbAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.disconnectFromDatabase();
			}
		};

		refreshSpListAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.refreshStoredProcedureList();
			}
		};

		execSpAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.execStoredProcedure();
			}
		};

		showSettingsAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.showSettings();
			}
		};

		showHelpAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.showHelp();
			}
		};

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

		resetAction(refreshSpListAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_REFRESHSP).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_REFRESHSP_DESCR).getText(),
				AppConstants.ICON_NAME_REFRESH);

		resetAction(execSpAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_EXECSP).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_EXECSP_DESCR).getText(),
				AppConstants.ICON_NAME_EXECUTE);

		resetAction(showSettingsAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_SETTINGS).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_SETTINGS_DESCR).getText(),
				AppConstants.ICON_NAME_SETTINGS);

		resetAction(showHelpAction,
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

	public AbstractAction getRefreshSpListAction()
	{
		return refreshSpListAction;
	}

	public AbstractAction getExecSpAction()
	{
		return execSpAction;
	}

	public AbstractAction getShowSettingsAction()
	{
		return showSettingsAction;
	}

	public AbstractAction getShowHelpAction()
	{
		return showHelpAction;
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

	public void adjustActionsToAppState()
	{
		connectDbAction.setEnabled(controller.checkAppStatuses(AppStatus.DISCONNECTED));
		disconnectDbAction.setEnabled(controller.checkAppStatuses(AppStatus.CONNECTED));
		refreshSpListAction.setEnabled(controller.checkAppStatuses(AppStatus.CONNECTED));
		execSpAction.setEnabled(controller.checkAppStatuses(AppStatus.CONNECTED));
		showSettingsAction.setEnabled(controller.checkAppStatuses(AppStatus.DISCONNECTED));
	}
}
