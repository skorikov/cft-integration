package de.proskor.cft.test

import de.proskor.automation._
import org.junit.Assert.{assertEquals, assertNotNull, assertFalse, assertTrue}
import org.junit.{AfterClass, Before, BeforeClass, Test, Ignore}
import de.proskor.cft.model.ea.peers.Peer
import de.proskor.cft.model.ea.peers.impl.EAProxyPeer
import de.proskor.cft.model.ea.peers.impl.EAElementPeer
import de.proskor.cft.model.ea.peers.ElementPeer
import de.proskor.automation.impl.dummy.DummyElement

class PeerTests {
  @Before
  def clear() = PeerTests.clearRepository()

  @Test
  def testProxy() {
    val name = "name"
    val stereotype = "stereotype"
    val peer: Peer = new EAProxyPeer(name, stereotype)

    assertTrue(peer.isProxy)
    assertEquals(peer.name, name)
    assertEquals(peer.stereotype, stereotype)
  }

  @Test
  def testElementPeer() {
    val peer: ElementPeer = new EAElementPeer(new DummyElement(null, null, "name"))
  }
}

object PeerTests {
  @BeforeClass
  def configure() {
    Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
  }

  @AfterClass
  def clearRepository() {
    Repository.instance.models.clear()
  }
}