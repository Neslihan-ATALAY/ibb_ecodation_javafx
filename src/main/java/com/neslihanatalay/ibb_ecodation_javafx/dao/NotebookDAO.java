package com.neslihanatalay.ibb_ecodation_javafx.dao;

import com.neslihanatalay.ibb_ecodation_javafx.database.SingletonPropertiesDBConnection;
import com.neslihanatalay.ibb_ecodation_javafx.dto.NotebookDTO;
import com.neslihanatalay.ibb_ecodation_javafx.utils.ECategory;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotebookDAO implements IDaoImplements<NotebookDTO>, ILogin<NotebookDTO> {

    private Connection connection;

    public NotebookDAO() {
        this.connection = SingletonPropertiesDBConnection.getInstance().getConnection();
    }

    @Override
    public Optional<NotebookDTO> create(NotebookDTO notebookDTO) {
        String sql = "INSERT INTO notebooktable (title, content, createdDate, updatedDate, category, pinned) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, notebookDTO.getTitle());
            preparedStatement.setString(2, notebookDTO.getContent());
            preparedStatement.setDate(3, Date.valueOf(notebookDTO.getCreatedDate()));
            preparedStatement.setDate(4, Date.valueOf(notebookDTO.getUpdatedDate()));
			preparedStatement.setString(5, notebookDTO.getCategory().name());
			preparedStatement.setBoolean(6, notebookDTO.getPinned());
			//preparedStatement.setInteger(7, notebookDTO.getUserDTO().getId());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        notebookDTO.setId(generatedKeys.getInt(1));
                        return Optional.of(notebookDTO);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<NotebookDTO>> list() {
        List<NotebookDTO> notebookDTOList = new ArrayList<>();
        String sql = "SELECT * FROM notebooktable ORDER BY pinned DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                notebookDTOList.add(mapToObjectDTO(resultSet));
            }
            return notebookDTOList.isEmpty() ? Optional.empty() : Optional.of(notebookDTOList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<NotebookDTO> findById(int id) {
        String sql = "SELECT * FROM notebooktable WHERE id=?";
        return selectSingle(sql, id);
    }

    @Override
    public Optional<NotebookDTO> update(int id, NotebookDTO notebookDTO) {
        Optional<NotebookDTO> optionalUpdate = findById(id);
        if (optionalUpdate.isPresent()) {
            String sql = "UPDATE notebooktable SET title=?, content=?, createdDate=?, updatedDate=?, category=?, pinned=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, notebookDTO.getTitle());
                preparedStatement.setString(2, notebookDTO.getContent());
                preparedStatement.setDate(3, Date.valueOf(notebookDTO.getCreatedDate()));
                preparedStatement.setDate(4, Date.valueOf(notebookDTO.getUpdatedDate()));
				preparedStatement.setString(5, notebookDTO.getCategory().name());
				preparedStatement.setBoolean(6, notebookDTO.getPinned());
				//preparedStatement.setInteger(7, notebookDTO.getUserDTO().getId());
                preparedStatement.setInt(7, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    notebookDTO.setId(id);
                    return Optional.of(notebookDTO);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<NotebookDTO> delete(int id) {
        Optional<NotebookDTO> optionalDelete = findById(id);
        if (optionalDelete.isPresent()) {
            String sql = "DELETE FROM notebooktable WHERE id=?";
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
    public NotebookDTO mapToObjectDTO(ResultSet resultSet) throws SQLException {
        return NotebookDTO.builder()
                .id(resultSet.getInt("id"))
                .username(resultSet.getString("title"))
                .password(resultSet.getString("content"))
                .createdDate(resultSet.getDate("createdDate").toLocalDate())
				.updatedDate(resultSet.getDate("updatedDate").toLocalDate())
                .category(ECategory.fromString(resultSet.getString("category")))
				.pinned(resultSet.getBoolean("pinned"))
				//.userDTO(resultSet.getUserDTO("userDTO"))
                .build();
    }

    @Override
    public Optional<NotebookDTO> selectSingle(String sql, Object... params) {
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
}
