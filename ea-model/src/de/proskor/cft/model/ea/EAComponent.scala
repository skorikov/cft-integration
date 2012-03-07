package de.proskor.cft.model.ea
import de.proskor.cft.model._
import de.proskor.cft.model.ea.peers._

private class EAComponent(initialPeer: EAPeer) extends EAElement(initialPeer) with Component {
  def elements: Set[Element] = peer.elements.map(EAFactory.create)
  def components: Set[Component] = peer.elementsOfType("Component").map(EAFactory.create).asInstanceOf[Set[Component]]
  def gates: Set[Gate] = peer.elementsOfType("AND", "OR").map(EAFactory.create).asInstanceOf[Set[Gate]]
  def outports: Set[Outport] = peer.elementsOfType("Output").map(EAFactory.create).asInstanceOf[Set[Outport]]
  def inports: Set[Inport] = peer.elementsOfType("Input").map(EAFactory.create).asInstanceOf[Set[Inport]]
  def events: Set[Event] = peer.elementsOfType("Event").map(EAFactory.create).asInstanceOf[Set[Event]]

  def add(element: Element) {
    require(element.isInstanceOf[EAElement])
    val el = element.asInstanceOf[EAElement]
    element.parent foreach {
      case container => container -= element
    }
    el.peer = peer.addElement(el.peer.name, el.peer.stereotype)
  }

  def remove(element: Element) {
    require(element.isInstanceOf[EAElement])
    val el = element.asInstanceOf[EAElement]
    el.peer match {
      case peer: EAProxyPeer =>
      case elPeer: EAElementPeer => peer.deleteElement(elPeer); el.peer = new EAProxyPeer(elPeer)
    }
  }
}