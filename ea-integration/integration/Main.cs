using System;
using System.Reflection;
using Microsoft.Win32;
using EA;
using de.proskor.automation;

namespace de.proskor.integration {

	/**
	 * AddInAdapter. Delegates the EA events to the addin implementation.
	 */
	public class AddInAdapter {
		/** AddIn implementation */
		private AddIn addin;

		/** Windows registry paths */
		private const string assemblyPath = "Software\\Sparx Systems\\EAAddins\\Wrapper\\Assembly";
		private const string addinClassPath = "Software\\Sparx Systems\\EAAddins\\Wrapper\\MainClass";

		/**
		 * Constructor. Creates the AddIn instance.
		 */
		public AddInAdapter() {
			this.addin = this.createAddIn();
		}

		/**
		 * Create an instance of the addin.
		 */
		private AddIn createAddIn() {
			string assembly = this.getRegistryValue(assemblyPath);
			string addinClass = this.getRegistryValue(addinClassPath);
			return (AddIn) this.createInstance(assembly, addinClass);
		}

		/**
		 * Read a value from the windows registry.
		 */
		private string getRegistryValue(string path) {
			RegistryKey key = Registry.CurrentUser.OpenSubKey(path);
			string value = (string) key.GetValue("");
			key.Close();
			return value;
		}

		/**
		 * Create an instance given an assembly name and a class name.
		 */
		private object createInstance(string assembly, string clazz) {
			Type typ = AppDomain.CurrentDomain.Load(assembly).GetType(clazz);
			return Activator.CreateInstance(typ);
		}

		/**
		 * Called when the EA is starting.
		 */
		public void EA_Connect(IRepository repository) {
			this.addin.start();
		}

		/**
		 * Called when the repository is ready.
		 */
		public void EA_OnPostInitialized(IRepository repository) {
			this.addin.initialize(repository);
		}

		/**
		 * Called when EA is closing.
		 */
		public void EA_Disconnect() {
			this.addin.stop();
		}

		/**
		 * Query the menu items from the extension.
		 */
		public object EA_GetMenuItems(IRepository repository, String location, String menu) {
			string[] items = this.addin.getMenuItems(repository, location, menu);
			if (items.Length == 0) return null;
			else if (items.Length == 1) return items[0];
			else return items;
		}

		/**
		 * Query the menu items state from the extension.
		 */
		public void EA_GetMenuState(IRepository repository, String location, String menu, String item, ref bool isEnabled, ref bool isChecked) {
			MenuState state = this.addin.getMenuState(repository, location, menu, item);
			isEnabled = state.isEnabled();
			isChecked = state.isChecked();
		}

		/**
		 * Called when a menu item was clicked.
		 **/
		public void EA_MenuClick(IRepository repository, String location, String menu, String item) {
			this.addin.menuItemClicked(repository, location, menu, item);
		}

		/**
		 * Called when an element is being deleted.
		 */
		public bool EA_OnPreDeleteElement(IRepository repository, IEventProperties properties) {
			return this.addin.deleteElement(repository, properties);
		}

		/**
		 * Called when a package is being deleted.
		 */
		public bool EA_OnPreDeletePackage(IRepository repository, IEventProperties properties) {
			return this.addin.deletePackage(repository, properties);
		}

		/**
		 * Called when a diagram is being deleted.
		 */
		public bool EA_OnPreDeleteDiagram(IRepository repository, IEventProperties properties) {
			return this.addin.deleteDiagram(repository, properties);
		}
	}
}

