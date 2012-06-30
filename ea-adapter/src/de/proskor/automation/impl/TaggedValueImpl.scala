package de.proskor.automation.impl

import de.proskor.automation.TaggedValue
import cli.EA.ITaggedValue
import de.proskor.automation.Element

class TaggedValueImpl(peer: ITaggedValue) extends TaggedValue {
  override def id: Int = peer.get_PropertyID
  override def guid: String = peer.get_PropertyGUID.asInstanceOf[String]

  override def element: Element = new ElementImpl(RepositoryImpl.getElementById(peer.get_ElementID))

  override def name: String = peer.get_Name.asInstanceOf[String]
  override def name_=(name: String) {
    peer.set_Name(name)
    peer.Update()
  }

  override def value: String = peer.get_Value.asInstanceOf[String]
  override def value_=(text: String) {
    peer.set_Value(text)
    peer.Update()
  }

  override def description: String = peer.get_Notes.asInstanceOf[String]
  override def description_=(description: String) {
    peer.set_Notes(description)
    peer.Update()
  }
}