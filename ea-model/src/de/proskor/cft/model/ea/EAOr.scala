package de.proskor.cft.model.ea
import de.proskor.cft.model.Or
import de.proskor.cft.model.ea.peers.ConnectedPeer

private class EAOr(initialPeer: ConnectedPeer) extends EAGate(initialPeer) with Or