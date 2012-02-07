package de.proskor.emc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.epsilon.commons.util.StringProperties;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolEnumerationValueNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.transactions.IModelTransactionSupport;
import org.eclipse.epsilon.eol.models.transactions.NoModelTransactionSupport;

import de.proskor.model.Model;
import de.proskor.model.Repository;

public class EAModel implements IModel {
	private String name;
	private Repository repository;

	public EAModel(Repository repository) {
		this.repository = repository;
	}

	@Override
	public void load() throws EolModelLoadingException {}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public Object getEnumerationValue(String enumeration, String label) throws EolEnumerationValueNotFoundException {
		throw new EolEnumerationValueNotFoundException(enumeration, "no enumerations", "EA");
	}

	@Override
	public Collection<?> allContents() {
		final Collection<Object> result = new ArrayList<Object>();
		result.add(repository);
		return result;
	}

	@Override
	public Collection<?> getAllOfType(String type) throws EolModelElementTypeNotFoundException {
		return Collections.emptyList();
	}

	@Override
	public Collection<?> getAllOfKind(String type) throws EolModelElementTypeNotFoundException {
		return Collections.emptyList();
	}

	@Override
	public Object getTypeOf(Object instance) {
		return instance.getClass();
	}

	@Override
	public String getTypeNameOf(Object instance) {
		return instance.getClass().getName();
	}

	@Override
	public Object createInstance(String type) throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException {
		throw new EolModelElementTypeNotFoundException("EA", type);
	}

	@Override
	public Object createInstance(String type, Collection<Object> parameters) throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException {
		throw new EolNotInstantiableModelElementTypeException("EA", type);
	}

	@Override
	public Object getElementById(String id) {
		return null;
	}

	@Override
	public String getElementId(Object instance) {
		return null;
	}

	@Override
	public void setElementId(Object instance, String newId) {}

	@Override
	public void deleteElement(Object instance) throws EolRuntimeException {
		throw new EolRuntimeException();
	}

	@Override
	public boolean isOfKind(Object instance, String type) throws EolModelElementTypeNotFoundException {
		if (type.equals("Repository")) {
			return instance instanceof Repository;
		}
		throw new EolModelElementTypeNotFoundException("EA", type);
	}

	@Override
	public boolean isOfType(Object instance, String type) throws EolModelElementTypeNotFoundException {
		if (type == "Repository") {
			return instance instanceof Repository;
		}
		throw new EolModelElementTypeNotFoundException("EA", type);
	}

	@Override
	public boolean owns(Object instance) {
		return instance == repository;
	}

	@Override
	public boolean knowsAboutProperty(Object instance, String property) {
		if (instance instanceof Repository) {
			if (property.equals("models")) return true;
		}
		return false;
	}

	@Override
	public boolean isInstantiable(String type) {
		return type.equals("Package");
	}

	@Override
	public boolean isModelElement(Object instance) {
		return instance instanceof Repository || instance instanceof Model;
	}

	@Override
	public boolean hasType(String type) {
		return type.equals("Repository") || type.equals("Model");
	}

	@Override
	public boolean store(String location) {
		return true;
	}

	@Override
	public boolean store() {
		return true;
	}

	@Override
	public void dispose() {}

	@Override
	public IPropertyGetter getPropertyGetter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertySetter getPropertySetter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStoredOnDisposal() {
		return false;
	}

	@Override
	public void setStoredOnDisposal(boolean storedOnDisposal) {}

	@Override
	public boolean isReadOnLoad() {
		return true;
	}

	@Override
	public void setReadOnLoad(boolean readOnLoad) {}

	@Override
	public IModelTransactionSupport getTransactionSupport() {
		return new NoModelTransactionSupport();
	}

	@Override
	public void load(StringProperties properties, String basePath) throws EolModelLoadingException {}

}
