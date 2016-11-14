package testsmellplugin.views;

import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TreeColumn;

public class DetailWizardPage extends WizardPage {

	protected DetailWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
			Composite container = new Composite(parent, SWT.NULL);
			setControl(container);
			GridLayout layout = new GridLayout();
			container.setLayout(layout);
			
			
			TreeViewer viewer = new TreeViewer(container, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
			TableLayout tableLayout = new TableLayout();
			tableLayout.addColumnData(new ColumnPixelData(200, true));
			tableLayout.addColumnData(new ColumnPixelData(400, true));
			viewer.getTree().setLayout(tableLayout);
			viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
			new TreeColumn(viewer.getTree(), SWT.LEFT).setText("smell type");
			new TreeColumn(viewer.getTree(), SWT.LEFT).setText("refactoring tips");
			viewer.expandAll();
			viewer.getTree().setLinesVisible(true);
			viewer.getTree().setHeaderVisible(true);
	}

}
