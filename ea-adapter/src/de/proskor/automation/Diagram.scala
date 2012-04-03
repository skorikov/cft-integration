package de.proskor.automation

import cli.EA.IDiagram

class Diagram(peer: IDiagram) {
  def id: Int = peer.get_DiagramID

  def name: String = peer.get_Name.asInstanceOf[String]
  def name_=(name: String): Unit = peer.set_Name(name)

  def stereotype: String = peer.get_Stereotype.asInstanceOf[String]
  def stereotype_(stereotype: String): Unit = peer.set_Stereotype(stereotype)
}