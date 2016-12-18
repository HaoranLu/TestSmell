package testsmellplugin.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.IProgressService;

import it.unisa.scam14.beans.TestClassBean;
import it.unisa.scam14.utility.TestSmellUtilities;
import testsmell.DetectionExample;
import testsmell.PluginCandidate;
import testsmell.PluginCandidateProvider;
import testsmellplugin.Activator;
import testsmellplugin.preferences.PreferenceConstants;

import testsmellplugin.views.TestSmellResult;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
/**
 * Detect Test Smell menu option
 * based on sample handler extends AbstractHandler, an IHandler base class.
 * for key binding please check plugin.xml
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class Handler1 extends AbstractHandler {
	protected IJavaProject selectedProject;
	protected IJavaProject activeProject;
	private IPackageFragmentRoot selectedPackageFragmentRoot;
	private IPackageFragment selectedPackageFragment;
	private ICompilationUnit selectedCompilationUnit;
	private IType selectedType;
	private IMethod selectedMethod;
	private IField selectedField;
	protected IPath selectedPath;
	protected boolean projectSelected = false;
	protected ArrayList<PluginCandidate> candidates;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		//ISelection selection = HandlerUtil.getCurrentSelection(event);
		//IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		//following comments show how to get preference of this plugin
//		boolean ena = store.getBoolean(PreferenceConstants.PREF_AUTO_DETECT_TESTSMELL);
//		String choice = store.getString(PreferenceConstants.PREF_AUTO_DETECT_RANGE);
//		MessageDialog.openInformation(
//				window.getShell(),
//				"preference",
//				"range: "+choice + " enable: " + ena);
		window.getSelectionService().addSelectionListener(selectionListener);
		if (projectSelected) {
			//excute detectTestSmell and show test smell result view
			//testsmell result use singleton.
			this.activeProject = this.selectedProject;
			IPath activePath = this.selectedPath;
			IPath root = activeProject.getResource().getLocation().removeLastSegments(1).addTrailingSeparator();
			IPath selectedProjectPath = root.append(activeProject.getPath());
			IPath activefullPath = root.append(activePath);
//			System.out.println(selectedProjectPath);
//			System.out.println(activefullPath);
			File projectfile = selectedProjectPath.toFile();
			File testClassFolder = activefullPath.toFile();
			if(!TestSmellUtilities.extractTestCases(testClassFolder).isEmpty()){
				detectTestSmell(projectfile,testClassFolder);
				IWorkbenchPage page= HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
				IViewPart viewPart = page.findView(TestSmellResult.ID);
				if (viewPart != null) {
					page.hideView(viewPart);
				}
				try {
					HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(TestSmellResult.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}else{
				MessageDialog.openInformation(
						window.getShell(),
						"Warning: No Test Classes Found",
						"No Test Classes have been found in the selected resources");
				try {
					HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView("org.eclipse.ui.navigator.ProjectExplorer");
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else {
			MessageDialog.openInformation(
					window.getShell(),
					"Warning: No Source Code Selected",
					"Please select a Source File, Package or Project folder in the project explorer view.");
			try {
				HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView("org.eclipse.ui.navigator.ProjectExplorer");
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * Detect test smell in given test class file in the range of project file
	 * write the test smell result into the singleton PluginCandidateProvider
	 * @param ProjectFile The project directory
	 * @param TestClassFile The diretory of test classes
	 */
	public void detectTestSmell(File ProjectFile, File TestClassFile ){
		IWorkbench workbench = PlatformUI.getWorkbench();
		IProgressService ps = workbench.getProgressService();
		try {
			ps.busyCursorWhile(new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						DetectionExample detectionExample = new DetectionExample(ProjectFile, TestClassFile);
						HashMap<TestClassBean, Object> testSmellResult = new HashMap<>();
						int timer = 2;	
							detectionExample.prepareForDetection(monitor);
							detectionExample.getDetectionResult(monitor);
							testSmellResult .putAll(detectionExample.getTestSmellResult());
						candidates = new ArrayList<PluginCandidate>();
						int idcounter = 0;
						for (TestClassBean key : testSmellResult.keySet()) {
							candidates.add(new PluginCandidate(idcounter, "smelltype", "testSmellSourceEntity", "testSmellTargetClass", key, (HashMap<String, Object>)testSmellResult.get(key)));
							idcounter ++;
						}
						PluginCandidateProvider.INSTANCE.setPluginCandidates(candidates);
					} catch (Exception e) {
						// TODO: handle exception
					}// TODO Auto-generated method stub
					
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * this listener get the project and selection to feed the test smell detection process.
	 * <p>
	 * The logic about different granularity Detection can be realize here
	 */
	protected ISelectionListener selectionListener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection)selection;
				Object element = structuredSelection.getFirstElement();
				/*if (element instanceof IAdaptable) {
					IProject project = (IProject)((IAdaptable)element).getAdapter(IProject.class);
		            IPath path = project.getFullPath();
		            System.out.println(path);

				}*/
				IJavaProject javaProject = null;
				if(element instanceof IJavaProject) {
					System.out.println("javaProject selected");
					javaProject = (IJavaProject)element;
					selectedPath = javaProject.getPath();
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedType = null;
					selectedMethod = null;
					selectedField = null;
				}else if(element instanceof IProject){
					IProject ipProject = (IProject)element;
					try {
						if(ipProject.hasNature(JavaCore.NATURE_ID)){
							javaProject = JavaCore.create(ipProject);
							selectedPath = javaProject.getPath();
							selectedPackageFragmentRoot = null;
							selectedPackageFragment = null;
							selectedCompilationUnit = null;
							selectedType = null;
							selectedMethod = null;
							selectedField = null;
						}
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(element instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot)element;
					javaProject = packageFragmentRoot.getJavaProject();
					selectedPath = packageFragmentRoot.getPath().addTrailingSeparator();
					//selectedPath = javaProject.getPath().addTrailingSeparator();
					selectedPackageFragmentRoot = packageFragmentRoot;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedType = null;
					selectedMethod = null;
					selectedField = null;
				}
				else if(element instanceof IPackageFragment) {
					IPackageFragment packageFragment = (IPackageFragment)element;
					javaProject = packageFragment.getJavaProject();
					selectedPath = packageFragment.getPath().addTrailingSeparator();
					selectedPackageFragment = packageFragment;
					selectedPackageFragmentRoot = null;
					selectedCompilationUnit = null;
					selectedType = null;
					selectedMethod = null;
					selectedField = null;
				}
				else if(element instanceof ICompilationUnit) {
					ICompilationUnit compilationUnit = (ICompilationUnit)element;
					javaProject = compilationUnit.getJavaProject();
					//IPath path = compilationUnit.getPath().removeLastSegments(1).addTrailingSeparator();
					IPath path = compilationUnit.getPath();
					selectedPath = path;
					selectedCompilationUnit = compilationUnit;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedType = null;
					selectedMethod = null;
					selectedField = null;
				}
				else if(element instanceof IType) {
					IType type = (IType)element;
					javaProject = type.getJavaProject();
					IPath path = type.getPackageFragment().getPath().addTrailingSeparator();
					selectedPath = path;
					selectedType = type;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedMethod = null;
					selectedField = null;
				}
				else if (element instanceof IMethod) {
					IMethod method = (IMethod)element;
					javaProject = method.getJavaProject();
					IPath path = null;
					try {
						path = method.getCompilationUnit().getPath().removeLastSegments(1).addTrailingSeparator();
					} catch (Exception e) {
						// TODO: handle exception
						path = method.getPath().addTrailingSeparator();
					}
					selectedPath = path;
					selectedMethod = method;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedMethod = method;
					selectedField = null;
					
				}
				else if (element instanceof IField) {
					IField field = (IField)element;
					javaProject = field.getJavaProject();
					IPath path = field.getCompilationUnit().getPath().removeLastSegments(1).addTrailingSeparator();
					selectedPath = path;
					selectedField = field;
					selectedType = null;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedMethod = null;
					selectedField = field;
				}
				if(javaProject != null && !javaProject.equals(selectedProject)) {
					selectedProject = javaProject;
					projectSelected = true;
				}
			}
		}
	};
	
}
