package ut.no.steria.jira.confluence.plugins.sdkintro;

import org.junit.Test;
import no.steria.jira.confluence.plugins.sdkintro.MyPluginComponent;
import no.steria.jira.confluence.plugins.sdkintro.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}