/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.Objects;

/**
 *
 * @author jonitaajamo
 */
public class RaakaAine {
    private int id;
    private String nimi;
    
    public RaakaAine(Integer id, String nimi) {
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
    
    @Override
   public boolean equals(Object o){
        if(o==null){
            return false;
        }
        if(getClass()!=o.getClass()){
            return false;
        }
        RaakaAine verrattava=(RaakaAine)o;
        if(this.nimi.equals(verrattava.nimi)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.nimi);
        return hash;
    }
}
