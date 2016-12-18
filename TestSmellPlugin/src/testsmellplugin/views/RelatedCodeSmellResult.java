package testsmellplugin.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import it.unisa.scam14.beans.ClassBean;
import testsmell.PluginCandidateProvider;
import testsmell.relatedCodeSmell.codeSmellResult;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.SWT;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class RelatedCodeSmellResult extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "testsmellplugin.views.RelatedCodeSmellResult";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	 
	class TreeObject implements IAdaptable {
		private String ProductionClass;
		private String CodeSmell;
		private TreeParent parent;
		
		public TreeObject(String className, String smell) {
			this.ProductionClass = className;
			this.CodeSmell = smell;
		}
		public String getProductionClass() {
			return ProductionClass;
		}
		public String getCodeSmell() {
			return CodeSmell;
		}
		
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return "ProductionClass: " + ProductionClass + ", CodeSmell: " + CodeSmell + " Parent is " + parent;
		}
		public <T> T getAdapter(Class<T> key) {
			return null;
		}
	}
	
	class TreeParent extends TreeObject {
		private ArrayList children;
		private String TestClass;
		private String TestSmell;
		public TreeParent(String testclass, String testsmell) {
			super(testclass, testsmell);
			this.TestClass = testclass;
			this.TestSmell = testsmell;
			children = new ArrayList();
		}
		public void addChild(Object child){
			children.add(child);
			if(child instanceof TreeParent){
				((TreeParent)child).setParent(this);
			}else{
				((TreeObject)child).setParent(this);
			}
		}
		public void removeChild(TreeParent child){
			children.remove(child);
			child.setParent(null);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public Object[] getChildren() {
			return children.toArray( new Object[children.size()]);
		}
//		public TreeObject [] getChildren() {
//			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
//		}
		public boolean hasChildren() {
			return children.size()>0;
		}
		/**
		 * @return the testClass
		 */
		public String getTestClass() {
			return TestClass;
		}
		/**
		 * @param testClass the testClass to set
		 */
		public void setTestClass(String testClass) {
			TestClass = testClass;
		}
		/**
		 * @return the testSmell
		 */
		public String getTestSmell() {
			return TestSmell;
		}
		/**
		 * @param testSmell the testSmell to set
		 */
		public void setTestSmell(String testSmell) {
			TestSmell = testSmell;
		}
		public String toString(){
			return "TestClass: " + TestClass + " TestSmell: " + TestSmell; 
		}
	}

	class ViewContentProvider implements ITreeContentProvider {
		private TreeParent[] invisibleRoot;

		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return invisibleRoot;
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}
/*
 * We will set up a dummy model to initialize tree heararchy.
 * In a real code, you will connect to a real model and
 * expose its hierarchy.
 */
		private void initialize() {
			List<codeSmellResult> results = PluginCandidateProvider.INSTANCE.getCodeSmellResults();
//			TreeObject to1 = new TreeObject("pclass1", "code smell1");
//			TreeObject to2 = new TreeObject("pclass1", "code smell2");
//			TreeObject to3 = new TreeObject("pclass1", "code smell3");
//			TreeParent p1 = new TreeParent("tclass1", "test smell1");
//			p1.addChild(to1);
//			p1.addChild(to2);
//			p1.addChild(to3);
//			
//			TreeObject to4 = new TreeObject("pclass2","codesmell4");
//			TreeParent p2 = new TreeParent("tclass2", "test smell2");
//			p2.addChild(to4);
			
//			TreeParent root = new TreeParent("Root","root");
//			root.addChild(p1);
//			root.addChild(p2);
			ArrayList<TreeParent> treeParents = new ArrayList<>();
			for (codeSmellResult csr : results) {
				String TestClass = csr.getTestClass().getBelongingPackage() + "." + csr.getTestClass().getName();
				String TestSmell = csr.getTestSmellType();
				TreeParent treeParent = new TreeParent(TestClass, TestSmell);
				if (!csr.getCodeSmellResult().isEmpty()) {
					for (Entry<String, ClassBean> codeSmell : csr.getCodeSmellResult().entrySet()) {
						String Class = codeSmell.getValue().getBelongingPackage() + "." + codeSmell.getValue().getName();
						String smell = codeSmell.getKey();
						TreeObject treeObject = new TreeObject(Class, smell);
						treeParent.addChild(treeObject);
					}
				}
				
				treeParents.add(treeParent);
			}
			invisibleRoot = treeParents.toArray(new TreeParent[treeParents.size()]);
//			invisibleRoot = new TreeParent("Root","root");
//			invisibleRoot.addChild(p1);
//			invisibleRoot.addChild(p2);
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider{

		public String getColumnText(Object object, int index){
			//System.out.println("Object: " +object );
			if (object instanceof TreeParent) {
				TreeParent tParent = (TreeParent)object;
				//System.out.println("tree parent: " + tParent);
				switch (index) {
				case 0:
					return tParent.getTestClass();
				case 1:
					return tParent.getTestSmell();
				default:
					return "";
				}
			}else if(object instanceof TreeObject){
				TreeObject treeObject = (TreeObject)object;
				//System.out.println("tree object: " + treeObject);
				switch (index) {
				case 2:
					return treeObject.getCodeSmell();
				case 3:
					return treeObject.getProductionClass();
				default:
					return "";
				}
			}else {
				return "shenmedoubushi";
			}
		}
//		public String getText(Object obj) {
//			return obj.toString();
//		}
//		public Image getImage(Object obj) {
//			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
//			if (obj instanceof TreeParent)
//			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
//			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
//		}
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}
	}

/**
	 * The constructor.
	 */
	public RelatedCodeSmellResult() {
	}
	public RelatedCodeSmellResult(Object relatedCodeSmell) {
		
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		drillDownAdapter = new DrillDownAdapter(viewer);
		
	viewer.setContentProvider(new ViewContentProvider());
	viewer.setLabelProvider(new ViewLabelProvider());
	TableLayout layout = new TableLayout();
	layout.addColumnData(new ColumnWeightData(40, true));
	layout.addColumnData(new ColumnWeightData(20, true));
	layout.addColumnData(new ColumnWeightData(20, true));
	layout.addColumnData(new ColumnWeightData(40, true));
	viewer.getTree().setLayout(layout);
	viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
	new TreeColumn(viewer.getTree(), SWT.LEFT).setText("Test Class");
	new TreeColumn(viewer.getTree(), SWT.LEFT).setText("Test Smell");
	new TreeColumn(viewer.getTree(), SWT.LEFT).setText("Related Code Smell");
	new TreeColumn(viewer.getTree(), SWT.LEFT).setText("class");

	viewer.setInput(getViewSite());//be careful of the position of this method!!!!!! Second time!!!Waste me 3 hours to find the issue
	viewer.expandAll();
	for (int i = 0, n = viewer.getTree().getColumnCount(); i < n; i++) {
		viewer.getTree().getColumn(i).pack();
	}
	viewer.getTree().setLinesVisible(true);
	viewer.getTree().setHeaderVisible(true);
		getSite().setSelectionProvider(viewer);
		//makeActions();
		//hookContextMenu();
		//hookDoubleClickAction();
		//contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				RelatedCodeSmellResult.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Related Code Smell",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
