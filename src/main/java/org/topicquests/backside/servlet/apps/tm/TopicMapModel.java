/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.common.api.ITopicQuestsOntology;
import org.topicquests.model.Node;
import org.topicquests.model.api.ITicket;
import org.topicquests.model.api.node.INode;
import org.topicquests.model.api.node.INodeModel;
import org.topicquests.model.api.provider.ITopicDataProvider;
import org.topicquests.topicmap.json.model.JSONTopicmapEnvironment;

/**
 * @author park
 *
 */
public class TopicMapModel implements ITopicMapModel {
	private ServletEnvironment environment;
	private JSONTopicmapEnvironment tmEnvironment;
	private ITopicDataProvider topicMap;
	private INodeModel nodeModel;

	/**
	 * 
	 */
	public TopicMapModel(ServletEnvironment env) {
		environment = env;
		tmEnvironment = environment.getTopicMapEnvironment();
		topicMap = (ITopicDataProvider)tmEnvironment.getDataProvider();
		nodeModel = topicMap.getNodeModel();
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#putTopic(net.minidev.json.JSONObject, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult putTopic(JSONObject topic, boolean checkVersion) {
		INode n = new Node(topic);
		IResult result = topicMap.putNode(n, checkVersion);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#getTopic(java.lang.String, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult getTopic(String topicLocator, ITicket credentials) {
		return topicMap.getNode(topicLocator, credentials);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#removeTopic(java.lang.String, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult removeTopic(String topicLocator, ITicket credentials) {
		return topicMap.removeNode(topicLocator, credentials);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#query(net.minidev.json.JSONObject, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult query(JSONObject query, int start, int count, ITicket credentials) {
		IResult result = topicMap.runQuery(query.toJSONString(), start, count, credentials);
		return result;
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IResult listSubclassTopics(String superClassLocator,
			int start, int count, ITicket credentials) {
		IResult result = topicMap.listSubclassNodes(superClassLocator, start, count, credentials);
		return result;
	}

	@Override
	public IResult listInstanceTopics(String typeLocator, int start, int count, ITicket credentials) {
		IResult result = topicMap.listInstanceNodes(typeLocator, start, count, credentials);
		return result;
	}

	@Override
	public IResult listTopicsByKeyValue(String propertyKey, String value,
			int start, int count, ITicket credentials) {
		IResult result = null; //
		return result;
	}

	@Override
	public IResult listUserTopics(int start, int count, ITicket credentials) {
		IResult result = topicMap.listInstanceNodes(ITopicQuestsOntology.USER_TYPE, start, count, credentials);
		return result;
	}

	@Override
	public IResult newInstanceNode(JSONObject theTopicShell) {
		String locator = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LOCATOR);
		String typeLocator =  (String)theTopicShell.get(ITopicMapMicroformat.SUPERTYPE_LOCATOR);
		String lang = (String)theTopicShell.get(ITopicMapMicroformat.LANGUAGE);
		String userId = (String)theTopicShell.get(ICredentialsMicroformat.USER_NAME);
		String label = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LABEL);
		String description = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_DETAILS);
		String smallImagePath = (String)theTopicShell.get(ITopicMapMicroformat.SMALL_IMAGE_PATh);
		String largeImagePath = (String)theTopicShell.get(ITopicMapMicroformat.LARGE_IMAGE_PATH);
		String isp = (String)theTopicShell.get(ITopicMapMicroformat.IS_PRIVATE);
		boolean isPrivate = false;
		if (isp.equalsIgnoreCase("t"))
			isPrivate = true;
		INode n = null;
		if (locator != null)
			n = nodeModel.newInstanceNode(locator, typeLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
		else 
			n = nodeModel.newInstanceNode(typeLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
		IResult result = topicMap.putNode(n, false);
		result.setResultObject(n.getProperties());
		return result;
	}

	@Override
	public IResult newSubclassNode(JSONObject theTopicShell) {
		String locator = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LOCATOR);
		String superClassLocator =  (String)theTopicShell.get(ITopicMapMicroformat.PARENT_LOCATOR);
		String lang = (String)theTopicShell.get(ITopicMapMicroformat.LANGUAGE);
		String userId = (String)theTopicShell.get(ICredentialsMicroformat.USER_NAME);
		String label = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LABEL);
		String description = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_DETAILS);
		String smallImagePath = (String)theTopicShell.get(ITopicMapMicroformat.SMALL_IMAGE_PATh);
		String largeImagePath = (String)theTopicShell.get(ITopicMapMicroformat.LARGE_IMAGE_PATH);
		String isp = (String)theTopicShell.get(ITopicMapMicroformat.IS_PRIVATE);
		boolean isPrivate = false;
		if (isp.equalsIgnoreCase("t"))
			isPrivate = true;
		INode n = null;
		if (locator != null)
			n = nodeModel.newSubclassNode(locator, superClassLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
		else
			n = nodeModel.newSubclassNode(superClassLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
			
		IResult result = topicMap.putNode(n, false);
		result.setResultObject(n.getProperties());
		return result;
	}

}
