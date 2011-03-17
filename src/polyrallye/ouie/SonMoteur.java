package polyrallye.ouie;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import polyrallye.ouie.Sound;


public class SonMoteur {

	static Map<Integer, Sound> sons;
	
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
		

		sons = new HashMap<Integer, Sound>();
		
		
		for (int i = 2000; i < 10000; i+= 500) {
			Sound s = new Sound("Sons/moteur/"+i+".wav");
			s.setLoop(true);
			s.setOffset(2.0f);
			s.play();
			s.setGain(0.0f);
			//s.pause(true);
			sons.put(i, s);
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
		
		// Astuce sur la gestion des entiers
		final int min = ((int) regime)/500*500;
		final int max = min + 500;
		
		
		final float marge = 500;

		/*final float t_min = min + 250 - marge / 2;
		final float t_max = max - 250 + marge / 2;*/
		
		// TODO Recoder ça proprement
		float gain = (0.3f + 0.7f * (regime/10000.0f));
		
		//System.out.println(gain);

		Sound s_min = sons.get(min);
		Sound s_max = sons.get(max);
		
		s_min.setPitch(regime/min);
		s_max.setPitch(regime/max);
		
		/*if (regime < t_min)
		{
			s_min.setGain(gain);
			s_max.setGain(0.0f);

			/*s_min.pause(false);
			s_max.pause(true);
		}
		else if (regime > t_max)
		{
			s_max.setGain(gain);
			s_min.setGain(0.0f);

			/*s_max.pause(false);
			s_min.pause(true);
		}
		else
		{*/
			
			float r_a = (max-regime)/marge;
			
			r_a = r_a > 0.1f ? r_a : 0.0f;
			
			float r_b = (regime-min)/marge;
			r_b = r_b > 0.1f ? r_b : 0.0f;
			
			/*System.out.println("t_min : "+t_min);
			System.out.println("t_max : "+t_max);*/

			/*System.out.println("r_a : "+r_a);
			System.out.println("r_b : "+r_b);*/
			

			s_min.setGain(r_a*gain);
			s_max.setGain(r_b*gain);

			/*s_min.pause(false);
			s_max.pause(false);*/
			
			Sound a = sons.get(min-500);
			if (a != null)
			{
				//a.pause(true);
			}
			a = sons.get(max+500);
			if (a  != null)
			{
				//a.pause(true);
			}
			
			/*s_max.pause(false);
			s_min.pause(false);*/
		/*}
		
		/*float r_a = (4000-regime)/500.0f;
		
		float r_b = (regime-3500)/500.0f;
		
		System.out.println("r_a : "+r_a);
		System.out.println("r_b : "+r_b);
		

		s_3500.setGain(r_a);
		s_4000.setGain(r_b);
		
		
		float l_min = 4000;
		
		if (regime<3600)
		{
			s_3500.setGain(1.0f);
			s_4000.setGain(0.0f);			
		}
		else if (regime > 3900)
		{
			s_3500.setGain(0.0f);
			s_4000.setGain(1.0f);	
		}
		else
		{
			float r_a = (3900-regime)/300.0f;
			
			float r_b = (regime-3600)/300.0f;
			
			System.out.println("r_a : "+r_a);
			System.out.println("r_b : "+r_b);
			

			s_3500.setGain(r_a);
			s_4000.setGain(r_b);
		}
		
		
		s_3500.setPitch(regime/3500.0f);
		s_4000.setPitch(regime/4000.0f);*/
			
	}

}
