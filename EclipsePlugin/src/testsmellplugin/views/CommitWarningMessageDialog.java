/**
 * 
 */
package testsmellplugin.views;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.svn.core.operation.CompositeOperation;
import org.eclipse.team.svn.ui.extension.factory.ICommentDialogPanel;
import org.eclipse.team.svn.ui.extension.factory.ICommitDialog;
import org.eclipse.team.svn.ui.extension.impl.DefaultCommitActionFactory;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author luhaoran
 *
 */
public class CommitWarningMessageDialog extends MessageDialog implements ICommitDialog {
	private Collection allFilesToCommit;
	private ICommentDialogPanel panel;
	private Shell parent;
	/**
	 * open the warning window
	 * @param allFilesToCommit
	 * @param panel I don't know what this is but keep it is better than remove it
	 * @param parent
	 * @param title
	 * @param message
	 * @return the warning window will be return but not opened.
	 */
	public static CommitWarningMessageDialog open(Collection allFilesToCommit, ICommentDialogPanel panel,Shell parent, String title,
			String message){
		String[] dialogButtonLabels = {"Proceed", "Test Smells"};
		CommitWarningMessageDialog dialog = new CommitWarningMessageDialog(allFilesToCommit, panel, parent, title, getDefaultImage(), message, WARNING, 0, dialogButtonLabels);
		return dialog;
	}
	public CommitWarningMessageDialog(Collection allFilesToCommit, ICommentDialogPanel panel, Shell parentShell, String dialogTitle, Image dialogTitleImage,
			String dialogMessage, int dialogImageType, int defaultIndex, String[] dialogButtonLabels) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, defaultIndex, dialogButtonLabels);
		this.parent = parentShell;
		this.panel = panel;
		this.allFilesToCommit = allFilesToCommit;
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getMessage() {
		String message = this.panel.getMessage();
		return message;
	}
	/**
	 * open different view according to different button and pass the return code
	 */
	protected void buttonPressed(int buttonId) {
		setReturnCode(buttonId);
		if (buttonId == CANCEL) {
			close();
			IWorkbenchPage page= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart viewPart = page.findView(TestSmellResult.ID);
			
			if (viewPart != null) {
				page.hideView(viewPart);
			}
			try {
				setReturnCode(-1);
				close();
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TestSmellResult.ID);
				IViewPart vpIViewPart = page.findView(TestSmellResult.ID);
				vpIViewPart.setFocus();
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}else if (buttonId == OK) {
			DefaultCommitActionFactory defaultCommitActionFactory = new DefaultCommitActionFactory();
			ICommitDialog defaultCommitDialog = defaultCommitActionFactory.getCommitDialog(getParentShell(), this.allFilesToCommit, this.panel);
			int option = defaultCommitDialog.open();
			setReturnCode(option);
		}
		close();
		
	}
	public boolean close() {
		// ESC pressed? (ESC handling is hardcoded in SWT and corresponding event is not translated to the user nor as "KeyEvent" nor as "button pressed")
		
		return super.close();
	}

}

