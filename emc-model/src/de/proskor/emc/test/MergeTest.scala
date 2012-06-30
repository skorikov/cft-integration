package de.proskor.emc.test

//import org.eclipse.epsilon.ecl.IEclModule
//import org.eclipse.epsilon.ecl.EclModule
import java.util.Scanner
import java.io.InputStream
import java.io.FileInputStream
import de.proskor.emc.cft.CftModel
import de.proskor.cft.model.Repository
import de.proskor.cft.model.Package
import de.proskor.cft.model.Component
//import org.eclipse.epsilon.eml.EmlModule
import org.eclipse.epsilon.eol.execute.context.Variable
import de.proskor.cft.model.Event

object MergeTest {
  /*def main(args: Array[String]) {
    val module: IEclModule = new EclModule
    val is: InputStream = new FileInputStream("/home/andrey/git/cft-integration/emc-model/epsilon/merge.ecl")
    module.parse(convertStreamToString(is))

    val repository = Repository("/")
    val model = Package(repository, "MODEL")
    val pkg = Package(model, "PKG")
    val left = Component(pkg, "C1")
    val leftEvent = Event(left, "B1")
    val right = Component(pkg, "C1")
    val rightEvent = Event(right, "B1")
    val targetPkg = Package(model, "TARGET")

    val leftModel = new CftModel(Set(left)); leftModel.setName("LEFT")
    val rightModel = new CftModel(Set(right)); rightModel.setName("RIGHT")
    val resultModel = new CftModel(Set(repository)); resultModel.setName("RESULT")
    module.getContext.getModelRepository.addModel(leftModel)
    module.getContext.getModelRepository.addModel(rightModel)

    module.execute

    val matches = module.getContext.getMatchTrace

    val merger = new EmlModule
    val ism: InputStream = new FileInputStream("/home/andrey/git/cft-integration/emc-model/epsilon/merge.eml")
    merger.parse(convertStreamToString(ism))

    merger.getContext.getModelRepository.addModel(leftModel)
    merger.getContext.getModelRepository.addModel(rightModel)
    merger.getContext.getModelRepository.addModel(resultModel)
    merger.getContext.setMatchTrace(matches)
    merger.getContext.getFrameStack.putGlobal(Variable.createReadOnlyVariable("target", targetPkg))

    merger.execute

    println("done")
  }*/

  protected def convertStreamToString(is: InputStream): String = {
    new Scanner(is).useDelimiter("\\A").next
  }
}