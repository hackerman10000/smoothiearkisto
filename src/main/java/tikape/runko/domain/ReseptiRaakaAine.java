/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.Comparator;

/**
 *
 * @author jonitaajamo
 */
public class ReseptiRaakaAine implements Comparator<ReseptiRaakaAine>, Comparable<ReseptiRaakaAine>{
    private int raakaAineId;
    private String raakaAineNimi;
    private int reseptiId;
    private int jarjestys;
    private String maara;
    private String ohje;
    
    public ReseptiRaakaAine(Integer raakaAineId, Integer reseptiId, Integer jarjestys, String maara, String ohje) {
        this.raakaAineId = raakaAineId;
        this.reseptiId = reseptiId;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }
    
    public void setRaakaAineId(Integer id) {
        this.raakaAineId = id;
    }
    
    public Integer getRaakaAineId() {
        return this.raakaAineId;
    }
    
    public void setReseptiId(Integer id) {
        this.reseptiId = id;
    }
    
    public Integer getReseptiId() {
        return this.reseptiId;
    }
    
    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }
    
    public Integer getJarjestys() {
        return this.jarjestys;
    }
    
    public void setMaara(String maara) {
        this.maara = maara;
    }
    
    public String getMaara() {
        return this.maara;
    }
    
    public void setOhje(String ohje) {
        this.ohje = ohje;
    }
    
    public String getOhje() {
        return this.ohje;
    }

    public void setRaakaAineNimi(String raakaAineNimi) {
        this.raakaAineNimi = raakaAineNimi;
    }

    public String getRaakaAineNimi() {
        return raakaAineNimi;
    }

    @Override
    public String toString() {
        return (jarjestys+". " +raakaAineNimi+": " + maara+ ", Ohje: "+ ohje);
    }

    @Override
    public int compare(ReseptiRaakaAine o1, ReseptiRaakaAine o2) {
        return o1.getJarjestys() - o2.getJarjestys();
    }

    @Override
    public int compareTo(ReseptiRaakaAine rra) {
        return this.jarjestys-rra.jarjestys;
    }
       
}
