package testsmellplugin.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.IProgressService;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.TestClassBean;
import testsmell.DetectionExample;
import testsmell.PluginCandidate;
import testsmell.PluginCandidateProvider;
import testsmell.relatedCodeSmell.CodeSmellComputation;
import testsmell.relatedCodeSmell.codeSmellResult;
import testsmell.relatedCodeSmell.smellAssociationRules;
import testsmellplugin.views.RelatedCodeSmellResult;
import testsmellplugin.views.TestSmellResult;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * Related Code Smell menu option
 * the test smell detection function is from Handler1
 * @see org.eclipse.core.commands.IHandler
 * @see Handler1
 */
public class Handler2 extends Handler1 {

	private List<PluginCandidate> testSmells;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		window.getSelectionService().addSelectionListener(selectionListener);
		if (projectSelected) {
			this.activeProject = this.selectedProject;
			IPath activePath = this.selectedPath;
			IPath root = activeProject.getResource().getLocation().removeLastSegments(1).addTrailingSeparator();
			IPath selectedProjectPath = root.append(activeProject.getPath());
			IPath activefullPath = root.append(activePath);
			System.out.println(selectedProjectPath);
			System.out.println(activefullPath);
			File projectfile = selectedProjectPath.toFile();
			File testClassFolder = activefullPath.toFile();
			detectTestSmell(projectfile,testClassFolder);
			testSmells = PluginCandidateProvider.INSTANCE.getPluginCandidates();
			if (!testSmells.isEmpty()) {
				PluginCandidateProvider.INSTANCE.setCodeSmellResult(detectCodeSmell(testSmells));
			}
			IWorkbenchPage page= HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
			IViewPart viewPart = page.findView(RelatedCodeSmellResult.ID);
			if (viewPart != null) {
				page.hideView(viewPart);
			}
			try {
				HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(RelatedCodeSmellResult.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}else{
			MessageDialog.openInformation(
					window.getShell(),
					"TestSmellPlugin",
					"Please at least selet a code file in the project explorer");
			try {
				HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView("org.eclipse.ui.navigator.ProjectExplorer");
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		testSmells = PluginCandidateProvider.INSTANCE.getPluginCandidates();
		
		
		return null;
	}
	/**
	 * Similar with Handler1.detectTestSmell but this detect code smell use rules from the smellAssociationRules.INSTANCE
	 * @param testSmells
	 * @return A list of codeSmellResult will be return
	 */
	private List<codeSmellResult> detectCodeSmell(List<PluginCandidate> testSmells){
		IWorkbench workbench = PlatformUI.getWorkbench();
		IProgressService ps = workbench.getProgressService();
		List<codeSmellResult> results = new ArrayList<>();
		CodeSmellComputation metric = new CodeSmellComputation();
		try {
			ps.busyCursorWhile(new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						if (monitor != null) {
							monitor.beginTask("Detecting Related Code Smells...", testSmells.size());
							for (PluginCandidate pluginCandidate : testSmells) {
								ClassBean testClass = pluginCandidate.getTestClass().getTestCase();
								ArrayList<ClassBean> productionClasses = pluginCandidate.getTestClass().getProductionClasses();
								HashMap<String, Object> TestSmellResult = pluginCandidate.getTestSmell();
								for (java.util.Map.Entry<String, Object> smell : TestSmellResult.entrySet()) {
									String SmellType = smell.getKey();
									Object SmellResult = smell.getValue();
									HashMap<String, String[]> rules = smellAssociationRules.INSTANCE.getRules();
									for (java.util.Map.Entry<String, String[]> rule : rules.entrySet()) {
										if (SmellType.equalsIgnoreCase(rule.getKey())) {
											Map<String, ClassBean> codeSmell = metric.detectUseRule(productionClasses, rule.getValue());
											if (codeSmell != null) {
												codeSmellResult csResult = new codeSmellResult(testClass, SmellType, codeSmell);
												results.add(csResult);
											}
											
										}
									}
								}
								if(monitor != null && monitor.isCanceled())
					    			throw new OperationCanceledException();
								monitor.worked(1);
							}
							monitor.done();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}// TODO Auto-generated method stub
					
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return results;
	}
}
