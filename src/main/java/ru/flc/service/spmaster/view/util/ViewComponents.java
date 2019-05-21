package ru.flc.service.spmaster.view.util;

import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import org.dav.service.view.TitleAdjuster;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;
import java.awt.*;

public class ViewComponents
{
	public static JPanel getTitledPanelWithBorderLayout(ResourceManager resourceManager, TitleAdjuster titleAdjuster,
														Component inhabitant, Object constraints, Border baseBorder,
														String titleKey, int titleJustification, int titlePosition)
	{
		JPanel panel = new JPanel(new BorderLayout());

		panel.setBorder(BorderFactory.createTitledBorder(baseBorder,
				"", titleJustification, titlePosition));
		titleAdjuster.registerComponent(panel, new Title(resourceManager, titleKey));
		panel.add(inhabitant, constraints);

		return panel;
	}

	public static DefaultFormatter getSpinnerNumberFormatter(JSpinner spinner)
	{
		JFormattedTextField textField = getSpinnerNumberTextField(spinner);

		return getTextFieldFormatter(textField);
	}

	public static JFormattedTextField getSpinnerNumberTextField(JSpinner spinner)
	{
		JFormattedTextField field = null;

		if (spinner != null)
		{
			JComponent innerEditorComponent = spinner.getEditor();

			if (innerEditorComponent.getClass().getSimpleName().equals(AppConstants.CLASS_NAME_NUMBEREDITOR))
			{
				JSpinner.NumberEditor innerEditor = (JSpinner.NumberEditor) innerEditorComponent;

				field = innerEditor.getTextField();
			}
		}

		return field;
	}

	public static DefaultFormatter getTextFieldFormatter(JFormattedTextField textField)
	{
		DefaultFormatter formatter = null;

		if (textField != null)
		{
			JFormattedTextField.AbstractFormatter abstractFormatter = textField.getFormatter();
			if (abstractFormatter instanceof DefaultFormatter)
				formatter = (DefaultFormatter) abstractFormatter;
		}

		return formatter;
	}
}
