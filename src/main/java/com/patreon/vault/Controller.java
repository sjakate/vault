package com.patreon.vault;


import static spark.Spark.get;
import static spark.Spark.post;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

public class Controller {
	
	private AmazonS3 s3;
	private AWSKMS kmsClient;
    private static final String BUCKET_NAME = "patreon-hackday-vault";
    private static final String KMS_KEY = "arn:aws:kms:us-west-1:792916230465:key/4d763cc8-9757-49d1-a8db-e917795643a2";

	public Controller() {
		s3 = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_1)
				.build();
		kmsClient = AWSKMSClientBuilder.standard()
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
    		byte[] bytesPayload = encrypt(payload);
    		ByteArrayInputStream stream = new ByteArrayInputStream(bytesPayload);
    		ObjectMetadata md = new ObjectMetadata();
            md.setContentLength(bytesPayload.length);
    		s3.putObject(BUCKET_NAME, newKey, stream, md);   	
        	return newKey;
    	}catch(Exception e) {
    		System.out.println(e);
    		return "error";
    	}
    }
    
    /**
     * Get card
     * @param ref: reference to a card
     * @return card number
     */
    public String getCard(String ref) {
    	try {
    		S3Object S3Object = s3.getObject(BUCKET_NAME, ref);
    		byte[] bytesPayload = IOUtils.toByteArray(S3Object.getObjectContent());
    		String decryptedString = decrypt(bytesPayload);
    		return decryptedString;
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    		return "error";
    	}
    }
    
    private byte[] encrypt(String plainText) {
    	EncryptRequest req = new EncryptRequest().withKeyId(KMS_KEY).withPlaintext(ByteBuffer.wrap(plainText.getBytes()));
    	return kmsClient.encrypt(req).getCiphertextBlob().array();
    }
    
    private String decrypt(byte[] cipherText) {
    	DecryptRequest req = new DecryptRequest().withCiphertextBlob(ByteBuffer.wrap(cipherText));
    	return new String(kmsClient.decrypt(req).getPlaintext().array());
    }
}