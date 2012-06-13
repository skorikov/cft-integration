package de.proskor.cft.model.simple

import de.proskor.cft.model.{Component, Gate, Source}

private abstract class SimpleGate(initialName: String) extends SimpleTarget(initialName) with Gate