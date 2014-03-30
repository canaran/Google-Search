import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

import javax.imageio.*;



public class veloz {
	public static final int NUM_OF_IMAGE_TO_SAVE = 10;
	public static String getResults(int start, String key, String query) throws Exception {
		
		URL url = new URL(
	            "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx=014479037408042406474:eqp9oalv2lu&q="+ query + "&searchType=image&num=10&start=" + start);
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
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		/*URL url = new URL(imageUrl);
		BufferedImage image = ImageIO.read(url);
		File f = new File(destinationFile);
		f.setWritable(true);*/
		//ImageIO.write(image, destinationFile.substring(destinationFile.lastIndexOf(".")+1), f);
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
		String key="AIzaSyCWj0r9SAYuz25Si_XRk-_zQ5hVgXPpOJM";
	    String qry="aeron";
	    int start = 1;
	    int count = 1;
	    int count2 = 0;
	    String res = "";
	    System.out.println("Output from Server .... \n");
	    String url = "http://www.davidcraddock.net/wp-content/uploads/2013/12/hero_aeron_work_1.jpg";
	    File f = new File(url);
	    System.out.println(f.getAbsolutePath());
	    String imageFile = url.substring(url.lastIndexOf("/")+1);
	    saveImage(url, imageFile);
	    /*while(count <= NUM_OF_IMAGE_TO_SAVE/10) {
	    	
	    	res = getResults(start, key, qry);
	    	String[] lines = res.split("\n");

	    	int i = 0;
	    	while (i<lines.length) {
		        if(lines[i].contains("\"link\": \"")){                
		            String link=lines[i].substring(lines[i].indexOf("\"link\": \"")+("\"link\": \"").length(), lines[i].indexOf("\","));
		            String size=lines[i].substring(lines[i].indexOf("\"link\": \"")+("\"link\": \"").length(), lines[i].indexOf("\","));
		            System.out.println(link);       //Will print the google search links
		            count2++;
		        } 
		        i++;
	    	}
	    	count++;
	    	start += 10;
	    } 
	    System.out.println(count2);*/
	                       
	}
}
