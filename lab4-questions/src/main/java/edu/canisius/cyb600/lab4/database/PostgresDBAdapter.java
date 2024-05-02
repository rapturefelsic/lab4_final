package edu.canisius.cyb600.lab4.database;

import edu.canisius.cyb600.lab4.dataobjects.Actor;
import edu.canisius.cyb600.lab4.dataobjects.Category;
import edu.canisius.cyb600.lab4.dataobjects.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Posgres Implementation of the db adapter.
 */
public class PostgresDBAdapter extends AbstractDBAdapter {

    public PostgresDBAdapter(Connection conn) {
        super(conn);
    }

    @Override
    public Actor addActor(Actor actor) {
        String sql = "INSERT INTO ACTOR (first_name, last_name) VALUES (? , ? ) returning ACTOR_ID,LAST_UPDATE";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            int i = 1;
            statement.setString(i++, actor.getFirstName().toUpperCase());
            statement.setString(i++, actor.getLastName().toUpperCase());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                actor.setActorId(results.getInt("ACTOR_ID"));
                actor.setLastUpdate(results.getDate("LAST_UPDATE"));
            }
            return actor;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return actor;
    }

    @Override
    public List<Actor> insertAllActorsWithAnOddNumberLastName(List<Actor> actors) {

        List<Actor> filteredActors = new ArrayList<>();
        for (Actor actor : actors) {
            if (actor.getLastName().length() % 2 != 0) {
                Actor filteredactor = addActor(actor);
                filteredActors.add(filteredactor);
            }
        }
        return filteredActors;
    }

    public List<Film> getFilmsInCategory(Category category) {
            //Create a string with the sql statement that is perfect
            String sql = " select * from film_category, category, film where category.category_id = film_category.category_id and film.film_id = film_category.film_id and category.category_id = ?";
            //Prepare the SQL statement with the code
            try (PreparedStatement statement =
                         conn.prepareStatement(sql)) {
                //Substitute a string for last name for the ? in the sql
                statement.setInt(1, category.getCategoryId());
                ResultSet results = statement.executeQuery();
                //Initialize an empty List to hold the return set of films.
                List<Film> films = new ArrayList<>();
                //Loop through all the results and create a new Film object to hold all its information
                while (results.next()) {
                    Film film = new Film();
                    film.setFilmId(results.getInt("FILM_ID"));
                    film.setTitle(results.getString("TITLE"));
                    film.setDescription(results.getString("DESCRIPTION"));
                    film.setReleaseYear(results.getString("RELEASE_YEAR"));
                    film.setLanguageId(results.getInt("LANGUAGE_ID"));
                    film.setRentalDuration(results.getInt("RENTAL_DURATION"));
                    film.setRentalRate(results.getDouble("RENTAL_RATE"));
                    film.setLength(results.getInt("LENGTH"));
                    film.setReplacementCost(results.getDouble("REPLACEMENT_COST"));
                    film.setRating(results.getString("RATING"));
                    film.setSpecialFeatures(results.getString("SPECIAL_FEATURES"));
                    film.setLastUpdate(results.getDate("LAST_UPDATE"));
                    //Add film to the array
                    films.add(film);
                }
                //Return all the films.
                return films;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return new ArrayList<>();
        }
    public List<Film> getAllFilmsWithALengthLongerThanX(int length) {
        //Create a string with the sql statement
        String sql = "Select * from film where length > ?";
        //Prepare the SQL statement with the code
        try (PreparedStatement statement =
                     conn.prepareStatement(sql)) {
            //Substitute a string for last name for the ? in the sql
            statement.setInt(1, length);
            ResultSet results = statement.executeQuery();
            //Initialize an empty List to hold the return set of films.
            List<Film> films = new ArrayList<>();
            //Loop through all the results and create a new Film object to hold all its information
            while (results.next()) {
                //System.out.println("found one");
                Film film = new Film();
                film.setFilmId(results.getInt("FILM_ID"));
                film.setTitle(results.getString("TITLE"));
                film.setDescription(results.getString("DESCRIPTION"));
                film.setReleaseYear(results.getString("RELEASE_YEAR"));
                film.setLanguageId(results.getInt("LANGUAGE_ID"));
                film.setRentalDuration(results.getInt("RENTAL_DURATION"));
                film.setRentalRate(results.getDouble("RENTAL_RATE"));
                film.setLength(results.getInt("LENGTH"));
                film.setReplacementCost(results.getDouble("REPLACEMENT_COST"));
                film.setRating(results.getString("RATING"));
                film.setSpecialFeatures(results.getString("SPECIAL_FEATURES"));
                film.setLastUpdate(results.getDate("LAST_UPDATE"));
                //Add film to the array
                films.add(film);
            }
            System.out.println("size of films: " + films.size());
            return films;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();


    }
    public List<Actor> getActorsFirstNameStartingWithX(char firstLetter){
        try (Statement statement = conn.createStatement()) {
            //This statement is easy
            //Select * from actor is saying "Return all Fields for all rows in films". Because there
            //is no "where clause", all rows are returned
            ResultSet results = statement.executeQuery("Select * from actor");
            //Initialize an empty List to hold the return set of films.
            List<Actor> actors = new ArrayList<>();
            //Loop through all the results and create a new Film object to hold all its information
            while (results.next()) {
                Actor actor = new Actor();
                actor.setActorId(results.getInt("ACTOR_ID"));
                actor.setFirstName(results.getString("FIRST_NAME"));
                actor.setLastName(results.getString("LAST_NAME"));
                actor.setLastUpdate(results.getDate("LAST_UPDATE"));
                //Add film to the array
                if(actor.getFirstName().startsWith(firstLetter + "")){
                    actors.add(actor);
                }
            }
            //Return all the films.
            return actors;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }
    public List<String> getAllDistinctCategoryNames() {
        //First, we are going to open up a new statement
        try (Statement statement = conn.createStatement()) {
            //This statement is easy
            //Select * from actor is saying "Return all Fields for all rows in films". Because there
            //is no "where clause", all rows are returned
            ResultSet results = statement.executeQuery("Select distinct name from category");
            //Initialize an empty List to hold the return set of films.
            List<String> categorys = new ArrayList<>();
            //Loop through all the results and create a new Film object to hold all its information
            while (results.next()) {
                categorys.add(results.getString("NAME"));
            }
            //Return all the films.
            return categorys;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }


}
