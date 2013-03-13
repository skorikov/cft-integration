package de.proskor.cft.merge.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.proskor.cft.Repository;

public class MergeDialog {
	public void show(Repository repository) {
		Display display = new Display();
		Shell shell = this.createShell(display, new RepositoryWrapper(repository));
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private Shell createShell(Display display, RepositoryWrapper repository) {
		final Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE);
		shell.setText("Merge");
		final GridLayout shellLayout = new GridLayout();
		shellLayout.numColumns = 2;
		shell.setLayout(shellLayout);

		final Label mergeLabel = new Label(shell, SWT.NONE);
		mergeLabel.setText("Merge");
		mergeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Combo left = new Combo(shell, SWT.READ_ONLY);
		left.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		for (final ComponentWrapper component : repository.getComponents()) {
			left.add(component.getName());
		}

		final Label withLabel = new Label(shell, SWT.NONE);
		withLabel.setText("with");
		withLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Combo right = new Combo(shell, SWT.READ_ONLY);
		right.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		for (final ComponentWrapper component : repository.getComponents()) {
			right.add(component.getName());
		}

		final Label intoLabel = new Label(shell, SWT.NONE);
		intoLabel.setText("into");
		intoLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Combo target = new Combo(shell, SWT.READ_ONLY);
		target.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		for (final PackageWrapper pkg : repository.getPackages()) {
			target.add(pkg.getName());
		}

		final Composite buttons = new Composite(shell, SWT.NONE);
		final GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gridData.horizontalSpan = 2;
		buttons.setLayoutData(gridData);
		buttons.setLayout(new RowLayout());

		final Button merge = new Button(buttons, SWT.PUSH);
		merge.setText("Merge");
		merge.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			}
		});

		final Button cancel = new Button(buttons, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			}
		});

		shell.pack();

		return shell;
	}
}
