package polyrallye.ouie;
import java.io.File;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import polyrallye.ouie.Sound;


public class SonMoteur {

	protected static NavigableMap<Integer, Sound> sons;
	
	public static boolean accelere = false;
	
	public static float regime;

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		lancer();
	}
	
	public static void lancer() {
		
		
		//Sound ralenti = new Sound("Sons/moteur/ralenti.wav");
		

		sons = new TreeMap<Integer, Sound>();
		
		File dossier = new File("Sons/moteur");
		
		for (File son : dossier.listFiles()) {
			String nom = son.getName();
			Integer nb = Integer.parseInt(nom.substring(0, nom.indexOf(".")));
			
			Sound s = new Sound(son.getAbsolutePath());
			s.setLoop(true);
			s.setOffset(2.0f);
			s.play();
			s.pause(true);
			s.setGain(0.0f);
			sons.put(nb, s);
		}
		// régime de 3800
		
		regime = 2000;
		
		Timer t = new Timer();
		
		TimerTask tt = new TimerTask() {
			
			@Override
			public void run() {
				//System.out.println(SonMoteur.accelere);
				float regime = SonMoteur.regime;
				if (SonMoteur.accelere && regime < 8997){
					regime += 10;
				}
				else if (regime > 2010)
				{
					regime -= 20;
				}
				
				SonMoteur.setRegime(regime);
				
			}
		};
		
		t.schedule(tt, 0, 10);
		
		/*while(true) {
			//System.out.println("R: "+ regime + " - "+accelere);
			
			for (int i = 0; i < 1000; ++i);
				//Thread.sleep(0, 1);
		}
		/*while (regime<9400)
		{
			setRegime(regime);
				Thread.sleep(0,1);
			regime += 1;
		}
		while(regime > 3000)
		{
			setRegime(regime);
				Thread.sleep(1);
			regime -= 10;
		}
		setRegime(2000);
		while(regime > 1000)
		{
			setRegime(regime);
				Thread.sleep(1);
			regime -= 20;
		}*/
		
		
		
	}
	
	public static void setRegime(float regime)
	{		
		//System.out.println(regime);
		SonMoteur.regime = regime;
		
		Integer intRegime = (int) regime;
		
		// TODO Recoder ça proprement
		float gain = (0.5f + 0.5f * (regime/10000.0f));
		
		// Cas particulier plutôt rare, le régime est déjà dans les sons
		Sound sonParfait = sons.get(intRegime);
		if (sonParfait != null) {
			sonParfait.setPitch(1.0f);
			sonParfait.setGain(gain);
			
			for (Entry<Integer, Sound> e : sons.entrySet()) {
				if (e.getKey() != intRegime) {
					e.getValue().pause(true);
					//e.getValue().setGain(0.0f);
				}
			}
		}
		else
		{
			// NavigableMap <3
			Entry<Integer, Sound> min = sons.lowerEntry(intRegime);
			Entry<Integer, Sound> max = sons.higherEntry(intRegime);
			
			Integer i_min = min.getKey();
			Integer i_max = max.getKey();
			
			Sound s_min = min.getValue();
			Sound s_max = max.getValue();
			
			//System.out.println(gain);

			final float marge = i_max-i_min;
			
			s_min.setPitch(regime/i_min);
			s_max.setPitch(regime/i_max);
			
				
				float r_a = (i_max-regime)/marge;
				
				r_a = r_a > 0.01f ? r_a*1.3f : 0.0f;
				
				float r_b = (regime-i_min)/marge;
				r_b = r_b > 0.01f ? r_b*1.3f : 0.0f;

				s_min.setGain(r_a*gain);
				s_max.setGain(r_b*gain);
				
				s_min.pause(false);
				s_max.pause(false);

				for (Entry<Integer, Sound> e : sons.entrySet()) {
					if (e.getKey() != i_max && e.getKey() != i_min) {
						e.getValue().pause(true);
						//e.getValue().setGain(0.0f);
					}
				}
		}			
	}

}
