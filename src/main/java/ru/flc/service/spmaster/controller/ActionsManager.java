package ru.flc.service.spmaster.controller;

import org.dav.service.util.Constants;
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
	private AbstractAction showSpInfoAction;
	private AbstractAction execSpAction;
	private AbstractAction cancelSpAction;
	private AbstractAction showSettingsAction;
	private AbstractAction showHelpAction;
	private AbstractAction saveActiveResultPageAction;
	private AbstractAction saveAllResultPagesAction;

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

		showSpInfoAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.showStoredProcedureInfo();
			}
		};

		execSpAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.execStoredProcedure();
			}
		};

		cancelSpAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.cancelStoredProcedure();
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

		saveActiveResultPageAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.saveActiveResultPage();
			}
		};

		saveAllResultPagesAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.saveAllResultPages();
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

		resetAction(showSpInfoAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_EXECSP).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_EXECSP_DESCR).getText(),
				AppConstants.ICON_NAME_EXECUTE);

		resetAction(execSpAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_EXECSP).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_EXECSP_DESCR).getText(),
				AppConstants.ICON_NAME_EXECUTE);

		resetAction(cancelSpAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_CANCELSP).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_CANCELSP_DESCR).getText(),
				Constants.ICON_NAME_CANCEL);

		resetAction(showSettingsAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_SETTINGS).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_SETTINGS_DESCR).getText(),
				AppConstants.ICON_NAME_SETTINGS);

		resetAction(showHelpAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_HELP).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_SHOW_HELP_DESCR).getText(),
				AppConstants.ICON_NAME_QUESTION);

		resetAction(saveActiveResultPageAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_SAVE_ACTIVE_RESULT_PAGE).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_SAVE_ACTIVE_RESULT_PAGE_DESCR).getText(),
				AppConstants.ICON_NAME_SAVE);

		resetAction(saveAllResultPagesAction,
				new Title(resourceManager, AppConstants.KEY_ACTION_SAVE_ALL_RESULT_PAGES).getText(),
				new Title(resourceManager, AppConstants.KEY_ACTION_SAVE_ALL_RESULT_PAGES_DESCR).getText(),
				AppConstants.ICON_NAME_SAVE_ALL);
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

	public AbstractAction getShowSpInfoAction()
	{
		return showSpInfoAction;
	}

	public AbstractAction getExecSpAction()
	{
		return execSpAction;
	}

	public AbstractAction getCancelSpAction()
	{
		return cancelSpAction;
	}

	public AbstractAction getShowSettingsAction()
	{
		return showSettingsAction;
	}

	public AbstractAction getShowHelpAction()
	{
		return showHelpAction;
	}

	public AbstractAction getSaveActiveResultPageAction()
	{
		return saveActiveResultPageAction;
	}

	public AbstractAction getSaveAllResultPagesAction()
	{
		return saveAllResultPagesAction;
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

	public void adjustToAppStatus(AbstractAction... actionsToAdjust)
	{
		if (actionsToAdjust == null || actionsToAdjust.length == 0)
		{
			connectDbAction.setEnabled(controller.checkAppStatuses(AppStatus.DISCONNECTED));
			disconnectDbAction.setEnabled(controller.checkAppStatuses(AppStatus.CONNECTED));
			refreshSpListAction.setEnabled(controller.checkAppStatuses(AppStatus.CONNECTED));
			showSpInfoAction.setEnabled(controller.checkAppStatuses(AppStatus.CONNECTED) &&
					controller.activeStoredProcExists());
			showSettingsAction.setEnabled(controller.checkAppStatuses(AppStatus.DISCONNECTED));

			//TODO: Not like that exactly. Think about it later.
			saveActiveResultPageAction.setEnabled(controller.checkAppStatuses(AppStatus.CONNECTED) &&
					controller.activeStoredProcExists());
			saveAllResultPagesAction.setEnabled(controller.checkAppStatuses(AppStatus.CONNECTED) &&
					controller.activeStoredProcExists());
		}
		else
			for (AbstractAction action : actionsToAdjust)
				if (action.equals(showSpInfoAction) ||
						action.equals(saveActiveResultPageAction) ||
						action.equals(saveAllResultPagesAction))
					action.setEnabled(controller.checkAppStatuses(AppStatus.CONNECTED) &&
						controller.activeStoredProcExists());
	}
}
