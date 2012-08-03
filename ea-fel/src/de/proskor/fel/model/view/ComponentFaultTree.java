package de.proskor.fel.model.view;

public interface ComponentFaultTree extends View {
	public ArchitecturalView getContext();
	public void setContext(ArchitecturalView view);
}
