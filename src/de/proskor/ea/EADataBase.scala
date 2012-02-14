package de.proskor.ea
import java.io.File
import java.io.FileInputStream
import java.net.URI
import scala.io.Source
import de.proskor.ea.model.fel.EABasicFailureMode
import de.proskor.ea.model.fel.EAInputFailureMode
import de.proskor.ea.model.fel.EAOutputFailureMode
import de.proskor.model.fel.FailureMode
import de.proskor.model.Element
import de.proskor.model.Repository
import de.proskor.ea.model.EARepository
import java.io.PrintWriter
import de.proskor.model.fel.BasicFailureMode
import de.proskor.model.fel.InputFailureMode
import de.proskor.model.fel.OutputFailureMode

object EADataBase {
  var dataFile = System.getProperty("user.home") + System.getProperty("file.separator") + "ea.fm";
  var cache: Option[Seq[FailureMode]] = None

  def loadFailureModes() {
    val repository = Repository.getCurrent
    val lines = for (line <- Source.fromFile(dataFile).getLines.map(_.split(";"))) yield line(1) match {
      case "basic" => new EABasicFailureMode(repository.get(line(0).toInt), line(2), line(3), line(4))
      case "input" => new EAInputFailureMode(repository.get(line(0).toInt), line(2), line(3), line(4))
      case "output" => new EAOutputFailureMode(repository.get(line(0).toInt), line(2), line(3), line(4))
    }
    cache = Some(lines.toSeq)
  }

  def getCache: Seq[FailureMode] = cache match {
    case None => loadFailureModes(); cache.get
    case Some(list) => list
  }

  def failureModes(element: Element): Seq[FailureMode] = getCache.filter(_.element == element)

  def setFailureModes(element: Element, fmodes: Seq[FailureMode]) {
    val list = getCache.filterNot(_.element == element) ++ fmodes
    cache = Some(list)
  }

  def saveFailureModes() {
    cache foreach {
      case list => {
        val writer = new PrintWriter(dataFile)
        writer.write(list.map({
          case bfm: BasicFailureMode => Array(bfm.element.id, "basic", bfm.name, bfm.description, bfm.author).mkString(";")
          case ifm: InputFailureMode => Array(ifm.element.id, "input", ifm.name, ifm.description, ifm.author).mkString(";")
          case ofm: OutputFailureMode => Array(ofm.element.id, "output", ofm.name, ofm.description, ofm.author).mkString(";")
        }).mkString("\n"))
        writer.close()
      }
    }
  }
}