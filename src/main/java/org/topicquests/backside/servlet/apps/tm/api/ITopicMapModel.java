/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm.api;

import net.minidev.json.JSONObject;

import org.topicquests.common.api.IResult;
import org.topicquests.model.api.ITicket;

/**
 * @author park
 *
 */
public interface ITopicMapModel {
	////////////////
	// General TopicMap handlers
	////////////////
	IResult putTopic(JSONObject topic, ITicket credentials);
	
	IResult getTopic(String topicLocator, ITicket credentials);
	
	IResult removeTopic(String topicLocator, ITicket credentials);
	
	IResult query(JSONObject query, ITicket credentials);
	
	////////////////
	// Specialized TopicMap handlers
	////////////////
	
	IResult listSubclassTopics(String superClassLocator, ITicket credentials);
	
	IResult listInstanceTopics(String typeLocator, ITicket credentials);
	
	IResult listTopicsByKeyValue(String propertyKey, String value, ITicket credentials);
	
	void shutDown();
}
