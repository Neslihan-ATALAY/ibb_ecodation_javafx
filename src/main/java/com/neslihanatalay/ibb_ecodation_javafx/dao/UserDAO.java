package com.neslihanatalay.ibb_ecodation_javafx.dao;

import com.neslihanatalay.ibb_ecodation_javafx.database.SingletonPropertiesDBConnection;
import com.neslihanatalay.ibb_ecodation_javafx.dto.UserDTO;
import com.neslihanatalay.ibb_ecodation_javafx.utils.ERole;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements IDaoImplements<UserDTO>, ILogin<UserDTO> {

    private Connection connection;

    public UserDAO() {
        this.connection = SingletonPropertiesDBConnection.getInstance().getConnection();
    }

    @Override
    public Optional<UserDTO> create(UserDTO userDTO) {
        String sql = "INSERT INTO usertable (username, password, email, role, count) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
            preparedStatement.setString(1, userDTO.getUsername());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, userDTO.getEmail());
            preparedStatement.setString(4, userDTO.getRole().name());
			preparedStatement.setInteger(5, userDTO.getCount());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userDTO.setId(generatedKeys.getInt(1));
                        userDTO.setPassword(hashedPassword);
                        return Optional.of(userDTO);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<UserDTO>> list() {
        List<UserDTO> userDTOList = new ArrayList<>();
        String sql = "SELECT * FROM usertable";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userDTOList.add(mapToObjectDTO(resultSet));
            }
            return userDTOList.isEmpty() ? Optional.empty() : Optional.of(userDTOList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDTO> findByName(String name) {
        String sql = "SELECT * FROM usertable WHERE email=?";
        return selectSingle(sql, name);
    }

    @Override
    public Optional<UserDTO> findById(int id) {
        String sql = "SELECT * FROM usertable WHERE id=?";
        return selectSingle(sql, id);
    }

    @Override
    public Optional<UserDTO> update(int id, UserDTO userDTO) {
        Optional<UserDTO> optionalUpdate = findById(id);
        if (optionalUpdate.isPresent()) {
            String sql = "UPDATE usertable SET username=?, password=?, email=?, role=?, count=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
                preparedStatement.setString(1, userDTO.getUsername());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, userDTO.getEmail());
                preparedStatement.setString(4, userDTO.getRole().name());
				preparedStatement.setInteger(5, userDTO.getCount());
                preparedStatement.setInt(6, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    userDTO.setId(id);
                    userDTO.setPassword(hashedPassword);
                    return Optional.of(userDTO);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDTO> delete(int id) {
        Optional<UserDTO> optionalDelete = findById(id);
        if (optionalDelete.isPresent()) {
            String sql = "DELETE FROM usertable WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    return optionalDelete;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public UserDTO mapToObjectDTO(ResultSet resultSet) throws SQLException {
        return UserDTO.builder()
                .id(resultSet.getInt("id"))
                .username(resultSet.getString("username"))
                .password(resultSet.getString("password"))
                .email(resultSet.getString("email"))
                .role(ERole.fromString(resultSet.getString("role")))
				.count(resultSet.getInteger("count"))
                .build();
    }

    @Override
    public Optional<UserDTO> selectSingle(String sql, Object... params) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject((i + 1), params[i]);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToObjectDTO(resultSet));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDTO> loginUser(String usernameOrEmail, String password) {
        String sql = "SELECT * FROM usertable WHERE username = ? OR email = ?";
        Optional<UserDTO> userOpt = selectSingle(sql, usernameOrEmail, usernameOrEmail);

        if (userOpt.isPresent()) {
            UserDTO user = userOpt.get();
            if (BCrypt.checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM usertable WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT 1 FROM usertable WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true; 
        }
    }
}
