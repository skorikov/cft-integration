package de.proskor.cft.merge.ui;

import java.util.List;

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

import de.proskor.cft.Component;
import de.proskor.cft.Package;
import de.proskor.cft.Repository;
import de.proskor.cft.merge.Merge;

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

	private Shell createShell(Display display, final RepositoryWrapper repository) {
		final Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE);
		shell.setText("Merge");
		final GridLayout shellLayout = new GridLayout();
		shellLayout.numColumns = 2;
		shell.setLayout(shellLayout);

		final Label mergeLabel = new Label(shell, SWT.NONE);
		mergeLabel.setText("Merge");
		mergeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Combo leftCombo = new Combo(shell, SWT.READ_ONLY);
		leftCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		for (final ComponentWrapper component : repository.getComponents()) {
			leftCombo.add(component.getName());
		}

		final Label withLabel = new Label(shell, SWT.NONE);
		withLabel.setText("with");
		withLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Combo rightCombo = new Combo(shell, SWT.READ_ONLY);
		rightCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		for (final ComponentWrapper component : repository.getComponents()) {
			rightCombo.add(component.getName());
		}

		final Label intoLabel = new Label(shell, SWT.NONE);
		intoLabel.setText("into");
		intoLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Combo targetCombo = new Combo(shell, SWT.READ_ONLY);
		targetCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		for (final PackageWrapper pkg : repository.getPackages()) {
			targetCombo.add(pkg.getName());
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
				final List<ComponentWrapper> components = repository.getComponents();
				final Component left = components.get(leftCombo.getSelectionIndex()).getComponent();
				final Component right = components.get(rightCombo.getSelectionIndex()).getComponent();
				final Package target = repository.getPackages().get(targetCombo.getSelectionIndex()).getPackage();
				final Merge merge = new Merge();
				merge.run(left, right, target);
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
