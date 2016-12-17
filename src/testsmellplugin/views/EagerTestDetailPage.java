package testsmellplugin.views;

import java.awt.Label;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.TestClassBean;
import testsmell.PluginCandidate;
import testsmell.PluginCandidateProvider;

/* This class shows an about box, based on TitleAreaDialog
 */
public class EagerTestDetailPage extends TitleAreaDialog {
	private TestClassBean testClassBean;
	private HashMap<MethodBean, HashMap<ClassBean, ArrayList<MethodBean>>> eagertest;
	private boolean multiClass;
	private int ClassNumber;
	private boolean multiMethod;
	private int MethodNumber;
	private Image image;

	/**
	 * MyTitleAreaDialog constructor
	 * 
	 * @param shell
	 *            the parent shell
	 */
	public EagerTestDetailPage(TestClassBean testClassBean, Object smellData, Shell shell) {
		super(shell);

		// Create the image
		try {
			//image = new Image(null, new FileInputStream("java2s.gif"));
			System.out.println(smellData.getClass());
			this.eagertest = (HashMap<MethodBean, HashMap<ClassBean, ArrayList<MethodBean>>>)smellData;
			this.testClassBean = testClassBean;
			//check the smell data 
		} catch (Exception e) {
			image = null;
			eagertest = null;
			testClassBean = null;
		}
		for(MethodBean testMethod : this.eagertest.keySet()) {
			HashMap<ClassBean, ArrayList<MethodBean>> result = this.eagertest.get(testMethod);
			if (result.entrySet().size() > 1) {
				this.multiClass = true;
				this.MethodNumber = result.entrySet().size();
			}
			for (Entry<ClassBean, ArrayList<MethodBean>> onEntry : result.entrySet() ) {
				if(onEntry.getValue().size() > 1){
					this.multiMethod = true;
				}
			}
				
		}
	}

	/**
	 * Closes the dialog box Override so we can dispose the image we created
	 */
	public boolean close() {
		if (image != null)
			image.dispose();
		if (resizeHasOccurred) {
			
		}
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
		setTitle("Eager Test Information");
		// Set the image
		if (image != null)
			setTitleImage(image);
		if (eagertest != null && testClassBean != null) {
			String str = "";
			if (this.multiClass) {
				str = " " + this.MethodNumber + " Classes in only one method";
			}else if(this.multiMethod){
				str = "more than one production class method in one test method";
			}
			setMessage("Test Class " + 
					testClassBean.getTestCase().getBelongingPackage() + "." +
					testClassBean.getTestCase().getName() + 
					" is testing " + 
					str
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
		composite.setLayout(new GridLayout(1, true));
		//TableViewer tableViewer = new TableViewer(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		if (multiClass) {
			org.eclipse.swt.widgets.Label tableTitle1 = new org.eclipse.swt.widgets.Label(composite, SWT.NULL);
			tableTitle1.setText("Production Classes");
			createProductionClassTable(composite);
		}
		if (multiMethod) {
			org.eclipse.swt.widgets.Label tableTitle2 = new org.eclipse.swt.widgets.Label(composite, SWT.NULL);
			tableTitle2.setText("Production Mehods");
			createProductionMethodTable(composite);
		}
		
		
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
	private void createProductionClassTable(Composite composite){
		// Create a table
				Table table = new Table(composite,SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
//				table.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
				TableLayout layout = new TableLayout();
				layout.addColumnData(new ColumnWeightData(140, true));
				table.setLayout(layout);
				// Create column and show
				TableColumn one = new TableColumn(table, SWT.LEAD);
				one.setWidth(DIALOG_DEFAULT_BOUNDS);
				one.setText("Production Classes");


				//table.setHeaderVisible(true);
				table.setLinesVisible(true);

				// Add some data
				for(MethodBean testMethod : this.eagertest.keySet()) {
					HashMap<ClassBean, ArrayList<MethodBean>> result = this.eagertest.get(testMethod);
					for (ClassBean CBkey : result.keySet()) {
						TableItem item = new TableItem(table, SWT.NONE);
						//item.setText(0,"In you test class method: " + testMethod.getName() + " is testing production class: " + CBkey.getBelongingPackage() +"." + CBkey.getName());
						item.setText(0," " + CBkey.getBelongingPackage() + "." + CBkey.getName());
					}
				}

				
				//one.setToolTipText("refactoring tips");
				one.pack();
				table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				
	}
	private void createProductionMethodTable(Composite composite){
		// Create a table
			Tree tree = new Tree(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
			TableLayout layout = new TableLayout();
			layout.addColumnData(new ColumnWeightData(100, true));
			layout.addColumnData(new ColumnWeightData(140, true));
			layout.addColumnData(new ColumnWeightData(100, true));
			tree.setLayout(layout);
			TreeColumn t1 = new  TreeColumn(tree, SWT.LEFT);
			TreeColumn t2 = new TreeColumn(tree, SWT.LEFT);
			TreeColumn t3 = new TreeColumn(tree, SWT.LEFT);
			t1.setText("Test Method");
			t2.setText("Production Class");
			t3.setText("Method");
			tree.setHeaderVisible(true);
			tree.setLinesVisible(true);
			for(MethodBean testMethod : this.eagertest.keySet()) {
				HashMap<ClassBean, ArrayList<MethodBean>> result = this.eagertest.get(testMethod);
				TreeItem tmItem = new TreeItem(tree, SWT.NONE);
				tmItem.setText(0, testMethod.getName() + "()");
				for (ClassBean CBkey : result.keySet()) {
					TreeItem pcItem = new TreeItem(tmItem, SWT.NONE);
					pcItem.setText(1, CBkey.getBelongingPackage() + "." + CBkey.getName());
					for (MethodBean testedMethod : result.get(CBkey)) {
						TreeItem pmItem = new TreeItem(pcItem, SWT.NONE);
						pmItem.setText(2, testedMethod.getName() + "(" + testedMethod.getParameters().toString().substring(1, testedMethod.getParameters().toString().length()-1) + ")" );
					}
				}
			}
			t1.pack();
			t2.pack();
			t3.pack();
			tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
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
						"Consider only test one productiong class in one test class and test only one production method in one test method");
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
	@Override
	protected boolean isResizable() {
	    return true;
	}
//	@Override
//	protected org.eclipse.swt.graphics.Point getInitialSize(){
//		return new org.eclipse.swt.graphics.Point(450, 300);
//	}

}