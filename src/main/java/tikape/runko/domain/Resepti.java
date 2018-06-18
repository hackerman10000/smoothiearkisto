/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author jonitaajamo
 */
public class Resepti {

    private int id;
    private String nimi;
    private List<ReseptiRaakaAine> raakaAineet;

    public Resepti(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getNimi() {
        return this.nimi;
    }

    public void setRaakaAineet(List<ReseptiRaakaAine> raakaAineet) {
        this.raakaAineet = raakaAineet;
    }

    public List<ReseptiRaakaAine> getRaakaAineet() {
        return raakaAineet;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Resepti verrattava = (Resepti) o;
        if (this.nimi.equals(verrattava.nimi)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.nimi);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nimi + "\n");
        for (ReseptiRaakaAine rra : raakaAineet) {
            sb.append("\t" + rra.toString() + "\n");
        }
        return sb.toString();
    }

}
