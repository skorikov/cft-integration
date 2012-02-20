package de.proskor.cft.model.ea
import de.proskor.cft.model.ea.peers.EAPeer
import de.proskor.cft.model.Outport

private class EAOutport(initialPeer: EAPeer) extends EAPort(initialPeer) with Outport