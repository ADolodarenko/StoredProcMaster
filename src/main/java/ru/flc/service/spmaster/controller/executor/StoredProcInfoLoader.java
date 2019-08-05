package ru.flc.service.spmaster.controller.executor;

import org.dav.service.util.Constants;
import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcStatus;
import ru.flc.service.spmaster.view.View;

import java.util.List;

public class StoredProcInfoLoader extends AbstractExecutor<Void, Object>
{
	private StoredProc storedProc;
	private DataModel model;
	private View view;
	private List<String> storedProcTextLines;

	public StoredProcInfoLoader(String executionMessageKey, StoredProc storedProc, DataModel model, View view)
	{
		super(executionMessageKey);

		this.storedProc = storedProc;
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
				model.updateStoredProcHeaders(storedProc);

				if (storedProc.getStatus() != StoredProcStatus.DEAD)
					storedProcTextLines = model.getStoredProcText(storedProc);
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
		{
			try
			{
				get();
			}
			catch (Exception e)
			{
				view.addToLog(e);
			}

			view.showStoredProc(storedProc);

			if (storedProcTextLines != null)
				view.showStoredProcText(storedProcTextLines);
		}
	}
}
