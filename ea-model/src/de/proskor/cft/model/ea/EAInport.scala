package de.proskor.cft.model.ea
import de.proskor.cft.model.ea.peers.EAPeer
import de.proskor.cft.model.Inport

private class EAInport(initialPeer: EAPeer) extends EAPort(initialPeer) with Inport