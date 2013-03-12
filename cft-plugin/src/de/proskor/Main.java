package de.proskor;

import de.proskor.extension.AddInBridge;
import de.proskor.extension.Extension;

public class Main extends AddInBridge {
	@Override
	protected Extension createExtension() {
		return new CftExtension();
	}
}
