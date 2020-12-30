package DatabaseConnection;

import Data.Intersection;
import Data.Traffic;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;

public class DatabaseConnection {
    public static DatabaseConnection instance = null;

    private Connection conn;

    private DatabaseConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/TrafficDB";
        System.out.println("Database connection was created");
        conn = DriverManager.getConnection(url, "postgres", "0000");
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

    private String saveImage(String base64Image, int id_traffic_light) {
        String path = "images/" + id_traffic_light + ".png";
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
            FileUtils.writeByteArrayToFile(new File(path), decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return path;
    }


    private String getBase64Image(String path) {
        String encodedString = "";
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(path));
            encodedString = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedString;
    }

    public boolean checkTrafficExistence(int trafficLightId) {
        int id = 0;
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "select count(*) from traffic where id_traffic_light=?");
            statement.setInt(1, trafficLightId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id>0;
    }


    public Boolean addTraffic(int id_traffic_light, int nrCarsDetected, String image) {
        if (checkTrafficExistence(id_traffic_light))
            return updateTraffic(id_traffic_light, nrCarsDetected, image);
        else
            return insertNewTraffic(id_traffic_light, nrCarsDetected, image);
    }

    private Boolean updateTraffic(int id_traffic_light, int nrCarsDetected, String image) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "update traffic SET nr_cars=? WHERE id_traffic_light=?");
            statement.setInt(1, nrCarsDetected);
            statement.setInt(2, id_traffic_light);

            saveImage(image, id_traffic_light);
            statement.executeUpdate();
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    private Boolean insertNewTraffic(int id_traffic_light, int nrCarsDetected, String image) {
        try {
            PreparedStatement statement = conn.prepareStatement("insert into traffic(id, id_traffic_light, nr_cars, image) values(?, ?, ?, ?)");
            statement.setInt(1, getTrafficId());
            statement.setInt(2, id_traffic_light);
            statement.setInt(3, nrCarsDetected);

            statement.setString(4, saveImage(image, id_traffic_light));
            statement.executeUpdate();
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }


    public ArrayList<Intersection> getAllIntersections() {
        ArrayList<Intersection> intersectionList = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("select * from intersections");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                intersectionList.add(new Intersection(resultSet.getInt(1),
                        resultSet.getString(2)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return intersectionList;
    }

    public Traffic getTrafficByLightId(int id) {
        Traffic traffic = null;
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "select * from traffic where id_traffic_light=?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String base64Img = getBase64Image(resultSet.getString(4));
            traffic = new Traffic(resultSet.getInt(1),
                                  resultSet.getInt(2),
                                  resultSet.getInt(3),
                                  base64Img);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return traffic;

    }
}


