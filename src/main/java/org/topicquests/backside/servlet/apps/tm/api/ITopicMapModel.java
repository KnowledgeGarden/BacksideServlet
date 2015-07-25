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
	//NOTE about <em>version</em>
	//  If checking version, the system will issue
	//  an Exception with the message <em>OptimisticLockException</ema>
	//  if the version number in the saved node is less than that which exists
	//  in the index.
	////////////////
	
	/**
	 * Store this <code>topic</code> to the topicmap
	 * @param topic
	 * @param checkVersion <code>true</code> if version sensitive
	 * @return
	 */
	IResult putTopic(JSONObject topic, boolean checkVersion);
	
	IResult getTopic(String topicLocator, ITicket credentials);
	
	IResult removeTopic(String topicLocator, ITicket credentials);
	
	IResult query(JSONObject query, int start, int count, ITicket credentials);
	
	////////////////
	// Specialized TopicMap handlers
	////////////////
	
	IResult listSubclassTopics(String superClassLocator, int start, int count, ITicket credentials);
	
	IResult listInstanceTopics(String typeLocator, int start, int count, ITicket credentials);
	
	IResult listTopicsByKeyValue(String propertyKey, String value, int start, int count, ITicket credentials);
	
    /**
     * List users in the TopicMap
     * @param start
     * @param count
     * @param credentials
     * @return
     */
    IResult listUserTopics(int start, int count, ITicket credentials);

	void shutDown();
}
