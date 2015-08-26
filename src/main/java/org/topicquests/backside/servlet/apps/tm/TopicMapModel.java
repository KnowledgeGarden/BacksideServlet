/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import java.util.*;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITagModel;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.ICoreIcons;
import org.topicquests.common.api.IResult;
import org.topicquests.common.api.ITopicQuestsOntology;
import org.topicquests.model.Node;
import org.topicquests.model.TicketPojo;
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
	private ITagModel tagModel;
	private ITicket systemCredentials;

	/**
	 * 
	 */
	public TopicMapModel(ServletEnvironment env) {
		environment = env;
		tmEnvironment = environment.getTopicMapEnvironment();
		topicMap = (ITopicDataProvider)tmEnvironment.getDataProvider();
		nodeModel = topicMap.getNodeModel();
		tagModel = new TagModel(environment);
		systemCredentials = new TicketPojo(ITopicQuestsOntology.SYSTEM_USER);
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
	public IResult newInstanceNode(JSONObject theTopicShell, ITicket credentials) {
	//	System.out.println("CARGO2 "+theTopicShell.toJSONString());
	//	System.out.println("LOX "+theTopicShell.get("lox"));
		String locator = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LOCATOR);
		String typeLocator =  (String)theTopicShell.get(ITopicMapMicroformat.SUPERTYPE_LOCATOR);
		String lang = (String)theTopicShell.get(ITopicMapMicroformat.LANGUAGE);
		String userId = (String)theTopicShell.get(ITopicQuestsOntology.CREATOR_ID_PROPERTY);
		String label = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LABEL);
		String description = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_DETAILS);
		String smallImagePath = (String)theTopicShell.get(ITopicMapMicroformat.SMALL_IMAGE_PATh);
		String largeImagePath = (String)theTopicShell.get(ITopicMapMicroformat.LARGE_IMAGE_PATH);
		boolean isp = (boolean)theTopicShell.get(ITopicMapMicroformat.IS_PRIVATE);
		boolean isPrivate = isp;
	//	if (isp.equalsIgnoreCase("t"))
	//		isPrivate = true;
		INode n = null;
		IResult r;
		if (locator != null) {
			//First, see if this exists
			r = topicMap.getNode(locator, systemCredentials);
			if (r.getResultObject() != null)
				return r;
			n = nodeModel.newInstanceNode(locator, typeLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
		} else 
			n = nodeModel.newInstanceNode(typeLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
		IResult result = topicMap.putNode(n, false);
		result.setResultObject(n.getProperties());
		r = relateNodeToUser(n, typeLocator, userId, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		return result;
	}
	
	IResult relateNodeToUser(INode node, String superTypeLocator, String userId, ITicket credentials) {
		IResult result = new ResultPojo();
		//NOW, relate this puppy
		String relation = null;
		IResult r;
		if (superTypeLocator.equals("BookmarkNodeType") || //TODO add more types
				superTypeLocator.equals("TagNodeType"))
			relation = "DocumentCreatorRelationType";
		if (relation != null) {
			r = topicMap.getNode(userId, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			INode user = (INode)r.getResultObject();
			if (user != null) {
				r = nodeModel.relateExistingNodes(node, user, "DocumentCreatorRelationType", userId, 
						ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			} else
				result.addErrorString("Missing User "+userId);
		}
		return result;
	}

	@Override
	public IResult newSubclassNode(JSONObject theTopicShell, ITicket credentials) {
		String locator = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LOCATOR);
		String superClassLocator =  (String)theTopicShell.get(ITopicMapMicroformat.PARENT_LOCATOR);
		String lang = (String)theTopicShell.get(ITopicMapMicroformat.LANGUAGE);
		String userId = (String)theTopicShell.get(ITopicQuestsOntology.CREATOR_ID_PROPERTY);
		String label = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LABEL);
		String description = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_DETAILS);
		String smallImagePath = (String)theTopicShell.get(ITopicMapMicroformat.SMALL_IMAGE_PATh);
		String largeImagePath = (String)theTopicShell.get(ITopicMapMicroformat.LARGE_IMAGE_PATH);
		String isp = (String)theTopicShell.get(ITopicMapMicroformat.IS_PRIVATE);
		boolean isPrivate = false;
		IResult r;
		if (isp.equalsIgnoreCase("t"))
			isPrivate = true;
		INode n = null;
		if (locator != null) {
			//First, see if this exists
			r = topicMap.getNode(locator, systemCredentials);
			if (r.getResultObject() != null)
				return r;
			n = nodeModel.newSubclassNode(locator, superClassLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
		} else
			n = nodeModel.newSubclassNode(superClassLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
			
		IResult result = topicMap.putNode(n, false);
		result.setResultObject(n.getProperties());
		r = relateNodeToUser(n, superClassLocator, userId, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		return result;
	}

	@Override
	public IResult addFeaturesToNode(JSONObject cargo, ITicket credentials) {
		String lox = (String)cargo.get(ITopicMapMicroformat.TOPIC_LOCATOR);
		String userId = (String)cargo.get(ITopicQuestsOntology.CREATOR_ID_PROPERTY);
		IResult r = topicMap.getNode(lox, credentials);
		IResult result = new ResultPojo();
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		INode n = (INode)r.getResultObject();
		boolean isChanged = false;
		if (n != null) {
			Iterator<String> itr = cargo.keySet().iterator();
			String key;
			while (itr.hasNext()) {
				key = itr.next();
				if (!key.equals(ITopicMapMicroformat.TOPIC_LOCATOR) &&
					!key.equals(ITopicQuestsOntology.CREATOR_ID_PROPERTY)) {
					if (key.equals(ITopicQuestsOntology.RESOURCE_URL_PROPERTY)) {
						isChanged = true;
						n.setURL((String)cargo.get(ITopicQuestsOntology.RESOURCE_URL_PROPERTY));
					}
				}
			}
			
		} else {
			result.addErrorString("Missing Node "+lox);
		}

		return result;
	}

}
