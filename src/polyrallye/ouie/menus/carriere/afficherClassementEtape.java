package polyrallye.ouie.menus.carriere;

import java.util.ArrayList;
import java.util.List;
import polyrallye.modele.championnat.Rang;
import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class afficherClassementEtape extends Menu implements ActionMenu {

    protected List<Rang> classements = new ArrayList<Rang>();

    public afficherClassementEtape(Menu menuPrecedent, List<Rang> classements) {
        super(menuPrecedent);

        this.classements = classements;

        messageMenuVide = "Vous n'avez pas encore participer à cet étape";
    }

    @Override
    public void actionMenu() {
        lancer();
    }

    @Override
    public void remplir() {
        ajouterElement("votre classement", new ActionMenu() {

            @Override
            public void actionMenu() {
                for (int i = 0; i < classements.size(); ++i) {
                    if (Joueur.session.getNom() == classements.get(i)
                            .getPersonne().getNom())
                        Liseuse.lire("numéro "
                                + classements.get(i).getClassement() + ", "
                                + classements.get(i).getPersonne().getNom()
                                + ", "
                                + classements.get(i).getDuree().getHeures()
                                + " heures "
                                + classements.get(i).getDuree().getMinutes()
                                + " minutes "
                                + classements.get(i).getDuree().getSecondes()
                                + " secondes "
                                + classements.get(i).getDuree().getDixiemes()
                                + " dixièmes ");
                }

            }
        });
        ajouterElement("votre position par rappor au premier",
                new ActionMenu() {

                    @Override
                    public void actionMenu() {
                        Liseuse.lire("numéro "
                                + classements.get(0).getClassement() + ", "
                                + classements.get(0).getPersonne().getNom()
                                + ", "
                                + classements.get(0).getDuree().getHeures()
                                + " heures "
                                + classements.get(0).getDuree().getMinutes()
                                + " minutes "
                                + classements.get(0).getDuree().getSecondes()
                                + " secondes "
                                + classements.get(0).getDuree().getDixiemes()
                                + " dixièmes ");
                        for (int i = 0; i < classements.size(); ++i) {
                            if (Joueur.session.getNom() == classements.get(i)
                                    .getPersonne().getNom())
                                Liseuse.lire("vous êtes classé numéro "
                                        + classements.get(i).getClassement()
                                        + ", "
                                        + classements.get(i).getPersonne()
                                                .getNom()
                                        + ", "
                                        + classements.get(i).getDuree()
                                                .getHeures()
                                        + " heures "
                                        + classements.get(i).getDuree()
                                                .getMinutes()
                                        + " minutes "
                                        + classements.get(i).getDuree()
                                                .getSecondes()
                                        + " secondes "
                                        + classements.get(i).getDuree()
                                                .getDixiemes() + " dixièmes ");
                        }
                    }
                });
        ajouterElement("tou le classement", new ActionMenu() {

            @Override
            public void actionMenu() {
                for (int i = 0; i < classements.size(); ++i) {
                    Liseuse.lire("numéro " + classements.get(i).getClassement()
                            + ", " + classements.get(i).getPersonne().getNom()
                            + ", " + classements.get(i).getDuree().getHeures()
                            + " heures "
                            + classements.get(i).getDuree().getMinutes()
                            + " minutes "
                            + classements.get(i).getDuree().getSecondes()
                            + " secondes "
                            + classements.get(i).getDuree().getDixiemes()
                            + " dixièmes ");
                }
            }
        });
    }
}
