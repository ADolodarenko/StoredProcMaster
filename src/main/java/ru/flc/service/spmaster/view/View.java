package ru.flc.service.spmaster.view;

import org.dav.service.settings.ViewSettings;
import ru.flc.service.spmaster.model.settings.ViewConstraints;

public interface View
{
	void repaintFrame();
	void showHelpInfo();
	void setPreferredBounds(ViewConstraints settings);
	void setActualBounds(ViewSettings settings);
}
