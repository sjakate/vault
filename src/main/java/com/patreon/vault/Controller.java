package com.patreon.vault;


import static spark.Spark.get;
import static spark.Spark.post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class Controller {
	
	private AmazonS3 s3;
    private static final String BUCKET_NAME = "patreon-hackday-vault";

	public Controller() {
		s3 = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_1)
				.build();
	}
	
    public static void main(String[] args) {
    	Controller controller = new Controller();
        post("/cards", (req, res) -> controller.addCard(req.body()));
        get("/cards", (req, res) -> controller.getCard(req.body()));
    }
    
    /**
     * Add card 
     * @param payload: card
     * @return reference to the card
     */
    public String addCard(String payload) {
    	String newKey = UUID.randomUUID().toString();
    	try {
    		s3.putObject(BUCKET_NAME, newKey, payload);
    	}catch(Exception e) {
    		System.out.println(e);
    		return "error";
    	}
    	return newKey;
    }
    
    /**
     * Get card
     * @param ref: reference to a card
     * @return card number
     */
    public String getCard(String ref) {
    	try {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(s3.getObject(BUCKET_NAME, ref).getObjectContent()));
    		return reader.readLine();
    	}catch(Exception e) {
    		System.out.println(e);
    		return "error";
    	}
    }
}