package polyrallye.ouie.menus.carriere;

import java.util.ArrayList;
import java.util.List;
import polyrallye.modele.championnat.Rang;
import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class afficherClassementChampionnat extends Menu implements ActionMenu {

    protected List<Rang> classements = new ArrayList<Rang>();

    boolean isplayed = false;

    public afficherClassementChampionnat(Menu menuPrecedent,
            List<Rang> classements) {
        super(menuPrecedent);

        this.classements = classements;

        messageMenuVide = "Vous n'avez pas encore participer à ce championnat";
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
                String alire = "";

                for (int i = 0; i < classements.size(); ++i) {
                    if (Joueur.session.getNom().equals(
                            classements.get(i).getPersonne().getNom())) {
                        isplayed = true;
                        alire += "numéro " + classements.get(i).getClassement()
                                + ", "
                                + classements.get(i).getPersonne().getNom()
                                + ", ";
                        if (classements.get(i).getDuree().getHeures() != 0)
                            alire += classements.get(i).getDuree().getHeures()
                                    + " heures ";
                        if (classements.get(i).getDuree().getMinutes() != 0)
                            alire += classements.get(i).getDuree().getMinutes()
                                    + " minutes ";
                        if (classements.get(i).getDuree().getSecondes() != 0)
                            alire += classements.get(i).getDuree()
                                    .getSecondes()
                                    + " secondes ";
                        if (classements.get(i).getDuree().getDixiemes() != 0)
                            alire += classements.get(i).getDuree()
                                    .getDixiemes()
                                    + " dixièmes ";
                    }
                }
                Liseuse.lire(alire);
                if (!isplayed)
                    Liseuse
                            .lire("Vous n'avez pas encore participer à cette étape");

            }
        });
        ajouterElement("votre position par rappor au premier",
                new ActionMenu() {

                    @Override
                    public void actionMenu() {

                        for (int i = 0; i < classements.size(); ++i) {
                            if (Joueur.session.getNom().equals(
                                    classements.get(i).getPersonne().getNom())) {
                                isplayed = true;
                            }
                        }
                        String alire = "";
                        if (isplayed) {
                            alire += "numéro "
                                    + classements.get(0).getClassement() + ", "
                                    + classements.get(0).getPersonne().getNom()
                                    + ", ";
                            if (classements.get(0).getDuree().getHeures() != 0)
                                alire += classements.get(0).getDuree()
                                        .getHeures()
                                        + " heures ";
                            if (classements.get(0).getDuree().getMinutes() != 0)
                                alire += classements.get(0).getDuree()
                                        .getMinutes()
                                        + " minutes ";
                            if (classements.get(0).getDuree().getSecondes() != 0)
                                alire += classements.get(0).getDuree()
                                        .getSecondes()
                                        + " secondes ";
                            if (classements.get(0).getDuree().getDixiemes() != 0)
                                alire += classements.get(0).getDuree()
                                        .getDixiemes()
                                        + " dixièmes ";
                            Liseuse.lire(alire);
                            alire = "";
                            for (int i = 0; i < classements.size(); ++i) {
                                if (Joueur.session.getNom().equals(
                                        classements.get(i).getPersonne()
                                                .getNom())) {
                                    isplayed = true;
                                    if (classements.get(i).getClassement() != 1) {
                                        alire += "numéro "
                                                + classements.get(i)
                                                        .getClassement()
                                                + ", "
                                                + classements.get(i)
                                                        .getPersonne().getNom()
                                                + ", ";
                                        if (classements.get(i).getDuree()
                                                .getHeures() != 0)
                                            alire += classements.get(i)
                                                    .getDuree().getHeures()
                                                    + " heures ";
                                        if (classements.get(i).getDuree()
                                                .getMinutes() != 0)
                                            alire += classements.get(i)
                                                    .getDuree().getMinutes()
                                                    + " minutes ";
                                        if (classements.get(i).getDuree()
                                                .getSecondes() != 0)
                                            alire += classements.get(i)
                                                    .getDuree().getSecondes()
                                                    + " secondes ";
                                        if (classements.get(i).getDuree()
                                                .getDixiemes() != 0)
                                            alire += classements.get(i)
                                                    .getDuree().getDixiemes()
                                                    + " dixièmes ";
                                    }
                                }
                            }
                            Liseuse.lire(alire);
                        } else
                            Liseuse
                                    .lire("Vous n'avez pas encore participer à cette étape");
                    }
                });

        ajouterElement("tou le classement", new ActionMenu() {

            @Override
            public void actionMenu() {

                for (int i = 0; i < classements.size(); ++i) {
                    if (Joueur.session.getNom().equals(
                            classements.get(i).getPersonne().getNom())) {
                        isplayed = true;
                    }
                }

                String alire = "";
                if (isplayed) {
                    for (int i = 0; i < classements.size(); ++i) {
                        alire += "numéro " + classements.get(i).getClassement()
                                + ", "
                                + classements.get(i).getPersonne().getNom()
                                + ", ";
                        if (classements.get(i).getDuree().getHeures() != 0)
                            alire += classements.get(i).getDuree().getHeures()
                                    + " heures ";
                        if (classements.get(i).getDuree().getMinutes() != 0)
                            alire += classements.get(i).getDuree().getMinutes()
                                    + " minutes ";
                        if (classements.get(i).getDuree().getSecondes() != 0)
                            alire += classements.get(i).getDuree()
                                    .getSecondes()
                                    + " secondes ";
                        if (classements.get(i).getDuree().getDixiemes() != 0)
                            alire += classements.get(i).getDuree()
                                    .getDixiemes()
                                    + " dixièmes";
                        alire += "\n";
                    }
                    Liseuse.marquerPause();
                    Liseuse.lire(alire);
                } else
                    Liseuse
                            .lire("Vous n'avez pas encore participer à ce championnat");
            }
        });
    }
}
