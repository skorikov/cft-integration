package de.proskor.automation.impl.dummy

import de.proskor.automation.Package

class Cache[T] {
  var nextId: Int = 1
  private var map: Map[Int, T] = Map()

  def put(element: T): Int = {
    map += (nextId -> element)
    nextId += 1
    nextId - 1
  }

  def get(id: Int): T = map(id)
}

object PackageCache extends Cache[Package]