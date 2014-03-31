import java.io.*;
import java.net.*;

public class GoogleSearch {
	/* Number of images to save is limited because Google Custom Search API does not let me 
	* to see the results with start index > 100. I need to pay for that. Thus, NUM_OF_IMAGE_TO_SAVE variable
	* can be in the range of 1-100.
	*/
	private static final int NUM_OF_IMAGE_TO_SAVE = 7;				// Number of images to save in the current directory
	private static final String key="AIzaSyCWj0r9SAYuz25Si_XRk-_zQ5hVgXPpOJM";	// Google Custom Search API key 
	private static final String qry="aeron";					// Query for the search
	private static final String cx = "014479037408042406474:e7m5zbvlphe";		// The custom search engine ID
	private static int errors = 0;						
	
	// Returns search results (JSON format) in each call for specified API key, query, start index and custom search engine id (cx).
	public static String getResults( String key, String query, int start, String cx, int num) throws IOException {
		
		URL url = new URL(	// Construct the URL
	            "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx="+ cx + "&q="+ query + "&searchType=image&num="+num +"&start=" + start);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();			// Open the connection
		conn.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));	// Get the stream
		
		// Construct the string to return from the reponse
		StringBuilder response = new StringBuilder();
		String line = "";
		while ((line = br.readLine()) != null)
		    response.append(line+"\n");
		    
		br.close();				// Close the reader
		conn.disconnect();			// Close the connection
		return response.toString();
	}

	// Saves the image in the specified url to the specified destination file.
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {

		URL url = new URL(imageUrl);					// Initialize the URL from the image link
		InputStream is = url.openStream();				// Open an input stream
		OutputStream os = new FileOutputStream(destinationFile);	// Open an output stream to write/save the image data.

		byte[] b = new byte[4096];	// Initialize the byte chunk
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);			// Write out to the file using output stream
		}
		
		// Close the streams
		is.close();
		os.close();

	}
	// Find the links in the search results and save the images from those links
	public static void findLinksAndSave(String [] response) throws IOException {
		int i = 0;
	    	while (i<response.length) {
		        if(response[i].contains("\"link\": \"")){
				// Obtain the link of the image
				String link=response[i].substring(response[i].indexOf("\"link\": \"")+("\"link\": \"").length(), response[i].indexOf("\","));
				// Obtain the name of the image
				String image_name = link.substring(link.lastIndexOf("/")+1);
				// Save image
				try {
				saveImage(link, image_name);
				} catch(IOException e) {
					System.out.println("Server returned:" + e.getMessage() + " for the link: " + link);
					System.out.println("Image cannot be saved");
					errors++;				
				}
		        }
		        i++;
	    	}
	}

	public static void main(String[] args) throws IOException {
		
		int start = 1;
		int count = 1;
		String res = "";
		String[] lines; 
		
		/*
		* if we want to retrieve and save less than 10 images.
		*/
		if (NUM_OF_IMAGE_TO_SAVE < 10 && NUM_OF_IMAGE_TO_SAVE > 0) {
			res = getResults(key, qry, start, cx, NUM_OF_IMAGE_TO_SAVE);
			lines = res.split("\n");	
		
		    	// Find the links in the search results and save the images from those links
		    	findLinksAndSave(lines);
			if(errors==0)
				System.out.println(NUM_OF_IMAGE_TO_SAVE + " images are saved.");
			else
				System.out.println(errors +" out of " + NUM_OF_IMAGE_TO_SAVE + " images cannot be downloaded and saved because of the server response. Other images are saved.");	
		} 
		/*
		* In Google Custom API, maximum number of results to recieve in one call is 10. 
		* Thus, I am getting 10 results in every iteration.
		*/
		else if(NUM_OF_IMAGE_TO_SAVE >= 10) {
			while(count <= NUM_OF_IMAGE_TO_SAVE/10) {
				
				// Get the image results for "aeron"
				res = getResults(key, qry, start, cx, 10);
				lines = res.split("\n");	

				// Find the links in the search results and save the images from those links
				findLinksAndSave(lines);

				count++;
				start += 10;
			}
			
			// if there are more images to save, get them and save them.
			int rest = NUM_OF_IMAGE_TO_SAVE%10;
			if(rest > 0) {
				// Get the last 'rest' number of image
				res = getResults(key, qry, start, cx, rest);
				lines = res.split("\n");	
			
			    	// Find the links in the search results and save the images from those links
			    	findLinksAndSave(lines);
			}
			if(errors==0)
				System.out.println(NUM_OF_IMAGE_TO_SAVE + " images are saved.");
			else
				System.out.println(errors + " out of " + NUM_OF_IMAGE_TO_SAVE + " images cannot be downloaded and saved because of the server response. Other images are saved.");
		} else {
			System.out.println("NUM_OF_IMAGE_TO_SAVE should be in the range of 1-100");		
		}
		
	}
}
