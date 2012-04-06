package de.proskor.cft.model.ea
import de.proskor.cft.model.ea.peers.ConnectedPeer
import de.proskor.cft.model.Outport

private class EAOutport(initialPeer: ConnectedPeer) extends EAPort(initialPeer) with Outport