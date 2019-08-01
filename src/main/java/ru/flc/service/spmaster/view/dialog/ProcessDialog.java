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
	private String message;

	private JLabel imageLabel;
	private JLabel messageLabel;
	private JButton cancelButton;

	public ProcessDialog(Frame owner, SettingsDialogInvoker invoker, ResourceManager resourceManager)
	{
		super(owner, "", true);

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

	private void initComponents()
	{
		add(initMainPanel(), BorderLayout.CENTER);
	}

	private JPanel initMainPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		UsableGBC constraints;

		constraints = new UsableGBC(0, 0);
		constraints.setInsets(new Insets(0, OUTER_SIDE_INSET, 0, INNER_SIDE_INSET));

		imageLabel = new JLabel();
		ViewComponents.setComponentAnySize(imageLabel, IMAGE_SIZE);
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setIcon(resourceManager.getImageIcon(AppConstants.ICON_NAME_EXECUTING));

		panel.add(imageLabel, constraints);

		constraints = new UsableGBC(1, 0);
		constraints.setInsets(new Insets(0, INNER_SIDE_INSET, 0, INNER_SIDE_INSET));

		messageLabel = new JLabel();
		/*messageLabel.setMinimumSize(new Dimension(MESSAGE_MIN_WIDTH, COMPONENT_HEIGHT));
		messageLabel.setPreferredSize(new Dimension(MESSAGE_PREFERRED_WIDTH, COMPONENT_HEIGHT));
		messageLabel.setMaximumSize(new Dimension(getMaximumMessageWidth(), COMPONENT_HEIGHT));*/
		messageLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 14));
		panel.add(messageLabel, constraints);

		constraints = new UsableGBC(2, 0);
		constraints.setInsets(new Insets(0, INNER_SIDE_INSET, 0, OUTER_SIDE_INSET));

		cancelButton = new JButton();
		titleAdjuster.registerComponent(cancelButton, new Title(resourceManager, Constants.KEY_BUTTON_CANCEL));
		ViewComponents.setComponentAnySize(cancelButton, BUTTON_MAX_SIZE);
		cancelButton.setIcon(resourceManager.getImageIcon(Constants.ICON_NAME_CANCEL));
		panel.add(cancelButton, constraints);

		//cancelButtonListener = new CancelButtonListener(false);
		//cancelButton.addActionListener(cancelButtonListener);

		return panel;
	}

	private int getMaximumMessageWidth()
	{
		int leftWidth = owner.getMinimumSize().width;
		leftWidth -= 2 * OUTER_SIDE_INSET + 2 * 2 * INNER_SIDE_INSET;
		leftWidth -= IMAGE_SIZE.width + BUTTON_MAX_SIZE.width;

		return leftWidth;
	}
}
