package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Package}

class DummyPackageCollection(parent: Package) extends Collection[Package] {
  private var packages: Map[Int, Package] = Map()

  override def add(name: String, typ: String): Package = {
    val id = if (packages.isEmpty) 1 else packages.keys.max + 1
    val pkg = new DummyPackage(Option(parent), id, name)
    packages += (id -> pkg)
    pkg
  }

  override def delete(element: Package) = element match {
    case pkg: DummyPackage => packages -= pkg.id
  }

  override def contains(element: Package): Boolean = element match {
    case pkg: DummyPackage => packages.keySet.contains(pkg.id)
  }

  override def clear() {
    packages = Map()
  }

  override def iterator: Iterator[Package] = new Iterator[Package] {
    val keys: Seq[Int] = packages.keys.toSeq
    var current: Int = -1
    override def next: Package = {
      current += 1
      packages(keys(current))
    }
    override def hasNext: Boolean = current + 1 < packages.size
  }
}