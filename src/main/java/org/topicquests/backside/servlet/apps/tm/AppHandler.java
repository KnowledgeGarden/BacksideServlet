/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.api.IErrorMessages;
import org.topicquests.backside.servlet.apps.BaseHandler;
import org.topicquests.backside.servlet.apps.admin.api.IAdminMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.backside.servlet.apps.usr.api.IUserMicroformat;
import org.topicquests.common.api.IResult;
import org.topicquests.model.api.ITicket;
import org.topicquests.model.api.node.INode;

/**
 * @author park
 *
 */
public class AppHandler  extends BaseHandler {
	private ITopicMapModel model;
	
	/**
	 * 
	 */
	public AppHandler(ServletEnvironment env, String basePath) {
		super(env,basePath);
		model = new TopicMapModel(environment);
	}

	public void handleGet(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		JSONObject returnMessage = newJSONObject();
		String message = "", rtoken="";
		String verb = (String)jsonObject.get(ICredentialsMicroformat.VERB);
		int code = 0;
		IResult r;
		if (verb.equals(IUserMicroformat.LIST_USERS)) {
			String startS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_FROM));
			String countS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_COUNT));
			int start = 0, count = -1;
			if (!startS.equals("")) {
				try {
					start = Integer.valueOf(startS);
				} catch (Exception e1) {}
			}
			if (!countS.equals("")) {
				try {
					count = Integer.valueOf(countS);
				} catch (Exception e2) {}
			}
			//TODO: note: we are ignoring any SORT modifiers
			//This really returns some live cargo in the form of a list of user objects in JSON format
			// We are restricting this to: name, email, avatar, homepage, geolocation, role
			r = model.listUserTopics(start, count, credentials);
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
				message = r.getErrorString();
			} else {
				//Time to take that list apart
				if (r.getResultObject() != null) {
					List<INode> usrs = (List<INode>)r.getResultObject();
					Iterator<INode>itr = usrs.iterator();
					List<JSONObject>jsonUsers = new ArrayList<JSONObject>();
					while (itr.hasNext()) {
						jsonUsers.add((JSONObject)itr.next().getProperties());
					}
					returnMessage.put(ICredentialsMicroformat.CARGO, jsonUsers);
				}
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			}
		} else if (verb.equals(ITopicMapMicroformat.GET_TOPIC)) {
			String locator = notNullString((String)jsonObject.get(ITopicMapMicroformat.TOPIC_LOCATOR));
			r = model.getTopic(locator, credentials);
			if (r.getResultObject() != null) {
				INode n = (INode)r.getResultObject();
				returnMessage.put(ICredentialsMicroformat.CARGO, (JSONObject)n.getProperties());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			}
		}  else {
			String x = IErrorMessages.BAD_VERB+"-UserServletGet-"+verb;
			environment.logError(x, null);
			throw new ServletException(x);
		}
		returnMessage.put(ICredentialsMicroformat.RESP_TOKEN, rtoken);
		returnMessage.put(ICredentialsMicroformat.RESP_MESSAGE, message);
		super.sendJSON(returnMessage.toJSONString(), code, response);
		returnMessage = null;
	}
	
	
	public void handlePost(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		JSONObject returnMessage = newJSONObject();
		String message = "", rtoken="";
		String verb = (String)jsonObject.get(ICredentialsMicroformat.VERB);
		int code = 0;
		IResult r;
		if (verb.equals(ITopicMapMicroformat.PUT_TOPIC)) {
			//TODO
		} else if (verb.equals(ITopicMapMicroformat.NEW_INSTANCE_TOPIC)) {
			JSONObject theTopic = (JSONObject)jsonObject.get(ICredentialsMicroformat.CARGO);
			if (theTopic != null) {
				r = model.newInstanceNode(theTopic);
				returnMessage.put(ICredentialsMicroformat.CARGO, (JSONObject)r.getResultObject());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				String x = IErrorMessages.MISSING_TOPIC+"-TMServletPost-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			}
		} else if (verb.equals(ITopicMapMicroformat.NEW_SUBCLASS_TOPIC)) {
			JSONObject theTopic = (JSONObject)jsonObject.get(ICredentialsMicroformat.CARGO);
			if (theTopic != null) {
				r = model.newSubclassNode(theTopic);
				returnMessage.put(ICredentialsMicroformat.CARGO, (JSONObject)r.getResultObject());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				String x = IErrorMessages.MISSING_TOPIC+"-TMServletPost-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			}
		} else if (verb.equals(ITopicMapMicroformat.REMOVE_TOPIC)) {
			//TODO
	//	} else if (verb.equals(ITopicMapMicroformat.PUT_TOPIC)) {
			
			
		} else {
			String x = IErrorMessages.BAD_VERB+"-AdminServletPost-"+verb;
			environment.logError(x, null);
			throw new ServletException(x);
		}
		returnMessage.put(ICredentialsMicroformat.RESP_TOKEN, rtoken);
		returnMessage.put(ICredentialsMicroformat.RESP_MESSAGE, message);
		super.sendJSON(returnMessage.toJSONString(), code, response);
		returnMessage = null;	}
	
	public void shutDown() {
		model.shutDown();
	}

}
