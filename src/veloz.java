import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

import javax.imageio.*;



public class veloz {
	private static final int NUM_OF_IMAGE_TO_SAVE = 10;
	private static final String key="AIzaSyCWj0r9SAYuz25Si_XRk-_zQ5hVgXPpOJM";
	private static final String qry="aeron";
	private static final String cx = "014479037408042406474:eqp9oalv2lu";

	// Returns 10 search results (JSON format) for specified API key, query, start index and custom search engine id (cx).
	public static String getResults( String key, String query, int start, String cx) throws Exception {

		URL url = new URL(
	            "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx="+ cx + "&q="+ query + "&searchType=image&num=10&start=" + start);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringBuilder response = new StringBuilder();
		String line = "";
		while ((line = br.readLine()) != null)
		    response.append(line+"\n");
		br.close();
		conn.disconnect();
		return response.toString();
	}

	// Saves the image in the specified url to the specified destination file.
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {

		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();

	}

	public static void main(String[] args) throws Exception {
		int start = 1;
		int count = 1;
		String res = "";
		System.out.println("Output from Server .... \n");
		while(count <= NUM_OF_IMAGE_TO_SAVE/10) {

		    	res = getResults(key, qry, start, cx);
		    	String[] lines = res.split("\n");
	
		    	int i = 0;
		    	// Find the links in the search results and save the images from those links
		    	while (i<lines.length) {
			        if(lines[i].contains("\"link\": \"")){
			        	// Obtain the link of the image
			            String link=lines[i].substring(lines[i].indexOf("\"link\": \"")+("\"link\": \"").length(), lines[i].indexOf("\","));
			            // Obtain the name of the image
			            String image_name = link.substring(link.lastIndexOf("/")+1);
			            // Save image
			            saveImage(link, image_name);
			        }
			        i++;
	    	}
	    	count++;
	    	start += 10;
	    }
	}
}
