package br.com.fiap.ClyvoPet.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PasswordResetTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public PasswordResetTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void salvar(Long veterinarioId, String tokenHash, LocalDateTime expiraEm) {

        String sql = """
                INSERT INTO PASSWORD_RESET_TOKENS
                (VETERINARIO_ID, TOKEN_HASH, EXPIRA_EM)
                VALUES (?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                veterinarioId,
                tokenHash,
                Timestamp.valueOf(expiraEm)
        );
    }

    /**
     * Retorna o ID do veterinário dono do token, se o token
     * existir, não tiver sido usado e ainda não tiver expirado.
     */
    public Optional<Long> buscarVeterinarioIdPorTokenValido(String tokenHash) {

        String sql = """
                SELECT VETERINARIO_ID
                FROM PASSWORD_RESET_TOKENS
                WHERE TOKEN_HASH = ?
                  AND USADO = 0
                  AND EXPIRA_EM > ?
                """;

        List<Long> ids = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getLong("VETERINARIO_ID"),
                tokenHash,
                Timestamp.valueOf(LocalDateTime.now())
        );

        return ids.stream().findFirst();
    }

    /** Invalida todos os tokens ainda ativos do veterinário. */
    public void invalidarTokensAtivos(Long veterinarioId) {

        String sql = """
                UPDATE PASSWORD_RESET_TOKENS
                SET USADO = 1
                WHERE VETERINARIO_ID = ?
                  AND USADO = 0
                """;

        jdbcTemplate.update(sql, veterinarioId);
    }
}
