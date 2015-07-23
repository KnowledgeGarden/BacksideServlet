/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.common.api.IResult;
import org.topicquests.model.api.ITicket;

/**
 * @author park
 *
 */
public class TopicMapModel implements ITopicMapModel {
	private ServletEnvironment environment;

	/**
	 * 
	 */
	public TopicMapModel(ServletEnvironment env) {
		environment = env;
		//TODO access to the topic map
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#putTopic(net.minidev.json.JSONObject, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult putTopic(JSONObject topic, ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#getTopic(java.lang.String, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult getTopic(String topicLocator, ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#removeTopic(java.lang.String, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult removeTopic(String topicLocator, ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#query(net.minidev.json.JSONObject, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult query(JSONObject query, ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IResult listSubclassTopics(String superClassLocator,
			ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult listInstanceTopics(String typeLocator, ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult listTopicsByKeyValue(String propertyKey, String value,
			ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

}
