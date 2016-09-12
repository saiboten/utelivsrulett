package gasoghopsfyrerullett.saiboten.no.gasoghopsfyrerullett;

/**
 * Created by Tobias on 11.09.2016.
 */
public class Club {
    private String navn;
    private boolean drinker;
    private boolean guiness;

    public boolean isGuiness() {
        return guiness;
    }

    public void setGuiness(boolean guiness) {
        this.guiness = guiness;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public boolean isDrinker() {
        return drinker;
    }

    public void setDrinker(boolean drinker) {
        this.drinker = drinker;
    }

    @Override
    public String toString() {
        return "Club{" +
                "navn='" + navn + '\'' +
                ", drinker=" + drinker +
                ", guiness=" + guiness +
                '}';
    }
}
