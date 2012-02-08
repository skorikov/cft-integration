package de.proskor.core
import de.proskor.model.Diagram
import de.proskor.model.cft.Component
import de.proskor.model.Node
import de.proskor.model.Element
import de.proskor.ea.model.cft.EAComponent
import de.proskor.ea.model.EARepository
import de.proskor.model.cft.Source
import de.proskor.model.cft.Gate
import de.proskor.model.cft.Input
import de.proskor.model.cft.Port
import de.proskor.model.cft.Event

object DiagramCreator {

  var mainTop = 40
  var mainLeft = 40
  var horizontalMargin = 40
  var verticalMargin = 80
  var horizontalSpacing = 40
  var verticalSpacing = 80
  var portSpacing = 100
  var subHeight = 80
  var gateHeight = 40
  var gateWidth = 40
  var eventHeight = 40
  var eventWidth = 40

  def calculateElementSize(element: Element): (Int, Int) = element match {
    case component: Component => ((component.inputs.size max component.outputs.size) * portSpacing, subHeight)
    case gate: Gate => (gateWidth, gateHeight)
    case event: Event => (eventWidth, eventHeight)
  }

  def calculateLayerSize(layer: Seq[Element]): (Int, Int) = {
    layer.map(calculateElementSize).foldLeft((horizontalMargin * 2 - horizontalSpacing, 0))((left, right) => (left._1 + horizontalSpacing + right._1, left._2 max right._2))
  }

  def calculateComponentSize(layers: Seq[Seq[Element]]): (Int, Int) = {
    layers.map(calculateLayerSize).foldLeft((0, verticalMargin * 2 - verticalSpacing))((top, bottom) => (top._1 max bottom._1, top._2 + verticalSpacing + bottom._2))
  }

  def create(component: Component, diagram: Diagram) {
    val repository = new EARepository(component.asInstanceOf[EAComponent].repository)
    val kids: Seq[Element] = (component.gates ::: component.components ::: component.events)

    def depth(element: Element, set: Set[Element]): Int = element match {
      case source: Source => {
        if (source.targets.toSet.map(targetElement).intersect(set).isEmpty) 0
        else source.targets.toSet.map(targetElement).intersect(set).map(depth(_, set)).max + 1
      }
      case component: Component => {
        if (component.outputs.isEmpty) 0
        else component.outputs.map(output => depth(output, set)).max
      }
    }

    def targetElement(element: Element) = element match {
      case gate: Gate => gate
      case port: Port => port.parent.get.asInstanceOf[Element]
    }

    val layers = kids.groupBy(depth(_, kids.toSet))
    val numLayers = layers.keys.size
    val (width, height) = calculateComponentSize(layers.values.toSeq)

    val componentNode = diagram.addNode
    componentNode.element = component
    componentNode.sequence = 2
    componentNode.left = mainLeft
    componentNode.top = mainTop
    componentNode.width = width
    componentNode.height = height

    addPortsToComponent(componentNode, diagram)

    var topOffset = mainTop + verticalMargin
    for (i <- 0 until numLayers; layer = layers(i)) {
      val (layerWidth, layerHeight) = calculateLayerSize(layer)
      var leftOffset = mainLeft + horizontalMargin + (width - layerWidth) / 2
      for (element <- layer) {
        val (elementWidth, elementHeight) = calculateElementSize(element)
        val x = leftOffset
        val y = topOffset + (layerHeight - elementHeight) / 2
        val node = createNode(element, diagram, x, y, elementWidth, elementHeight)
        node.element match {
          case component: Component => addPortsToComponent(node, diagram)
          case _ =>
        }
        leftOffset += (elementWidth + horizontalSpacing)
      }
      topOffset += (layerHeight + verticalSpacing)
    }
  }

  def addPortsToComponent(node: Node, diagram: Diagram) {
    val component = node.element.asInstanceOf[Component]
    val width = node.width
    val top = node.top
    val left = node.left
    val bottom = node.top + node.height
    val istep = width / (component.inputs.size + 1)
    val ostep = width / (component.outputs.size + 1)
    for (i <- 0 until component.inputs.size) {
      val input = component.inputs(i)
      val node = diagram.addNode
      node.sequence = 1
      node.element = input
      node.left = left + (i + 1) * istep - 7
      node.top = bottom - 7
      node.width = 20
      node.height = 20
    }
    for (i <- 0 until component.outputs.size) {
      val output = component.outputs(i)
      val node = diagram.addNode
      node.sequence = 1
      node.element = output
      node.left = left + (i + 1) * ostep - 7
      node.top = top - 7
      node.width = 20
      node.height = 20
    }
  }

  def createNode(element: Element, diagram: Diagram, x: Int, y: Int, width: Int, height: Int): Node = {
    val node = diagram.addNode
    node.sequence = 1
    node.element = element
    node.left = x
    node.top = y
    node.width = width
    node.height = height
    node
  }
}