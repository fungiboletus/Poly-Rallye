package polyrallye.ouie.utilitaires;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import polyrallye.ouie.utilitaires.SoundScape;
import polyrallye.utilitaires.CallbackArretSon;
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
	
	protected String identifiant;
	
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
	
	public Sound(File fichier, String identifiantCache) {
		this();
		
		try {
			charger(fichier.getPath(), identifiantCache);
		} catch (SoundException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static Sound depuisCache(String identifiant) {
		TupleData cache = cacheData.get(identifiant);
		
		if (cache != null) {
			Sound son = new Sound();
			son.data = cache.data;
			son.id = SoundScape.makeSoundSource(son.data);
			++cache.nbReferences;
			
			return son;
		} else {
			return null;
		}
	}

	public void pause(boolean pause) {
		if (isAlive() && enPause != pause) {
			SoundScape.pause(id, pause);
			enPause = pause;
		}
	}
	
	public boolean isAlive() {
		return (id != -1 && data != -1);
	}

	public void play() {
		if (isAlive())
			SoundScape.play(id);
	}

	public void stop() {
		if (isAlive())
			SoundScape.stop(id);
	}

	public float getGain() {
		if (isAlive())
			return SoundScape.getGain(id);
		return -1f;
	}

	public void setOffset(float offset) {
		if (isAlive())
			SoundScape.setOffset(id, offset);
	}

	public void setPitch(float pitch) {
		if (isAlive())
			SoundScape.setPitch(id, pitch);
	}

	public void setGain(float gain) {
		if (isAlive())
			SoundScape.setGain(id, gain);
	}

	public void setVelocity(float x, float y, float z) {
		if (isAlive())
			SoundScape.setVelocity(id, x, y, z);
	}

	public void setPosition(float x, float y, float z) {
		if (isAlive())
			SoundScape.setSoundPosition(id, x, y, z);
	}

	public void setReferenceDistance(float distance) {
		if (isAlive())
			SoundScape.setReferenceDistance(id, distance);
	}

	public void setLoop(boolean on) {
		if (isAlive())
			SoundScape.setLoop(id, on);
	}

	public boolean isPlaying() {
		if (isAlive())
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
		if (isAlive()) {
			this.stop();
			SoundScape.deleteSoundSource(id);
			
			TupleData cache = cacheData.get(identifiant);
			
			if (cache != null) {
				--cache.nbReferences;
			}
			//SoundScape.deleteSoundData(data);
			
			id = -1;
			data = -1;
		}
	}

	public void charger(String chemin) throws SoundException {
		charger(chemin, chemin);
	}
	
	public void charger(String chemin, String identifiant) throws SoundException {
		
		if (isAlive()) {
			delete();
		}
		
		this.identifiant = identifiant;

		TupleData cache = cacheData.get(identifiant);
		
		if (cache != null) {
			data = cache.data;
			++cache.nbReferences;
			System.out.println("Récupération du cache pour «"+chemin+"»");
		}
		else
		{
			// Il ne faut pas garder trop de sons dans le cache
			if (cacheData.size() > 64) {
				List<String> aSupprimer = new ArrayList<String>();
				
				for (Entry<String, TupleData> e : cacheData.entrySet()) {
					if (e.getValue().nbReferences == 0) {
						aSupprimer.add(e.getKey());
					}
				}
				
				for (String clef : aSupprimer) {
					TupleData td = cacheData.remove(clef);
					
					SoundScape.deleteSoundData(td.data);
					System.out.println("Suppression du cache pour «"+clef+"»");					
				}
			}
			data = SoundScape.loadSoundData(chemin);
			if (data != -1) {
				cache = new TupleData();
				cache.data = data;
				cache.nbReferences = 1;
				cacheData.put(identifiant, cache);				
			}
		}
		
		id = SoundScape.makeSoundSource(data);

		if (id == -1 || data == -1) {
			throw new SoundException("Impossible de charger le son");
		}
	}

	public void fadeOut(final long duree) {
		if (isAlive()) {
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
		if (isAlive()) {
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
