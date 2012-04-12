package de.proskor.cft.model.ea

import de.proskor.cft.model.Outport
import de.proskor.cft.model.ea.peers.ElementPeer

class EAOutport(var peer: ElementPeer) extends EAPort with Outport