package asr.proyectoFinal.servlets;

import asr.proyectoFinal.services.TextoToAudio; 
import java.io.BufferedWriter;
import java.util.Collections;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;

import asr.proyectoFinal.dao.CloudantPalabraStore;
import asr.proyectoFinal.dominio.Palabra;
import asr.proyectoFinal.services.Traductor;
import asr.proyectoFinal.services.AnalisisLP;

import java.util.ArrayList;
import java.util.*;
/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns = {"/listar", "/insertar", "/traducir","/analizar" ,"/reproducir"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		//out.println("<div>");
		
		CloudantPalabraStore store = new CloudantPalabraStore();
		//System.out.println(request.getServletPath());
		switch(request.getServletPath())
		{
			case "/listar":				
				PrintWriter out = response.getWriter();
				String msg = request.getParameter("mensaje");
				String nombre = request.getParameter("nombre");			
				
				Collection<Palabra> coleccion2 = store.getAll();
				ArrayList<Palabra> newList = new ArrayList<Palabra>(coleccion2);
				Collections.sort(newList);
				
				Palabra ultimapal = newList.get(newList.size() -1 );
				int indice = Integer.parseInt(ultimapal.getName().substring(0,ultimapal.getName().indexOf("#")));
				indice = indice +1;
				String indice2 = String.valueOf(indice);
				String msgGuardar = indice2	 + "#" + nombre + " : " + msg ;
				Palabra palabra2 = new Palabra();
				palabra2.setName(msgGuardar);					
				store.persist(palabra2);	
				
				Collection<Palabra> coleccion3 = store.getAll();
					
				ArrayList<Palabra> newList2 = new ArrayList<Palabra>(coleccion3);
				Collections.sort(newList2);
				String resultado = newList2.toString();
				resultado = resultado.replaceAll("[^:\n\\sA-Za-z]", "");				
				
				/*
				resultado = resultado.replaceAll("[", "");
				resultado = resultado.replaceAll("]", "");
				*/
				out.println(resultado);
				out.close();
				
			break;
			
			case "/analizar":
				PrintWriter out2 = response.getWriter();
				String msg2 = request.getParameter("mensaje");
				//msg2="me gusta mucho el futbol";
			
				String resultadoNLP ="";				
				resultadoNLP = AnalisisLP.analizarLenguaje(msg2,"es");
				
				out2.println(String.format("%s", resultadoNLP));
				out2.close();
			break;	
			
			case "/traducir":
				
				PrintWriter out3 = response.getWriter();
				String msg3 = request.getParameter("mensaje");
				String nombre2 = request.getParameter("nombre");
				String msgTraducido = Traductor.translate(msg3, "es", "en",false, "");
				
				Collection<Palabra> coleccion5 = store.getAll();
				ArrayList<Palabra> newList5 = new ArrayList<Palabra>(coleccion5);
				Collections.sort(newList5);
				
				Palabra ultimapal2 = newList5.get(newList5.size() -1 );
				int indice5 = Integer.parseInt(ultimapal2.getName().substring(0,ultimapal2.getName().indexOf("#")));
				indice5 = indice5 +1;
				String indice6 = String.valueOf(indice5);
				String msgGuardar5 = indice6	 + "#" + nombre2 + " : " + msgTraducido ;
				Palabra palabra5 = new Palabra();
				palabra5.setName(msgGuardar5);					
				store.persist(palabra5);	
				
				Collection<Palabra> coleccion9 = store.getAll();
					
				ArrayList<Palabra> newList9 = new ArrayList<Palabra>(coleccion9);
				Collections.sort(newList9);
				String resultado9 = newList9.toString();
				resultado9 = resultado9.replaceAll("[^:\n\\sA-Za-z]", "");
				out3.println(resultado9);
				out3.close();
				
			break;
		
			case "/reproducir":
				String audio = request.getParameter("mensaje");
				if(audio==null) {
					audio ="not yet";
				}
				
				 byte[] bytes = TextoToAudio.reproducir(audio, "");

			     response.getOutputStream().write(bytes);
			     response.getOutputStream().flush();
			    
			     response.getOutputStream().close();
			    //out.println("Audio"); 
			     
			break;
						
		}
		//out.println("</div>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
