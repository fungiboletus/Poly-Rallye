package polyrallye.ouie.environnement;


import org.jdom.Element;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;


public class Environnement {
	
	protected String nom;
	protected String type;
	protected Meteo meteo;

	
	  /** Buffers hold sound data. */
	  IntBuffer buffer = BufferUtils.createIntBuffer(1);

	  /** Sources are points emitting sound. */
	  IntBuffer source = BufferUtils.createIntBuffer(1);

	  /** Position of the source sound. */
	  FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	  /** Velocity of the source sound. */
	  FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	  /** Position of the listener. */
	  FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	  /** Velocity of the listener. */
	  FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	  /** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
	  FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();
	
	public Environnement() {
		super();
		nom="foret";
		meteo= new Meteo("Vent",nom);
	}
	
	public Environnement(Element noeud) {
		
		
	}
	
	
	
	 int loadALData(String fichier) {
		    // Load wav data into a buffer.
		    AL10.alGenBuffers(buffer);

		    if(AL10.alGetError() != AL10.AL_NO_ERROR)
		      return AL10.AL_FALSE;

		    //Loads the wave file from your file system
		    
		    /*BULLSHIT
		    FileInputStream fin = null;
		    
		    try {
		      fin = new FileInputStream(fichier+".wav");
		      
		    } catch (java.io.FileNotFoundException ex) {
		      ex.printStackTrace();
		      return AL10.AL_FALSE;
		    }
		    System.out.println(fin
		    		);
		    */
		    
		    File fin = new File(fichier+".wav");
		    AudioInputStream aud = null;
		    
		    try {
				aud = AudioSystem.getAudioInputStream(fin);
			} catch (UnsupportedAudioFileException e) {
				System.out.println("Format non supporte");
				e.printStackTrace();
			      return AL10.AL_FALSE;
			} catch (IOException e) {
				e.printStackTrace();
			      return AL10.AL_FALSE;
			}
	    
		    WaveData waveFile;
				waveFile = WaveData.create(aud);


			/*
		    try {
		      fin.close();
		    } catch (java.io.IOException ex) {
		    }*/

		    //Loads the wave file from this class's package in your classpath
		    //WaveData waveFile = WaveData.create("FancyPants.wav");

		    System.out.println(waveFile);
		    AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
		    waveFile.dispose();

		    // Bind the buffer with the source.
		    AL10.alGenSources(source);

		    if (AL10.alGetError() != AL10.AL_NO_ERROR)
		      return AL10.AL_FALSE;

		    AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(0) );
		    AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
		    AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f          );
		    AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos     );
		    AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel     );
		    AL10.alSourcei(source.get(0), AL10.AL_LOOPING,  AL10.AL_TRUE  );

		    // Do another error check and return.
		    if (AL10.alGetError() == AL10.AL_NO_ERROR)
		      return AL10.AL_TRUE;

		    return AL10.AL_FALSE;
		  }

		  /**
		   * void setListenerValues()
		   *
		   *  We already defined certain values for the Listener, but we need
		   *  to tell OpenAL to use that data. This function does just that.
		   */
		  void setListenerValues() {
		    AL10.alListener(AL10.AL_POSITION,    listenerPos);
		    AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
		    AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
		  }

		  /**
		   * void killALData()
		   *
		   *  We have allocated memory for our buffers and sources which needs
		   *  to be returned to the system. This function frees that memory.
		   */
		  void killALData() {
		    AL10.alDeleteSources(source);
		    AL10.alDeleteBuffers(buffer);
		  }

	
	private void jouer(String son)
	{
	    // Initialize OpenAL and clear the error bit.
	    try{
	      AL.create();
	    } catch (LWJGLException le) {
	      le.printStackTrace();
	      return;
	    }

	    AL10.alGetError();
	 
	    // Load the wav data.
	    if(loadALData("Sons/"+nom+"/"+son) == AL10.AL_FALSE) {
	      System.out.println("Error loading data.");
	      return;
	    }
	    setListenerValues();
	    AL10.alSourcePlay(source.get(0));
		Scanner sc = new Scanner(System.in);

		while (!sc.next().equals("e"))
		{

		}
		
		this.killALData();


	}
	
	public void play()
	{
		meteo.play();
	}
	
	public void stop()
	{
		meteo.stop();
	}

	public static void main(String[] args) {
			Environnement test = new Environnement();
			test.play();
			Scanner sc = new Scanner(System.in);

			while (!sc.next().equals("e"))
			{

			}
			test.stop();
			while (!sc.next().equals("e"))
			{

			}

	}

}
