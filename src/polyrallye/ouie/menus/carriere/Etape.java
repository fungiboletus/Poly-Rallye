package polyrallye.ouie.menus.carriere;

import java.util.List;
import java.util.ListIterator;

import polyrallye.modele.championnat.Championnat;
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

    protected Championnat champ;

    public Etape(Menu menuPrecedent,
            List<polyrallye.modele.championnat.Etape> et, Championnat champ) {
        super(menuPrecedent);

        this.champ = champ;
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
            ajouterElement(c.getNom(), new VoitureChampionnat(menuPrecedent, c
                    .getCircuit(), c, champ));
            ajouterElement("classement " + c.getNom(),
                    new afficherClassementEtape(menuPrecedent, c
                            .getClassement(), champ));
        }
    }

}
