package polyrallye.ouie.utilitaires;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import polyrallye.ouie.CallbackArretSon;
import polyrallye.utilitaires.Multithreading;

public class Sound {

	public class SoundException extends Exception {
		private static final long serialVersionUID = -494164003851555709L;

		public SoundException() {
			super();
		}

		public SoundException(String message, Throwable cause) {
			super(message, cause);
		}

		public SoundException(String message) {
			super(message);
		}

		public SoundException(Throwable cause) {
			super(cause);
		}
	}
	
	protected class TupleData {
		public int nbReferences;
		public int data;
	}

	protected static Map<String, TupleData> cacheData; 
	
	protected String chemin;
	
	protected int data;

	protected int id;
	
	protected boolean enPause = false;

	static {
		cacheData = new HashMap<String, Sound.TupleData>();
	}
	
	public Sound() {
		data = -1;
		id = -1;
	}

	public Sound(String chemin, Object exceptionMode) throws SoundException {
		this();

		charger(chemin);
	}
	
	public Sound(String chemin) {
		this();

		try {
			charger(chemin);
		} catch (SoundException se) {
			System.err.println(se.getMessage());
		}
	}
	
	public Sound(Sound source) {
		data = source.data;
		id = SoundScape.makeSoundSource(data);
	}

	public void pause(boolean pause) {
		if (id != -1 && data != -1 && enPause != pause) {
			SoundScape.pause(id, pause);
			enPause = pause;
		}
	}

	public void play() {
		if (id != -1 && data != -1)
			SoundScape.play(id);
	}

	public void stop() {
		if (id != -1 && data != -1)
			SoundScape.stop(id);
	}

	public float getGain() {
		if (id != -1 && data != -1)
			return SoundScape.getGain(id);
		return -1f;
	}

	public void setOffset(float offset) {
		if (id != -1 && data != -1)
			SoundScape.setOffset(id, offset);
	}

	public void setPitch(float pitch) {
		if (id != -1 && data != -1)
			SoundScape.setPitch(id, pitch);
	}

	public void setGain(float gain) {
		if (id != -1 && data != -1)
			SoundScape.setGain(id, gain);
	}

	public void setVelocity(float x, float y, float z) {
		if (id != -1 && data != -1)
			SoundScape.setVelocity(id, x, y, z);
	}

	public void setPosition(float x, float y, float z) {
		if (id != -1 && data != -1)
			SoundScape.setSoundPosition(id, x, y, z);
	}

	public void setReferenceDistance(float distance) {
		if (id != -1 && data != -1)
			SoundScape.setReferenceDistance(id, distance);
	}

	public void setLoop(boolean on) {
		if (id != -1 && data != -1)
			SoundScape.setLoop(id, on);
	}

	public boolean isPlaying() {
		if (id != -1 && data != -1)
			return SoundScape.isPlaying(id);
		return false;
	}

	public void playAndWait() {
		play();

		while (isPlaying()) {
			Multithreading.dormir(100);
		}
	}

	public void playAndWaitWithCallback(CallbackArretSon car) {
		play();

		while (isPlaying() && car.continuerLecture()) {
			Multithreading.dormir(100);
		}
	}

	public void delete() {
		if (id != -1 && data != -1) {
			this.stop();
			SoundScape.deleteSoundSource(id);
			
			TupleData cache = cacheData.get(chemin);
			
			if (cache != null) {
				--cache.nbReferences;
			}
			//SoundScape.deleteSoundData(data);
			
			id = -1;
			data = -1;
		}
	}

	public void charger(String chemin) throws SoundException {
		
		if (id != -1 && data != -1) {
			delete();
		}
		
		this.chemin = chemin;

		TupleData cache = cacheData.get(chemin);
		
		if (cache != null) {
			data = cache.data;
			++cache.nbReferences;
			System.out.println("Cache pour "+chemin);
		}
		else
		{
			// Il ne faut pas garder trop de sons dans le cache
			if (cacheData.size() > 64) {
				for (Entry<String, TupleData> e : cacheData.entrySet()) {
					TupleData td = e.getValue();
					if (td.nbReferences == 0) {
						SoundScape.deleteSoundData(td.data);
						cacheData.remove(e.getKey());
						System.out.println("enlèvement du cache pour "+e.getKey());
					}
				}
			}
			
			data = SoundScape.loadSoundData(chemin);
			if (data != -1) {
				cache = new TupleData();
				cache.data = data;
				cache.nbReferences = 1;
				cacheData.put(chemin, cache);				
			}
		}
		
		id = SoundScape.makeSoundSource(data);

		if (id == -1 || data == -1) {
			throw new SoundException("Impossible de charger le son");
		}
	}

	public void fadeOut(final long duree) {
		if (id != -1 && data != -1) {
			new Thread() {
				public void run() {
					
					float gain = getGain();
					
					int nbEtapes = (int) (duree/50);
					
					float d = gain/nbEtapes;

					gain -= d;
					
					do {
						setGain(gain);
						gain -= d;
						Multithreading.dormir(50);
					} while (gain > 0.0f);
					
					pause(true);
					
				}
			}.run();
			
		}
	}
	
	public void fadeIn(final long duree, final float gainFinal) {
		if (id != -1 && data != -1) {
			new Thread() {
				public void run() {
					
					pause(false);
					
					float gain = getGain();
					
					int nbEtapes = (int) (duree/50);
					
					float d = (gainFinal - gain)/nbEtapes;

					gain += d;
					
					do {
						setGain(gain);
						gain += d;
						Multithreading.dormir(50);
					} while (gain < gainFinal);
					
					setGain(gainFinal);
					
					
				}
			}.run();
			
		}
	}

}
