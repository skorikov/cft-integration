package de.proskor.cft.model.ea
import de.proskor.cft.model.Or
import de.proskor.cft.model.ea.peers.EAPeer

private class EAOr(initialPeer: EAPeer) extends EAGate(initialPeer) with Or