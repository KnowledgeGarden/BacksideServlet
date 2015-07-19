/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.BaseHandler;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.model.api.ITicket;

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
		super.sendHTML("<h1>Hello World</h1>", response);
	}
	
	
	public void handlePost(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		//TODO
	}
	
	public void shutDown() {
		model.shutDown();
	}

}
