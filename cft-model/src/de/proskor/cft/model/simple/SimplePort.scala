package de.proskor.cft.model.simple

import de.proskor.cft.model.{Component, Port, Source}

private class SimplePort(initialName: String) extends SimpleTarget(initialName) with Port