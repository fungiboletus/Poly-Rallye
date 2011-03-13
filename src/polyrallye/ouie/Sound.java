package polyrallye.ouie;

public class Sound {

	protected int data;

	protected int id;

	public Sound() {
		data = -1;
		id = -1;
	}

	public Sound(String chemin) {
		data = SoundScape.loadSoundData(chemin);

		id = SoundScape.makeSoundSource(data);
	}

	public void pause(boolean pause) {
		if (id != -1 && data != -1)
		SoundScape.pause(id, pause);
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

	public void delete() {
		if (id != -1 && data != -1) {
		this.stop();
		SoundScape.deleteSoundSource(id);
		SoundScape.deleteSoundData(data);
		id = -1;
		data = -1;
		}
	}

	public void charger(String chemin) {
		if (id == -1 && data == -1) {
			data = SoundScape.loadSoundData(chemin);
			id = SoundScape.makeSoundSource(data);
		}
	}
	
	public void fade() {
		if (id == -1 && data == -1) {
			this.setVelocity(0, 0, 0.1f);
		}
	}
	

}
