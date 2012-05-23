package de.proskor.cft.model.ea.peers.impl

import de.proskor.cft.model.ea.peers._

class EAProxyPeer(var name: String, var stereotype: String) extends ProxyPeer {
  def id: Int = throw new IllegalStateException
  def elements: Set[ElementPeer] = throw new IllegalStateException
  def connect(element: ElementPeer): Unit = throw new IllegalStateException
  def disconnect(element: ElementPeer): Unit = throw new IllegalStateException
  def inputs: Set[ElementPeer] = throw new IllegalStateException
  def add(element: ElementPeer): ElementPeer = throw new IllegalStateException
  def remove(element: ElementPeer): ElementPeer = throw new IllegalStateException
  def packages: Set[PackagePeer] = throw new IllegalStateException
  def container: Option[PackagePeer] = throw new IllegalStateException
  def add(element: PackagePeer): PackagePeer = throw new IllegalStateException
  def remove(element: PackagePeer): PackagePeer = throw new IllegalStateException
}