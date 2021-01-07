package asr.proyectoFinal.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.model.GetVoiceOptions;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;

public class TextoToAudio
{
	public static void reproducir(String frase, String path)
	{
		IamAuthenticator authenticator = new IamAuthenticator("cF5DgSlI1iA75KwQ9T6_y6vbqyADpcVDxQ3sZGK8AdAc");
		TextToSpeech textToSpeech = new TextToSpeech(authenticator);
		textToSpeech.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/d9225fe6-7591-49f5-a4a0-ddc45ee06687");
		HttpConfigOptions configOptions = new HttpConfigOptions.Builder().disableSslVerification(true).build();
		textToSpeech.configureClient(configOptions);
		try {
			  SynthesizeOptions synthesizeOptions =
			    new SynthesizeOptions.Builder()
			      .text(frase)
			      .accept("audio/mp3")
			      .voice("en-US_AllisonVoice")
			      .build();
			  
			  
			  System.out.println(path);
			  
			  InputStream inputStream =
			    textToSpeech.synthesize(synthesizeOptions).execute().getResult();
			  InputStream in = WaveUtils.reWriteWaveHeader(inputStream);
			  
			  OutputStream out = new FileOutputStream(path);
			  byte[] buffer = new byte[1024];
			  int length;
			  while ((length = in.read(buffer)) > 0) {
			    out.write(buffer, 0, length);
			  }

			  out.close();
			  in.close();
			  inputStream.close();
			} catch (IOException e) {
			  e.printStackTrace();
			}
	}
}