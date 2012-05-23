package de.proskor.emc.test

import de.proskor.emc.automation.AutomationModel
import de.proskor.automation.Repository
import org.eclipse.epsilon.eol.models.IModel

object AutomationTest extends AbstractTest {
  private lazy val repository = Repository.instance

  override def configure() {
    de.proskor.automation.Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
  }

  override def code: String = "/home/andrey/git/cft-integration/emc-model/epsilon/automation.eol"

  override def model: IModel = {
    val result = new AutomationModel(Set(repository))
    result.setName("MODEL")
    result
  }

  override def finish() {}
}