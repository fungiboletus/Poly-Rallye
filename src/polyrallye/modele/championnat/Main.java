package polyrallye.modele.championnat;

import polyrallye.modele.personnes.Joueur;

public class Main {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("----------------------------------");

        // insertion xml
        // Joueur J = new Joueur("Marco");
        // J.setSessionCourante();
        //        
        // List<Etape> LE = new ArrayList<Etape>();
        //                
        // LE.add(new Etape(1, "Rallye de Monaco", null,"Calenzana"));
        // LE.add(new Etape(2, "Rallye de Tunisie", null,"CoteReserveCalvi"));
        //        
        // List<Championnat> LC = new ArrayList<Championnat>();
        //
        // Voiture v = StockVoitures.getVoitureParNom("Ferrari Enzo serie");
        //        
        // LC.add(new Championnat(J, "championnat mondial", LE, v, 30000));
        // LC.add(new Championnat(J, "championnat européen", LE, v, 30000));
        //        
        // LE.get(0).setClassement(new Duree(0,5,21,9),
        // StockVoitures.getVoitureParNom("Peugeot 307 WRC"));
        // LE.get(1).setClassement(new Duree(0,5,21,9),
        // StockVoitures.getVoitureParNom("Ferrari Enzo serie"));
        //        
        // System.out.println("---------------------------");
        //        
        // for (int i = 0 ; i< LC.size(); ++i)
        // LC.get(i).EnregistrerChampionnat(LC.get(i));
        //        
        // for (int i = 0; i<LE.size();++i)
        // LE.get(i).EnregistrerEtape(LE.get(i));

        // chargement xml
        String nomJoueur = args.length > 1 ? args[0] : "Marco";
        Joueur j = Joueur.chargerJoueur(nomJoueur);
        j.setSessionCourante();

        Championnat.chargerChampionnat("championnat1");

        //Etape Et = Etape.chargerEtape("Rallye de Monaco");

        // System.out.println(Et.getClassement());

//        Championnat Champ = Championnat
//                .chargerChampionnat("championnat européen");
//        Champ.setClassement();
        
        // on lance la course en donnant l'étape en cours
        // aprés avoir joué la course --> on sauvegarde l'étape et le championnat en cours
    }
}
