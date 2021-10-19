package com.spring.kodo.unittest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountServiceImplUnitTest.class,
        ContentServiceImplUnitTest.class,
        CourseServiceImplUnitTest.class
})
public class UnitTest
{
}
