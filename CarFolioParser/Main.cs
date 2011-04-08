using System;
using System.Text;
using System.Text.RegularExpressions;
using System.Net;
using System.Net.Sockets;
using System.IO;
using System.Threading;

using HtmlAgilityPack;

namespace CarFolioParser
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			string numero;
			
			while(true) {
				numero = demander("Quelle numéro de voiture ?");
				if (numero == null || numero.Length <= 1)
					break;
				analyserDocument(numero);
				Console.WriteLine(" ==================== ");
			};
		}
		
		public static void analyserDocument(string numero)
		{
			Console.WriteLine("Traitement de la voiture numéro "+numero);
			
			Console.WriteLine("Récupération de la page web");
			
			HttpWebRequest requete = (HttpWebRequest) WebRequest.Create("http://www.carfolio.com/specifications/models/car/?car="+numero);
			requete.Timeout = Timeout.Infinite;
			requete.UserAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.27 (KHTML, like Gecko) Ubuntu/10.10 Chromium/12.0.720.0 Chrome/12.0.720.0 Safari/534.27";
			requete.Method = "GET";
			
			HttpWebResponse reponse = (HttpWebResponse) requete.GetResponse();
			
			HtmlDocument document = new HtmlDocument();
			
			StreamReader SR = new StreamReader(reponse.GetResponseStream(), System.Text.Encoding.UTF8);
			string page = (string) (SR.ReadToEnd());
			
			
			//document.Load(reponse.GetResponseStream(), System.Text.Encoding.UTF8);
			//document.Load("../../"+numero+".html");
			document.LoadHtml(page);
			
			HtmlNode n = document.DocumentNode.SelectSingleNode("id('contentmain')");
			
			//Console.WriteLine(n.InnerHtml);
			String constructeur = confirmerUtilisateur("le constructeur", n.SelectSingleNode("ul/li[3]/a").InnerText.Replace("cars","").Trim());
			
			Regex r_number = new Regex("^\\d*");
			
			String nom = "";
			try {
				nom = n.SelectSingleNode("ul/li[2]/a").InnerText.Replace("models","").Replace(constructeur, "").Trim();
			} catch (Exception ex) { }
			nom = confirmerUtilisateur("le nom", nom);
			
			String version = "";
			try {
				version = n.SelectSingleNode("ul/li[1]/strong").InnerText;
			} catch (Exception ex) { }
			
			String annee = r_number.Match(version).Groups[0].Value;
			if (annee == "") {
				annee = "canard";
			}
			
			version = confirmerUtilisateur("la version", version.Replace(annee,"").Replace(nom,"").Replace(constructeur, "").Trim());
			annee = confirmerUtilisateur("l'année", annee);
			
			String rarete = demander("Quelle est sa rareté ?");
			//String prix = demander("Quel est son prix ?");
			
			String poids = "";
			try {
				poids = n.SelectSingleNode("table/tbody[6]/tr[9]/td[1]/strong").InnerText.Replace("kg","").Trim();
			} catch (Exception ex) { }
			poids = confirmerUtilisateur("le poids", poids);
			
			String largeur = "";
			try {
				largeur = n.SelectSingleNode("table/tbody[6]/tr[5]/td[1]").InnerText.Replace("mm","").Trim();
			} catch (Exception ex) { }
			largeur = confirmerUtilisateur("la largeur", largeur);
			
			String longueur = "";
			try {
				longueur = n.SelectSingleNode("table/tbody[6]/tr[4]/td[1]/strong").InnerText.Replace("mm","").Trim();
			} catch (Exception ex) { }
			longueur = confirmerUtilisateur("la longueur", longueur);


			String empattement = "";
			try {
				empattement = n.SelectSingleNode("table/tbody[6]/tr[1]/td[1]/strong").InnerText.Replace("mm","").Trim();
			} catch (Exception ex) { }
			empattement = confirmerUtilisateur("l'empattement", empattement);

			String nb_rapports = "";
			try {
				nb_rapports = r_number.Match(n.SelectSingleNode("table/tbody[16]/tr[18]/td").InnerText).Groups[0].Value.Trim();
			} catch (Exception ex) { }
			nb_rapports = confirmerUtilisateur("le nombre de rapports", nb_rapports);
			
			Regex r_drive = new Regex("(.+) wheel");

			String drive = "";
			
			try {
				drive = r_drive.Match(page).Groups[1].Value.Trim().ToLower();
			} catch (Exception ex) { }
			
			//Console.WriteLine(document.DocumentNode.InnerHtml);
			
			if (drive.Contains("front"))
			{
				drive = "traction";
			} else if (drive.Contains("all") || drive.Contains("four"))
			{
				drive = "4x4";
			} else if (drive.Contains("rear")) {
				drive = "propulsion";
			}
			else {
				drive = "CANARD";
			}
			drive = confirmerUtilisateur("la transmission", drive);
			
			Regex r_cylindree = new Regex("(\\d+) cc");
			
			String cylindree = "";
			try {
				cylindree = r_cylindree.Match(n.SelectSingleNode("table/tbody[10]/tr[3]/td/strong").InnerText).Groups[1].Value.Trim();
			} catch (Exception ex) { }
			cylindree = confirmerUtilisateur("la cylindrée", cylindree);
			
			Regex r_regime = new Regex("@ (\\d+)");
			
			Regex r_puissance_max = new Regex("\\((\\d+) bhp\\)");
			Regex r_puissance_max_b = new Regex("\\((\\d+)\\.\\d bhp\\)");
			
			String infos_puissance = "";
			try {
				infos_puissance = n.SelectSingleNode("table/tbody[10]/tr[9]/td").InnerText;
			} catch (Exception ex) { }

			String puissance_max = r_puissance_max.Match(infos_puissance).Groups[1].Value.Trim();
			if (puissance_max.Length == 0)
			{
				puissance_max = r_puissance_max_b.Match(infos_puissance).Groups[1].Value.Trim();
			}
			puissance_max = confirmerUtilisateur("la puissance maximale", puissance_max);
			String regime_puissance_max = confirmerUtilisateur("le régime associé", r_regime.Match(infos_puissance).Groups[1].Value.Trim());
			
			
			Regex r_couple_max = new Regex("(\\d+)\\.\\d Nm");
			
			String infos_couple = "";
			try {
				infos_couple = n.SelectSingleNode("table/tbody[10]/tr[11]/td").InnerText;
			} catch (Exception ex) { }

			String couple_max = confirmerUtilisateur("le couple maximal", r_couple_max.Match(infos_couple).Groups[1].Value.Trim());
			String regime_couple_max = confirmerUtilisateur("le régime associé", r_regime.Match(infos_couple).Groups[1].Value.Trim());
			
			String compression = "";
			try {
				compression = n.SelectSingleNode("table/tbody[10]/tr[21]/td").InnerText.Trim();
			} catch (Exception ex) { }
			compression = confirmerUtilisateur("la compression", compression.ToLower());
			
			Regex r_nb_valves = new Regex("(\\d+) valves in");
			
			String nb_valves = "";
			try {
				nb_valves = n.SelectSingleNode("table/tbody[10]/tr[4]/td").InnerText;
			} catch (Exception ex) { }
			nb_valves = confirmerUtilisateur("le nombre de valves", r_nb_valves.Match(nb_valves).Groups[1].Value.Trim());

			String infos_cylindres = "";
			try {
				infos_cylindres = n.SelectSingleNode("table/tbody[10]/tr[2]/td/strong").InnerText;
			} catch (Exception ex) { }

			Regex r_nb_cylindres = new Regex("\\d+");
			String nb_cylindres = confirmerUtilisateur("le nombre de cylindres", r_nb_cylindres.Match(infos_cylindres).Groups[0].Value.Trim());
			String disposition = "ligne";
			if (infos_cylindres.Contains("V"))
			{
				disposition = "V";
			}
			//nom = confirmerUtilisateur("le nom", nom.InnerText.Trim());
			//Console.WriteLine(constructeur);
			/*
			HtmlNode type = n.SelectSingleNode("id('contentmain')/x:ul/x:li)");
			Console.WriteLine(type.InnerText);*/
			//Console.WriteLine(constructeur.InnerText.Replace("cars","").Trim());
			
			Directory.CreateDirectory("Voitures");
			Directory.CreateDirectory("Voitures/"+constructeur);
			Directory.CreateDirectory("Voitures/"+constructeur+"/"+nom);
			
			StreamWriter st = new StreamWriter("Voitures/"+constructeur+"/"+nom+"/"+constructeur+"_"+nom+"_"+(version.Length > 0 ? version.Replace(" ","_") : "serie" )+".xml");
			st.WriteLine("<!-- Créé avec CarFolioParser -->");
			st.WriteLine("<voiture>");
			
			st.WriteLine("\t<presentation>");
			st.WriteLine("\t\t<constructeur>"+constructeur+"</constructeur>");
			st.WriteLine("\t\t<nom>"+nom+"</nom>");
			if (version.Length > 0) {
				st.WriteLine("\t\t<version>"+version+"</version>");
			}
			st.WriteLine("\t\t<periode annee=\""+annee+"\" />");
			st.WriteLine("\t</presentation>");
			
			st.WriteLine("\t<economie>");
			st.WriteLine("\t\t<rarete>"+rarete+"</rarete>");
			//st.WriteLine("\t\t<prix>"+prix+"</prix>");
			st.WriteLine("\t</economie>");
			
			st.WriteLine("\t<moteur>");
			st.WriteLine("\t\t<configuration nb_cylindres=\""+nb_cylindres+"\" nb_soupapes=\""+nb_valves+"\" disposition=\""+disposition+"\" compression=\""+compression+"\"/>");
			st.WriteLine("\t\t<cylindree>"+cylindree+"</cylindree>");
			st.WriteLine("\t\t<puissance regime=\""+regime_puissance_max+"\">"+puissance_max+"</puissance>");
			st.WriteLine("\t\t<couple_max regime=\""+regime_couple_max+"\">"+couple_max+"</couple_max>");
			st.WriteLine("\t</moteur>");
			
			st.WriteLine("\t<transmission>");
			st.WriteLine("\t\t<nb_rapports>"+nb_rapports+"</nb_rapports>");
			st.WriteLine("\t\t<type>"+drive+"</type>");
			st.WriteLine("\t</transmission>");
			
			st.WriteLine("\t<chassis>");
			st.WriteLine("\t\t<poids>"+poids+"</poids>");
			st.WriteLine("\t\t<largeur>"+largeur+"</largeur>");
			st.WriteLine("\t\t<longueur>"+longueur+"</longueur>");
			st.WriteLine("\t\t<empattement>"+empattement+"</empattement>");
			st.WriteLine("\t</chassis>");
			
			st.WriteLine("\t<sources>");
			st.WriteLine("\t\t<source>http://www.carfolio.com/specifications/models/car/?car="+numero+"</source>");
			st.WriteLine("\t</sources>");
			
			st.WriteLine("</voiture>");
			
			st.Close();
		}
		
		public static string confirmerUtilisateur(string message, string valeur)
		{
			string retour = demander("Modifier "+message+" «"+valeur+"» ?");
			
			if (retour.Length == 0)
			{
				return valeur;
			}
			else {
				return retour;
			}
		}
		
		public static string demander(string message)
		{
			Console.WriteLine(message);
			
			Console.Write("> ");
			String ligne = Console.ReadLine();
			
			return (ligne != null) ? ligne.Trim() : null;
		}
	}
}

