using System;
using System.Reflection;
using Microsoft.Win32;
using EA;
using de.proskor.automation;

namespace de.proskor.integration {
	public class Main {
		private AddIn addin;
		private const string assemblyPath = "Software\\Sparx Systems\\EAAddins\\Wrapper\\Assembly";
		private const string addinClassPath = "Software\\Sparx Systems\\EAAddins\\Wrapper\\MainClass";

		public Main() {
			this.addin = this.createAddIn();
		}

		private AddIn createAddIn() {
			string assembly = this.getRegistryValue(assemblyPath);
			string addinClass = this.getRegistryValue(addinClassPath);
			return (AddIn) this.createInstance(assembly, addinClass);
		}

		private string getRegistryValue(string path) {
			RegistryKey key = Registry.CurrentUser.OpenSubKey(path);
			string value = (string) key.GetValue("");
			key.Close();
			return value;
		}

		private object createInstance(string assembly, string clazz) {
			Type typ = AppDomain.CurrentDomain.Load(assembly).GetType(clazz);
			return Activator.CreateInstance(typ);
		}

		public void EA_Connect(IRepository repository) {
			this.addin.start();
		}

		public void EA_OnPostInitialized(IRepository repository) {
			this.addin.initialize(repository);
		}

		public void EA_Disconnect() {
			this.addin.stop();
		}

		public object EA_GetMenuItems(IRepository repository, String location, String menu) {
			string[] items = this.addin.getMenuItems(repository, location, menu);
			if (items.Length == 0) return null;
			else if (items.Length == 1) return items[0];
			else return items;
		}

		public void EA_GetMenuState(IRepository repository, String location, String menu, String item, ref bool isEnabled, ref bool isChecked) {
			MenuState state = this.addin.getMenuState(repository, location, menu, item);
			isEnabled = state.isEnabled();
			isChecked = state.isChecked();
		}

		public void EA_MenuClick(IRepository repository, String location, String menu, String item) {
			this.addin.menuItemClicked(repository, location, menu, item);
		}
	}
}

