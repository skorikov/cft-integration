package de.proskor.fel.ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


import de.proskor.fel.container.EventContainer;
import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.Event;
import de.proskor.fel.event.EventInstance;
import de.proskor.fel.event.EventType;
import de.proskor.fel.impl.EventTypeImpl;
import de.proskor.fel.impl.EventInstanceImpl;

public class FailureEventListCreateEventGui extends Shell {
	private class UserInputData {
		String author, description;
		int cftComboIndex;
	}
	private UserInputData userInputData = null;
	
	private Text textEventName;
	private Text textAuthor;
	private StyledText styledTextDescription;
	private Button btnCancel;
	private Button btnOk;
	private Combo comboCFTs;
	private boolean userAccepted;

	private boolean eventIsValid() {
		String author = textAuthor.getText();
		String description = styledTextDescription.getText();
		
		return (author != "") && (description != "");
	}
	
	public EventType createEvent(final String eventName, ArrayList<EventTypeContainer> possibleContainers, EventTypeContainer defaultSelectedContainer) {
		configContentsForEvent(eventName, possibleContainers, defaultSelectedContainer);
		prepareAndShow();

		if (!userAccepted)
			return null;
		
		String author = userInputData.author;
		String description = userInputData.description;
		EventTypeContainer container = possibleContainers.get(userInputData.cftComboIndex); // Indizes zwischen combo-Box und Arraylist verlaufen gleich. 
		
		// ID & GUID werden von EA zugewie�en und beim Erstellen eines Events hier nicht gesetzt.
		String guid = "";
		int id = -1;
		
		return new EventTypeImpl(eventName, container, author, description, guid, id);
	}

	public EventInstance createEventInstance(EventType event, ArrayList<EventInstanceContainer> possibleContainers, EventInstanceContainer defaultSelectedContainer) {
		configContentsForEvent(event.getName(), possibleContainers, defaultSelectedContainer);
		prepareAndShow();

		if (!userAccepted)
			return null;
		
		EventInstanceContainer cft = possibleContainers.get(userInputData.cftComboIndex); // Indizes zwischen combo-Box und Arraylist verlaufen gleich. 
		String author = userInputData.author;
		String description = userInputData.description;
		String guid = "";
		int id = -1;
		
		return new EventInstanceImpl(event, cft, author, description, guid, id);
	}

	public FailureEventListCreateEventGui() {
		super(SWT.MIN | SWT.TITLE);

		// TODO: KEIN Dispose on close. Manuell durchf�hren nachdem Daten vom Interface gelesen wurden!!
		
		createContents();
	}
	
	private void prepareAndShow() {
		Display display = Display.getDefault();

		checkUserOkOption();
		
		open();
		layout();
		while (!isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void configContentsForEvent(String eventName, ArrayList<? extends EventContainer> possibleContainers, EventContainer defaultSelectedContainer) {
		textEventName.setText(eventName);
		
		String[] items = new String[possibleContainers.size()];
		
		int i=0;
		for(EventContainer container : possibleContainers) {
			items[i] = container.getName();
			i++;
		}
		
		comboCFTs.setItems(items);
		
		if (defaultSelectedContainer != null) 
			comboCFTs.select(possibleContainers.indexOf(defaultSelectedContainer));
		else
			comboCFTs.select(0);
	}
	
	protected void createContents() {
		setText("FEL: Create new Event");
		setSize(388, 275);

		Label lblName = new Label(this, SWT.NONE);
		lblName.setBounds(10, 10, 47, 15);
		lblName.setText("Name:");
		
		textEventName = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		textEventName.setBounds(71, 10, 303, 21);
		
		Label lblCft = new Label(this, SWT.NONE);
		lblCft.setBounds(10, 41, 47, 15);
		lblCft.setText("CFT:");
		
		comboCFTs = new Combo(this, SWT.READ_ONLY);
		comboCFTs.setBounds(71, 38, 303, 23);
		
		Label lblAuthor = new Label(this, SWT.NONE);
		lblAuthor.setBounds(10, 70, 47, 15);
		lblAuthor.setText("Author:");
		
		textAuthor = new Text(this, SWT.BORDER);
		textAuthor.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkUserOkOption();
			}
		});
		textAuthor.setBounds(71, 67, 303, 21);
		
		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setBounds(10, 99, 63, 15);
		lblDescription.setText("Description:");
		
		styledTextDescription = new StyledText(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.WRAP);
		styledTextDescription.addExtendedModifyListener(new ExtendedModifyListener() {
			public void modifyText(ExtendedModifyEvent event) {
				checkUserOkOption();
			}
		});
		styledTextDescription.setBounds(10, 120, 364, 89);
		
		btnOk = new Button(this, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				finishAndClose(true);
			}
		});
		btnOk.setBounds(218, 217, 75, 25);
		btnOk.setText("OK");
		
		btnCancel = new Button(this, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				finishAndClose(false);
			}
		});
		btnCancel.setBounds(299, 217, 75, 25);
		btnCancel.setText("Cancel");
	}
	
	private void checkUserOkOption() {
		boolean canCommit = eventIsValid();
		
		btnOk.setEnabled(canCommit);
	}
	
	private void finishAndClose(boolean userAccepted) {
		this.userAccepted = userAccepted;
		
		if (userAccepted) {
			userInputData = new UserInputData();
			userInputData.author = textAuthor.getText();
			userInputData.description = styledTextDescription.getText();
			userInputData.cftComboIndex = comboCFTs.getSelectionIndex();
		}
		
		close();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
