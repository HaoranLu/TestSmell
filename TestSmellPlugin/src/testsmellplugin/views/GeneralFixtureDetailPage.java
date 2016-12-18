package testsmellplugin.views;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import it.unisa.scam14.beans.InstanceVariableBean;
import it.unisa.scam14.beans.TestClassBean;

/* This class shows an about box, based on TitleAreaDialog
 */
public class GeneralFixtureDetailPage extends TitleAreaDialog {
	private TestClassBean testClassBean;
	private HashSet<InstanceVariableBean> unusedVariable;
	private Image image;

	/**
	 * MyTitleAreaDialog constructor
	 * 
	 * @param shell
	 *            the parent shell
	 */
	public GeneralFixtureDetailPage(TestClassBean testClassBean, Object smellData, Shell shell) {
		super(shell);

		try {
			//image = new Image(null, new FileInputStream("java2s.gif"));
			System.out.println(smellData.getClass());
			this.unusedVariable = (HashSet<InstanceVariableBean>) smellData;
			this.testClassBean = testClassBean;
		} catch (Exception e) {
			image = null;
			unusedVariable = null;
			testClassBean = null;
		}
	}

	/**
	 * Closes the dialog box Override so we can dispose the image we created
	 */
	public boolean close() {
		if (image != null)
			image.dispose();
		return super.close();
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param parent
	 *            the parent composite
	 * @return Control
	 */
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);

		// Set the title
		setTitle("General Fixture Information");
		// Set the image
		if (image != null)
			setTitleImage(image);
		if (unusedVariable != null && testClassBean != null) {
			setMessage("Test Class " + 
					testClassBean.getTestCase().getBelongingPackage() + 
					testClassBean.getTestCase().getName() + 
					" has " + 
					unusedVariable.size() + 
					" general fixtures"
					, IMessageProvider.INFORMATION);
		}

		return contents;
	}

	/**
	 * Creates the gray area
	 * 
	 * @param parent
	 *            the parent composite
	 * @return Control
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		// Create a table
		Table table = new Table(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(140, true));
		table.setLayout(layout);
		// Create column and show
		TableColumn one = new TableColumn(table, SWT.LEAD);
		one.setWidth(DIALOG_DEFAULT_BOUNDS);
		one.setText("Asssertations");


		//table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Add some data
		for (InstanceVariableBean instanceVariableBean : unusedVariable) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, instanceVariableBean.getType() +" " + instanceVariableBean.getName());
			
		}
		one.setToolTipText("refactoring Tips");
		one.pack();

		//TableViewer tableViewer = new TableViewer(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		
		return composite;
	}

	/**
	 * Creates the buttons for the button bar
	 * 
	 * @param parent
	 *            the parent composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		IcreateButton(parent, "Refacting Tips", false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		
	}

	private Button IcreateButton(Composite parent, String buttonText, boolean defaultButton) {
		// TODO Auto-generated method stub
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(buttonText);
		button.setFont(JFaceResources.getDialogFont());
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				MessageDialog.openInformation(
						parent.getShell(),
						buttonText,
						"In your test class the setup method is too generic and the test methods only access part of the fixture. Consider to refactor the setup method keeping only the part of the fixture shared by all the test methods and move the rest of the fixture in the method that uses it.");
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}

		setButtonLayoutData(button);
		return button;
		
	}

}