package de.proskor.cft.emc.test

import java.io.InputStream
import java.io.FileInputStream
import java.util.Scanner
import de.proskor.cft.model.Repository
import de.proskor.cft.model.Element
import org.eclipse.epsilon.eol.models.IModel
import de.proskor.cft.emc.CftModel
import org.eclipse.epsilon.eol.EolModule
import org.eclipse.epsilon.eol.IEolModule

object Main {
  def main(args: Array[String]) {
    val repository = Repository("/")
    val model: IModel = new CftModel(Set[Element](repository))
    model.setName("MODEL")
    val module: IEolModule = new EolModule();
    module.getContext.getModelRepository.addModel(model)
    val is: InputStream = new FileInputStream("/home/andrey/workspaces/misc/emc-model/epsilon/test.eol")
    module.parse(convertStreamToString(is))
    module.execute
  }

  private def convertStreamToString(is: InputStream): String = {
    new Scanner(is).useDelimiter("\\A").next
  }
}
