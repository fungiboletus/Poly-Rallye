

/**
 * SoundScape wraps OpenAL functions to create a sound environment. Can load and
 * play several sounds, set properties of sounds (pitch, position, volume,
 * dropoff), pause all sounds or one sound, control global volume.
 * 
 * Requires the WaveData class to load wav files.
 * <P>
 * Borrows heavily from Brian Matzon's PlayTest demo (at http://lwjgl.org)
 * <P>
 * 
 * This class is highly modified for PolyRallye.
 */


package polyrallye.ouie.utilitaires;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.openal.*;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.List;

import polyrallye.ouie.utilitaires.ogg.OggData;
import polyrallye.ouie.utilitaires.ogg.OggDecoder;


public abstract class SoundScape {

	static List<Integer> soundDataBuffers;
	static List<Integer> soundSources;

	static boolean ALcreated = false;
	static boolean haveVorbis = false;
	static float globalGain = 1f;
	static float referenceDistance = 5f;
	static int lastError;

	static {
		soundDataBuffers = new ArrayList<Integer>();
		soundSources = new ArrayList<Integer>();
		
		// Création automatique (et oui)
		create();
	}

	/**
	 * Prepare the SoundScape class for use
	 */
	public static void create() {
		if (!ALcreated) {
			try {
				AL.create();
			} catch (Exception e) {
				System.out
						.println("Unable to create OpenAL.\nPlease make sure that OpenAL is available on this system. Exception: "
								+ e);
				return;
			}

			// Set these here?
			// AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE);
			// AL10.alListenerf(AL10.AL_ROLLOFF_FACTOR, .001f);
			// AL10.alListenerf(AL10.AL_REFERENCE_DISTANCE, 100f);

			// Check if VORBIS sound format is supported
			System.out
					.print("SoundScape.create(): Checking for Vorbis extension...");
			if (AL10.alIsExtensionPresent("AL_EXT_vorbis")) {
				System.out.println("Vorbis extension found.");
				haveVorbis = true;
			} else {
				System.out.println("Vorbis extension not supported.");
			}

			ALcreated = true;
		}
	}

	/**
	 * Free up allocated sounds and shutdown audio environment.
	 */
	public static void destroy() {
		// stop all sounds
		for (Integer i : soundSources) {
			stop(i);
			deleteSoundSource(i);
		}

		// delete buffers and sources
		for (Integer i : soundDataBuffers) {
			deleteSoundData(i);
		}

		// reset counters and flags
		ALcreated = false;

		// shutdown
		alExit();
	}

	static protected void alExit() {
		if (AL.isCreated()) {
			AL.destroy();
		}
	}

    public static void exit(int arg) {
    	System.out.println("SoundScape: got error # " + arg);
    	System.out.println("Correspond a "+getALErrorString(arg));
    	//System.exit(arg);
    }
    
    /**
     * Gestion des erreurs
     */
    public static String getALErrorString(int err) {
    	  switch (err) {
    	    case AL10.AL_NO_ERROR:
    	      return "AL_NO_ERROR";
    	    case AL10.AL_INVALID_NAME:
    	      return "AL_INVALID_NAME";
    	    case AL10.AL_INVALID_ENUM:
    	      return "AL_INVALID_ENUM";
    	    case AL10.AL_INVALID_VALUE:
    	      return "AL_INVALID_VALUE";
    	    case AL10.AL_INVALID_OPERATION:
    	      return "AL_INVALID_OPERATION";
    	    case AL10.AL_OUT_OF_MEMORY:
    	      return "AL_OUT_OF_MEMORY";
    	    default:
    	      return "No such error code";
    	  }
    	}

	// ========================================================================
	// functions that control entire sound environment
	// ========================================================================

	/**
	 * Pause or resume all sounds playing in the SoundScape
	 */
	public static void pause(boolean bool) {
		for (Integer i : soundSources) {
			pause(i, bool);
		}
	}

	/**
	 * Stop all sounds playing in the SoundScape
	 */
	public static void stop() {
		for (Integer i : soundSources) {
			stop(i);
		}
	}

	/**
	 * Set the volume for all sounds. Use this to adjust ambient volume level
	 * for entire sound scape.
	 * 
	 * @param gain
	 *            float: 0 - 1
	 */
	public static void setGlobalGain(float gain) {
		if (gain != globalGain) {
			if (gain > 1) {
				gain = 1;
			} else if (gain < 0) {
				gain = 0;
			}
			globalGain = gain;

			for (Integer i : soundSources) {
				setGain(i, getGain(i));
			}
		}
	}

	/**
	 * Set the reference distance, used by OpenAL to calculate how fast sound
	 * drops off. This distance should be proportional to the size of your
	 * scene, ie. a scene that is hundreds of units wide might have a reference
	 * distance of 10 for a "small" sound, or 100 for a "big" sound that fills
	 * the whole space.
	 * <P>
	 * 
	 * @see makeSoundSource().
	 */
	public static void setReferenceDistance(float d) {
		referenceDistance = d;
	}

	/**
	 * Set world coordinates of listener.
	 */
	public static void setListenerPosition(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
	}

	/**
	 * Set world coordinates of listener.
	 */
	public static void setListenerVelocity(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_VELOCITY, x, y, z);
	}

	/**
	 * Set orientation of the listener. first 3 elements are "at", second 3 are
	 * "up"
	 */
	public static void setListenerOrientation(float lookatx, float lookaty,
			float lookatz, float upx, float upy, float upz) {
		float[] fvals = { lookatx, lookaty, lookatz, upx, upy, upz };
		AL10.alListener(AL10.AL_ORIENTATION, allocFloats(fvals));
	}

	// ========================================================================
	// functions to control a sound source
	// ========================================================================

	/**
	 * Pause or resume one sound
	 */
	public static void pause(int soundSourceHandle, boolean pause) {
		if (pause) {
			AL10.alSourcePause(soundSourceHandle);
		} else {
			AL10.alSourcePlay(soundSourceHandle);
		}
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			exit(lastError);
		}
	}

	/**
	 * Play a sound source
	 */
	public static void play(int soundSourceHandle) {
		AL10.alSourcePlay(soundSourceHandle);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			exit(lastError);
		}
	}

	/**
	 * Stop playing one sound.
	 */
	public static void stop(int soundSourceHandle) {
		AL10.alSourceStop(soundSourceHandle);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			exit(lastError);
		}
	}

	public static float getGain(int soundSourceHandle) {
		return AL10.alGetSourcef(soundSourceHandle, AL10.AL_GAIN);
	}

	public static void setVelocity(int soundSourceHandle, float x, float y,
			float z) {
		AL10.alSource3f(soundSourceHandle, AL10.AL_VELOCITY, x, y, z);
	}

	public static void setOffset(int soundSourceHandle, float offset) {
		AL10.alSourcef(soundSourceHandle, AL11.AL_SEC_OFFSET, offset);
	}

	/**
	 * @param soundSourceHandle
	 *            set pitch of this sound source
	 * @param pitch
	 */
	public static void setPitch(int soundSourceHandle, float pitch) {
		AL10.alSourcef(soundSourceHandle, AL10.AL_PITCH, pitch);
	}

	/**
	 * Set the soundsource volume. Gain range is 0.0 or above. A value of 1.0
	 * means unattenuated/unchanged. Each division by 2 equals an attenuation of
	 * -6dB. Each multiplication by 2 equals an amplification of +6dB. A value
	 * of 0.0 turns sound off.
	 * 
	 * @param soundSourceHandle
	 * @param gain
	 */
	public static void setGain(int soundSourceHandle, float gain) {
		AL10.alSourcef(soundSourceHandle, AL10.AL_GAIN, gain * globalGain);
	}

	public static void setSoundPosition(int soundSourceHandle, float x,
			float y, float z) {
		AL10.alSource3f(soundSourceHandle, AL10.AL_POSITION, x, y, z);
	}

	public static void setSoundPosition(int soundSourceHandle, float[] xyz) {
		AL10.alSource(soundSourceHandle, AL10.AL_POSITION, allocFloats(xyz));
	}

	/**
	 * set the dropoff distance for this sound. this number controls how far the
	 * sound travels. The distance parameter should be proportional to the units
	 * of distance used in the scene.
	 * <P>
	 * 
	 * @param soundSourceHandle
	 * @param distance
	 */
	public static void setReferenceDistance(int soundSourceHandle,
			float distance) {
		AL10.alSourcef(soundSourceHandle, AL10.AL_REFERENCE_DISTANCE, distance);
	}

	public static void setLoop(int soundSourceHandle, boolean on) {
		AL10.alSourcei(soundSourceHandle, AL10.AL_LOOPING, (on ? AL10.AL_TRUE
				: AL10.AL_FALSE));
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			exit(lastError);
		}
	}

	/**
	 * return true if this sound is playing
	 * 
	 * @param soundSourceHandle
	 * @return true if sound is playing
	 */
	public static boolean isPlaying(int soundSourceHandle) {
		int state = AL10.alGetSourcei(soundSourceHandle, AL10.AL_SOURCE_STATE);
		
		return (state == AL10.AL_PLAYING);
	}

	// ========================================================================
	// functions to load sound samples and create sources
	// ========================================================================

	/**
	 * Reads the file into a ByteBuffer
	 * 
	 * @param filename
	 *            Name of file to load
	 * @return ByteBuffer containing file data
	 */
	static protected ByteBuffer getData(String filename) {
		ByteBuffer buffer = null;

		System.out.println("Attempting to load: " + filename);

		try {
			BufferedInputStream bis = new BufferedInputStream(WaveData.class
					.getClassLoader().getResourceAsStream(filename));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int bufferLength = 4096;
			byte[] readBuffer = new byte[bufferLength];
			int read = -1;

			while ((read = bis.read(readBuffer, 0, bufferLength)) != -1) {
				baos.write(readBuffer, 0, read);
			}

			// done reading, close
			bis.close();

			// if ogg vorbis data, we need to pass it unmodified to alBufferData
			buffer = ByteBuffer.allocateDirect(baos.size());
			buffer.order(ByteOrder.nativeOrder());
			buffer.put(baos.toByteArray());
			buffer.rewind();
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return buffer;
	}

	/**
	 * Return integer handle to allocated sound data buffer.
	 * 
	 * @return
	 */
	static int allocateSoundData() {
		// check that create() was run first
		if (ALcreated == false) {
			System.out
					.println("SoundScape.allocateSoundData() error: OpenAL has not been initialized -- call SoundScape.create() first!");
		}
		IntBuffer soundDataHandle = allocInts(1);
		// al generate buffers and sources
		AL10.alGenBuffers(soundDataHandle);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			exit(lastError);
		}
		
		soundDataBuffers.add(soundDataHandle.get(0));
		
		return soundDataHandle.get(0);
	}

	/**
	 * Return integer handle to new sound source. Expects a handle to a loaded
	 * sound data buffer (see loadSoundData()). Multiple sound sources can share
	 * the same data buffer. Each sound source can have a position, pitch,
	 * velocity that will affect the way the sound plays.
	 * 
	 * @return
	 */
	public static int makeSoundSource(int soundDataHandle) {
		IntBuffer soundSourceHandle = allocInts(1);
		// al generate buffers and sources
		AL10.alGenSources(soundSourceHandle);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			exit(lastError);
		}
		// bind the sound source to a sound data buffer
		AL10.alSourcei(soundSourceHandle.get(0), AL10.AL_BUFFER,
				soundDataHandle);
		// set the distance for sound droppoff calculations (a typical distance
		// at which we should hear this sound)
		AL10.alSourcef(soundSourceHandle.get(0), AL10.AL_REFERENCE_DISTANCE,
				referenceDistance);
		// set volume to current environment volume
		AL10.alSourcef(soundSourceHandle.get(0), AL10.AL_GAIN, globalGain);
		// hold onto the new sound source
		soundSources.add(soundSourceHandle.get(0));
		
		return soundSourceHandle.get(0);
	}

	/**
	 * Delete the sound data buffer with the given handle
	 * 
	 * @param whichSoundHandle
	 */
	public static void deleteSoundData(int soundDataHandle) {
		IntBuffer ib = allocInts(1);
		ib.put(soundDataHandle);
		ib.position(0).limit(1);
		AL10.alDeleteBuffers(ib);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			exit(lastError);
		}
	}

	/**
	 * Delete the sound source with the given handle
	 * 
	 * @param whichSoundHandle
	 */
	public static void deleteSoundSource(int soundSourceHandle) {
		IntBuffer ib = allocInts(1);
		ib.put(soundSourceHandle);
		ib.position(0).limit(1);
		AL10.alDeleteSources(ib);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			exit(lastError);
		}
	}

	/**
	 * Load sound data from a file into a buffer. Returns a handle that is
	 * passed to makeSoundSource() to create a sound in the environment.
	 * Multiple sound sources can be bound to one data buffer.
	 * <P>
	 * 
	 * @param soundFilename
	 *            name of file to load (.wav)
	 * @return numeric handle to the sound data buffer
	 */
	public static int loadSoundData(String soundFilename) // , int
	// soundDataBufferHandle)
	{
		int soundDataBufferHandle = allocateSoundData();
		if (soundFilename.endsWith(".ogg")) {
			if (haveVorbis) {
				ByteBuffer filebuffer = getData(soundFilename);
				AL10.alBufferData(soundDataBufferHandle,
						AL10.AL_FORMAT_VORBIS_EXT, filebuffer, -1);
				filebuffer.clear();
			} else {

				try {
					OggDecoder decoder = new OggDecoder();
					FileInputStream fis = new FileInputStream(soundFilename);

					OggData ogg = decoder.getData(fis);

					AL10
							.alBufferData(soundDataBufferHandle,
									ogg.channels > 1 ? AL10.AL_FORMAT_STEREO16
											: AL10.AL_FORMAT_MONO16, ogg.data,
									ogg.rate);

					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} else {
	          // load wave data into buffer (manière de l'auteur mais j'y crois plus)
	          //WaveData wavefile = WaveData.create(soundFilename);
	        	
	        	//FAIT MAISON BEURK PAS BEAU
			  
	          File fin = new File(soundFilename);
			    AudioInputStream aud = null;
			    
			    try {
					aud = AudioSystem.getAudioInputStream(fin);
				} catch (UnsupportedAudioFileException e) {
					System.out.println("Format non supporte");
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				WaveData wavefile = WaveData.create(aud);
			
			
			
			
			AL10.alBufferData(soundDataBufferHandle, wavefile.format,
					wavefile.data, wavefile.samplerate);
			wavefile.dispose();
		}
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			exit(lastError);
		}
		return soundDataBufferHandle;
	}

	/**
	 * 
	 * <P>
	 * 
	 * @param soundFilename
	 *            name of file to load (.wav)
	 * @return numeric handle to the sound data buffer
	 */
	public static int makeSound(String soundFilename) {
		int soundData; // handle to sound data buffers
		int source; // handle to sound sources

		// start up OpenAL env
		if (!ALcreated)
			create();

		// Load sound data into buffer
		soundData = loadSoundData(soundFilename);

		// Create sound source bound to the sound data
		source = makeSoundSource(soundData);

		// place it at origin
		setSoundPosition(source, 0, 0, 0);

		// return the sound (play it with SoundScape.play(soundHandle);)
		return source;
	}

	// =========================================================================
	// These are taken verbatim from GLApp
	// =========================================================================
	public static final int SIZE_FLOAT = 4;
	public static final int SIZE_INT = 4;

	public static IntBuffer allocInts(int howmany) {
		return ByteBuffer.allocateDirect(howmany * SIZE_INT).order(
				ByteOrder.nativeOrder()).asIntBuffer();
	}

	public static FloatBuffer allocFloats(float[] floatarray) {
		FloatBuffer fb = ByteBuffer.allocateDirect(
				floatarray.length * SIZE_FLOAT).order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		fb.put(floatarray).flip();
		return fb;
	}

}
