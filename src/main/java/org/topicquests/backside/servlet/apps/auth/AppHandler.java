/**
 * 
 */
package org.topicquests.backside.servlet.apps.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.api.IErrorMessages;
import org.topicquests.backside.servlet.apps.BaseHandler;
import org.topicquests.backside.servlet.apps.auth.api.IAuthMicroformat;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.common.api.IResult;
import org.topicquests.model.api.ITicket;

import com.google.common.io.BaseEncoding;

/**
 * @author park
 *
 */
public class AppHandler  extends BaseHandler {
	private IUserModel model;
	/**
	 * 
	 */
	public AppHandler(ServletEnvironment env, String basePath) {
		super(env,basePath);
		model = environment.getUserModel();
	}

	//////////////////////////////////////////////
	// There are two legal verbs in this app:
	//  IAuthMicroformat.AUTHENTICATE
	//  and
	//  IAuthMicroformat.LOGOUT
	// Both come in as Post commands
	//////////////////////////////////////////////
	public void handleGet(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		JSONObject returnMessage = newJSONObject();
		System.out.println("AUTHPOST "+jsonObject.toJSONString());
		String message = "", rtoken="";
		String verb = (String)jsonObject.get(ICredentialsMicroformat.VERB);
		int code = 0;
		if (verb.equals(IAuthMicroformat.VALIDATE)) {
			String name = (String)jsonObject.get(ICredentialsMicroformat.USER_NAME);
			IResult r = model.existsUsername(name);
			Boolean x = (Boolean)r.getResultObject();
			if (x.booleanValue()) {
				message = "ok";
				code = BaseHandler.RESPONSE_OK;
			} else {
				code = BaseHandler.RESPONSE_NOT_FOUND;
				message = "not found";
			}
		} else if (verb.equals(IAuthMicroformat.AUTHENTICATE)) {
			String auth = (String)jsonObject.get("hash");//request.getHeader("Authorization");
			System.out.println("AUTHORIZATION "+auth);
			//auth = auth.substring("Basic".length()).trim();
			System.out.println("AUTHORIZATION "+auth);
			//AUTHORIZATION Basic c2FtQHNsb3cuY29tOnNhbSE=

			byte [] foo = BaseEncoding.base64().decode(auth);
			String creds = new String(foo);
			System.out.println("AUTHORIZATION2 "+creds);
			//AUTHORIZATION2 foo:bar
			int where = creds.indexOf(':');
			String email = creds.substring(0,where);
			String pwd = creds.substring(where+1);
			
			System.out.println("LOGIN "+email+" "+pwd);
			IResult r = model.authenticate(email, pwd);
			System.out.println("LOGIN-1 "+r.getErrorString()+" | "+r.getResultObject());
			ITicket t = (ITicket)r.getResultObject();
			if (t != null) {
				rtoken = newUUID();
				returnMessage.put(ICredentialsMicroformat.CARGO, ticketToUser(t));
				message = "ok";
				code = BaseHandler.RESPONSE_OK;
				credentialCache.putTicket(rtoken, t);
			} else {
				code = BaseHandler.RESPONSE_NOT_FOUND;
				message = r.getErrorString();
			}
		} else {
			String x = IErrorMessages.BAD_VERB+"-AuthServletPost-"+verb;
			environment.logError(x, null);
			throw new ServletException(x);			
		}
		
		returnMessage.put(ICredentialsMicroformat.RESP_TOKEN, rtoken);
		returnMessage.put(ICredentialsMicroformat.RESP_MESSAGE, message);
		System.out.println("ADMINGET "+returnMessage.toJSONString());
		super.sendJSON(returnMessage.toJSONString(), code, response);
		returnMessage = null;

	}
	
	public void handlePost(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		JSONObject returnMessage = newJSONObject();
		System.out.println("AUTHPOST "+jsonObject.toJSONString());
		String message = "", rtoken="";
		String verb = (String)jsonObject.get(ICredentialsMicroformat.VERB);
		int code = 0;
		
		//We have nothing to do here
		returnMessage.put(ICredentialsMicroformat.RESP_TOKEN, rtoken);
		returnMessage.put(ICredentialsMicroformat.RESP_MESSAGE, message);
		super.sendJSON(returnMessage.toJSONString(), code, response);
		returnMessage = null;
	}

}
