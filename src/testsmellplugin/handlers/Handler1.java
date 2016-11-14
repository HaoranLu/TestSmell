package testsmellplugin.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.IProgressService;

import testsmellplugin.Activator;
import testsmellplugin.preferences.PreferenceConstants;
import testsmellplugin.views.SampleView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class Handler1 extends AbstractHandler {
	private IJavaProject selectedProject;
	private IJavaProject activeProject;
	private IPackageFragmentRoot selectedPackageFragmentRoot;
	private IPackageFragment selectedPackageFragment;
	private ICompilationUnit selectedCompilationUnit;
	private IType selectedType;
	private boolean projectSelected = false;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String cho = testsmellplugin.preferences.PreferenceConstants.P_CHOICE;
		String cho2 = store.getString(PreferenceConstants.P_CHOICE);
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		//ISelection selection = HandlerUtil.getCurrentSelection(event);
		/*MessageDialog.openInformation(
				window.getShell(),
				"TestSmellPlugin",
				"Handler1, called"+" pchoice is " + cho + " " + cho2);*/
		window.getSelectionService().addSelectionListener(selectionListener);
		if (projectSelected) {
			//excute detectTestSmell and show test smell result view
			//testsmell result use singleton.
			activeProject = selectedProject;
			detectTestSmell(activeProject);
			try {
				HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(SampleView.ID);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
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
		return null;
	}
	private void detectTestSmell(Object testSmellTarget){
		if (testSmellTarget instanceof IJavaProject) {
			System.out.println("run test smell on " + testSmellTarget.toString());
		}
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IProgressService ps = workbench.getProgressService();
		try {
			ps.busyCursorWhile(new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						int timer = 10;
						if (monitor != null) {
							monitor.beginTask("Detecting Test Smell", timer);
						}
						for (int i = 0; i < timer; i++) {
							if(monitor != null && monitor.isCanceled())
				    			throw new OperationCanceledException();
							TimeUnit.SECONDS.sleep(1);//fake the detecting process
							if(monitor != null)
								monitor.worked(1);
						}
						if (monitor != null) {
							monitor.done();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}// TODO Auto-generated method stub
					
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private ISelectionListener selectionListener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection)selection;
				Object element = structuredSelection.getFirstElement();
				IJavaProject javaProject = null;
				if(element instanceof IJavaProject) {
					javaProject = (IJavaProject)element;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedType = null;
				}
				else if(element instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot)element;
					javaProject = packageFragmentRoot.getJavaProject();
					selectedPackageFragmentRoot = packageFragmentRoot;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedType = null;
				}
				else if(element instanceof IPackageFragment) {
					IPackageFragment packageFragment = (IPackageFragment)element;
					javaProject = packageFragment.getJavaProject();
					selectedPackageFragment = packageFragment;
					selectedPackageFragmentRoot = null;
					selectedCompilationUnit = null;
					selectedType = null;
				}
				else if(element instanceof ICompilationUnit) {
					ICompilationUnit compilationUnit = (ICompilationUnit)element;
					javaProject = compilationUnit.getJavaProject();
					selectedCompilationUnit = compilationUnit;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedType = null;
				}
				else if(element instanceof IType) {
					IType type = (IType)element;
					javaProject = type.getJavaProject();
					selectedType = type;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
				}
				if(javaProject != null && !javaProject.equals(selectedProject)) {
					selectedProject = javaProject;
					/*if(candidateRefactoringTable != null)
						tableViewer.remove(candidateRefactoringTable);*/
					//identifyBadSmellsAction.setEnabled(true);
					projectSelected = true;
				}
			}
		}
	};
	
}
