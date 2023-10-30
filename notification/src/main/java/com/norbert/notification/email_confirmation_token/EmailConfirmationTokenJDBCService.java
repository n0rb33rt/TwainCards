package com.norbert.notification.email_confirmation_token;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailConfirmationTokenJDBCService implements EmailConfirmationTokenDAO {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(EmailConfirmationToken emailConfirmationToken) {
        String sql = "INSERT INTO email_confirmation_token (token,confirmed) VALUES (?, ?)";
        jdbcTemplate.update(sql, emailConfirmationToken.getToken(), emailConfirmationToken.getConfirmed());
    }

    private DataSource createUserDataBaseSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/user");
        dataSource.setUsername("twaincards");
        dataSource.setPassword("twaincards");
        return dataSource;
    }
    @Override
    public void enableAccount(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = createUserDataBaseSource().getConnection();
            String sql = """
                UPDATE "user" SET enabled = true WHERE email = ?
                """;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while confirming email");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Error while confirming email");
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Error while confirming email");
                }
            }
        }
    }




    @Override
    public Optional<EmailConfirmationToken> findByToken(String token) {
        String sql = "SELECT * FROM email_confirmation_token where token = ?";
        List<EmailConfirmationToken> tokens = jdbcTemplate.query(sql, new Object[]{token}, (rs, rowNum) -> {
            EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();
            emailConfirmationToken.setId(rs.getLong("id"));
            emailConfirmationToken.setConfirmed(rs.getBoolean("confirmed"));
            emailConfirmationToken.setToken(rs.getString("token"));

            return emailConfirmationToken;
        });
        if(tokens.isEmpty()){
            return Optional.empty();
        }else {
            return Optional.of(tokens.get(0));
        }
    }

    @Override
    public void update(EmailConfirmationToken emailConfirmationToken) {
        String updateSql = "UPDATE email_confirmation_token SET confirmed = ? WHERE id = ?";
        jdbcTemplate.update(updateSql, emailConfirmationToken.getConfirmed(), emailConfirmationToken.getId());
    }
}
