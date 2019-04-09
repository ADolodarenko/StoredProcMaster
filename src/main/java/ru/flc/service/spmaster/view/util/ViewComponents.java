package ru.flc.service.spmaster.view.util;

import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import org.dav.service.view.TitleAdjuster;

import javax.swing.*;
import javax.swing.border.Border;
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
}
