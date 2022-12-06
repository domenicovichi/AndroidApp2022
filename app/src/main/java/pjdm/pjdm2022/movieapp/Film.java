package pjdm.pjdm2022.movieapp;

public class Film {
    private String titolo;
    private int annoDiUscita;
    private int durata;
    private String genereFilm;

    public Film(String titolo, int annoDiUscita, int durata, String genereFilm) {
        this.titolo = titolo;
        this.annoDiUscita = annoDiUscita;
        this.durata = durata;
        this.genereFilm = genereFilm;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
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

    public String getGenereFilm() {
        return genereFilm;
    }

    public void setGenereFilm(String genereFilm) {
        this.genereFilm = genereFilm;
    }

    @Override
    public String toString() {
        return "Film{" +
                "titolo='" + titolo + '\'' +
                ", annoDiUscita=" + annoDiUscita +
                ", durata=" + durata +
                ", genereFilm='" + genereFilm + '\'' +
                '}';
    }
}