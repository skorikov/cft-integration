package de.proskor.emc.test

import de.proskor.emc.cft.CftModel
import de.proskor.cft.model.{Factory, Repository}
import org.eclipse.epsilon.eol.models.IModel

object CftTest extends AbstractTest {
  private lazy val repository = Repository("/")

  override def configure() {
    de.proskor.automation.Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
    Factory.use(de.proskor.cft.model.ea.EAFactory)
  }

  override def model: IModel = {
    val result = new CftModel(Set(repository))
    result.setName("CFT")
    result
  }

  override def finish() {
    println(format(repository))
  }

  override def code: String = "/home/andrey/git/cft-integration/emc-model/epsilon/cft.eol"
}
