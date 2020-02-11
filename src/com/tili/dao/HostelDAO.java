package com.tili.dao;

import com.tili.model.Bed;
import com.tili.model.Bed_Multiple;
import com.tili.model.Bed_Single;
import com.tili.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HostelDAO {

    // Constants Queries
    private final String INSERT_ROOM = "INSERT INTO room (capacity, name, type) VALUES (?, ?, ?)";
    private final String INSERT_BED = "INSERT INTO bed (capacity, name, status, occupied, idRoom) VALUES (?, ?, ?, ?, ?)";

    private final String SELECT_ROOM_BY_ID = "SELECT * FROM room WHERE id=?";
    private final String SELECT_ALL_ROOMS = "SELECT * FROM room";
    private final String SELECT_BEDS_BY_ID_ROOM = "SELECT * FROM bed WHERE idRoom=?";
    private final String SELECT_ROOM_MAX = "SELECT MAX(id) AS idMax FROM room";

    private final String UPDATE_OCCUPIED_BED_BY_ID = "UPDATE bed SET occupied=? WHERE id=?";

    private final String DELETE_BEDS_BY_ROOM = "DELETE FROM bed WHERE idRoom=?";
    private final String DELETE_ROOM_BY_ID = "DELETE FROM room WHERE id=?";

    // JDBC driver name and database URL
    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:~/test";

    //  Database credentials
    private final String USER = "sa";
    private final String PASS = "";

    public HostelDAO() {
        try {
            Statement stm = getConnection().createStatement();
            String sql = "DROP ALL OBJECTS;\n" +
                    "create table IF NOT EXISTS bed (\n" +
                    "id int(3) NOT NULL AUTO_INCREMENT, \n" +
                    "capacity int(3) NOT NULL,\n" +
                    "name varchar(300) NOT NULL,\n" +
                    "status varchar(300) NOT NULL,\n" +
                    "occupied varchar(100) NOT NULL,\n" +
                    "idRoom int(3) NOT NULL," +
                    "PRIMARY KEY (id)\n" +
                    ");\n" +
                    "create table IF NOT EXISTS room (\n" +
                    "id int(3) NOT NULL AUTO_INCREMENT,\n" +
                    "name varchar(300) NOT NULL,\n" +
                    "capacity int(3) NOT NULL,\n" +
                    "type varchar(300) NOT NULL,\n" +
                    "PRIMARY KEY (id)\n" +
                    ");";
            stm.executeUpdate(sql);
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public List<Room> selectAllRooms() {
        List<Room> rooms = new ArrayList<>();

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ROOMS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer capacity = resultSet.getInt("capacity");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                List<Bed> bedList =selectBedsByIdRoom(id);

                Room room = new Room(id, name, capacity, type, bedList);
                room.calculateOccupied();
                rooms.add(room);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    private Room generateRoom(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        Integer capacity = resultSet.getInt("capacity");
        String name = resultSet.getString("name");
        String type = resultSet.getString("type");
        List<Bed> bedList = selectBedsByIdRoom(id);

        return new Room(id, name, capacity, type, bedList);
    }

    public Room selectRoomById(Integer id) {
        Room room = null;

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROOM_BY_ID);
            preparedStatement.setInt(1, id);

            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                room = generateRoom(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return room;
    }

    public List<Bed> selectBedsByIdRoom(Integer idRoom) {
        List<Bed> beds = new ArrayList<>();
        List<Boolean> occupiedList;
        Boolean isOccupied;

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BEDS_BY_ID_ROOM);

            preparedStatement.setInt(1, idRoom);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer capacity = resultSet.getInt("capacity");
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                String occupied = resultSet.getString("occupied");

                if (capacity > 1) {
                    occupiedList = new ArrayList<>();
                    String[] occupiedArray = occupied.split(";");
                    for (String value:
                         occupiedArray) {
                        occupiedList.add( Integer.valueOf(value) == 0  ? false : true);
                    }

                    beds.add(new Bed_Multiple(id, capacity, name, status, occupiedList));
                } else {
                    isOccupied = Integer.valueOf(occupied) == 0 ? false : true;
                    beds.add(new Bed_Single(id, capacity, name, status, isOccupied));
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beds;
    }

    public void insertRoom(Room room) {
        System.out.println(INSERT_ROOM);

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ROOM);
            preparedStatement.setInt(1, room.getCapacity());
            preparedStatement.setString(2, room.getName());
            preparedStatement.setString(3, room.getType());

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBed(Bed bed) {
        System.out.println(INSERT_BED);

        try {
            String occupied = "0";
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BED);
            preparedStatement.setInt(1, bed.getCapacity());
            preparedStatement.setString(2, bed.getName());
            preparedStatement.setString(3, bed.getStatus());

            if (bed instanceof Bed_Multiple) {
                for (int i=1; i < bed.getCapacity(); i++) {
                    occupied += ";0";
                }
            }

            preparedStatement.setString(4, occupied);

            Integer idRoom = getIdRoomMAX();
            preparedStatement.setInt(5, idRoom);

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getIdRoomMAX() {
        Integer idMax = -1;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROOM_MAX);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                idMax = resultSet.getInt("idMax");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idMax;
    }

    public void updateOccupiedBedById(Integer id, boolean occupied) {
        updateOccupiedBedById(id, occupied ? "1" : "0");
    }

    public void updateOccupiedBedById(Integer id, List<Boolean> listOccupied) {
        String sOccupied = listOccupied.get(0) ? "1" : "0";

        for (int i=1; i < listOccupied.size(); i++) {
            sOccupied += ";" + (listOccupied.get(i) ? "1" : "0");
        }

        updateOccupiedBedById(id, sOccupied);
    }

    private void updateOccupiedBedById (Integer id, String listOccupied) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_OCCUPIED_BED_BY_ID);
            preparedStatement.setString(1, listOccupied);
            preparedStatement.setInt(2, id);

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteBedsByRoomId(Integer id) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BEDS_BY_ROOM);
            preparedStatement.setInt(1, id);

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoom(Integer id) {
        try {
            deleteBedsByRoomId(id);

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ROOM_BY_ID);
            preparedStatement.setInt(1, id);

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
