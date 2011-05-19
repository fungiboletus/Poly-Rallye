package polyrallye.modele.voiture;

public enum TypeTerrain {
    //valeurs arbitraires pour l'instant 
    ASPHALT(0.65),BOUE(0.3),SABLE(0.1),GRAVIER(0.2),TERRE(0.1),HERBE(0.1);
    protected double frottement;
    
    TypeTerrain(double frottement){
        this.frottement=frottement;
    }
}

