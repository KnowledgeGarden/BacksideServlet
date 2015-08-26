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
	public IResult addTagsToNode(INode node, String[] tagNames,
			ITicket credentials) {
		String userId = credentials.getUserLocator();
		IResult result = new ResultPojo();
		IResult r;
		int len = tagNames.length;
		String name,lox;
		INode tag;
		for (int i=0;i<len;i++) {
			name = tagNames[i];
			lox = tagNameToLocator(name);
			//do we already know this tag?
			r = topicMap.getNode(lox, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			tag = (INode)r.getResultObject();
			if (r == null) {
				//create a tag
				tag = nodeModel.newInstanceNode(lox, INodeTypes.TAG_TYPE, name, "", "en", userId, 
						ICoreIcons.TAG_SM, ICoreIcons.TAG, false);
				r = topicMap.putNode(tag, false);
			}
			//relate the tag to the user
			r = topicMap.getNode(userId, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			INode user = (INode)r.getResultObject();
			if (user != null) {
				r = nodeModel.relateExistingNodesAsPivots(tag, user, "DocumentCreatorRelationType", userId, 
						ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			}
			//relate the tag to the topic
			r = nodeModel.relateExistingNodesAsPivots(tag, node, "TagDocumentRelationType", userId, 
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
		result = result.replaceAll("+", "P");
		result = result.replaceAll("-", "M");
		result = result+"_TAG";
		return result;
	}
}
