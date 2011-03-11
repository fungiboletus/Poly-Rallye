package polyrallye.ouie;

public class Sound {

	protected int data;

	protected int id;

	public Sound(String chemin) {
		data = SoundScape.loadSoundData(chemin);

		id = SoundScape.makeSoundSource(data);
	}

	public void pause(boolean pause) {
		SoundScape.pause(id, pause);
	}

	public void play() {
		SoundScape.play(id);
	}

	public void stop() {
		SoundScape.stop(id);
	}

	public float getGain() {
		return SoundScape.getGain(id);
	}

	public void setOffset(float offset) {
		SoundScape.setOffset(id, offset);
	}

	public void setPitch(float pitch) {
		SoundScape.setPitch(id, pitch);
	}

	public void setGain(float gain) {
		SoundScape.setGain(id, gain);
	}

	public void setVelocity(float x, float y, float z) {
		SoundScape.setVelocity(id, x, y, z);
	}

	public void setPosition(float x, float y, float z) {
		SoundScape.setSoundPosition(id, x, y, z);
	}

	public void setReferenceDistance(float distance) {
		SoundScape.setReferenceDistance(id, distance);
	}

	public void setLoop(boolean on) {
		SoundScape.setLoop(id, on);
	}
	
	public boolean isPlaying() {
		return SoundScape.isPlaying(id);
	}
}
