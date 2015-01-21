package no.steria.jira.confluence.plugins.sdkintro.macros;

import java.util.Map;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;

public class HelloJiraIssueMacro implements Macro {

	@Override
	public String execute(Map<String, String> parameters, String body, ConversionContext context) throws MacroExecutionException {
	        String jiraIssueKey = parameters.get("jiraIssueKey");
	        return "<b>HelloJiraIssue macro for issue: " + jiraIssueKey + "</b>";
	}

	@Override
	public BodyType getBodyType() {
	        return BodyType.NONE;
	}

	@Override
	public OutputType getOutputType() {
	        return OutputType.BLOCK;
	}

}
