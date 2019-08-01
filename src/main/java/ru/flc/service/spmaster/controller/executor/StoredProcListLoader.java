package ru.flc.service.spmaster.controller.executor;

import org.dav.service.util.Constants;
import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.view.View;

import java.util.List;

public class StoredProcListLoader extends AbstractExecutor<Void, Object>
{
	private DataModel model;
	private View view;

	private List<StoredProc> storedProcList;

	public StoredProcListLoader(DataModel model, View view, String executionMessage)
	{
		super(executionMessage);

		this.model = model;
		this.view = view;
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		if ( !isInterrupted() )
		{
			try
			{
				storedProcList = model.getStoredProcList();
			}
			catch (Exception e)
			{
				publishObjects(e);
			}
		}

		return null;
	}

	@Override
	protected void process(List<Object> chunks)
	{
		for (Object chunk : chunks)
			view.addToLog(chunk);

		Object lastChunk = chunks.get(chunks.size() - 1);

		if (Constants.CLASS_NAME_STRING.equals(lastChunk.getClass().getSimpleName()))
			view.showProcessMessage((String) lastChunk);
	}

	@Override
	protected void done()
	{
		if ( !isInterrupted() )
			view.showStoredProcList(storedProcList);
	}
}
