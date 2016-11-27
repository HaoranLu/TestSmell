package testsmellplugin.handlers;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import testsmell.PluginCandidate;
import testsmell.PluginCandidateProvider;
import testsmellplugin.views.SampleView;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class Handler2 extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"TestSmellPlugin",
				"detecting related code smell called");
		ArrayList<PluginCandidate> candidates = new ArrayList<PluginCandidate>();
        // Image here some fancy database access to read the persons and to
        // put them into the model
	candidates.add(new PluginCandidate("zhan", "Zufall", "male"));
	candidates.add(new PluginCandidate("Reiner", "Babbel", "male"));
	candidates.add(new PluginCandidate("Marie", "Dortmund", "female"));
	PluginCandidateProvider.INSTANCE.removePluginCandidates(candidates);
	try {
		HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(SampleView.ID);
		
	} catch (PartInitException e) {
		e.printStackTrace();
	}
		return null;
	}
}
