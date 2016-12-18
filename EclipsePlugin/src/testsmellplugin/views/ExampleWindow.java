package testsmellplugin.views;


import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.io.*;

/**
 * This class demonstrates JFace's TitleAreaDialog class
 */
public class ExampleWindow extends ApplicationWindow {
  /**
   * ShowCustomDialog constructor
   */
  public ExampleWindow() {
	  super(null);
  }

  /**
   * Runs the application
   */
  public void run() {
    // Don't return from open() until window closes
    setBlockOnOpen(true);

    // Open the main window
    open();

    // Dispose the display
    Display.getCurrent().dispose();
  }

  /**
   * Creates the main window's contents
   * 
   * @param parent the main window
   * @return Control
   */
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, true));

    // Create the button
    Button show = new Button(composite, SWT.NONE);
    show.setText("Show");

    final Shell shell = parent.getShell();

    // Display the TitleAreaDialog
    show.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        // Create and show the dialog
        MyTitleAreaDialog dlg = new MyTitleAreaDialog(shell);
        dlg.open();
      }
    });

    parent.pack();
    return composite;
  }

  /**
   * The application entry point
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    new ExampleWindow().run();
  }
}


/**
 * This class shows an about box, based on TitleAreaDialog
 */
class MyTitleAreaDialog extends TitleAreaDialog {
  // The image to display
  private Image image;

  /**
   * MyTitleAreaDialog constructor
   * 
   * @param shell the parent shell
   */
  public MyTitleAreaDialog(Shell shell) {
    super(shell);

    // Create the image
    try {
      image = new Image(null, new FileInputStream("java2s.gif"));
    } catch (FileNotFoundException e) {
      // Ignore
    }
  }

  /**
   * Closes the dialog box Override so we can dispose the image we created
   */
  public boolean close() {
    if (image != null) image.dispose();
    return super.close();
  }

  /**
   * Creates the dialog's contents
   * 
   * @param parent the parent composite
   * @return Control
   */
  protected Control createContents(Composite parent) {
    Control contents = super.createContents(parent);

    // Set the title
    setTitle("About This Application");

    // Set the message
    setMessage("This is a JFace dialog", IMessageProvider.INFORMATION);

    // Set the image
    if (image != null) setTitleImage(image);

    return contents;
  }

  /**
   * Creates the gray area
   * 
   * @param parent the parent composite
   * @return Control
   */
  protected Control createDialogArea(Composite parent) {
    Composite composite = (Composite) super.createDialogArea(parent);

    // Create a table
    Table table = new Table(composite, SWT.FULL_SELECTION | SWT.BORDER);
    table.setLayoutData(new GridData(GridData.FILL_BOTH));

    // Create two columns and show
    TableColumn one = new TableColumn(table, SWT.LEFT);
    one.setText("Real Name");

    TableColumn two = new TableColumn(table, SWT.LEFT);
    two.setText("Preferred Name");

    table.setHeaderVisible(true);

    // Add some data
    TableItem item = new TableItem(table, SWT.NONE);
    item.setText(0, "Robert Harris");
    item.setText(1, "Bobby");

    item = new TableItem(table, SWT.NONE);
    item.setText(0, "Robert Warner");
    item.setText(1, "Rob");

    item = new TableItem(table, SWT.NONE);
    item.setText(0, "Gabor Liptak");
    item.setText(1, "Gabor");

    one.pack();
    two.pack();

    return composite;
  }

  /**
   * Creates the buttons for the button bar
   * 
   * @param parent the parent composite
   */
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
  }
}