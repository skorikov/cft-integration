package de.proskor.ea.model
import de.proskor.model.Model

class EAModel(model: cli.EA.IPackage, repository: cli.EA.IRepository) extends EAPackage(model, repository) with Model