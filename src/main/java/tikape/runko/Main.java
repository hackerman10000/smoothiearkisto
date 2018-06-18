package tikape.runko;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.ReseptiDao;
import tikape.runko.database.ReseptiRaakaAineDao;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Resepti;
import tikape.runko.domain.ReseptiRaakaAine;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        //heroku
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        Database database = new Database("jdbc:sqlite:smoothiet.db");
        ReseptiDao reseptiDao = new ReseptiDao(database);
        RaakaAineDao raakaAineDao = new RaakaAineDao(database);
        ReseptiRaakaAineDao reseptiRaakaAineDao = new ReseptiRaakaAineDao(database);

        Spark.get("/smoothiet/", (req, res) -> {
            HashMap map = new HashMap<>();
            List<Resepti> annokset = reseptiDao.findAll();
            map.put("annokset", annokset);

            List<RaakaAine> raakaAineet = raakaAineDao.findAll();
            HashMap raakaAineMap = new HashMap<>();
            for (RaakaAine a : raakaAineet) {
                raakaAineMap.put(a.getId(), a);
            }
            map.put("raakaAineet", raakaAineMap);

            HashMap mappi = new HashMap<>();
            for (Resepti resepti : annokset) {
                mappi.put(resepti.getId(), reseptiRaakaAineDao.findAllReseptinRaakaAineet(resepti.getId()));

            }
            map.put("raakaAineetAnnoksissa", mappi);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/smoothiet/:id", (req, res) -> {
            HashMap map = new HashMap<>();

            List<RaakaAine> raakaAineet = raakaAineDao.findAll();
            Resepti resepti = reseptiDao.findOne(Integer.parseInt(req.params(":id")));
            resepti.setRaakaAineet(reseptiRaakaAineDao.findAllReseptinRaakaAineet(resepti.getId()));
            raakaAineDao.liitaNimet(resepti.getRaakaAineet());
            map.put("raakaAineet", raakaAineet);
            map.put("resepti", resepti);
            
            return new ModelAndView(map, "lisaa");
        }, new ThymeleafTemplateEngine());

        Spark.post("/uusiSmoothie", (req, res) -> {
            String nimi = req.queryParams("nimi");
            if (nimi.isEmpty() || nimi.equals("Lisaa nimi...")) {
                res.redirect("/smoothiet/");
                return "";
            }
            Resepti resepti = new Resepti(-1, req.queryParams("nimi"));
            resepti = reseptiDao.saveOrUpdate(resepti);
            res.redirect("/smoothiet/" + resepti.getId());
            return "";
        });

        Spark.post("/smoothiet/:id/uusiaine/", (req, res) -> {
            Integer reseptiId = Integer.parseInt(req.params(":id"));
            Integer raakaAineId = Integer.parseInt(req.queryParams("raakaAine"));
            if (req.queryParams("jarjestys").isEmpty()) {
                res.redirect("/smoothiet/" + reseptiId);
                return "";
            }
            
            Integer jarjestys = Integer.parseInt(req.queryParams("jarjestys"));
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");
            
            System.out.println("Lisätään raaka-aine... " + reseptiId + ", " + jarjestys + ", " + maara + ", " + ohje);
            if (maara.isEmpty()) {
                res.redirect("/smoothiet/" + reseptiId);
                return "";
            }
            System.out.println("Lisätty!");

            ReseptiRaakaAine reseptiRaakaAine = new ReseptiRaakaAine(raakaAineId, reseptiId, jarjestys, maara, ohje);
            
            reseptiRaakaAineDao.saveOrUpdate(reseptiRaakaAine);
            
            res.redirect("/smoothiet/" + reseptiId);
            return "";
        });

        Spark.post("/smoothiet/:id/poistaaine/:aineid", (req, res) -> {
            Integer reseptiId = Integer.parseInt(req.params(":id"));

            Integer raakaAineId = Integer.parseInt(req.params(":aineid"));
            reseptiRaakaAineDao.delete(raakaAineId, reseptiId);
            res.redirect("/smoothiet/" + reseptiId);
            return "";
        });

        Spark.post("/smoothiet/poista/:id", (req, res) -> {
            int reseptiId = Integer.parseInt(req.params(":id"));
            reseptiDao.delete(reseptiId);
            reseptiRaakaAineDao.deleteResepti(reseptiId);
            res.redirect("/smoothiet/");
            return "";
        });

        Spark.post("/raakaaineet/", (req, res) -> {
            String nimi = req.queryParams("nimi");

            if (!nimi.isEmpty()) {
                RaakaAine ra = new RaakaAine(-1, nimi);
                raakaAineDao.saveOrUpdate(ra);
            }

            res.redirect("/raakaaineet/");
            return "";
        });

        Spark.get("/raakaaineet/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", raakaAineDao.findAll());

            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());

        Spark.post("/raakaaineet/:aineid", (req, res) -> {
            int raakaAineId = Integer.parseInt(req.params(":aineid"));
            raakaAineDao.delete(raakaAineId);
            reseptiRaakaAineDao.deleteRaakaAine(raakaAineId);
            res.redirect("/raakaaineet/");
            return "";
        });

        Spark.get("*", (req, res) -> {
            res.redirect("/smoothiet/");
            return "";
        });
    }
}
