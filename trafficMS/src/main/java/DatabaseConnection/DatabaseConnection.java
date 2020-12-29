package DatabaseConnection;

import Data.Intersection;
import Data.TrafficLight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    public static DatabaseConnection instance = null;

    private Connection conn;

    private DatabaseConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/TrafficDB";
        conn = DriverManager.getConnection(url, "postgres", "mango");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            try {
                instance = new DatabaseConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return instance;
    }

    public int getTrafficId() {
        int id = 0;
        try {
            PreparedStatement statement = conn.prepareStatement("select count(*) from traffic");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id + 1;
    }

    public int getTrafficLightId() {
        int id = 0;
        try {
            PreparedStatement statement = conn.prepareStatement("select max(id) from traffic_lights");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    public Boolean addTraffic(int id_traffic_light, int nrCarsDetected, String image) {
        try {
            PreparedStatement statement = conn.prepareStatement("insert into traffic(id, id_traffic_light, nr_cars, image) values(?, ?, ?, ?)");
            statement.setInt(1, getTrafficId());
            statement.setInt(2, id_traffic_light );
            statement.setInt(3, nrCarsDetected);
            statement.setString(4, image);
            statement.executeUpdate();
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Intersection> getAllIntersections () {
        try {
            PreparedStatement statement = conn.prepareStatement("select * from intersections");
            ResultSet rs = statement.executeQuery();
            List<Intersection> intersections = new ArrayList<>();
            while (rs.next()) {
                Intersection intersection = new Intersection(rs.getInt("Id"), rs.getString("name"));
                intersections.add(intersection);
            }
            return intersections;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public List<TrafficLight> getAllTrafficLightsByIntersectionId ()


}


