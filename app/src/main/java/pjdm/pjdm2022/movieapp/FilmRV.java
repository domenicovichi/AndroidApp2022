package pjdm.pjdm2022.movieapp;

public class FilmRV {

    private String titolo;
    private String genere;
    private int annoDiUscita;
    private int durata;
    private String immagine;

    public FilmRV(String titolo, String genere, int annoDiUscita, int durata, String immagine) {
        this.titolo = titolo;
        this.genere = genere;
        this.annoDiUscita = annoDiUscita;
        this.durata = durata;
        this.immagine = immagine;
    }

    public String getImmagine(){
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public int getAnnoDiUscita() {
        return annoDiUscita;
    }

    public void setAnnoDiUscita(int annoDiUscita) {
        this.annoDiUscita = annoDiUscita;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    @Override
    public String toString() {
        return "FilmRV{" +
                "titolo='" + titolo + '\'' +
                ", genere='" + genere + '\'' +
                ", annoDiUscita=" + annoDiUscita +
                ", durata=" + durata +
                ", immagine='" + immagine + '\'' +
                '}';
    }
}
