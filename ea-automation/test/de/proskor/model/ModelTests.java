package de.proskor.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class ModelTests {
	private Repository repository;

	@Before
	public void initializeRepository() {
		repository = RepositoryProvider.getRepository();
		repository.getModels().clear();
	}

	@Test
	public void testRepository() {
		assertNotNull(repository);
		final Collection<Package> models = repository.getModels();
		assertNotNull(models);
		assertTrue(models.isEmpty());
	}

	@Test
	public void testModelCreation() {
		final String name = "model";
		final Collection<Package> models = repository.getModels();
		final Package model = models.add(name, Package.PACKAGE);
		assertNotNull(model);
		assertTrue(model.isModel());
	    assertEquals(name, model.getName());
	    assertEquals("", model.getDescription());
	    assertEquals("", model.getStereotype());
	    assertEquals(1, models.size());
	    assertTrue(models.contains(model));
	    assertEquals(0, models.indexOf(model));
	    assertEquals(model, models.get(0));
	    assertTrue(model.getPackages().isEmpty());
	}

	@Test
	public void testModelClear() {
		final Collection<Package> models = repository.getModels();
		final Package model = models.add("model", Package.PACKAGE);
		models.clear();
		assertTrue(models.isEmpty());
		assertFalse(models.contains(model));
	}

	@Test
	public void testModelRemoval() {
		final Collection<Package> models = repository.getModels();
		final Package model = models.add("model", Package.PACKAGE);
		models.remove(model);
		assertTrue(models.isEmpty());
		assertFalse(models.contains(model));
	}

	@Test
	public void testModelPropertiesChange() {
		final String name = "newname";
		final String description = "newdescription";
		final Collection<Package> models = repository.getModels();
		final Package model = models.add("model", Package.PACKAGE);
		model.setName(name);
		assertEquals(name, model.getName());
		model.setDescription(description);
		assertEquals(description, model.getDescription());
	}

	@Test
	public void testPackageCreation() {
		final String name = "pkg";
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Collection<Package> packages = model.getPackages();
		final Package pkg = model.getPackages().add(name, Package.PACKAGE);
		assertNotNull(pkg);
		assertFalse(pkg.isModel());
	    assertEquals(name, pkg.getName());
	    assertEquals("", pkg.getDescription());
	    assertEquals("", pkg.getStereotype());
	    assertEquals(1, packages.size());
	    assertTrue(packages.contains(pkg));
	    assertEquals(0, packages.indexOf(pkg));
	    assertEquals(pkg, packages.get(0));
	    assertTrue(pkg.getPackages().isEmpty());
	}

	@Test
	public void testPackageElement() {
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		final Element element = pkg.getElement();
		assertNotNull(element);
	}

	@Test
	public void testPackageRemoval() {
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Collection<Package> packages = model.getPackages();
		final Package pkg = packages.add("pkg", Package.PACKAGE);
		packages.remove(pkg);
		assertTrue(packages.isEmpty());
		assertFalse(packages.contains(pkg));
	}

	@Test
	public void testPackageMove() {
		final Package source = repository.getModels().add("source", Package.PACKAGE);
		final Package target = repository.getModels().add("target", Package.PACKAGE);
		final Package pkg = source.getPackages().add("pkg", Package.PACKAGE);
		pkg.setParent(target);
		assertEquals(target, pkg.getParent());
		assertFalse(source.getPackages().contains(pkg));
		assertTrue(target.getPackages().contains(pkg));
	}

	@Test
	public void testPackageIterator() {
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Collection<Package> packages = model.getPackages();
		final Package pkg = packages.add("pkg", Package.PACKAGE);
		final Package tmp = packages.add("tmp", Package.PACKAGE);
		final Iterator<Package> iterator = packages.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(pkg, iterator.next());
		assertTrue(iterator.hasNext());
		iterator.remove();
		assertTrue(iterator.hasNext());
		assertEquals(tmp, iterator.next());
		assertFalse(iterator.hasNext());
		assertEquals(1, model.getPackages().size());
		assertFalse(packages.contains(pkg));
		assertTrue(packages.contains(tmp));
	}

	@Test
	public void testPackagePropertiesChange() {
		final String name = "newname";
		final String description = "newdescription";
		final String stereotype = "newstereotype";
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		pkg.setName(name);
		assertEquals(name, pkg.getName());
		pkg.setDescription(description);
		assertEquals(description, pkg.getDescription());
		pkg.setStereotype(stereotype);
		assertEquals(stereotype, pkg.getStereotype());
	}

	@Test
	public void testElementCreation() {
		final String name = "element";
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		final Collection<Element> elements = pkg.getElements();
		final Element element = elements.add(name, Element.OBJECT);
		assertNotNull(element);
		assertFalse(element.isChild());
		assertEquals(pkg, element.getPackage());
	    assertEquals(name, element.getName());
	    assertEquals("", element.getDescription());
	    assertEquals("", element.getStereotype());
	    assertEquals(1, elements.size());
	    assertTrue(elements.contains(element));
	    assertEquals(0, elements.indexOf(element));
	    assertEquals(element, elements.get(0));
	    assertTrue(element.getElements().isEmpty());
	}

	@Test
	public void testElementRemoval() {
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		final Collection<Element> elements = pkg.getElements();
		final Element element = elements.add("element", Element.OBJECT);
		elements.remove(element);
		assertTrue(elements.isEmpty());
		assertFalse(elements.contains(element));
	}

	@Test
	public void testElementMove() {
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package source = model.getPackages().add("source", Package.PACKAGE);
		final Package target = model.getPackages().add("target", Package.PACKAGE);
		final Element element = source.getElements().add("element", Element.OBJECT);
		element.setPackage(target);
		assertEquals(target, element.getPackage());
		assertFalse(source.getElements().contains(element));
		assertTrue(target.getElements().contains(element));
	}

	@Test
	public void testElementPropertiesChange() {
		final String name = "newname";
		final String description = "newdescription";
		final String stereotype = "newstereotype";
		final String type = Element.COMPONENT;
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		final Element element = pkg.getElements().add("element", Element.OBJECT);
		element.setName(name);
		assertEquals(name, element.getName());
		element.setDescription(description);
		assertEquals(description, element.getDescription());
		element.setStereotype(stereotype);
		assertEquals(stereotype, element.getStereotype());
		element.setType(type);
		assertEquals(type, element.getType());
	}

	@Test
	public void testDiagramCreation() {
		final String name = "diagram";
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		final Collection<Diagram> diagrams = pkg.getDiagrams();
		final Diagram diagram = diagrams.add(name, Diagram.OBJECT);
		assertNotNull(diagram);
		assertEquals(pkg, diagram.getPackage());
	    assertEquals(name, diagram.getName());
	    assertEquals("", diagram.getDescription());
	    assertEquals("", diagram.getStereotype());
	    assertEquals(1, diagrams.size());
	    assertTrue(diagrams.contains(diagram));
	    assertEquals(0, diagrams.indexOf(diagram));
	    assertEquals(diagram, diagrams.get(0));
	    assertTrue(diagram.getNodes().isEmpty());
	}

	@Test
	public void testDiagramPropertiesChange() {
		final String name = "newname";
		final String description = "newdescription";
		final String stereotype = "newstereotype";
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		final Diagram diagram = pkg.getDiagrams().add("diagram", Diagram.OBJECT);
		diagram.setName(name);
		assertEquals(name, diagram.getName());
		diagram.setDescription(description);
		assertEquals(description, diagram.getDescription());
		diagram.setStereotype(stereotype);
		assertEquals(stereotype, diagram.getStereotype());
	}

	@Test
	public void testDiagramMove() {
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package source = model.getPackages().add("source", Package.PACKAGE);
		final Package target = model.getPackages().add("target", Package.PACKAGE);
		final Diagram diagram = source.getDiagrams().add("diagram", Diagram.OBJECT);
		diagram.setPackage(target);
		assertEquals(target, diagram.getPackage());
		assertFalse(source.getDiagrams().contains(diagram));
		assertTrue(target.getDiagrams().contains(diagram));
	}

	@Test
	public void testNodeCreation() {
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		final Element element = pkg.getElements().add("element", Element.OBJECT);
		final Diagram diagram = pkg.getDiagrams().add("diagram", Diagram.OBJECT);
		final Collection<Node> nodes = diagram.getNodes();
		final Node node = diagram.createNode(element);
		assertNotNull(node);
		assertEquals(element, node.getElement());
		assertEquals(diagram, node.getDiagram());
		assertTrue(nodes.contains(node));
		assertEquals(0, nodes.indexOf(node));
		assertEquals(node, nodes.get(0));
	}

	@Test
	public void testNodeMove() {
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		final Element element = pkg.getElements().add("element", Element.OBJECT);
		final Diagram diagram = pkg.getDiagrams().add("diagram", Diagram.OBJECT);
		final Node node = diagram.createNode(element);
		node.setLeft(10);
		assertEquals(10, node.getLeft());
		node.setTop(10);
		assertEquals(10, node.getTop());
		node.setRight(100);
		assertEquals(100, node.getRight());
		node.setBottom(100);
		assertEquals(100, node.getBottom());
		node.setSequence(20);
		assertEquals(20, node.getSequence());
	}

	@Test
	public void testConnectorCreation() {
		final Package model = repository.getModels().add("model", Package.PACKAGE);
		final Package pkg = model.getPackages().add("pkg", Package.PACKAGE);
		final Collection<Element> elements = pkg.getElements();
		final Element source = elements.add("source", Element.OBJECT);
		final Element target = elements.add("target", Element.OBJECT);
		final Connector connector = source.connectTo(target);
		assertNotNull(connector);
		assertEquals("", connector.getName());
		assertEquals("", connector.getDescription());
		assertEquals(Connector.CONNECTOR, connector.getType());
		assertEquals(Connector.DIRECTION_UNSPECIFIED, connector.getDirection());
		assertEquals(source, connector.getSource());
		assertEquals(target, connector.getTarget());
		assertEquals(1, source.getConnectors().size());
	    assertTrue(source.getConnectors().contains(connector));
	    assertEquals(1, target.getConnectors().size());
	    assertTrue(target.getConnectors().contains(connector));
	}
}
