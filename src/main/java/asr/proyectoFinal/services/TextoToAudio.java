package asr.proyectoFinal.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;

public class TextoToAudio {

	public static String reproducirAudio(String text)
	{
		IamAuthenticator authenticator = new IamAuthenticator("cF5DgSlI1iA75KwQ9T6_y6vbqyADpcVDxQ3sZGK8AdAc");
		TextToSpeech  textToSpeech = new TextToSpeech (authenticator);
		textToSpeech.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/d9225fe6-7591-49f5-a4a0-ddc45ee06687");
		OutputStream out = null; 
		try {
		  SynthesizeOptions synthesizeOptions =
		    new SynthesizeOptions.Builder()
		      .text(text)
		      .accept("audio/wav")
		      .voice("en-US_AllisonV3Voice")
		      .build();

		  InputStream inputStream =
		    textToSpeech.synthesize(synthesizeOptions).execute().getResult();
		  InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

		  out = new FileOutputStream("hello_world.wav");
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
		
		  return out.toString(); 

	}
}
