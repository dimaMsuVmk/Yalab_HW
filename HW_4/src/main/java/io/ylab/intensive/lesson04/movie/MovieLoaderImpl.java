package io.ylab.intensive.lesson04.movie;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class MovieLoaderImpl implements MovieLoader {
    private final DataSource dataSource;

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * select subject, count(*) from public.movie
     * group by subject
     */

    @Override
    public void loadData(File file) {
        // РЕАЛИЗАЦИЮ ПИШЕМ ТУТ
        List<Movie> list = new ArrayList<>();
       try(BufferedReader br = new BufferedReader(new FileReader(file))){
           String str = br.readLine(); str = br.readLine();
           while ((str = br.readLine()) != null){
               String[] arr = str.split(";");
               Movie movie = new Movie();
               if(arr[0].equals("")) movie.setYear(null); else movie.setYear(Integer.parseInt(arr[0]));
               if(arr[1].equals("")) movie.setLength(null); else movie.setLength(Integer.parseInt(arr[1]));
               if(arr[2].equals("")) movie.setTitle(null); else movie.setTitle(arr[2]);
               if(arr[3].equals("")) movie.setSubject(null); else movie.setSubject(arr[3]);
               if(arr[4].equals("")) movie.setActors(null); else movie.setActors(arr[4]);
               if(arr[5].equals("")) movie.setActress(null); else movie.setActress(arr[5]);
               if(arr[6].equals("")) movie.setDirector(null); else movie.setDirector(arr[6]);
               if(arr[7].equals("")) movie.setPopularity(null); else movie.setPopularity(Integer.parseInt(arr[7]));
               if(arr[8].equals("")) movie.setAwards(null); else movie.setAwards(arr[8].equals("Yes")?Boolean.TRUE:Boolean.FALSE);
               list.add(movie);
           }
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       try {
           for (Movie movie : list) {
               saveToDB(movie);
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    private void saveToDB(Movie movie) throws SQLException {
        String sql = "INSERT INTO movie (year,length,title,subject,actors,actress,director,popularity,awards)\n"
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            if (movie.getYear() != null) ps.setInt(1, movie.getYear());
            else ps.setNull(1, Types.INTEGER);
            if (movie.getLength() != null) ps.setInt(2, movie.getLength());
            else ps.setNull(2, Types.INTEGER);
            if (movie.getTitle() != null) ps.setString(3, movie.getTitle());
            else ps.setNull(3, Types.VARCHAR);
            if (movie.getSubject() != null) ps.setString(4, movie.getSubject());
            else ps.setNull(4, Types.VARCHAR);
            if (movie.getActors() != null) ps.setString(5, movie.getActors());
            else ps.setNull(5, Types.VARCHAR);
            if (movie.getActress() != null) ps.setString(6, movie.getActress());
            else ps.setNull(6, Types.VARCHAR);
            if (movie.getDirector() != null) ps.setString(7, movie.getDirector());
            else ps.setNull(7, Types.VARCHAR);
            if (movie.getPopularity() != null) ps.setInt(8, movie.getPopularity());
            else ps.setNull(8, Types.INTEGER);
            if (movie.getAwards() != null) ps.setBoolean(9, movie.getAwards());
            else ps.setNull(9, Types.BOOLEAN);

            ps.executeUpdate();
        }
    }
}
