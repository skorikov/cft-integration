package de.proskor.cft.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AutomationTests.class, AdapterTests.class, CftTests.class, PeerTests.class})
public class ModelTests {}
