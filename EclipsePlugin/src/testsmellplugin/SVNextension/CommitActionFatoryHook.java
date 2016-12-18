package testsmellplugin.SVNextension;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.svn.core.IStateFilter;
import org.eclipse.team.svn.core.SVNMessages;
import org.eclipse.team.svn.core.connector.SVNConflictDescriptor;
import org.eclipse.team.svn.core.operation.CompositeOperation;
import org.eclipse.team.svn.core.operation.IActionOperation;
import org.eclipse.team.svn.core.operation.IRevisionProvider;
import org.eclipse.team.svn.core.resource.ILocalResource;
import org.eclipse.team.svn.core.svnstorage.SVNRemoteStorage;
import org.eclipse.team.svn.core.utility.SVNUtility;
import org.eclipse.team.svn.ui.SVNTeamUIPlugin;
import org.eclipse.team.svn.ui.composite.ResourceSelectionComposite;
import org.eclipse.team.svn.ui.extension.factory.ICommentDialogPanel;
import org.eclipse.team.svn.ui.extension.factory.ICommentManager;
import org.eclipse.team.svn.ui.extension.factory.ICommitActionFactory;
import org.eclipse.team.svn.ui.extension.factory.ICommitDialog;
import org.eclipse.team.svn.ui.extension.impl.DefaultCommitActionFactory;
import org.eclipse.team.svn.ui.panel.AbstractDialogPanel;
import org.eclipse.team.svn.ui.preferences.SVNTeamPreferences;
import org.eclipse.team.svn.ui.repository.model.RepositoryTrunk;
import org.eclipse.team.svn.ui.verifier.ExistingResourceVerifier;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.TestClassBean;
import it.unisa.scam14.utility.TestSmellUtilities;
import testsmell.DetectionExample;
import testsmell.PluginCandidate;
import testsmell.PluginCandidateProvider;
import testsmellplugin.Activator;
import testsmellplugin.preferences.PreferenceConstants;
import testsmellplugin.views.CommitWarningMessageDialog;
/**
 * Contribute to the Subversice extension point.
 * <p>
 * only modified the getCommitDialog method
 * @author Haoran Lu
 *
 */
public class CommitActionFatoryHook implements ICommitActionFactory {
	private static final String className = CommitActionFatoryHook.class.getName();
	DefaultCommitActionFactory defaultCommitActionFactory = new DefaultCommitActionFactory();
	BlockingCommitActionFactory blockingCommitActionFactory = new BlockingCommitActionFactory();
	

	@Override
	public void initCommentManager(ICommentManager commentManager) {
		defaultCommitActionFactory.initCommentManager(commentManager);		
	}

	@Override
	public void confirmMessage(ICommentManager commentManager) {
		defaultCommitActionFactory.confirmMessage(commentManager);
		
	}

	@Override
	public void cancelMessage(ICommentManager commentManager) {
		
		defaultCommitActionFactory.cancelMessage(commentManager);		
	}

	/**
	 * check Test Smell according to the preference
	 * show warning if test smell is found
	 */
	@Override
	public ICommitDialog getCommitDialog(Shell shell, Collection allFilesToCommit, ICommentDialogPanel panel) {

		boolean haveTestSmell = false;
		ICommitDialog defaultCommitDialog = defaultCommitActionFactory.getCommitDialog(shell, allFilesToCommit, panel);
		//System.out.println("********************* customized commit action called**********************");
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean AutoDetect = store.getBoolean(PreferenceConstants.PREF_AUTO_DETECT_TESTSMELL);
		if(!AutoDetect){
			return defaultCommitDialog;
		}
		
		String cho = store.getString(PreferenceConstants.PREF_AUTO_DETECT_RANGE);
		if (cho.equalsIgnoreCase(PreferenceConstants.PREF_AUTO_DETECT_ONLYNEW)) {
			haveTestSmell = onlyNewTestHaveTestSmell(allFilesToCommit);
		}
		if (cho.equalsIgnoreCase(PreferenceConstants.PREF_AUTO_DETECT_ALLTEST)) {
			haveTestSmell = allTestHaveTestSmell(allFilesToCommit);
		}
		if (haveTestSmell) {
			//show the window
			return CommitWarningMessageDialog.open(
					allFilesToCommit, panel,
					shell,
					"Warning:Committing Smelly Test Classes",
					"You are about to commit Test Classes with test smells, do you want to proceed?");
		}
		return defaultCommitDialog;
	}

	@Override
	public void performAfterCommitTasks(CompositeOperation operation, IRevisionProvider revisionProvider,
			IActionOperation[] dependsOn, IWorkbenchPart part) {
		defaultCommitActionFactory.performAfterCommitTasks(operation, revisionProvider, dependsOn, part);
		
	}
	/**
	 * Given a collection of org.eclipse.core.internal.resources.File, run test smell detection and write result to PluginCandidateProvider
	 * @param allFilesToCommit
	 * @return
	 */
	public boolean allTestHaveTestSmell(Collection allFilesToCommit){
		boolean returnObj = false;
		ArrayList<PluginCandidate> candidates = new ArrayList<PluginCandidate>();
		PluginCandidateProvider.INSTANCE.setPluginCandidates(candidates);//clear the result view
		
		Collection<org.eclipse.core.internal.resources.File> FileList = new ArrayList<org.eclipse.core.internal.resources.File>();
		for (Object object : allFilesToCommit) {
			if(object instanceof org.eclipse.core.internal.resources.File){
				@SuppressWarnings("restriction")
				org.eclipse.core.internal.resources.File file = (org.eclipse.core.internal.resources.File) object;
				IProject project= file.getProject();
				IPath projectPath = file.getLocation();
				IPath objpath = file.getLocation();
				File projectFile = projectPath.toFile();
				File objFile = objpath.toFile();
				List<ClassBean> testClass = TestSmellUtilities.extractTestCases(objFile);
				if (!testClass.isEmpty()) {
					FileList.add(file);
				}
			}
		}
		if (FileList.size() > 0) {
			HashMap<File, List<File>> toBeTest = ExtractProject(FileList);
			int idcounter = 0;
			if(toBeTest == null){
				return returnObj;
			}
			for (Entry<File, List<File>> one : toBeTest.entrySet()) {
				File pFile = one.getKey();
				List<File> tFileList = one.getValue();
				for (File tfile : tFileList) {
					DetectionExample detectionExample = new DetectionExample(pFile, tfile);
					HashMap<TestClassBean, Object> testSmellResult = new HashMap<>();
					detectionExample.prepareForDetection();
					detectionExample.getDetectionResult();
					testSmellResult .putAll(detectionExample.getTestSmellResult());
					candidates = new ArrayList<PluginCandidate>();
					for (TestClassBean key : testSmellResult.keySet()) {
						candidates.add(new PluginCandidate(idcounter, "smelltype", "testSmellSourceEntity", "testSmellTargetClass", key, (HashMap<String, Object>)(testSmellResult.get(key))));
						idcounter ++;
					}
					PluginCandidateProvider.INSTANCE.addPluginCandidates(candidates);
				}
			}
			if(!PluginCandidateProvider.INSTANCE.getPluginCandidates().isEmpty()){
				return true;
			}
		}
		return returnObj;
	}
	/**
	 * extract only new File in the committed file and run test smell detection on them
	 * @param allFilesToCommit
	 * @return true for have test smell.
	 */
	public boolean onlyNewTestHaveTestSmell(Collection allFilesToCommit){
		boolean returnObj = false;
		HashSet<IResource> treatAsEdit = createTreatasEdit(allFilesToCommit);
		Collection newFiles = new HashSet<>();
		for (Object object : allFilesToCommit) {
			ILocalResource local = SVNRemoteStorage.instance().asLocalResource((IResource) object);
			System.out.println(contentStatusAsString(treatAsEdit, local));
			if (contentStatusAsString(treatAsEdit, local).equalsIgnoreCase("New")) {
				newFiles.add(object);
			}
		}
		if(newFiles.size() > 0){
			return allTestHaveTestSmell(newFiles);
		}else{
			return false;
		}
	}
	/**
	 * grouping files according to different project
	 * @param Files
	 * @return HashMap&lt;File, List&lt;File&gt;&gt; key is the project directory and value is a list of file belong to the project
	 */
	public HashMap<File, List<File>> ExtractProject(Collection<org.eclipse.core.internal.resources.File> Files){
		HashMap<File, List<File>> rtHashMap = new HashMap<>();
		HashMap<File, List<File>> tempMap = new HashMap<>();
		for (org.eclipse.core.internal.resources.File file : Files) {
			IProject project= file.getProject();
			IPath projectPath = project.getLocation();
			IPath objpath = file.getLocation();
			File projectFile = projectPath.toFile();
			File objFile = objpath.toFile();
			//System.out.println(projectFile);
			if (tempMap.containsKey(projectFile)) {
				List<File> existingFile = tempMap.get(projectFile);
				existingFile.add(objFile);
				tempMap.put(projectFile, existingFile);
			}else{
				List<File> tmp = new ArrayList<>();
				tmp.add(objFile);
				tempMap.put(projectFile, tmp);
			}
		}
		rtHashMap.putAll(tempMap);
		return rtHashMap;
	}
	/**
	 * Get SVN data
	 * @param resources
	 * @return HashSet&lt;IResource&gt; the resources as considered as edit
	 */
	protected HashSet<IResource> createTreatasEdit(Collection resources) {
		HashSet<IResource> treatAsEdit = new HashSet();
		if (SVNTeamPreferences.getBehaviourBoolean(SVNTeamUIPlugin.instance().getPreferenceStore(), SVNTeamPreferences.BEHAVIOUR_TREAT_REPLACEMENT_AS_EDIT_NAME)) {
			for (Object resource : resources) {
				resource = (IResource)resource;
				ILocalResource local = SVNRemoteStorage.instance().asLocalResource((IResource) resource);
				if (IStateFilter.SF_PREREPLACEDREPLACED.accept(local)) {
					treatAsEdit.add((IResource) resource);
				}				
			}
		}
		return treatAsEdit;
	}
	/**
	 * Get the content of the resource. modified/new
	 * @param treatAsEdit
	 * @param local
	 * @return modified/new
	 */
	protected String contentStatusAsString(HashSet<IResource> treatAsEdit, ILocalResource local) {
		String status = ""; //$NON-NLS-1$
		if (!IStateFilter.ST_NORMAL.equals(local.getTextStatus())) {
			status = SVNUtility.getStatusText(treatAsEdit.contains(local.getResource()) ? IStateFilter.ST_MODIFIED : local.getTextStatus());			
			if (local.isCopied()) {
				status += " (+)"; //$NON-NLS-1$
			}
		}
		if (local.hasTreeConflict() && local.getTreeConflictDescriptor().conflictKind == SVNConflictDescriptor.Kind.CONTENT) {
			if (!"".equals(status)) { //$NON-NLS-1$
				status += ", "; //$NON-NLS-1$
			}
			status += SVNMessages.TreeConflicting;			
		}
		return status;
	}
}
