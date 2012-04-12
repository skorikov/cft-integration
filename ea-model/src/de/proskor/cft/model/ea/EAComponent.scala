package de.proskor.cft.model.ea

import de.proskor.cft.model.Container
import de.proskor.cft.model.Element
import de.proskor.cft.model.Event
import de.proskor.cft.model.Component
import de.proskor.cft.model.Gate
import de.proskor.cft.model.Outport
import de.proskor.cft.model.Inport
import de.proskor.cft.model.ea.peers.Peer
import de.proskor.cft.model.ea.peers.ElementPeer
import de.proskor.cft.model.ea.peers.ProxyPeer

class EAComponent(var peer: ElementPeer) extends ElementPeered with Component {
  override def elements: Set[Element] = for {
    peer <- peer.elements
    stereotype = peer.stereotype
  } yield stereotype match {
    case "Event" => new EAEvent(peer)
    case "AND" => new EAAnd(peer)
    case "OR" => new EAOr(peer)
    case "Inport" => new EAInport(peer)
    case "Outport" => new EAOutport(peer)
    case "Component" => new EAComponent(peer)
  }

  override def parent: Option[Container] =
    if (peer.isProxy) None
    else Some(peer.parent.map(new EAComponent(_)).getOrElse(new EAPackage(peer.pkg)))

  override def events: Set[Event] = elementsWithStereotype("Event").map(new EAEvent(_))
  override def gates: Set[Gate] = elements.filter(_.isInstanceOf[Gate]).asInstanceOf[Set[Gate]]
  override def components: Set[Component] = elements.filter(_.isInstanceOf[Component]).asInstanceOf[Set[Component]]
  override def inports: Set[Inport] = elements.filter(_.isInstanceOf[Inport]).asInstanceOf[Set[Inport]]
  override def outports: Set[Outport] = elements.filter(_.isInstanceOf[Outport]).asInstanceOf[Set[Outport]]

  override def add(element: Element) = element match {
    case ea: ElementPeered => ea.peer = peer.add(ea.peer)
  }
  override def remove(element: Element) = element match {
    case ea: ElementPeered => ea.peer = peer.remove(ea.peer)
  }
}