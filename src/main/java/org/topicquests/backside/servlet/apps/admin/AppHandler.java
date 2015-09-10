/**
 * 
 */
package org.topicquests.backside.servlet.apps.admin;

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
import org.topicquests.backside.servlet.apps.admin.api.IAdminModel;
import org.topicquests.backside.servlet.apps.usr.UserModel;
import org.topicquests.backside.servlet.apps.usr.api.IUserMicroformat;
import org.topicquests.common.api.IResult;
import org.topicquests.model.api.ITicket;

/**
 * @author park
 *
 */
public class AppHandler  extends BaseHandler {
	private IAdminModel model;
	/**
	 * 
	 */
	public AppHandler(ServletEnvironment env, String basePath) {
		super(env,basePath);
		try {
			model = new AdminModel(environment);
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
	}

	public void handleGet(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		JSONObject returnMessage = newJSONObject();
		String message = "", rtoken="";
		String verb = (String)jsonObject.get(ICredentialsMicroformat.VERB);
		int code = 0;
		IResult r;
		String startS, countS;
		int start, count;
		if (verb.equals(IAdminMicroformat.LIST_USERS)) {
			startS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_FROM));
			countS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_COUNT));
			start = 0;
			count = -1;
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
			r = model.listUsers(start, count);
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
				message = r.getErrorString();
			} else {
				//Time to take that list apart
				if (r.getResultObject() != null) {
					List<ITicket> usrs = (List<ITicket>)r.getResultObject();
					Iterator<ITicket>itr = usrs.iterator();
					List<JSONObject>jsonUsers = new ArrayList<JSONObject>();
					while (itr.hasNext()) {
						jsonUsers.add(ticketToUser(itr.next()));
					}
					returnMessage.put(ICredentialsMicroformat.CARGO, jsonUsers);
				}
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			}
		} else if (verb.equals(IAdminMicroformat.EXISTS_INVITE)) {
			String email = (String)jsonObject.get(ICredentialsMicroformat.USER_EMAIL);
			r = model.existsInvite(email);
			Boolean b = (Boolean)r.getResultObject();
			if (b.booleanValue() == true) {
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				code = BaseHandler.RESPONSE_OK;
				message = "not found";
			}
		} else if (verb.equals(IAdminMicroformat.LIST_INVITES)) {
			startS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_FROM));
			countS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_COUNT));
			start = 0;
			count = -1;
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
			r = model.listInvites(start, count);
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
				message = r.getErrorString();
			} else {
				//Time to take that list apart
				if (r.getResultObject() != null) {
					List<String> invites = (List<String>)r.getResultObject();
					returnMessage.put(ICredentialsMicroformat.CARGO, invites);
				}
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			}	
		} else {
			String x = IErrorMessages.BAD_VERB+"-AdminServletGet-"+verb;
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
		String username, userrole, useremail;
		if (verb.equals(IAdminMicroformat.REMOVE_USER)) {
			//TODO
		} else if (verb.equals(IAdminMicroformat.UPDATE_USER_EMAIL)) {
			username=(String)jsonObject.get(ICredentialsMicroformat.USER_NAME);
			useremail=(String)jsonObject.get(ICredentialsMicroformat.USER_EMAIL);
			r = model.updateUserEmail(username, useremail);
			if (!r.hasError()) {
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			}			
		} else if (verb.equals(IAdminMicroformat.UPDATE_USER_ROLE)) {
			username=(String)jsonObject.get(ICredentialsMicroformat.USER_NAME);
			userrole=(String)jsonObject.get(IUserMicroformat.USER_ROLE);
			r = model.updateUserRole(username, userrole);
			if (!r.hasError()) {
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			}
		} else if (verb.equals(IAdminMicroformat.NEW_INVITE)) {
			useremail=(String)jsonObject.get(ICredentialsMicroformat.USER_EMAIL);
			r = model.addInvite(useremail);
			if (!r.hasError()) {
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			}
		} else if (verb.equals(IAdminMicroformat.REMOVE_INVITE)) {
			useremail=(String)jsonObject.get(ICredentialsMicroformat.USER_EMAIL);
			r = model.removeInvite(useremail);
			if (!r.hasError()) {
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			}
		} else {
			String x = IErrorMessages.BAD_VERB+"-AdminServletPost-"+verb;
			environment.logError(x, null);
			throw new ServletException(x);
		}
		returnMessage.put(ICredentialsMicroformat.RESP_TOKEN, rtoken);
		returnMessage.put(ICredentialsMicroformat.RESP_MESSAGE, message);
		super.sendJSON(returnMessage.toJSONString(), code, response);
		returnMessage = null;
	}

	public void shutDown() {
		model.shutDown();
	}
}
