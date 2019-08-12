package ru.flc.service.spmaster.view.dialog;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import org.dav.service.view.TitleAdjuster;
import org.dav.service.view.UsableGBC;
import org.dav.service.view.ViewUtils;
import org.dav.service.view.dialog.SettingsDialogInvoker;
import org.dav.service.view.table.listener.ForEditableCellsSelectionListener;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcParamType;
import ru.flc.service.spmaster.model.data.entity.StoredProcParameter;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.table.StoredProcParamsTable;
import ru.flc.service.spmaster.view.table.StoredProcParamsTableModel;
import ru.flc.service.spmaster.view.table.editor.TableCellEditorFactory;
import ru.flc.service.spmaster.view.table.renderer.ArbitraryCellRendererFactory;
import ru.flc.service.spmaster.view.util.ViewComponents;
import ru.flc.service.spmaster.view.thirdparty.TableColumnAdjuster;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
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

	private CancelAction cancelButtonAction;
	private JButton executeButton;
	private JButton cancelButton;

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

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

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

			titleAdjuster.resetComponents();
			resetActions();

			tableModel.fireTableStructureChanged();

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

			pack();
			repaint();
			setLocationRelativeTo(owner);
		}

		super.setVisible(b);
	}

	public void tune(StoredProc storedProc)
	{
		this.storedProc = storedProc;
		this.parameterList = getValuableParameters(storedProc.getParameters());
	}

	private List<StoredProcParameter> getValuableParameters(List<StoredProcParameter> allParameters)
	{
		if (allParameters != null)
		{
			List<StoredProcParameter> parameters = new LinkedList<>();

			for (StoredProcParameter parameter : allParameters)
				if (parameter.getType() != StoredProcParamType.RETURN)
					parameters.add(parameter);

			return parameters;
		}
		else
			return null;
	}

	public StoredProc getStoredProc()
	{
		return storedProc;
	}

	public void adjustToAppStatus(boolean isRunning)
	{
		cancelButtonAction.setSpRunning(isRunning);

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
				new TableCellEditorFactory(resourceManager),
				new ArbitraryCellRendererFactory(resourceManager), 1.3F);
		table.setFillsViewportHeight(false);

		table.getColumnModel().getSelectionModel().addListSelectionListener(new ForEditableCellsSelectionListener(table));

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
		resetActions();

		assignKeyStrokes(panel);

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

		cancelButtonAction = new CancelAction(false);
		cancelButton = new JButton(cancelButtonAction);
		cancelButton.setPreferredSize(BUTTON_MAX_SIZE);
		cancelButton.setMaximumSize(BUTTON_MAX_SIZE);
	}

	private void assignKeyStrokes(JComponent component)
	{
		InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), AppConstants.NAME_ACTION_CANCEL);

		ActionMap actionMap = component.getActionMap();
		actionMap.put(AppConstants.NAME_ACTION_CANCEL, cancelButtonAction);
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

	public void resetActions()
	{
		ViewUtils.resetAction(cancelButtonAction,
				resourceManager,
				Constants.KEY_BUTTON_CANCEL,
				null,
				Constants.ICON_NAME_CANCEL);
	}

	private class CancelAction extends AbstractAction
	{
		private boolean isSpRunning;

		public CancelAction(boolean isSpRunning)
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
