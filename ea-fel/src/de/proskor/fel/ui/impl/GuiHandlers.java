package de.proskor.fel.ui.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.proskor.fel.EventRepository;
import de.proskor.fel.Type;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.Event;
import de.proskor.fel.event.EventType;
import de.proskor.fel.ui.impl.Filters.EventTypeContainerFilter;
import de.proskor.fel.ui.impl.Filters.EventTypeFilter;
import de.proskor.fel.ui.impl.GuiRepositories.GuiRepository;
import de.proskor.fel.ui.impl.GuiRepositories.GuiRepositoryEventTypeContainer;
import de.proskor.fel.ui.impl.GuiRepositories.GuiRepositoryEventTypes;

class GuiHandlers {
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
			
			for(TreeItem item : items) {
				item.setExpanded(expanded);
			}
		}
	}
	
	private static abstract class GuiHandlerWithRepository extends GuiHandler {
		private List<Type> selectionBackup;
		private List<Type> expandedBackup;
		private final Tree elementsTree;
		
		protected GuiHandlerWithRepository(Tree elementsTree) {
			selectionBackup = null;
			this.elementsTree = elementsTree;
		}

		/**
		 * Stores each {@link Type} whose associated {@link TreeItem TreeItem} has <code>treeItem.getExpanded() == true</code>. 
		 * The {@link Tree} state can therefore be restored after clearing all items.
		 * 
		 * @see #expandedRestore()
		 */
		protected void expandedBackup() {
			expandedBackup = new ArrayList<Type>();
			
			for(TreeItem item : getGuiRepository().getTreeItems()) {
				if (item.getExpanded()) {
					Type type = getGuiRepository().getTypeByTreeItem(item);
					expandedBackup.add(type);
				}
			}
		}
		
		/**
		 * Restores the {@link TreeItem#getExpanded()} value of each {@link TreeItem} which is associated 
		 * with the stored {@link Type} from {@link #expandedBackup()}. 
		 */
		protected void expandedRestore() {
			List<Type> types = getGuiRepository().getTypes();
			
			for(Type expandedType : expandedBackup) {
				if (types.contains(expandedType)) {
					TreeItem treeItem = getGuiRepository().getTreeItemByType(expandedType);
					treeItem.setExpanded(true);
				}
			}
		}
		
		/**
		 * Stores the currently selected {@link TreeItem} Objects in the associated {@link Tree}.
		 * The selection can be restored by calling {@link #selectionRestore()}.
		 */
		protected void selectionBackup() {
			selectionBackup = getSelectedTypes();
		}

		/**
		 * Selects the {@link Type} Objects, which have been selected when calling {@link #selectionBackup()} and are still being part of the {@link Tree}.
		 */
		protected void selectionRestore() {
			List<Type> currentTypesInTree = getGuiRepository().getTypes(); 
			ArrayList<Type> newSelection = new ArrayList<Type>();
			
			// Events finden, die im alten und neuen Event-Repository sind.
			for(Type formerSelectedType : selectionBackup) {
				if (currentTypesInTree.contains(formerSelectedType))
					newSelection.add(formerSelectedType);
			}
			
			List<TreeItem> newSelectionItems = getGuiRepository().getTreeItemsByTypes(newSelection);
			getGuiRepository().elementsTree.setSelection(DataUtils.treeItemListToArray(newSelectionItems));
		}
		
		protected List<Type> getInvertedSelection() {
			List<Type> origSelection = getSelectedTypes();
			List<Type> allTypes = getGuiRepository().getTypes();

			ArrayList<Type> newSelection = new ArrayList<Type>();
			
			for(Type type : allTypes) {
				if (!origSelection.contains(type))
					newSelection.add(type);
			}
			
			return newSelection;
		}
		
		public void selectionInvert() {
			List<Type> invertedSelection = getInvertedSelection();
			List<TreeItem> newSelectionItems = getGuiRepository().getTreeItemsByTypes(invertedSelection);
			
			elementsTree.setSelection(DataUtils.treeItemListToArray(newSelectionItems));
		}
		
		public List<Type> getSelectedTypes() {
			ArrayList<TreeItem> selectedItems = DataUtils.treeItemArrayToList(elementsTree.getSelection());
			return getGuiRepository().getTypesByTreeItems(selectedItems);
		}

		protected void selectType(Type type) {
			TreeItem item = getGuiRepository().getTreeItemByType(type);
			elementsTree.setSelection(item);
		}
		
		public void clearSelection() {
			elementsTree.setSelection(new TreeItem[] {});
		}
		
		protected void selectFirstTypeInTree() {
			if (elementsTree.getItemCount() == 0)
				return;
			else
				elementsTree.setSelection(elementsTree.getItem(0));
		}

		protected abstract GuiRepository getGuiRepository();
	}
	
	public static class GuiHandlerEvents extends GuiHandlerWithRepository {
		private final GuiRepositoryEventTypes guiRepositoryEvents;
		private final EventTypeFilter eventTypeFilter;
		
		private final Tree treeEvents;
		private final Combo comboEventFilterMode;
		private final Text textEventFilterByFieldsMatch;
		private List<EventTypeContainer> currentComponentsSelection;
		
		public GuiHandlerEvents(Tree treeEvents, Combo comboEventFilterMode, Text textEventFilterByFieldsMatch) {
			super(treeEvents);
			this.treeEvents = treeEvents;
			this.comboEventFilterMode = comboEventFilterMode;
			this.textEventFilterByFieldsMatch = textEventFilterByFieldsMatch;
			
			guiRepositoryEvents = new GuiRepositoryEventTypes(treeEvents);
			eventTypeFilter = new EventTypeFilter("", comboEventFilterMode, textEventFilterByFieldsMatch);
			
			configGuiForFirstUse();			
		}
		
		private void configGuiForFirstUse() {
			comboEventFilterMode.setItems(eventTypeFilter.getFilterModeNames());
			comboEventFilterMode.select(0);
		}

		private void componentsSelectionOrFilterChanged() {
			eventTypeFilter.applyGuiFilterConfig();
			
			List<EventTypeContainer> currentComponentsSelectionReversed = new ArrayList<EventTypeContainer>();
			for(EventTypeContainer container : currentComponentsSelection)
				currentComponentsSelectionReversed.add(0, container);
				
			
			for(EventTypeContainer container : currentComponentsSelectionReversed) { // Container aus Selection...
				for(EventType event : container.getEvents()) { // beinhalten Events...
					if (eventTypeFilter.typeConformsToFilter(event)) { // es werden die verwendet, die dem Filter entsprechen
						guiRepositoryEvents.addEvent(event);
					}
				}
			}
		}
		
		public void componentsSetSelection(List<EventTypeContainer> selection) {
			this.currentComponentsSelection = selection;
			reloadEvents();
		}

		public void reloadEvents() {
			selectionBackup(); 
			
			guiRepositoryEvents.clear();
			componentsSelectionOrFilterChanged(); // Bewirkt das neu-Laden der Events
			
			selectionRestore();
		}

		public EventType getSelectedEvent() {
			List<Type> selectedTypes = getSelectedTypes();
			
			if (selectedTypes.size() > 0)
				return (EventType)selectedTypes.get(0);
			else
				return null;
		}

		@Override
		protected GuiRepository getGuiRepository() {
			return guiRepositoryEvents;
		}

		public List<EventType> getEvents() {
			return guiRepositoryEvents.getEvents();
		}

		public void selectEvent(EventType selectedEvent) {
			selectType(selectedEvent);
		}
	}
	
	public static class GuiHandlerCreateEvent extends GuiHandler {
		public static class EventData {
			public EventTypeContainer container;
			public String name;
			public String author;
			public String description;
		}
		
		private final Text textEventName;
		private final Text textEventAuthor;
		private final Text textEventComponent;
		private final StyledText textEventDescription;
		private final Button btnCreateEvent;
		private final Button btnChkIsValid;
		
		private EventTypeContainer currentContainer;
		private List<EventType> eventsCreatedByUser;
		
		public GuiHandlerCreateEvent(
				Text textEventName, Text textEventAuthor, Text textEventComponent, StyledText textEventDescription,
				Button btnCreateEvent, Button btnChkIsValid
				) {
			this.textEventName = textEventName;
			this.textEventAuthor = textEventAuthor;
			this.textEventComponent = textEventComponent;
			this.textEventDescription = textEventDescription;
			
			this.btnChkIsValid = btnChkIsValid;
			this.btnCreateEvent = btnCreateEvent;
			
			eventsCreatedByUser = new ArrayList<EventType>();

			/* 1. Nicht n�tig da Per default keine Daten vorhanden. 
			 * 2. Erzeugt null-pointer Exception durch: 
			 *    GuiHandlerCreateEvent guiHandlerCreateEvent = new GuiHandlerCreateEvent(...);
			 *    --> GuiHandlerCreateEvent.<Init>
			 *    --> clearData()
			 *    --> Text.setText() 
			 *    --> Text-Listener wird auf guiHandlerCreateEvent ausgel�st, welches noch nicht fertig instanziiert wurde,
			 *        da Konstruktor noch nicht terminiert.
			 */
			// clearData(); 
		}
		
		public List<EventType> getEventsCreatedByUser() {
			return eventsCreatedByUser;
		}
		
		private void setTextFieldsContent(String content) {
			textEventName.setText(content);
			textEventAuthor.setText(content);
			textEventComponent.setText(content);
			textEventDescription.setText(content);
		}
		
		public void clearData() {
			setTextFieldsContent("");
			eventDataChanged();
		}
		
		private void updateComponentQualifiedName(EventTypeContainer component) {
			if (component == null)
				textEventComponent.setText("");
			else
				textEventComponent.setText(component.getQualifiedName());
		}
		
		private String getCurrentEventName() {
			return textEventName.getText().trim();
		}
		
		private String getCurrentEventAuthor() {
			return textEventAuthor.getText().trim();
		}
		
		private String getCurrentEventDescription() {
			return textEventDescription.getText().trim();
		}
		
		private boolean eventDataIsValid() {
			if (getCurrentEventName().equals(""))
				return false;
			
			if (getCurrentEventAuthor().equals(""))
				return false;
			
			if (getCurrentEventDescription().equals(""))
				return false;
			
			if (currentContainer == null)
				return false;
			
			for(EventType event : currentContainer.getEvents()) {
				if (event.getName().equalsIgnoreCase(getCurrentEventName()))
					return false;
			}
			
			return true;
		}
		
		private void setCurrentContainer(EventTypeContainer container) {
			currentContainer = container;
			updateComponentQualifiedName(container);
		}

		public void componentsSelectionChanged(EventTypeContainer currentContainer) {
			setCurrentContainer(currentContainer);
			
			eventDataChanged();
		}

		public void eventDataChanged() {
			boolean isValid = eventDataIsValid();
			
			btnCreateEvent.setEnabled(isValid);
			btnChkIsValid.setSelection(isValid);
		}

		public Event createEvent() {
			EventType event = currentContainer.createEventType(getCurrentEventName());
			event.setAuthor(getCurrentEventAuthor());
			event.setDescription(getCurrentEventDescription());
			
			eventsCreatedByUser.add(event);
			
			return event;
		}

		/**
		 * Sets the <i>Event-Data</i> (also in the GUI). {@link EventData} Attributes with the value <code>null</code> are being ignored and not changed.
		 * @param eventData
		 */
		public void setEventData(EventData eventData) {
			if (eventData == null)
				return;
			
			if (eventData.container != null)
				setCurrentContainer(eventData.container);
			if (eventData.name != null)
				textEventName.setText(eventData.name);
			if (eventData.author != null)
				textEventAuthor.setText(eventData.author);
			if (eventData.description != null)
				textEventDescription.setText(eventData.description);
		}
	}
	
	public static class GuiHandlerComponents extends GuiHandlerWithRepository {
		private final GuiRepositoryEventTypeContainer guiRepositoryContainer;
		private final EventTypeContainerFilter containerFilter;
		private final EventRepository eventRepository;
		
		private final Tree treeComponents;
		private final Combo comboComponentsSelectByFieldsMatch;
		private final Text textComponentsSelectByFieldsMatch;
		
		public GuiHandlerComponents(EventRepository eventRepository, Tree treeComponents, Combo comboComponentsSelectByFieldsMatch, Text textComponentsSelectByFieldsMatch) {
			super(treeComponents);
			this.eventRepository = eventRepository;
			
			this.treeComponents = treeComponents;
			this.comboComponentsSelectByFieldsMatch = comboComponentsSelectByFieldsMatch;
			this.textComponentsSelectByFieldsMatch = textComponentsSelectByFieldsMatch;
			
			guiRepositoryContainer = new GuiRepositoryEventTypeContainer(treeComponents);
			containerFilter = new EventTypeContainerFilter("", comboComponentsSelectByFieldsMatch, textComponentsSelectByFieldsMatch);
			
			configGuiForFirstUse();
		}
		
		/**
		 * Retrieves the first of the Components of {@link #getSelectedComponents()} or <code>null</code> if there are none.
		 * @see
		 * #getSelectedComponents()
		 */
		public EventTypeContainer getSelectedComponent() {
			List<EventTypeContainer> selection = getSelectedComponents();
			
			if (selection.size() > 0)
				return selection.get(0);
			else
				return null;
		}

		/**
		 * Retrieves all Components resp. {@link EventTypeContainer EventTypeContainers} currently selected in the {@link Tree}. 
		 * 
		 * @see
		 * #getSelectedComponent()
		 */
		public List<EventTypeContainer> getSelectedComponents() {
			ArrayList<TreeItem> selection = DataUtils.treeItemArrayToList(treeComponents.getSelection());
			
			List<EventTypeContainer> selectedContainer = new ArrayList<EventTypeContainer>();
			for(TreeItem item : selection)
				selectedContainer.add((EventTypeContainer)guiRepositoryContainer.getTypeByTreeItem(item));
			
			return selectedContainer;
		}

		private void configGuiForFirstUse() {
			comboComponentsSelectByFieldsMatch.setItems(containerFilter.getFilterModeNames());
			comboComponentsSelectByFieldsMatch.select(0);
		}
		
		public void loadContainerList() {
			selectionBackup();
			expandedBackup();
			
			guiRepositoryContainer.clear();

			for (EventTypeContainer c : eventRepository.getEventTypeContainers()) {
				guiRepositoryContainer.addContainerAndSubContainers(c);
			}
			
			if (treeComponents.getItemCount() > 0)
				treeComponents.select(treeComponents.getItem(0));
			
			selectionRestore();
			expandedRestore();
		}
		
		public void selectContainerByFieldMatch(boolean clearOldSelection) {
			containerFilter.applyGuiFilterConfig();
			
			ArrayList<TreeItem> selection = new ArrayList<TreeItem>();
			for (EventTypeContainer c : guiRepositoryContainer.getContainers()) {
				if (containerFilter.typeConformsToFilter(c)) {
					TreeItem cItem = guiRepositoryContainer.getTreeItemByType(c);
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
			for (EventTypeContainer c : guiRepositoryContainer.getContainers()) {
				if (containerFilter.typeConformsToFilter(c)) {
					TreeItem cItem = guiRepositoryContainer.getTreeItemByType(c);
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
				EventTypeContainer container = (EventTypeContainer)guiRepositoryContainer.getTypeByTreeItem(selectedContainerItem);
				List<EventTypeContainer> subContainers = DataUtils.getAllSubContainer(container);
				
				for(EventTypeContainer sub : subContainers)
					if (!selection.contains(sub))
						selection.add(guiRepositoryContainer.getTreeItemByType(sub));
			}
			
			TreeItem[] selectionArray = DataUtils.treeItemListToArray(selection);
			treeComponents.setSelection(selectionArray);
		}

		public void selectSuperComponents() {
			ArrayList<TreeItem> selection = new ArrayList<TreeItem>();
			ArrayList<TreeItem> currentSelection = DataUtils.treeItemArrayToList(treeComponents.getSelection()); 
			selection.addAll(currentSelection);
			
			for(TreeItem selectedContainerItem : currentSelection) {
				EventTypeContainer container = (EventTypeContainer)guiRepositoryContainer.getTypeByTreeItem(selectedContainerItem);
				List<EventTypeContainer> superContainers = DataUtils.getAllSuperContainer(container);
				
				for(EventTypeContainer parent : superContainers)
					if (!selection.contains(parent))
						selection.add(guiRepositoryContainer.getTreeItemByType(parent));
			}
			
			TreeItem[] selectionArray = DataUtils.treeItemListToArray(selection);
			treeComponents.setSelection(selectionArray);
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

		@Override
		protected GuiRepository getGuiRepository() {
			return guiRepositoryContainer;
		}

		public List<EventTypeContainer> getComponents() {
			return guiRepositoryContainer.getContainers();
		}

		public void selectComponent(EventTypeContainer selectedContainer) {
			selectType(selectedContainer);
		}
	}
	
	public static class DataUtils {
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
