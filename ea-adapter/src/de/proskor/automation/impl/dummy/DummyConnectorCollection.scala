package de.proskor.automation.impl.dummy

import de.proskor.automation.Element
import de.proskor.automation.Connector

class DummyConnectorCollection(parent: Element) extends DummyCollection(parent, (name: String, typ: String, p: Element) => new DummyConnector(name, p).asInstanceOf[Connector]) {
  def add(connector: Connector) {
    contents += connector
  }
}