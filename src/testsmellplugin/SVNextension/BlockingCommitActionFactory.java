package testsmellplugin.SVNextension;

import java.util.Collection;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.svn.ui.extension.factory.ICommentDialogPanel;
import org.eclipse.team.svn.ui.extension.factory.ICommitDialog;
import org.eclipse.team.svn.ui.extension.impl.DefaultCommitActionFactory;

public class BlockingCommitActionFactory extends DefaultCommitActionFactory {
	private static final String className = BlockingCommitActionFactory.class.getName();

	/* (non-Javadoc)
	 * override to block the commit and performs TestSmell detection
	 * @see org.eclipse.team.svn.ui.extension.impl.DefaultCommitActionFactory#getCommitDialog(org.eclipse.swt.widgets.Shell, java.util.Collection, org.eclipse.team.svn.ui.extension.factory.ICommentDialogPanel)
	 */
	@Override
	public ICommitDialog getCommitDialog(Shell shell, Collection allFilesToCommit, ICommentDialogPanel commentPanel) {
		
			return super.getCommitDialog(shell, allFilesToCommit, commentPanel);

		//return super.getCommitDialog(shell, allFilesToCommit, commentPanel);
	}
	
	

}
