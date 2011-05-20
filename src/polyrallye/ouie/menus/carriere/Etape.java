package polyrallye.ouie.menus.carriere;

import java.util.List;
import java.util.ListIterator;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

/**
 * Etape : permet de lister les étapes ainsi que leurs classements respectifs
 * 
 * @author ochi
 *
 */
public class Etape extends Menu implements ActionMenu {

    protected List<polyrallye.modele.championnat.Etape> etapes;

    public Etape(Menu menuPrecedent,
            List<polyrallye.modele.championnat.Etape> et) {
        super(menuPrecedent);

        etapes = et;
    }

    @Override
    public void actionMenu() {
        Liseuse.lire("Veuillez selectionner une étape");
        Liseuse.marquerPause();
        lancer();
    }

    @Override
    public void remplir() {
        ListIterator<polyrallye.modele.championnat.Etape> i = etapes
                .listIterator(etapes.size());

        while (i.hasPrevious()) {
            polyrallye.modele.championnat.Etape c = i.previous();
            ajouterElement(c.getNom(), new VoitureChampionnat(menuPrecedent, c.getCircuit(), c));
            ajouterElement("classement " + c.getNom(),
                    new afficherClassementEtape(menuPrecedent, c
                            .getClassement()));
        }
    }

}
