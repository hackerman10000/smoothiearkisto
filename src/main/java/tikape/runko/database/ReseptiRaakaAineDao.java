/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tikape.runko.domain.ReseptiRaakaAine;

/**
 *
 * @author jonitaajamo
 */
public class ReseptiRaakaAineDao {

    private Database database;

    public ReseptiRaakaAineDao(Database database) {
        this.database = database;
    }

    public List<ReseptiRaakaAine> findAllReseptinRaakaAineet(Integer key_reseptiId) throws SQLException {
        List<ReseptiRaakaAine> tasks = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ReseptiRaakaAine WHERE resepti_id = ?");
            stmt.setInt(1, key_reseptiId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                tasks.add(new ReseptiRaakaAine(result.getInt("raaka_aine_id"), result.getInt("resepti_id"), result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje")));
            }

        }
        Collections.sort(tasks, new ReseptiRaakaAine(1, 1, 1, null, ""));
        return tasks;
    }

    public List<ReseptiRaakaAine> findAllRaakaAineResepteissa(Integer key_raakaAineId) throws SQLException {
        List<ReseptiRaakaAine> tasks = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ReseptiRaakaAine WHERE raaka_aine_id = ?");
            stmt.setInt(1, key_raakaAineId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                tasks.add(new ReseptiRaakaAine(result.getInt("raaka_aine_id"), result.getInt("resepti_id"), result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje")));
            }

        }

        return tasks;
    }

    public ReseptiRaakaAine saveOrUpdate(ReseptiRaakaAine object) throws SQLException {
        ReseptiRaakaAine findByIds = findByIds(object.getRaakaAineId(), object.getReseptiId());

        if (findByIds != null) {
            return findByIds;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO ReseptiRaakaAine (raaka_aine_id, resepti_id, jarjestys, maara, ohje) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, object.getRaakaAineId());
            stmt.setInt(2, object.getReseptiId());
            stmt.setInt(3, object.getJarjestys());
            stmt.setString(4, object.getMaara());
            stmt.setString(5, object.getOhje());
            stmt.executeUpdate();
        }
        System.out.println("Successfully added to recipe! ");

        return findByIds(object.getRaakaAineId(), object.getReseptiId());
    }

    private ReseptiRaakaAine findByIds(Integer key_raakaAineId, Integer key_reseptiId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT raaka_aine_id, resepti_id, jarjestys, maara, ohje FROM ReseptiRaakaAine WHERE raaka_aine_id = ? and resepti_id = ?");
            stmt.setInt(1, key_raakaAineId);
            stmt.setInt(2, key_reseptiId);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new ReseptiRaakaAine(result.getInt("raaka_aine_id"), result.getInt("resepti_id"), result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje"));
        }
    }

    public void delete(Integer key_raakaAineId, Integer key_reseptiId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM ReseptiRaakaAine WHERE raaka_aine_id = ? and resepti_id = ?");
            stmt.setInt(1, key_raakaAineId);
            stmt.setInt(2, key_reseptiId);

            stmt.executeUpdate();

        }
    }

    public void deleteRaakaAine(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM ReseptiRaakaAine WHERE raaka_aine_id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public void deleteResepti(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM ReseptiRaakaAine WHERE resepti_id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

}
