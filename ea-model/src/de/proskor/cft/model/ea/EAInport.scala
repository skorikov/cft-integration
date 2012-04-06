package de.proskor.cft.model.ea
import de.proskor.cft.model.ea.peers.ConnectedPeer
import de.proskor.cft.model.Inport

private class EAInport(initialPeer: ConnectedPeer) extends EAPort(initialPeer) with Inport