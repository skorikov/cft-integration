package de.proskor.ea

trait Extension extends Writable {
  def start(): Unit
  def stop(): Unit
  def merge(): Unit
  def tests(): Unit
}