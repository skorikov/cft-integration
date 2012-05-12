package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Package}

class DummyPackageCollection(parent: Package) extends Collection[Package] {
  private var packages: Set[Package] = Set()

  override def add(name: String, typ: String): Package = {
    val id = PackageCache.nextId
    val pkg = new DummyPackage(Option(parent), id, name)
    packages += pkg
    PackageCache.put(pkg)
    pkg
  }

  override def delete(element: Package) = element match {
    case pkg: DummyPackage => packages -= pkg
  }

  override def contains(element: Package): Boolean = element match {
    case pkg: DummyPackage => packages.contains(pkg)
  }

  override def clear() {
    packages = Set()
  }

  override def iterator: Iterator[Package] = packages.iterator
}