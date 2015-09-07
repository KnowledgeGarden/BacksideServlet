/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import java.util.*;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.tm.api.ITagModel;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.ICoreIcons;
import org.topicquests.common.api.INodeTypes;
import org.topicquests.common.api.IRelationsLegend;
import org.topicquests.common.api.IResult;
import org.topicquests.model.api.ITicket;
import org.topicquests.model.api.node.INode;
import org.topicquests.model.api.node.INodeModel;
import org.topicquests.model.api.provider.ITopicDataProvider;
import org.topicquests.topicmap.json.model.api.ISocialBookmarkLegend;

/**
 * @author park
 *
 */
public class TagModel implements ITagModel {
	private ServletEnvironment environment;
	private ITopicDataProvider topicMap;
	private INodeModel nodeModel;
	private ITagModel tagModel;

	/**
	 * 
	 */
	public TagModel(ServletEnvironment env) {
		environment = env;
		topicMap = (ITopicDataProvider)environment.getTopicMapEnvironment().getDataProvider();
		nodeModel = topicMap.getNodeModel();

	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITagModel#addTagsToNode(org.topicquests.model.api.node.INode, java.lang.String[], org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult addTagsToNode(INode node, List<String> tagNames,
			ITicket credentials) {
		String userId = credentials.getUserLocator();
		IResult result = new ResultPojo();
		IResult r;
		int len = tagNames.size();
		String name,lox;
		INode tag;
		for (int i=0;i<len;i++) {
			name = tagNames.get(i);
			lox = tagNameToLocator(name);
			//do we already know this tag?
			r = topicMap.getNode(lox, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			tag = (INode)r.getResultObject();
			if (tag == null) {
				//create a tag
				environment.logDebug("TagModel.addTagToNode-1 "+lox+" "+userId);
				System.out.println("TagModel.addTagToNode-1 "+lox+" "+userId);
				tag = nodeModel.newInstanceNode(lox, INodeTypes.TAG_TYPE, name, "", "en", userId, 
						ICoreIcons.TAG_SM, ICoreIcons.TAG, false);
				r = topicMap.putNode(tag, false);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			}
			//relate the tag to the user
			r = topicMap.getNode(userId, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			INode user = (INode)r.getResultObject();
			if (user != null) {
				environment.logDebug("TagModel.addTagToNode-2 "+tag+" "+user);
				System.out.println("TagModel.addTagToNode-2 "+tag+" "+user);
				r = nodeModel.relateExistingNodesAsPivots(tag, user, ISocialBookmarkLegend.TAG_USER_RELATION_TYPE, userId, 
						ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			}
			//relate the tag to the topic
			environment.logDebug("TagModel.addTagToNode-3 "+tag+" "+node);
			System.out.println("TagModel.addTagToNode-3 "+tag+" "+node);
			r = nodeModel.relateExistingNodesAsPivots(tag, node, ISocialBookmarkLegend.TAG_BOOKMARK_RELATION_TYPE, userId, 
					ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			//relating does all the puts
		}
		return result;
	}

	///////////////////////////
	// utililties
	///////////////////////////
	
	private String tagNameToLocator(String name) {
		String result = name;
		result = result.toLowerCase();
		result = result.replaceAll(" ", "_");
		result = result.replaceAll("'", "_");
		result = result.replaceAll(":", "_");
//		result = result.replaceAll("\+", "P");
//		result = result.replaceAll("-", "M");
		result = result+"_TAG";
		return result;
	}
}
