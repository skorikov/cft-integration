package de.proskor.automation.impl.dummy

import de.proskor.automation.{Connector, Element}

class DummyConnector(var name: String, parent: Element) extends Connector {
  private var _source: Element = null
  private var _target: Element = null

  val id: Int = IdGenerator.next
  var stereotype: String = ""

  def source: Element = _source
  def source_=(element: Element) {
    if (_source != null)
      _source.connectors.asInstanceOf[DummyConnectorCollection].remove(this)
    _source = element
    _source.connectors.asInstanceOf[DummyConnectorCollection].add(this)
  }
  def target: Element = _target
  def target_=(element: Element) {
    if (_target != null)
      _target.connectors.asInstanceOf[DummyConnectorCollection].remove(this)
    _target = element
    _target.connectors.asInstanceOf[DummyConnectorCollection].add(this)
  }
}