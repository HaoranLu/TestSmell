package testsmellplugin.views;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.*;


import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.TestClassBean;
import it.unisa.scam14.testSmellRules.SensitiveEquality;
import testsmell.PluginCandidate;
import testsmell.PluginCandidateProvider;

import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.core.runtime.IAdaptable;


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

public class TestSmellResult extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "testsmellplugin.views.TestSmellResult";
	public static final String MESSAGE_DIALOG_TITLE = "TestSmell Result";
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private PluginCandidate[] PluginCandidateTable;
	private TreeViewer treeViewer;
	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;
		
		public TreeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return getName();
		}
		public <T> T getAdapter(Class<T> key) {
			return null;
		}
	}
	
	class TreeParent extends TreeObject {
		private ArrayList children;
		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	/*class ViewContentProvider implements ITreeContentProvider {
		private TreeParent invisibleRoot;

		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
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
	
	

 * We will set up a dummy model to initialize tree heararchy.
 * In a real code, you will connect to a real model and
 * expose its hierarchy.
 
		private void initialize() {
			TreeObject to1 = new TreeObject("Leaf 1");
			TreeObject to2 = new TreeObject("Leaf 2");
			TreeObject to3 = new TreeObject("Leaf 3");
			TreeParent p1 = new TreeParent("Parent 1");
			p1.addChild(to1);
			p1.addChild(to2);
			p1.addChild(to3);
			
			TreeObject to4 = new TreeObject("Leaf 4");
			TreeParent p2 = new TreeParent("Parent 2");
			p2.addChild(to4);
			
			TreeParent root = new TreeParent("Root");
			root.addChild(p1);
			root.addChild(p2);
			
			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(root);
		}
	}*/
class ViewContentProvider implements ITreeContentProvider {
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		if (((TreeViewer)v).getTree() != null && ((TreeViewer)v).getTree().getChildren() != null) {
			for (Control item : ((TreeViewer)v).getTree().getChildren()) {
				if (item != null && !item.isDisposed()) {
					item.dispose();
				}
			}
		}
	}
	public void dispose() {
	}
	public Object[] getElements(Object parent) {
		if (parent instanceof List) {
			return ((List)parent).toArray();
		}
		else {
			return new PluginCandidate[] {};
		}
	}
	public Object[] getChildren(Object arg0) {
		if (arg0 instanceof PluginCandidate) {
			//convert the TestClassBean-TestSmell map into array
			HashMap<String, Object> testSmell = ((PluginCandidate) arg0).getTestSmell();
			ArrayList<Object> rt = new ArrayList<>();
			for (java.util.HashMap.Entry<String, Object> entry : testSmell.entrySet()) {
				ConcurrentHashMap<TestClassBean, Object> tmHashMap = new ConcurrentHashMap<>();
				tmHashMap.put(((PluginCandidate) arg0).getTestClass(),entry);
				rt.add(tmHashMap);
			}
			return  rt.toArray();
		}else {
			return new PluginCandidate[] {};
		}
	}
	public Object getParent(Object arg0) {
		if(arg0 instanceof HashMap<?, ?>) {
			//PluginCandidate who has the same TestClassBean is a testsmell's parent 
			HashMap<TestClassBean, Object> tmHashMap = (HashMap<TestClassBean, Object>)arg0;
			for (PluginCandidate pluginCandidate : PluginCandidateTable) {
				if (tmHashMap.containsKey(pluginCandidate.getTestClass())) {
					return pluginCandidate;
				}
			}
		}
		return null;
	}
	public boolean hasChildren(Object arg0) {
		return getChildren(arg0).length > 0;
	}
}
class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
	public String getColumnText(Object obj, int index) {
		if (obj instanceof PluginCandidate) {
			PluginCandidate entry = (PluginCandidate) obj;
			switch (index) {
			case 0:
				return entry.getTestClass().getTestCase().getBelongingPackage() + entry.getTestClass().getTestCase().getName();
			default:
				return "";
			}
		}
		else if(obj instanceof ConcurrentHashMap<?, ?>) {
			//get the test smell type inside the <String TestSmell type, Object Testsmell data> 
			//ConcurrentHashMap is not necessarily needed,
			@SuppressWarnings("unchecked")
			ConcurrentHashMap<TestClassBean, Object> entry = (ConcurrentHashMap<TestClassBean, Object>)obj;
			System.out.println("lable provider instance of hashmap: " + entry.toString()); 
			switch(index) {
			case 0:
				return "";
			case 1:
				for (TestClassBean testClassBean : entry.keySet()) {
					@SuppressWarnings("unchecked")
					java.util.HashMap.Entry<String, Object> tmp = (java.util.HashMap.Entry<String, Object>)(entry.get(testClassBean));
					return tmp.getKey();
				}
				return "not found";
			default:
				return "333";
			}
		}
		else {
			return "shenmedoubushi";
		}
	}
	public Image getColumnImage(Object obj, int index) {
		Image image = null;
		return image;
	}
	public Image getImage(Object obj) {
		return null;
	}
}
/*	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}*/

/**
	 * The constructor.
	 */
	public TestSmellResult() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
/*	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		
	viewer.setContentProvider(new ViewContentProvider());
	viewer.setInput(getViewSite());
	viewer.setLabelProvider(new ViewLabelProvider());
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}*/
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		treeViewer.setContentProvider(new ViewContentProvider());
		treeViewer.setLabelProvider(new ViewLabelProvider());
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(20, true));
		treeViewer.getTree().setLayout(layout);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		new TreeColumn(treeViewer.getTree(), SWT.LEFT).setText("Test Classes");
		new TreeColumn(treeViewer.getTree(), SWT.LEFT).setText("Test Smells");
		//new TreeColumn(treeViewer.getTree(), SWT.LEFT).setText("Detail");
		//create the detail button column. The Map of Object-Button have to be disposed.
		TreeColumn tc3 = new TreeColumn(treeViewer.getTree(), SWT.LEFT);
		tc3.setText("Detail");
		TreeViewerColumn detailColumn = new TreeViewerColumn(treeViewer, tc3);
		detailColumn.setLabelProvider(new ColumnLabelProvider(){
			 Map<Object, Button> buttons = new HashMap<Object, Button>();
	            @Override
	            public void update(ViewerCell cell) {

	                TreeItem item = (TreeItem) cell.getItem();
	                Button button;
	                if(buttons.containsKey(cell.getElement()) && !buttons.get(cell.getElement()).isDisposed())
	                {
	                    button = buttons.get(cell.getElement());
	                    button.setData(cell.getElement());
	                }
	                else if(!(cell.getElement() instanceof PluginCandidate))
	                {
	                    button = new Button((Composite) cell.getViewerRow().getControl(),SWT.NONE);
	                    button.setText("Detail");
	                    buttons.put(cell.getElement(), button);
	                    button.setData(cell.getElement());
	                }else {
						button = null;
					}
	                if(button != null){
	                	button.addListener(SWT.Selection, new Listener() {
		                    public void handleEvent(Event e) {
		                      switch (e.type) {
		                      case SWT.Selection:
		                    	System.out.println("button pressed at " + button.getData().toString());
		                    	openDetailWindow(button.getData(), PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		                    	//AssertationRouletteDetailPage assertationRouletteDetailPage =new AssertationRouletteDetailPage(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		                    	//assertationRouletteDetailPage.open();
		                    	/*DetailWizard dwDetailWizard = new DetailWizard();
		                    	WizardDialog dialog = new WizardDialog(
		                    			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		                    			dwDetailWizard);
		                    	dialog.open();*/
		                        break;
		                      }
		                    }
		                  });

		                TreeEditor editor = new TreeEditor(item.getParent());
		               // TableEditor editor = new TableEditor(item.getParent());
		                editor.grabHorizontal  = true;
		                editor.grabVertical = true;
		                editor.setEditor(button , item, cell.getColumnIndex());
		                editor.layout();
	                }
	                
	            }
		});
	
		treeViewer.expandAll();
		for (int i = 0, n = treeViewer.getTree().getColumnCount(); i < n; i++) {
			treeViewer.getTree().getColumn(i).pack();
		}
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.setInput(PluginCandidateProvider.INSTANCE.getPluginCandidates());
	}
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				TestSmellResult.this.fillContextMenu(manager);
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
			"Test Smell Result",
			message);
	}
	private boolean openDetailWindow(Object testSmellData, Shell shell){
		if(testSmellData == null || shell == null){
			return false;
		}
		ConcurrentHashMap<TestClassBean, Object> TestSmell = (ConcurrentHashMap<TestClassBean, Object>)testSmellData;
		for (Map.Entry<TestClassBean, Object> testSmell : TestSmell.entrySet()) {
			TestClassBean testClassBean = testSmell.getKey();
			HashMap.Entry<String, Object> OneSmell = (HashMap.Entry<String, Object>)testSmell.getValue();
				String smellType = OneSmell.getKey();
				Object smellData = OneSmell.getValue();
				if (smellType.equalsIgnoreCase("AssertationRoulette")) {
					AssertationRouletteDetailPage assertationRouletteDetailPage =new AssertationRouletteDetailPage(testClassBean,smellData,PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
                	assertationRouletteDetailPage.open();
				}else if (smellType.equalsIgnoreCase("EagerTest")){
					EagerTestDetailPage eagerTestDetailPage = new EagerTestDetailPage(testClassBean, smellData, shell);
					eagerTestDetailPage.open();
				}else if (smellType.equalsIgnoreCase("MysteryGuest")) {
					MysteryGuestDetailPage mysteryGuestDetailPage = new MysteryGuestDetailPage(testClassBean, smellData, shell);
					mysteryGuestDetailPage.open();
				}else if (smellType.equalsIgnoreCase("GeneralFixture")) {
					GeneralFixtureDetailPage generalFixtureDetailPage = new GeneralFixtureDetailPage(testClassBean, smellData, shell);
					generalFixtureDetailPage.open();
				}else if (smellType.equalsIgnoreCase("SensitiveEquality")) {
					SensitiveEqualityDetailPage sensitiveEqualityDetailPage = new SensitiveEqualityDetailPage(testClassBean, smellData, shell);
					sensitiveEqualityDetailPage.open();
				}
		}
		return true;
//		try {
//			ConcurrentHashMap<TestClassBean, Object> TestSmell = (ConcurrentHashMap<TestClassBean, Object>)testSmellData;
//			for (Map.Entry<TestClassBean, Object> testSmell : TestSmell.entrySet()) {
//				TestClassBean testClassBean = testSmell.getKey();
//				HashMap<String, Object> OneSmell = (HashMap<String, Object>)testSmell.getValue();
//				for (Map.Entry<String, Object> SpecifySmell : OneSmell.entrySet()) {
//					String smellType = SpecifySmell.getKey();
//					Object smellData = SpecifySmell.getValue();
//					if (smellType.equalsIgnoreCase("AssertationRoulette")) {
//						AssertationRouletteDetailPage assertationRouletteDetailPage =new AssertationRouletteDetailPage(smellData,PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
//                    	assertationRouletteDetailPage.open();
//					}
//				}
//			}
//			
//			return true;
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println(e.getStackTrace());
//			return false;
//		}
		
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
}
