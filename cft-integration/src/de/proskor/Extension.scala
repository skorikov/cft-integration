package de.proskor
import de.proskor.model.Repository

trait Extension {
  def start(): Unit
  def stop(): Unit
  def merge(): Unit
  def createDiagram(): Unit
  def failureModes(): Unit
  def printId(): Unit

  def test(): Unit
}