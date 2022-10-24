package jp.jc21.t.yoshizawa.WEB01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Sentiment {

	public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
		SentimentResult message = getSentiment("Stepover Toehold With Facelock");
		if (message != null) {
			System.out.println("positive:"+message.documents[0].confidenceScores.positive);
			System.out.println("neutral:"+message.documents[0].confidenceScores.neutral);
			System.out.println("negative:"+message.documents[0].confidenceScores.negative);
		}
	}

	static SentimentResult getSentiment(String s) throws IOException, URISyntaxException, InterruptedException {
		Gson gson = new Gson();

		String url = "https://r04jk3a20-text.cognitiveservices.azure.com/" + "text/analytics/v3.0/sentiment";
		Map<String, String> map = new HashMap<>();
		map.put("Ocp-Apim-Subscription-Key", "ac096a0a132943ec881d96d059e02dd2");

		SentimentDocs doc = new SentimentDocs();
		doc.id = "1";
		doc.text = s;

		SentimentSource src = new SentimentSource();
		src.documents = new SentimentDocs[1];
		src.documents[0] = doc;

		String jsonData = new Gson().toJson(src);

		InetSocketAddress proxy = new InetSocketAddress("172.17.0.2", 80);

		JsonReader reader = WebApiConnector.postJsonReader(url, proxy, map, jsonData);
		SentimentResult message = null;
		if (reader != null) {
			message = gson.fromJson(reader, SentimentResult.class);
			reader.close();
		}
		return message;
	}

}

class SentimentResult {
	SentimentDocuments[] documents;
	String[] errors;
	String modelVersion;
}

class SentimentDocuments {
	ConfidenceScores confidenceScores;
}

class ConfidenceScores {
	String positive;
	String neutral;
	String negative;
}

class SentimentSource {
	SentimentDocs[] documents;
}

class SentimentDocs {
	String id;
	String language;
	String text;
}
