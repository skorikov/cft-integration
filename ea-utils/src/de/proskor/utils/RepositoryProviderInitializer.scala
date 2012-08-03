package de.proskor.utils

import de.proskor.extension.Extension
import de.proskor.model.Repository
import de.proskor.model.RepositoryProvider

trait RepositoryProviderInitializer extends Extension {
  abstract override def initialize(repository: Repository) {
    super.initialize(repository)
    RepositoryProvider.setRepository(repository);
  }
}