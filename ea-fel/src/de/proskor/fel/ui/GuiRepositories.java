package de.proskor.fel.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.proskor.fel.Type;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;
import de.proskor.fel.ui.GuiHandlers.DataUtils;
import de.proskor.fel.ui.MappingUtils.ObjectMapping;

public class GuiRepositories {
	public static abstract class GuiRepository {
		protected final Tree elementsTree;

		//		protected final ObjectMapping<Type, TreeItem> mapping; 
		protected final ObjectMapping mapping; // extra keine Typ-Sicherheit, da ich sonst beim Exportieren der Listen diese Abschreiben müsste - weil Java keinen Cast erlaubt. -.-


		public GuiRepository(Tree elementsTree) {
			this.elementsTree = elementsTree;
			mapping = new ObjectMapping<Type, TreeItem>();
		}		

		public List<TreeItem> getTreeItems() {
			return mapping.getMappedObjectBs();
		}

		public TreeItem getTreeItemByType(Type type) {
			return (TreeItem) mapping.getObjectAMapping(type);
		}		

		public Type getTypeByTreeItem(TreeItem treeItem) {
			return (Type) mapping.getObjectBMapping(treeItem);
		}

		public boolean hasType(Type type) {
			return getTreeItemByType(type) != null;
		}

		public List<Type> getTypesByTreeItems(List<TreeItem> items) {
			ArrayList<Type> types = new ArrayList<Type>();

			for(TreeItem item : items)
				types.add(getTypeByTreeItem(item));

			return types;
		}		

		public List<TreeItem> getTreeItemsByTypes(List<Type> types) {
			ArrayList<TreeItem> items = new ArrayList<TreeItem>();

			for(Type type : types)
				items.add(getTreeItemByType(type));

			return items;
		}

		protected TreeItem addType(Type type) {
			TreeItem treeItem = createTreeItem(type);
			mapping.put(type, treeItem);

			return treeItem;
		}		

		protected TreeItem addSubType(TreeItem parent, Type subType) {
			TreeItem treeItem = createSubTreeItem(parent, subType);
			mapping.put(subType, treeItem);			
			return treeItem;
		}		

		protected String[] getTypeTreeItemText(Type type) {
			return new String[] {
					type.getName(),
					type.getDescription(),
					type.getAuthor(),
					type.getId()+" / "+type.getGuid()
			};
		}

		/**
		 * Calls {@link #getTypeTreeItemText(Type)} on default. Overwrite to change behavior.
		 * @param parent
		 * @param type
		 * @return
		 */
		protected String[] getTypeSubTreeItemText(TreeItem parent, Type subType) {
			return getTypeTreeItemText(subType);
		}

		private TreeItem createSubTreeItem(TreeItem parent, Type subType) {
			TreeItem item = new TreeItem(parent, 0);
			item.setText(getTypeSubTreeItemText(parent, subType));
			return item;
		}

		private TreeItem createTreeItem(Type type) {
			TreeItem item = new TreeItem(elementsTree, 0);
			item.setText(getTypeTreeItemText(type));
			return item;
		}

		public void clear() {
			elementsTree.removeAll();
			mapping.clear();
		}
		
		protected List<Type> getTypes() {
			return mapping.getMappedObjectAs();
		}
	}

	public static class GuiRepositoryEventTypeContainer extends GuiRepository {
		public List<EventTypeContainer> getContainers() {
			return mapping.getMappedObjectAs();
		}

		public TreeItem addContainer(EventTypeContainer container) {
			TreeItem item = addType(container);
			return item;
		}

		public TreeItem addContainerAndSubContainers(EventTypeContainer parentContainer) {
			TreeItem parentItem = addContainer(parentContainer);
			addAllSubContainer(parentItem, parentContainer);

			return parentItem;
		}

		private void addAllSubContainer(TreeItem parentContainerItem, EventTypeContainer parentContainer) {
			for(EventTypeContainer subContainer : parentContainer.getChildren()) {
				TreeItem subContainerItem = addSubType(parentContainerItem, subContainer);
				addAllSubContainer(subContainerItem, subContainer);
			}
		}

		public GuiRepositoryEventTypeContainer(Tree containerTree) {
			super(containerTree);
			clear();
		}
	}

	public static class GuiRepositoryEventTypes extends GuiRepository {
		public List<EventType> getEvents() {
			return mapping.getMappedObjectAs();
		}

		public GuiRepositoryEventTypes(Tree eventsTree) {
			super(eventsTree);
		}
		
		@Override
		protected String[] getTypeTreeItemText(Type type) {
			EventType eventType = (EventType)type;
			return new String[] {
					// name, component, description, author, id
					eventType.getName(),
					eventType.getContainer().getName(),
					eventType.getDescription(),
					eventType.getAuthor(),
					eventType.getId()+" / "+type.getGuid()
			};
		}

		public TreeItem addEvent(EventType event) {
			TreeItem item = addType(event);
			return item;
		}

	}
}
