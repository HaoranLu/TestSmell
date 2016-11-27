package testsmellplugin.views;


import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.*;

import testsmell.PluginCandidate;
import testsmell.PluginCandidateProvider;

import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;


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

public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "testsmellplugin.views.SampleView";

	private TableViewer viewer;
	private TableViewer tableViewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private PluginCandidate[] PluginCandidateTable;
	private IJavaProject selectedProject;
	private IJavaProject activeProject;
	private IPackageFragmentRoot selectedPackageFragmentRoot;
	private IPackageFragment selectedPackageFragment;
	private ICompilationUnit selectedCompilationUnit;
	private IType selectedType;
	
	//private CandidateTestSmell[] CandidateTestSmellTable;
	 

	class ViewContentProvider implements IStructuredContentProvider{

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			// This will dispose of all the control button that were created previously
		    /*if (((TableViewer)viewer).getTable() != null && ((TableViewer)viewer).getTable().getChildren() != null) {
		        for (Control item : ((TableViewer)viewer).getTable().getChildren()) {
		            // at this point there are no other controls embedded in the viewer, however different instances may require more checking of the controls here.
		            if ((item != null) && (!item.isDisposed())) {  
		                item.dispose();
		            }
		        }
		    }*/

		}
		public void dispose() {
		}
		/*public Object[] getElements(Object inputElement) {
			if(inputElement!=null) {
				if (inputElement instanceof ArrayList<?>) {
					List<PluginCandidate> lPluginCandidates = (ArrayList<PluginCandidate>)inputElement;
					//List<byte[]>  taskList = (ArrayList<byte[]>)obj;
					return lPluginCandidates.toArray(new Object[lPluginCandidates.size()]);
				}
			}
			else {
				return new PluginCandidate[] {};
			}
		}*/
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List)inputElement).toArray();
			}
			else {
				return new Object[0];
			}
		}
		
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			PluginCandidate pCandidate = (PluginCandidate) obj;
			switch (index) {
			case 1:
				return pCandidate.getTestSmellType();
			case 0:
				return pCandidate.getTestSmellSourceEntity();
			case 2:
				return "222";//pCandidate.getTestSmellTargetClass();
			case 3:
				return "column more";
			default:
				return "123";
			}
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return null;
			//return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
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
					action1.setEnabled(true);
					action2.setEnabled(true);
				}
			}
		}
	};
/**
	 * The constructor.
	 */
	public SampleView() {
	}

	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(new ViewContentProvider());
		//tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new ViewLabelProvider());
		//tableViewer.setInput(getViewSite());
		
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(40, true));
		layout.addColumnData(new ColumnWeightData(20, true));
		tableViewer.getTable().setLayout(layout);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		TableColumn column0 = new TableColumn(tableViewer.getTable(),SWT.NONE);
		column0.setText("TestSmell Type");
		column0.setResizable(true);
		column0.pack();
		TableColumn column1 = new TableColumn(tableViewer.getTable(),SWT.NONE);
		column1.setText("Source Entity");
		column1.setResizable(true);
		column1.pack();
		TableColumn column2 = new TableColumn(tableViewer.getTable(),SWT.NONE);
		column2.setText("Target Class");
		column2.setResizable(true);
		column2.pack();
		//the detail button column
		TableColumn column3 = new TableColumn(tableViewer.getTable(),SWT.LEFT);
		column3.setText("About");
		column3.setResizable(true);
		column3.pack();
		TableColumn column4 = new TableColumn(tableViewer.getTable(), SWT.NONE);
		column4.setText("More");
		TableViewerColumn actionsNameCol = new TableViewerColumn(tableViewer, column4);
        actionsNameCol.setLabelProvider(new ColumnLabelProvider(){
            //make sure you dispose these buttons when viewer input changes
            Map<Object, Button> buttons = new HashMap<Object, Button>();
            @Override
            public void update(ViewerCell cell) {

                TableItem item = (TableItem) cell.getItem();
                Button button;
                if(buttons.containsKey(cell.getElement()))
                {
                    button = buttons.get(cell.getElement());
                    button.setData(cell.getElement());
                }
                else
                {
                    button = new Button((Composite) cell.getViewerRow().getControl(),SWT.NONE);
                    button.setText("Detail");
                    buttons.put(cell.getElement(), button);
                    button.setData(cell.getElement());
                }
                button.addListener(SWT.Selection, new Listener() {
                    public void handleEvent(Event e) {
                      switch (e.type) {
                      case SWT.Selection:
                    	System.out.println("button pressed at " + button.getData().toString());
                    	DetailWizard dwDetailWizard = new DetailWizard();
                    	WizardDialog dialog = new WizardDialog(
                    			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    			dwDetailWizard);
                    	dialog.open();
                        break;
                      }
                    }
                  });

                TableEditor editor = new TableEditor(item.getParent());
                editor.grabHorizontal  = true;
                editor.grabVertical = true;
                editor.setEditor(button , item, cell.getColumnIndex());
                editor.layout();
            }

        });
		tableViewer.setInput(PluginCandidateProvider.INSTANCE.getPluginCandidates());//remember to write behind the table column ... wtf
		/*tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				tableViewer.getTable().setMenu(null);
				IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
				if(selection instanceof IStructuredSelection) {
					IStructuredSelection structuredSelection = (IStructuredSelection)selection;
					Object[] selectedItems = structuredSelection.toArray();
					if(selection.getFirstElement() instanceof MoveMethodCandidateRefactoring && selectedItems.length == 1) {
						MoveMethodCandidateRefactoring candidateRefactoring = (MoveMethodCandidateRefactoring)selection.getFirstElement();
						tableViewer.getTable().setMenu(getRightClickMenu(tableViewer, candidateRefactoring));
					}
				}
			}
		});
*/
		tableViewer.setColumnProperties(new String[] {"type", "source", "target"});
		

		getSite().setSelectionProvider(tableViewer);
		/*tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				RowData rowData = (RowData)selection.getFirstElement();
			}
		});*/
		makeActions();
		hookDoubleClickAction();
		contributeToActionBars();
		hookContextMenu();
		//getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(selectionListener);
		
//		JavaCore.addElementChangedListener(new ElementChangedListener());
		/*getSite().getWorkbenchWindow().getWorkbench().getOperationSupport().getOperationHistory().addOperationHistoryListener(new IOperationHistoryListener() {
			public void historyNotification(OperationHistoryEvent event) {
				int eventType = event.getEventType();
				if(eventType == OperationHistoryEvent.UNDONE  || eventType == OperationHistoryEvent.REDONE ||
						eventType == OperationHistoryEvent.OPERATION_ADDED || eventType == OperationHistoryEvent.OPERATION_REMOVED) {
					if(activeProject != null && CompilationUnitCache.getInstance().getAffectedProjects().contains(activeProject)) {
						applyRefactoringAction.setEnabled(false);
						saveResultsAction.setEnabled(false);
						//evolutionAnalysisAction.setEnabled(false);
						packageExplorerAction.setEnabled(false);
					}
				}
			}
		});*/

	}


	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, tableViewer);
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
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
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
				ISelection selection = tableViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			tableViewer.getControl().getShell(),
			"Sample View",
			message);
	}
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}
	public void dispose() {
		super.dispose();
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(selectionListener);
	}

	@Override
	public Object getAdapter(Class arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
