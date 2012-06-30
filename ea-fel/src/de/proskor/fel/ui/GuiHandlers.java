package de.proskor.fel.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.proskor.fel.EventRepository;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.ui.Filters.EventTypeContainerFilter;
import de.proskor.fel.ui.Filters.EventTypeFilter;
import de.proskor.fel.ui.GuiRepository.EventTypeContainerHandler;
import de.proskor.fel.ui.GuiRepository.EventTypesHandler;

public class GuiHandlers {
	private static interface GuiChangeListener {
	}
	public static interface GuiChangeListenerComponents extends GuiChangeListener {
		void selectionChanged(List<EventTypeContainer> newSelection);
	}
	
	private static abstract class GuiHandler {
		protected void expandTreeItems(Tree tree, boolean expanded) {
			for(TreeItem item : tree.getItems())
				expandTreeItem(item, expanded);
		}
		
		protected void expandTreeItem(TreeItem item, boolean expanded) {
			item.setExpanded(expanded);
			
			for(TreeItem subItem : item.getItems()) 
				expandTreeItem(subItem, expanded);
		}
		
		protected void expandSelectedTreeItems(Tree tree, boolean expanded) {
			TreeItem[] items = tree.getSelection();
			
			for(TreeItem item : items)
				item.setExpanded(expanded);
		}
	}
	
	public static class GuiHandlerEvents extends GuiHandler {
		private final EventTypesHandler eventsHandler;
		private final EventTypeFilter eventTypeFilter;
		
		private final Tree treeEvents;
		private final Combo comboEventFilterMode;
		private final Text textEventFilterByFieldsMatch;
		
		public GuiHandlerEvents(Tree treeEvents, Combo comboEventFilterMode, Text textEventFilterByFieldsMatch) {
			this.treeEvents = treeEvents;
			this.comboEventFilterMode = comboEventFilterMode;
			this.textEventFilterByFieldsMatch = textEventFilterByFieldsMatch;
			
			eventsHandler = new EventTypesHandler(treeEvents);
			eventTypeFilter = new EventTypeFilter("", comboEventFilterMode, textEventFilterByFieldsMatch);
			
			configGuiForFirstUse();			
		}
		
		public void configGuiForFirstUse() {
			comboEventFilterMode.setItems(eventTypeFilter.getFilterModeNames());
			comboEventFilterMode.select(0);
		}

		public void updateGui() {
		
		}
	}
	
	public static class GuiHandlerCreateEvent extends GuiHandler implements GuiChangeListenerComponents {
		private final Text textEventName;
		private final Text textEventAuthor;
		private final Text textEventComponent;
		private final StyledText textEventDescription;
//		private final GuiHandlerComponents guiHandlerComponents;
		
		public GuiHandlerCreateEvent(Text textEventName, Text textEventAuthor, Text textEventComponent, StyledText textEventDescription) {
//			this.guiHandlerComponents = guiHandlerComponents;
			
			this.textEventName = textEventName;
			this.textEventAuthor = textEventAuthor;
			this.textEventComponent = textEventComponent;
			this.textEventDescription = textEventDescription;
			
			clearData();
		}
		
		private void setTextFieldsContent(String content) {
			textEventName.setText(content);
			textEventAuthor.setText(content);
			textEventComponent.setText(content);
			textEventDescription.setText(content);
		}
		
		public void clearData() {
			setTextFieldsContent("");
		}
		
		private void updateComponentQualifiedName(EventTypeContainer component) {
			textEventComponent.setText(component.getQualifiedName());
		}

		@Override
		public void selectionChanged(List<EventTypeContainer> newSelection) {
			if (newSelection.size() > 0) 
				updateComponentQualifiedName(newSelection.get(0));
		}
	}
	
	public static class GuiHandlerComponents extends GuiHandler {
//		private final GuiHandlerCreateEvent guiHandlerCreateEvent;
		private final ArrayList<GuiChangeListenerComponents> componentsChangeListener = new ArrayList<GuiHandlers.GuiChangeListenerComponents>();
		
		private final EventTypeContainerHandler containerHandler;
		private final EventTypeContainerFilter containerFilter;
		private final EventRepository eventRepository;
		
		private final Tree treeComponents;
		private final Combo comboComponentsSelectByFieldsMatch;
		private final Text textComponentsSelectByFieldsMatch;
		
		public GuiHandlerComponents(EventRepository eventRepository, Tree treeComponents, Combo comboComponentsSelectByFieldsMatch, Text textComponentsSelectByFieldsMatch) {
			this.eventRepository = eventRepository;
			
			this.treeComponents = treeComponents;
			this.comboComponentsSelectByFieldsMatch = comboComponentsSelectByFieldsMatch;
			this.textComponentsSelectByFieldsMatch = textComponentsSelectByFieldsMatch;
			
			containerHandler = new EventTypeContainerHandler(treeComponents);
			containerFilter = new EventTypeContainerFilter("", comboComponentsSelectByFieldsMatch, textComponentsSelectByFieldsMatch);
			
			configGuiForFirstUse();
		}
		
		public void addGuiChangeListenerComponents(GuiChangeListenerComponents listener) {
			componentsChangeListener.add(listener);
		}
		
		public List<EventTypeContainer> getSelectedComponents() {
			ArrayList<TreeItem> selection = DataUtils.treeItemArrayToList(treeComponents.getSelection());
			
			List<EventTypeContainer> selectedContainer = new ArrayList<EventTypeContainer>();
			for(TreeItem item : selection)
				selectedContainer.add((EventTypeContainer)containerHandler.getTypeByTreeItem(item));
			
			return selectedContainer;
		}

		private void configGuiForFirstUse() {
			comboComponentsSelectByFieldsMatch.setItems(containerFilter.getFilterModeNames());
			comboComponentsSelectByFieldsMatch.select(0);
		}
		
		public void updateGui() {
			updateContainerList();
		}	
		
		private void updateContainerList() {
			containerHandler.clear();

			for (EventTypeContainer c : eventRepository.getEventTypeContainers()) {
				containerHandler.addContainerAndSubContainers(c);
			}
			
			if (treeComponents.getItemCount() > 0)
				treeComponents.select(treeComponents.getItem(0));
		}
		
		public void selectContainerByFieldMatch(boolean clearOldSelection) {
			containerFilter.applyGuiFilterConfig();
			
			ArrayList<TreeItem> selection = new ArrayList<TreeItem>();
			for (EventTypeContainer c : containerHandler.getContainers()) {
				if (containerFilter.typeConformsToFilter(c)) {
					TreeItem cItem = containerHandler.getTreeItemByType(c);
					selection.add(cItem);
				}
			}

			if (!clearOldSelection) {
				TreeItem[] oldSelection = treeComponents.getSelection(); 
				ArrayList<TreeItem> oldSelectinList = DataUtils.treeItemArrayToList(oldSelection);
				selection.addAll(oldSelectinList);
			}
			TreeItem[] selectionItems = DataUtils.treeItemListToArray(selection);
			
			treeComponents.setSelection(selectionItems);
		}
		
		public void unselectContainerByFieldMatch() {
			containerFilter.applyGuiFilterConfig();
			ArrayList<TreeItem> currentSelection = DataUtils.treeItemArrayToList(treeComponents.getSelection()); 
			
			ArrayList<TreeItem> unselection = new ArrayList<TreeItem>();
			for (EventTypeContainer c : containerHandler.getContainers()) {
				if (containerFilter.typeConformsToFilter(c)) {
					TreeItem cItem = containerHandler.getTreeItemByType(c);
					unselection.add(cItem);
				}
			}

			for(TreeItem item : unselection)
				currentSelection.remove(item);			
			
			TreeItem[] selectionArray = DataUtils.treeItemListToArray(currentSelection);
			treeComponents.setSelection(selectionArray);
		}

		public void selectSubComponents() {
			selectSubComponentsEx();
			expandSelectedTreeItems(treeComponents, true);
		}
			
		private void selectSubComponentsEx() {
			ArrayList<TreeItem> selection = new ArrayList<TreeItem>();
			ArrayList<TreeItem> currentSelection = DataUtils.treeItemArrayToList(treeComponents.getSelection()); 
			selection.addAll(currentSelection);
			
			for(TreeItem selectedContainerItem : currentSelection) {
				EventTypeContainer container = (EventTypeContainer)containerHandler.getTypeByTreeItem(selectedContainerItem);
				List<EventTypeContainer> subContainers = DataUtils.getAllSubContainer(container);
				
				for(EventTypeContainer sub : subContainers)
					if (!selection.contains(sub))
						selection.add(containerHandler.getTreeItemByType(sub));
			}
			
			TreeItem[] selectionArray = DataUtils.treeItemListToArray(selection);
			treeComponents.setSelection(selectionArray);
		}

		public void selectSuperComponents() {
			ArrayList<TreeItem> selection = new ArrayList<TreeItem>();
			ArrayList<TreeItem> currentSelection = DataUtils.treeItemArrayToList(treeComponents.getSelection()); 
			selection.addAll(currentSelection);
			
			for(TreeItem selectedContainerItem : currentSelection) {
				EventTypeContainer container = (EventTypeContainer)containerHandler.getTypeByTreeItem(selectedContainerItem);
				List<EventTypeContainer> superContainers = DataUtils.getAllSuperContainer(container);
				
				for(EventTypeContainer parent : superContainers)
					if (!selection.contains(parent))
						selection.add(containerHandler.getTreeItemByType(parent));
			}
			
			TreeItem[] selectionArray = DataUtils.treeItemListToArray(selection);
			treeComponents.setSelection(selectionArray);
		}		
		
		public void componentTreeSelectionChanged() {
			for (GuiChangeListenerComponents listener : componentsChangeListener)
				listener.selectionChanged(getSelectedComponents());
		}

		public void componentTreeExpand(boolean selectionOnly) {
			componentTreeExpandEx(selectionOnly, true);
		}

		public void componentTreeCollapse(boolean selectionOnly) {
			componentTreeExpandEx(selectionOnly, false);
		}
		
		private void componentTreeExpandEx(boolean selectionOnly, boolean expanded) {
			if (selectionOnly) {
				for(TreeItem item : treeComponents.getSelection()) {
					expandTreeItem(item, expanded);
				}
			} else {
				expandTreeItems(treeComponents, expanded);
			}
		}
	}
	
	private static class DataUtils {
		public static List<EventTypeContainer> getAllSubContainer(EventTypeContainer container) {
			ArrayList<EventTypeContainer> subContainerList = new ArrayList<EventTypeContainer>();
			
			for(EventTypeContainer sub : container.getChildren()) {
				subContainerList.add(sub);
				
				List<EventTypeContainer> subsSubContainerList = getAllSubContainer(sub);
				subContainerList.addAll(subsSubContainerList);
			}
			
			return subContainerList;
		}
		
		public static List<EventTypeContainer> getAllSuperContainer(EventTypeContainer container) {
			ArrayList<EventTypeContainer> superContainerList = new ArrayList<EventTypeContainer>();
			
			EventTypeContainer parent = container.getParent();
			while(parent != null) {
				superContainerList.add(parent);
				parent = parent.getParent();
			}
			
			return superContainerList;
		}

		public static ArrayList<TreeItem> treeItemArrayToList(TreeItem[] arr) {
			ArrayList<TreeItem> list = new ArrayList<TreeItem>();
			
			for(TreeItem item : arr)
				list.add(item);
			
			return list;
		}
		
		public static TreeItem[] treeItemListToArray(List<TreeItem> list) {
			TreeItem[] arr = new TreeItem[list.size()];
			
			int i=0;
			for(TreeItem item : list) {
				arr[i] = item;
				i++;
			}
			
			return arr;
		}
	}
}
