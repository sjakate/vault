package com.patreon.vault;


import static spark.Spark.get;
import static spark.Spark.post;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
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
import spark.Request;
import spark.Response;

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
		post("/users", (req, res) -> controller.addUser(req, res));
		get("/users/:token", (req, res) -> controller.getUser(req, res));

		post("/cards", (req, res) -> controller.addCard(req, res));
        get("/cards/:token", (req, res) -> controller.getCard(req, res));
    }

	public String addUser(Request request, Response response) {
		String token = UUID.randomUUID().toString();
		HashMap responseBody = new HashMap<String, String>();

		try {
			byte[] bytesPayload = encrypt(request.body());
			upload(BUCKET_NAME, token, bytesPayload);

			responseBody.put("token", token);
			response.status(201);
			response.body(JsonMapper.JSON.toJson(responseBody).get());

		} catch(Exception e) {
			System.out.println(e);

			responseBody.put("error", e.getMessage());
			response.status(500);
			response.body(JsonMapper.JSON.toJson(responseBody).get());
		}
		return response.body();
	}

	public String getUser(Request request, Response response) {

		try {
			String token = request.params("token");

			byte[] bytesPayload = download(BUCKET_NAME, token);
			String decryptedString = decrypt(bytesPayload);

			response.status(200);
			response.body(decryptedString);

		} catch(Exception e) {

			System.out.println(e.getMessage());
			HashMap responseBody = new HashMap<String, String>();
			responseBody.put("error", e.getMessage());

			response.status(500);
			response.body(JsonMapper.JSON.toJson(responseBody).get());
		}
		return response.body();
	}

	public String addCard(Request request, Response response) {
		String token = UUID.randomUUID().toString();
		HashMap responseBody = new HashMap<String, String>();

		try {
			byte[] bytesPayload = encrypt(request.body());
			upload(BUCKET_NAME, token, bytesPayload);

			responseBody.put("token", token);
			response.status(201);
			response.body(JsonMapper.JSON.toJson(responseBody).get());

		} catch(Exception e) {
			System.out.println(e);

			responseBody.put("error", e.getMessage());
			response.status(500);
			response.body(JsonMapper.JSON.toJson(responseBody).get());
		}
		return response.body();
	}

	public String getCard(Request request, Response response) {

		try {
			String token = request.params("token");

			byte[] bytesPayload = download(BUCKET_NAME, token);
			String decryptedString = decrypt(bytesPayload);

			response.status(200);
			response.body(decryptedString);

		} catch(Exception e) {

			System.out.println(e.getMessage());
			HashMap responseBody = new HashMap<String, String>();
			responseBody.put("error", e.getMessage());

			response.status(500);
			response.body(JsonMapper.JSON.toJson(responseBody).get());
		}
		return response.body();
	}

    private byte[] encrypt(String plainText) {
    	EncryptRequest req = new EncryptRequest().withKeyId(KMS_KEY).withPlaintext(ByteBuffer.wrap(plainText.getBytes()));
    	return kmsClient.encrypt(req).getCiphertextBlob().array();
    }
    
    private String decrypt(byte[] cipherText) {
    	DecryptRequest req = new DecryptRequest().withCiphertextBlob(ByteBuffer.wrap(cipherText));
    	return new String(kmsClient.decrypt(req).getPlaintext().array());
    }

    private void upload(String bucket, String token, byte[] bytesPayload) {
		ByteArrayInputStream stream = new ByteArrayInputStream(bytesPayload);
		ObjectMetadata md = new ObjectMetadata();
		md.setContentLength(bytesPayload.length);
		s3.putObject(bucket, token, stream, md);
	}

	private byte[] download(String bucket, String token) throws Exception {
		S3Object S3Object = s3.getObject(bucket, token);
		return IOUtils.toByteArray(S3Object.getObjectContent());
	}
}