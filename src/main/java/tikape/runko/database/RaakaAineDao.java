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
import java.util.List;
import tikape.runko.domain.ReseptiRaakaAine;
import tikape.runko.domain.RaakaAine;

/**
 *
 * @author jonitaajamo
 */
public class RaakaAineDao implements Dao <RaakaAine, Integer>{

    private Database database;
    private ReseptiRaakaAineDao ReseptiRaakaAineDao;

    public RaakaAineDao(Database database) {
        this.database = database;
        ReseptiRaakaAineDao = new ReseptiRaakaAineDao(database);
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM RaakaAine WHERE id = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new RaakaAine(result.getInt("id"), result.getString("nimi"));
        }
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
        List<RaakaAine> tasks = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement("SELECT id, nimi FROM RaakaAine").executeQuery()) {

            while (result.next()) {
                tasks.add(new RaakaAine(result.getInt("id"), result.getString("nimi")));
            }
        }

        return tasks;
    }

    public void liitaNimet(List<ReseptiRaakaAine> aineet) throws SQLException {
        for (ReseptiRaakaAine a : aineet) {
            a.setRaakaAineNimi(this.findOne(a.getRaakaAineId()).getNimi());
        }
    }

    @Override
    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {
        RaakaAine byName = findByName(object.getNimi());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }

        return findByName(object.getNimi());
    }

    private RaakaAine findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM RaakaAine WHERE nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new RaakaAine(result.getInt("id"), result.getString("nimi"));
        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            ReseptiRaakaAineDao.deleteRaakaAine(key);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
            stmt.setInt(1, key);

            stmt.executeUpdate();

        }
    }

    public List<RaakaAine> findAnnoksenRaakaAineet(Integer annosId) throws SQLException {
        List<RaakaAine> raakaAineet = new ArrayList<>();

        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT RaakaAine.id, RaakaAine.nimi FROM RaakaAine, ReseptiRaakaAine, Resepti\n"
                + "     WHERE RaakaAine.id = ReseptiRaakaAine.raaka_aine_id"
                + "     AND ReseptiRaakaAine.annos_id = Resepti.id"
                + "     AND Resepti.id = ?");
            
            stmt.setInt(1, annosId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                raakaAineet.add(new RaakaAine(rs.getInt("id"), rs.getString("nimi")));
            }

            rs.close();
            stmt.close();
            connection.close();
        }

        return raakaAineet;
    }

}
