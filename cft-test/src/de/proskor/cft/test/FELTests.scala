package de.proskor.cft.test

import org.junit.Assert.{assertEquals, assertNotNull, assertFalse, assertTrue}
import org.junit.{AfterClass, Before, BeforeClass, Test, Ignore}
import de.proskor.fel.EventRepository
import de.proskor.fel.stub.EventRepositoryImpl

class FELTests {
//  @Before
//  def clear() {}

  @Test
  def testFEL() {
    
  }

  private def eventRepository: EventRepository = new EventRepositoryImpl
}

object FELTests {
//  @BeforeClass
//  def configure() {
//    Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
//  }
}