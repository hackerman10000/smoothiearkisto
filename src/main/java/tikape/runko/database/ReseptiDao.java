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
import tikape.runko.domain.Resepti;

/**
 *
 * @author jonitaajamo
 */
public class ReseptiDao implements Dao<Resepti, Integer> {

    private Database database;
    private ReseptiRaakaAineDao ardao;

    public ReseptiDao(Database database) {
        this.database = database;
        ardao = new ReseptiRaakaAineDao(database);
    }

    @Override
    public Resepti findOne(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Resepti WHERE id = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Resepti(result.getInt("id"), result.getString("nimi"));
        }
    }

    @Override
    public List<Resepti> findAll() throws SQLException {
        List<Resepti> tasks = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement("SELECT id, nimi FROM Resepti").executeQuery()) {

            while (result.next()) {
                tasks.add(new Resepti(result.getInt("id"), result.getString("nimi")));
            }
        }

        return tasks;
    }

    @Override
    public Resepti saveOrUpdate(Resepti object) throws SQLException {
        Resepti byName = findByName(object.getNimi());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Resepti (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }

        return findByName(object.getNimi());
    }

    private Resepti findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Resepti WHERE nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Resepti(result.getInt("id"), result.getString("nimi"));
        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            ardao.deleteResepti(key);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Resepti WHERE id = ?");
            stmt.setInt(1, key);

            stmt.executeUpdate();;

        }
    }

}
