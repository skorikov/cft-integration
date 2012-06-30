package de.proskor.fel.ui;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import de.proskor.fel.Type;

public class Filters {
	public static class TypeFilter {
		/**
		 * Specifies which field the <b>filter-String</b> needs to match.
		 * @author Tw
		 *
		 */
		public static enum FilterMode { 
			anyField, name, author, description, id, guid;

			public static String filterPrefix;

			public String toString() {
				switch(this) {

				case anyField: return filterPrefix + "[[Any Field]]";
				case name : return filterPrefix + "Name";
				case author: return filterPrefix + "Author";
				case description : return filterPrefix + "Description";
				case guid: return filterPrefix + "GUID";
				case id: return filterPrefix + "ID";

				default: return ""; // alle Fälle behandelt, wird nicht eintreten.
				}
			};
		}

		private String filterString;
		private String filterStringLower;
		private FilterMode filterMode;

		public String getFilterString() {
			return filterString;
		}

		public void setFilterString(String filterString) {
			this.filterString = filterString;
			this.filterStringLower = filterString.toLowerCase();
		}

		public FilterMode getFilterMode() {
			return filterMode;
		}

		public void setFilterMode(FilterMode filterMode) {
			this.filterMode = filterMode;
		}

		public TypeFilter(String filterName) {
			filterMode = FilterMode.anyField;
			setFilterString("");
			
			FilterMode.filterPrefix = filterName;
		}

		public boolean typeConformsToFilter(Type type) {
			return typeConformsToFilter(type, filterMode);
		}
		
		protected boolean textIsMatch(String text) {
			String textLower = text.toLowerCase();
			return textLower.contains(filterStringLower);
		}

		private boolean typeConformsToFilter(Type type, FilterMode filterMode) {
			switch(filterMode) {
			case anyField: {
				for(FilterMode mode : FilterMode.values())
					if (!mode.equals(FilterMode.anyField))
						if (typeConformsToFilter(type, mode))
							return true;

				return false;					
			}
			case name: {
				return textIsMatch(type.getName());
			}
			case author: {
				return textIsMatch(type.getAuthor());
			}
			case description: {
				return textIsMatch(type.getDescription());
			}
			case guid: {
				return textIsMatch(type.getGuid()); 
			}
			case id: {
				return textIsMatch(type.getId() + "");
			}

			// Nach Java-Spec nötig, wird jedoch nie eintreten, da alle Fälle behandelt wurden.
			default: {
				return false;
			}
			}
		}
		
		public String[] getFilterModeNames() {
			FilterMode[] values = FilterMode.values();
			String[] items = new String[values.length];
			
			for(int i=0; i<values.length; i++) {
				FilterMode value = values[i];
				String text = value.toString();
				items[i] = text;
			}
			
			return items;
		}
		
		public FilterMode getFilterModeByName(String name) {
			for(FilterMode value : FilterMode.values()) 
				if (value.toString().equals(name))
					return value;
			
			return null;
		}
	}
	
	private static class GuiTypeFilter extends TypeFilter {
		protected final Combo comboBoxFilterMode;
		protected final Text textFilterString;
		
		public GuiTypeFilter(String filterName, Combo comboBox, Text textFilterString) {
			super(filterName);
			
			this.comboBoxFilterMode = comboBox;
			this.textFilterString = textFilterString;
		}
		
		public void applyGuiFilterConfig() {
			setFilterMode(getGuiActiveMode());
			setFilterString(getGuiFilterString());
		}
		
		public FilterMode getGuiActiveMode() {
			String selectedFilterName = comboBoxFilterMode.getText();
			FilterMode currentMode = getFilterModeByName(selectedFilterName);
			
			return currentMode;
		}
		
		public String getGuiFilterString() {
			return textFilterString.getText();	
		}
	}	
	
	public static class EventTypeFilter extends GuiTypeFilter {
		public EventTypeFilter(String filterName, Combo comboBox, Text textFilterString) {
			super(filterName, comboBox, textFilterString);
		}
	}	
	
	public static class EventTypeContainerFilter extends GuiTypeFilter {
		public EventTypeContainerFilter(String filterName, Combo comboBox, Text textFilterString) {
			super(filterName, comboBox, textFilterString);
		}
	}
	
}