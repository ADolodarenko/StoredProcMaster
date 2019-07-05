package ru.flc.service.spmaster.view.dialog;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import org.dav.service.view.TitleAdjuster;
import org.dav.service.view.UsableGBC;
import org.dav.service.view.dialog.SettingsDialogInvoker;
import org.dav.service.view.table.renderer.TableCellRendererFactory;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcParameter;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.table.StoredProcParamsTable;
import ru.flc.service.spmaster.view.table.StoredProcParamsTableModel;
import ru.flc.service.spmaster.view.table.editor.TableCellEditorFactory;
import ru.flc.service.spmaster.view.table.renderer.ArbitraryTableCellRendererFactory;
import ru.flc.service.spmaster.view.util.ViewComponents;
import ru.flc.service.spmaster.view.thirdparty.TableColumnAdjuster;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ExecutionDialog extends JDialog
{
	private static final Dimension BUTTON_MAX_SIZE = new Dimension(100, 30);

	private Frame owner;
	private SettingsDialogInvoker invoker;
	private AbstractAction execAction;
	private AbstractAction cancelAction;

	private ResourceManager resourceManager;
	private TitleAdjuster titleAdjuster;

	private StoredProc storedProc;
	private List<StoredProcParameter> parameterList;

	private StoredProcParamsTableModel tableModel;
	private StoredProcParamsTable table;
	private TableColumnAdjuster columnAdjuster;
	private JScrollPane tablePane;
	private JPanel settingsPanel;

	private JLabel imageLabel; //102, 70
	private JLabel procedureLabel;

	private JButton executeButton;
	private JButton cancelButton;

	private CancelButtonListener cancelButtonListener;

	public ExecutionDialog(Frame owner, SettingsDialogInvoker invoker,
						   ResourceManager resourceManager, AbstractAction execAction, AbstractAction cancelAction)
	{
		super(owner, "", true);

		this.owner = owner;
		this.invoker = invoker;

		this.resourceManager = resourceManager;
		this.titleAdjuster = new TitleAdjuster();
		this.execAction = execAction;
		this.cancelAction = cancelAction;

		initComponents();

		setResizable(false);
	}

	@Override
	public void setVisible(boolean b)
	{
		if (b)
		{
			imageLabel.setIcon(resourceManager.getImageIcon(AppConstants.ICON_NAME_STORED_PROCEDURE));

			if (storedProc == null)
				procedureLabel.setText("");
			else
				procedureLabel.setText(storedProc.getName() + " ");

			tableModel.clear();
			tableModel.fireTableDataChanged();

			if (parameterList != null && (!parameterList.isEmpty()))
			{
				tableModel.addAllRows(parameterList);
				tableModel.fireTableDataChanged();

				table.getTableHeader().setResizingAllowed(true);
				columnAdjuster.adjustColumns();
				table.getTableHeader().setResizingAllowed(false);

				settingsPanel.setVisible(true);
			}
			else
				settingsPanel.setVisible(false);

			titleAdjuster.resetComponents();

			pack();
			repaint();
			setLocationRelativeTo(owner);
		}

		super.setVisible(b);
	}

	public void tune(StoredProc storedProc)
	{
		this.storedProc = storedProc;
		this.parameterList = storedProc.getParameters();
	}

	public StoredProc getStoredProc()
	{
		return storedProc;
	}

	public void adjustToAppStatus(boolean isRunning)
	{
		cancelButtonListener.setSpRunning(isRunning);

		setControlsEnabled( !isRunning );

		String statusIconName = isRunning ? AppConstants.ICON_NAME_EXECUTING : AppConstants.ICON_NAME_STORED_PROCEDURE;
		imageLabel.setIcon(resourceManager.getImageIcon(statusIconName));
	}

	private void initComponents()
	{
		add(initTitlePanel(), BorderLayout.NORTH);

		settingsPanel = initSettingsPanel();
		add(settingsPanel, BorderLayout.CENTER);

		add(initCommandPanel(), BorderLayout.SOUTH);

		titleAdjuster.registerComponent(this, new Title(resourceManager, AppConstants.KEY_EXECUTION_DIALOG));
	}

	private JPanel initTitlePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		UsableGBC constraints;

		constraints = new UsableGBC(0, 0);
		constraints.setInsets(new Insets(0, 10, 0, 5));

		imageLabel = new JLabel();

		Dimension imageSize = new Dimension(102, 70);
		imageLabel.setMinimumSize(imageSize);
		imageLabel.setPreferredSize(imageSize);
		imageLabel.setMaximumSize(imageSize);
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(imageLabel, constraints);

		constraints = new UsableGBC(1, 0);
		constraints.setInsets(new Insets(0, 5, 0, 10));

		procedureLabel = new JLabel();
		procedureLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 14));
		panel.add(procedureLabel, constraints);

		return panel;
	}

	private JPanel initSettingsPanel()
	{
		tableModel = new StoredProcParamsTableModel(resourceManager, StoredProcParameter.getTitleKeys(), null);

		table = new StoredProcParamsTable(tableModel,
				new TableCellEditorFactory(),
				new ArbitraryTableCellRendererFactory(resourceManager), 1.3F);
		table.setFillsViewportHeight(false);

		columnAdjuster = new TableColumnAdjuster(table, 6, 200);

		tablePane = new JScrollPane(table);

		JPanel panel = ViewComponents.getTitledPanelWithBorderLayout(resourceManager, titleAdjuster,
				tablePane, BorderLayout.CENTER, BorderFactory.createEtchedBorder(),
				AppConstants.KEY_PANEL_PROC_PARAMETERS, TitledBorder.TOP, TitledBorder.CENTER);

		return panel;
	}

	private JPanel initCommandPanel()
	{
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder());

		initButtons();

		panel.add(executeButton);
		panel.add(cancelButton);

		return panel;
	}

	private void initButtons()
	{
		executeButton = new JButton();
		titleAdjuster.registerComponent(executeButton, new Title(resourceManager, AppConstants.KEY_BUTTON_EXECUTE));
		executeButton.setPreferredSize(BUTTON_MAX_SIZE);
		executeButton.setMaximumSize(BUTTON_MAX_SIZE);
		executeButton.setIcon(resourceManager.getImageIcon(AppConstants.ICON_NAME_EXECUTE));
		executeButton.addActionListener(event -> execute());

		cancelButtonListener = new CancelButtonListener(false);
		cancelButton = new JButton();
		titleAdjuster.registerComponent(cancelButton, new Title(resourceManager, Constants.KEY_BUTTON_CANCEL));
		cancelButton.setPreferredSize(BUTTON_MAX_SIZE);
		cancelButton.setMaximumSize(BUTTON_MAX_SIZE);
		cancelButton.setIcon(resourceManager.getImageIcon(Constants.ICON_NAME_CANCEL));
		cancelButton.addActionListener(cancelButtonListener);
	}

	private void execute()
	{
		stopTableEditing();

		if (execAction != null)
			execAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	}

	private void cancel()
	{
		if (cancelAction != null)
			cancelAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	}

	private void exit()
	{
		stopTableEditing();

		setVisible(false);

		if (invoker != null)
			invoker.setFocus();
	}

	private void setControlsEnabled(boolean enabled)
	{
		if (settingsPanel.isVisible())
			table.setEnabled(enabled);

		executeButton.setEnabled(enabled);
	}

	private void stopTableEditing()
	{
		if (table != null && table.isEditing())
			table.getCellEditor().stopCellEditing();
	}

	private class CancelButtonListener implements ActionListener
	{
		private boolean isSpRunning;

		public CancelButtonListener(boolean isSpRunning)
		{
			this.isSpRunning = isSpRunning;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (isSpRunning)
				cancel();
			else
				exit();
		}

		public void setSpRunning(boolean isSpRunning)
		{
			this.isSpRunning = isSpRunning;
		}
	}
}
