package de.proskor
import de.proskor.model.Repository

trait Extension {
  def merge(repository: Repository): Unit
  def close(): Unit
  def createDiagram(repository: Repository): Unit
}