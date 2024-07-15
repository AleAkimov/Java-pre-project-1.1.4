package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    private final Connection conn = Util.getConnection();

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO new_schema.users (name, lastname, age) VALUES (?,?,?)")) {
            conn.setAutoCommit(false);
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUsersTable() {
        try (PreparedStatement ps = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS new_schema.users (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                        "name VARCHAR(50) NOT NULL," +
                        "lastname VARCHAR(50) NOT NULL," +
                        "age TINYINT NOT NULL)")) {
            conn.setAutoCommit(false);
            ps.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (PreparedStatement ps = conn.prepareStatement("DROP TABLE IF EXISTS new_schema.users")) {
            conn.setAutoCommit(false);
            ps.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM new_schema.users WHERE id = ?")) {
            conn.setAutoCommit(false);
            ps.setLong(1, id);
            ps.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("select * from new_schema.users")) {
            conn.setAutoCommit(false);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastname");
                byte age = rs.getByte("age");
                users.add(new User(name, lastName, age));
            }
            conn.commit();

            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM new_schema.users")) {
            conn.setAutoCommit(false);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }
}
