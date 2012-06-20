package de.proskor.cft.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ModelTests.class, MergeTests.class, RedundancyTests.class, FELAdapterTests.class})
public class AllTests {}
