package de.proskor.cft.model.ea
import de.proskor.cft.model.And
import de.proskor.cft.model.ea.peers.ConnectedPeer

private class EAAnd(initialPeer: ConnectedPeer) extends EAGate(initialPeer) with And