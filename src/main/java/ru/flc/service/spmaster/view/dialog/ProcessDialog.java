package ru.flc.service.spmaster.view.dialog;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import org.dav.service.view.TitleAdjuster;
import org.dav.service.view.UsableGBC;
import org.dav.service.view.dialog.SettingsDialogInvoker;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.util.ViewComponents;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ProcessDialog extends JDialog
{
	private static final int COMPONENT_HEIGHT = 70;
	private static final int MESSAGE_MIN_WIDTH = 200;
	private static final int MESSAGE_PREFERRED_WIDTH = 250;
	private static final int OUTER_SIDE_INSET = 10;
	private static final int INNER_SIDE_INSET = 5;
	private static final Dimension BUTTON_MAX_SIZE = new Dimension(100, 30);
	private static final Dimension IMAGE_SIZE = new Dimension(102, COMPONENT_HEIGHT);

	private Frame owner;
	private SettingsDialogInvoker invoker;
	private ResourceManager resourceManager;
	private TitleAdjuster titleAdjuster;
	private boolean cancellationPossible;
	private String message;

	private JPanel mainPanel;
	private JLabel imageLabel;
	private JLabel messageLabel;
	private JButton cancelButton;

	public ProcessDialog(Frame owner, SettingsDialogInvoker invoker,
						 ResourceManager resourceManager)
	{
		super(owner, "", true);  //If we set "modal" to "true" here, we have to face with the SwingWorker trouble sometimes.

		this.owner = owner;
		this.invoker = invoker;
		this.resourceManager = resourceManager;

		this.titleAdjuster = new TitleAdjuster();

		initComponents();

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);
		pack();
		setLocationRelativeTo(owner);
	}

	@Override
	public void setVisible(boolean b)
	{
		if (b)
		{
			titleAdjuster.resetComponents();

			pack();
			repaint();
			setLocationRelativeTo(owner);
		}

		super.setVisible(b);
	}

	public void setMessage(String message)
	{
		if ( !Objects.equals(this.message, message) )
		{
			this.message = message;

			messageLabel.setText(this.message);
		}
	}

	public boolean isCancellationPossible()
	{
		return cancellationPossible;
	}

	public void setCancellationPossible(boolean cancellationPossible)
	{
		if (this.cancellationPossible != cancellationPossible)
		{
			this.cancellationPossible = cancellationPossible;

			rebuildMainPanel();
		}
	}

	private void initComponents()
	{
		mainPanel = buildMainPanel();

		add(mainPanel, BorderLayout.CENTER);
	}

	private JPanel buildMainPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		addImageLabel(panel);
		addMessageLabel(panel);

		if (cancellationPossible)
			addCancelButton(panel);

		return panel;
	}

	private void rebuildMainPanel()
	{
		if (cancelButton != null)
			mainPanel.remove(cancelButton);
		mainPanel.remove(messageLabel);

		addMessageLabel(mainPanel);
		messageLabel.setText(this.message);

		if (cancellationPossible)
			addCancelButton(mainPanel);
	}

	private void addImageLabel(JPanel parentPanel)
	{
		UsableGBC constraints = new UsableGBC(0, 0);
		constraints.setInsets(new Insets(0, OUTER_SIDE_INSET, 0, INNER_SIDE_INSET));

		imageLabel = createImageLabel();
		parentPanel.add(imageLabel, constraints);
	}

	private void addMessageLabel(JPanel parentPanel)
	{
		UsableGBC constraints = new UsableGBC(1, 0);

		int rightInset = OUTER_SIDE_INSET;
		if (cancellationPossible)
			rightInset = INNER_SIDE_INSET;

		constraints.setInsets(new Insets(0, INNER_SIDE_INSET, 0, rightInset));

		messageLabel = createMessageLabel();
		parentPanel.add(messageLabel, constraints);
	}

	private void addCancelButton(JPanel parentPanel)
	{
		UsableGBC constraints = new UsableGBC(2, 0);
		constraints.setInsets(new Insets(0, INNER_SIDE_INSET, 0, OUTER_SIDE_INSET));

		cancelButton = createCancelButton();
		parentPanel.add(cancelButton, constraints);

		//cancelButtonListener = new CancelButtonListener(false);
		//cancelButton.addActionListener(cancelButtonListener);
	}

	private JLabel createImageLabel()
	{
		JLabel label = new JLabel();
		ViewComponents.setComponentAnySize(label, IMAGE_SIZE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setIcon(resourceManager.getImageIcon(AppConstants.ICON_NAME_EXECUTING));

		return label;
	}

	private JLabel createMessageLabel()
	{
		JLabel label = new JLabel();
		label.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 14));

		return label;
	}

	private JButton createCancelButton()
	{
		JButton button = new JButton();
		titleAdjuster.registerComponent(button, new Title(resourceManager, Constants.KEY_BUTTON_CANCEL));
		ViewComponents.setComponentAnySize(button, BUTTON_MAX_SIZE);
		button.setIcon(resourceManager.getImageIcon(Constants.ICON_NAME_CANCEL));

		return button;
	}

	private int getMaximumMessageWidth()
	{
		int leftWidth = owner.getMinimumSize().width;

		leftWidth -= 2 * OUTER_SIDE_INSET + 2 * INNER_SIDE_INSET;
		leftWidth -= IMAGE_SIZE.width;

		if (cancellationPossible)
		{
			leftWidth -= 2 * INNER_SIDE_INSET;
			leftWidth -= BUTTON_MAX_SIZE.width;
		}

		return leftWidth;
	}
}
