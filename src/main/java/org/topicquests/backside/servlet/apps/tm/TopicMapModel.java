/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import java.util.*;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITagModel;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.ICoreIcons;
import org.topicquests.common.api.INodeTypes;
import org.topicquests.common.api.IResult;
import org.topicquests.common.api.ITopicQuestsOntology;
import org.topicquests.model.Node;
import org.topicquests.model.TicketPojo;
import org.topicquests.model.api.ITicket;
import org.topicquests.model.api.node.INode;
import org.topicquests.model.api.node.INodeModel;
import org.topicquests.model.api.provider.ITopicDataProvider;
import org.topicquests.topicmap.json.model.JSONTopicmapEnvironment;
import org.topicquests.topicmap.json.model.api.IJSONTopicDataProvider;
import org.topicquests.topicmap.json.model.api.ISocialBookmarkLegend;

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
		IResult result  = new ResultPojo();
		environment.logDebug("TopicMapModel.newInstanceNode- "+theTopicShell.toJSONString());
		//{"uName":"joe","isPrv":"F","sIco":"\/images\/bookmark_sm.png","Lang":"en",
		//"label":"Tech Reports | Knowledge Media Institute | The Open University",
		//"lIco":"\/images\/bookmark.png","inOf":"BookmarkNodeType","url":"http:\/\/kmi.open.ac.uk\/publications\/techreport\/kmi-10-01"}
		String locator = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LOCATOR);
		String typeLocator =  (String)theTopicShell.get(ITopicMapMicroformat.PARENT_LOCATOR);
		String lang = (String)theTopicShell.get(ITopicMapMicroformat.LANGUAGE);
		String userId = (String)theTopicShell.get(ICredentialsMicroformat.USER_NAME);
		String label = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LABEL);
		String description = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_DETAILS);
		String smallImagePath = (String)theTopicShell.get(ITopicMapMicroformat.SMALL_IMAGE_PATh);
		String largeImagePath = (String)theTopicShell.get(ITopicMapMicroformat.LARGE_IMAGE_PATH);
		String isp = (String)theTopicShell.get(ITopicMapMicroformat.IS_PRIVATE);
		//Added feature
		String url = (String)theTopicShell.get(ITopicMapMicroformat.URL);
		System.out.println("NEWINSTANCE "+url);
		boolean isPrivate = false;
		IResult r;
		if (isp.equalsIgnoreCase("t"))
			isPrivate = true;
		INode n = null;
		if (locator != null) {
			//First, see if this exists
			r = topicMap.getNode(locator, systemCredentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			n = (INode)r.getResultObject();
			result.setResultObject(n);
			return result;
			//if (r.getResultObject() != null)
			//	return r;
			//n = nodeModel.newInstanceNode(locator, typeLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
		} else {
			n = nodeModel.newInstanceNode(typeLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
			if (url != null)
				n.setURL(url);
			if (n.getLocator() == null) {
				environment.logError("Missing lox for "+typeLocator+" | "+label, null);
			}
			environment.logDebug("TopicMapModel.newInstance-1 "+n.toJSON());
			r = topicMap.putNode(n, false);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
		}
		
		environment.logDebug("TopicMapModel.newInstance-2 "+n.toJSON());
		r = relateNodeToUser(n, typeLocator, userId, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		result.setResultObject(n);
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
				//INode sourceNode,INode targetNode, String relationTypeLocator, String userId,
				//String smallImagePath, String largeImagePath, boolean isTransclude,boolean isPrivate
				r = nodeModel.relateExistingNodesAsPivots(user, node, ISocialBookmarkLegend.USER_BOOKMARK_RELATIONTYPE, userId, 
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
		String superClassLocator =  (String)theTopicShell.get(ITopicMapMicroformat.SUPERTYPE_LOCATOR);
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

	@Override
	public IResult getNodeTree(String rootLocator, int maxDepth, int start, int count, ITicket credentials) {
		return ((IJSONTopicDataProvider)topicMap).loadTree(rootLocator, maxDepth, start, count, credentials);
	}

	@Override
	public IResult getTopicByURL(String url, ITicket credentials) {
		QueryBuilder qb1 = QueryBuilders.termQuery(ITopicQuestsOntology.RESOURCE_URL_PROPERTY, url);
		environment.logDebug("TopicMapModel.getTopicByURL- "+qb1.toString());
		//runQuery returns a list of JSON strings
		IResult result = topicMap.runQuery(qb1.toString(), 0, -1, credentials);
		environment.logDebug("TopicMapModel.getTopicByURL+ "+result.getErrorString()+" | "+result.getResultObject());
		List<String> lx = (List<String>)result.getResultObject();
		if (lx != null) {
			if (lx.size() > 0) {
				try {
					JSONObject jo = (JSONObject)new JSONParser(JSONParser.MODE_JSON_SIMPLE).parse(lx.get(0));
					INode n = new Node(jo);
					result.setResultObject(n);
				} catch (Exception ex) {
					environment.logError(ex.getMessage(), ex);
					result.addErrorString(ex.getMessage());
					result.setResultObject(null);
				}
			} else
				result.setResultObject(null);
		}
		System.out.println("GETTOPICBYURL+ "+result.getErrorString()+" | "+result.getResultObject());
		return result;
	}

	@Override
	public IResult addPivot(String topicLocator, String pivotLocator,
			String pivotRelationType, String smallImagePath,
			String largeImagePath, boolean isTransclude, boolean isPrivate,
			ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult addRelation(String topicLocator, String pivotLocator,
			String pivotRelationType, String smallImagePath,
			String largeImagePath, boolean isTransclude, boolean isPrivate,
			ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult findOrProcessTags(String bookmarkLocator, List<String> tagLabels,
			String language, ITicket credentials) {
		//get the bookmark node
		IResult r = topicMap.getNode(bookmarkLocator, credentials);
		if (r.hasError() && r.getResultObject() == null)
			return r;
		//now, process tags
		INode bkmk = (INode)r.getResultObject();
		r = tagModel.addTagsToNode(bkmk, tagLabels, credentials);
		return r;
	}

	@Override
	public IResult findOrCreateBookmark(String url, String title,
			String language, String userId, List<String> tagLabels, ITicket credentials) {
		environment.logDebug("TopicMapModel.findOrDreateBookmark- "+url+" | "+userId);
		IResult result = this.getTopicByURL(url, credentials);
		INode bkmk = (INode)result.getResultObject();
		System.out.println("FindOrCreateBookmark-1 "+bkmk);
		if (bkmk == null) {
			//make a new one
			JSONObject jo = new JSONObject();
			jo.put(ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE, INodeTypes.BOOKMARK_TYPE);
			jo.put(ITopicMapMicroformat.IS_PRIVATE, "F");
			jo.put(ITopicMapMicroformat.TOPIC_LABEL, title);
			jo.put(ITopicQuestsOntology.RESOURCE_URL_PROPERTY, url);
			jo.put(ICredentialsMicroformat.USER_NAME, credentials.getUserLocator());
			jo.put(ITopicQuestsOntology.SMALL_IMAGE_PATH, ICoreIcons.BOOKMARK_SM);
			jo.put(ITopicQuestsOntology.LARGE_IMAGE_PATH, ICoreIcons.BOOKMARK);
			jo.put(ITopicMapMicroformat.LANGUAGE, language);
			result = this.newInstanceNode(jo, credentials);
			bkmk = (INode)result.getResultObject();
		}
		System.out.println("FindOrCreateBookmark-2 "+bkmk+" "+tagLabels);
		if (bkmk != null && tagLabels != null && tagLabels.size() > 0) {
			result = this.findOrProcessTags(bkmk.getLocator(), tagLabels, language, credentials);
		}
		System.out.println("FindOrCreateBookmark-3 "+result.getErrorString());
		return result;
	}

}
