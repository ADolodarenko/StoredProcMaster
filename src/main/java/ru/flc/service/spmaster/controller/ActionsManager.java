package ru.flc.service.spmaster.controller;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppStatus;
import ru.flc.service.spmaster.view.util.ViewComponents;

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
		if (resourceManager == null)
			throw new IllegalArgumentException(Constants.EXCPT_RESOURCE_MANAGER_EMPTY);

		this.resourceManager = resourceManager;
		this.controller = controller;

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
		ViewUtils.resetAction(connectDbAction,
				resourceManager,
				AppConstants.KEY_ACTION_CONNECTDB,
				AppConstants.KEY_ACTION_CONNECTDB_DESCR,
				AppConstants.ICON_NAME_CONNECTDB);

		ViewUtils.resetAction(disconnectDbAction,
				resourceManager,
				AppConstants.KEY_ACTION_DISCONNECTDB,
				AppConstants.KEY_ACTION_DISCONNECTDB_DESCR,
				AppConstants.ICON_NAME_DISCONNECTDB);

		ViewUtils.resetAction(refreshSpListAction,
				resourceManager,
				AppConstants.KEY_ACTION_REFRESHSP,
				AppConstants.KEY_ACTION_REFRESHSP_DESCR,
				AppConstants.ICON_NAME_REFRESH);

		ViewUtils.resetAction(showSpInfoAction,
				resourceManager,
				AppConstants.KEY_ACTION_EXECSP,
				AppConstants.KEY_ACTION_EXECSP_DESCR,
				AppConstants.ICON_NAME_EXECUTE);

		ViewUtils.resetAction(execSpAction,
				resourceManager,
				AppConstants.KEY_ACTION_EXECSP,
				AppConstants.KEY_ACTION_EXECSP_DESCR,
				AppConstants.ICON_NAME_EXECUTE);

		ViewUtils.resetAction(cancelSpAction,
				resourceManager,
				AppConstants.KEY_ACTION_CANCELSP,
				AppConstants.KEY_ACTION_CANCELSP_DESCR,
				Constants.ICON_NAME_CANCEL);

		ViewUtils.resetAction(showSettingsAction,
				resourceManager,
				AppConstants.KEY_ACTION_SHOW_SETTINGS,
				AppConstants.KEY_ACTION_SHOW_SETTINGS_DESCR,
				AppConstants.ICON_NAME_SETTINGS);

		ViewUtils.resetAction(showHelpAction,
				resourceManager,
				AppConstants.KEY_ACTION_SHOW_HELP,
				AppConstants.KEY_ACTION_SHOW_HELP_DESCR,
				AppConstants.ICON_NAME_QUESTION);

		ViewUtils.resetAction(saveActiveResultPageAction,
				resourceManager,
				AppConstants.KEY_ACTION_SAVE_ACTIVE_RESULT_PAGE,
				AppConstants.KEY_ACTION_SAVE_ACTIVE_RESULT_PAGE_DESCR,
				AppConstants.ICON_NAME_SAVE);

		ViewUtils.resetAction(saveAllResultPagesAction,
				resourceManager,
				AppConstants.KEY_ACTION_SAVE_ALL_RESULT_PAGES,
				AppConstants.KEY_ACTION_SAVE_ALL_RESULT_PAGES_DESCR,
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
