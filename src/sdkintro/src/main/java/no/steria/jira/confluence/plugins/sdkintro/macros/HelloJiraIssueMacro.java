package no.steria.jira.confluence.plugins.sdkintro.macros;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.applinks.api.EntityLink;
import com.atlassian.applinks.api.EntityLinkService;
import com.atlassian.applinks.api.application.jira.JiraProjectEntityType;
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.sal.api.net.Request.MethodType;
import com.atlassian.sal.api.net.ResponseException;

public class HelloJiraIssueMacro implements Macro {
	private SpaceManager spaceManager;
	private EntityLinkService entityLinkService;

	public HelloJiraIssueMacro(SpaceManager spaceManager, EntityLinkService entityLinkService) {
	        this.entityLinkService = entityLinkService;
	        this.spaceManager = spaceManager;
	}

	@Override
	public String execute(Map<String, String> parameters, String body, ConversionContext context) throws MacroExecutionException {
	        String jiraIssueKey = parameters.get("jiraIssueKey");
	        if (null == jiraIssueKey) {
	                throw new MacroExecutionException("A jiraIssueKey referencing a valid Jira issue is required");
	        }

	        // Find the factory that creates requests
	        Space currentSpaceForPageWithMacro = spaceManager.getSpace(context.getSpaceKey());
	        EntityLink jiraProjectLink = entityLinkService.getPrimaryEntityLink(currentSpaceForPageWithMacro, JiraProjectEntityType.class);
	        if (null == jiraProjectLink) {
	                throw new MacroExecutionException("No application link from the Confluence space \"" + currentSpaceForPageWithMacro.getName() + "\" to a Jira project!");
	        }
	        ApplicationLinkRequestFactory jiraRequestFactory = jiraProjectLink.getApplicationLink().createAuthenticatedRequestFactory();

	        // Do a REST API call to fetch the data of the issue of the macro parameter
	        String jiraIssueRESTEndpoint = "/rest/api/2/issue/" + jiraIssueKey;
	        String jiraIssueJson = null;
	        try {
	                ApplicationLinkRequest jiraIssueGetRequest = jiraRequestFactory.createRequest(MethodType.GET, jiraIssueRESTEndpoint);
	                jiraIssueJson = jiraIssueGetRequest.execute();
	                JSONObject jiraIssue = new JSONObject(jiraIssueJson);
	                JSONObject jiraIssueFields = jiraIssue.getJSONObject("fields");

	                String jiraIssueDescription = jiraIssueFields.getString("description");

	                return "<b>Description for issue " + jiraIssueKey + ":</b> " + jiraIssueDescription + "<br/>";
	        } catch (CredentialsRequiredException e) {
	                // Re-throw as a MacroExecutionException, which is what Confluence expects.
	                throw new MacroExecutionException(e);
	        } catch (ResponseException e) {
	                // Re-throw as a MacroExecutionException, which is what Confluence expects.
	                throw new MacroExecutionException(e);
	        } catch (JSONException e) {
	                String msg = "Problems parsing response as JSON: " + e.getMessage() + "  received JSON: " + jiraIssueJson;
	                throw new MacroExecutionException(msg);
	        }
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
