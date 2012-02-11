package de.proskor
import de.proskor.model.Repository

trait Extension {
  def start(repository: Repository): Unit
  def close(): Unit
  def merge(repository: Repository): Unit
  def createDiagram(repository: Repository): Unit
  def failureModes(repository: Repository): Unit
  def printId(repository: Repository): Unit
}