package polyrallye.ouie.menus.carriere;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

/**
 * Championnat : Permet de lister les championnats ainsi que leurs classements
 * respectifs
 * 
 * @author ochi
 * 
 */
public class Championnat extends Menu implements ActionMenu {

    public Championnat(Menu menuPrecedent) {
        super(menuPrecedent);

        messageMenuVide = "Il n'y a pas de championnat";
    }

    @Override
    public void actionMenu() {

        Liseuse
                .lire("Vous êtes dans le menu Championnat Veuillez sélectionner un championnat");
        Liseuse.marquerPause();

        lancer();
    }

    @Override
    public void remplir() {
        List<polyrallye.modele.championnat.Championnat> l = new ArrayList<polyrallye.modele.championnat.Championnat>();
        l.add(polyrallye.modele.championnat.Championnat
<<<<<<< HEAD
                .chargerChampionnat("championnatEurope"));
        l.add(polyrallye.modele.championnat.Championnat
                .chargerChampionnat("championnatAfrique"));
=======
                .chargerChampionnat("championnatAfrique"));
        l.add(polyrallye.modele.championnat.Championnat
                .chargerChampionnat("championnatEurope"));
        // l.add(polyrallye.modele.championnat.Championnat
        // .chargerChampionnat("championnat européen"));
>>>>>>> 8c1fced5609b80d206f4f64b8c583ff4d2702aa3
        ListIterator<polyrallye.modele.championnat.Championnat> i = l
                .listIterator(l.size());

        while (i.hasPrevious()) {
            polyrallye.modele.championnat.Championnat c = i.previous();
            ajouterElement(c.getNom(), new Etape(this, c.getEtapes()));
            c.setClassement();
            ajouterElement("classement " + c.getNom(),
                    new afficherClassementChampionnat(menuPrecedent, c
                            .getClassement()));
        }
    }
}
