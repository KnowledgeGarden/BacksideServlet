/**
 * 
 */
package org.topicquests.backside.servlet.apps.base;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.BaseHandler;
import org.topicquests.model.api.ITicket;

/**
 * @author park
 *
 */
public class AppHandler  extends BaseHandler {

	/**
	 * 
	 */
	public AppHandler(ServletEnvironment env, String basePath) {
		super(env, basePath);
	}

	public void handleGet(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		System.out.println("FOO "+super.getPath(request));
		System.out.println("BAR "+super.getQueryString(request));
		System.out.println("BAH "+getClientIpAddr(request));
		System.out.println("BAAH "+getIpAddr(request));
//FOO 
//BAR 
//BAH 0:0:0:0:0:0:0:1
//BAAH 0:0:0:0:0:0:0:1

//FOO images/snowflake.png
//BAR 
//BAH 0:0:0:0:0:0:0:1
//BAAH 0:0:0:0:0:0:0:1


		if (isImage(super.getPath(request))) {
			sendImage(super.getPath(request),response);
		} else
			super.sendHTML("<h1>Hello World</h1><img src=\"images/snowflake.png\">", response);
	}
	
	public void handlePost(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		//TODO
	}

    protected boolean isImage(String path) {
        return (path.endsWith("gif") || path.endsWith("GIF") ||
                        path.endsWith("x-png") || path.endsWith("png") ||
                        path.endsWith("PNG") ||
                        path.endsWith(".jpg") || path.endsWith(".JPG") ||
                        path.endsWith("tiff") || path.endsWith("tif")  ||
                        path.endsWith("ico"));
    }
    
    protected void sendImage(String imagePath, HttpServletResponse response) 
    		throws IOException {
	  String path = _basePath+imagePath;//normalizePathString(imagePath);
	  File f = new File(path);
	  if (f == null || !f.exists()) {
		  if (!path.endsWith("ico"))
			  environment.logDebug("Missing image for "+path);
	  } else if (f.exists()) {
		  String mimeType = "images/";
          
		  if (path.endsWith("gif") || path.endsWith("GIF"))
			  mimeType +="gif";
		  else if (path.endsWith("x-png") || path.endsWith("png") || path.endsWith("PNG"))
			  mimeType += "png";
		  else if (path.endsWith(".jpg") || path.endsWith(".JPG"))
			  mimeType += "jpg";
		  else if (path.endsWith("tiff") || path.endsWith("tif"))
			  mimeType += "tif";
		  else if (path.endsWith("ico"))
			  mimeType += "ico";
           response.setContentType(mimeType);
	    int len = 0;
	    byte[] buf = new byte[4096];
	    InputStream input = null;
	    OutputStream output = null;
	    try {
		    input = new BufferedInputStream(new FileInputStream(f));
		    output = response.getOutputStream();
            while ((len = input.read(buf)) > -1) {
                output.write(buf, 0, len);
            }
	    } catch (IOException e) {
	    	environment.logError("IssueQuestServer.sendImage error "+e.getMessage(),e);
	    } finally {
            if (input != null) input.close();
            if (output != null) output.close();
	    }
	  }
	}

}
