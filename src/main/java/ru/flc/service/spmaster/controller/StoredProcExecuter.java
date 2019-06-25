package ru.flc.service.spmaster.controller;

import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.view.View;

import javax.swing.*;

public class StoredProcExecuter extends SwingWorker<Void, Void>
{
	private StoredProc storedProc;
	private DataModel model;
	private View view;

	public StoredProcExecuter(StoredProc storedProc, DataModel model, View view)
	{
		this.storedProc = storedProc;
		this.model = model;
		this.view = view;
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		while ( !isCancelled() )
			Thread.sleep(1000);

		return null;
	}

	@Override
	protected void done()
	{
		if ( !isCancelled() )
		{

		}
	}
}
