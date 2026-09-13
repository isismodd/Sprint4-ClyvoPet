package br.com.fiap.ClyvoPet.repository;

import br.com.fiap.ClyvoPet.model.Veterinario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class VeterinarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public VeterinarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void salvar(Veterinario veterinario) {

        String sql = """
                INSERT INTO VETERINARIOS
                (NOME, EMAIL, SENHA, CRMV, TELEFONE, ESPECIALIDADE, ROLE)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String role = veterinario.getRole();

        if (role == null || role.isBlank()) {
            role = "VETERINARIO";
        }

        jdbcTemplate.update(
                sql,
                veterinario.getNome(),
                veterinario.getEmail(),
                veterinario.getSenha(),
                veterinario.getCrmv(),
                veterinario.getTelefone(),
                veterinario.getEspecialidade(),
                role
        );
    }

    public Optional<Veterinario> buscarPorEmail(String email) {

        String sql = """
                SELECT ID,
                       NOME,
                       EMAIL,
                       SENHA,
                       CRMV,
                       TELEFONE,
                       ESPECIALIDADE,
                       DATA_CADASTRO,
                       ATIVO,
                       ROLE
                FROM VETERINARIOS
                WHERE EMAIL = ?
                """;

        List<Veterinario> veterinarios = jdbcTemplate.query(
                sql,
                this::mapear,
                email
        );

        return veterinarios.stream().findFirst();
    }

    public Optional<Veterinario> buscarPorId(Long id) {

        String sql = """
                SELECT ID,
                       NOME,
                       EMAIL,
                       SENHA,
                       CRMV,
                       TELEFONE,
                       ESPECIALIDADE,
                       DATA_CADASTRO,
                       ATIVO,
                       ROLE
                FROM VETERINARIOS
                WHERE ID = ?
                """;

        List<Veterinario> veterinarios = jdbcTemplate.query(
                sql,
                this::mapear,
                id
        );

        return veterinarios.stream().findFirst();
    }

    public List<Veterinario> listarTodos() {

        String sql = """
                SELECT ID,
                       NOME,
                       EMAIL,
                       SENHA,
                       CRMV,
                       TELEFONE,
                       ESPECIALIDADE,
                       DATA_CADASTRO,
                       ATIVO,
                       ROLE
                FROM VETERINARIOS
                ORDER BY NOME
                """;

        return jdbcTemplate.query(
                sql,
                this::mapear
        );
    }

    public void atualizar(Veterinario veterinario) {

        String sql = """
                UPDATE VETERINARIOS
                SET NOME = ?,
                    TELEFONE = ?,
                    ESPECIALIDADE = ?
                WHERE ID = ?
                """;

        jdbcTemplate.update(
                sql,
                veterinario.getNome(),
                veterinario.getTelefone(),
                veterinario.getEspecialidade(),
                veterinario.getId()
        );
    }

    public void atualizarSenha(Long id, String senhaHash) {

        String sql = """
                UPDATE VETERINARIOS
                SET SENHA = ?
                WHERE ID = ?
                """;

        jdbcTemplate.update(sql, senhaHash, id);
    }

    public void deletar(Long id) {

        String sql = """
                DELETE FROM VETERINARIOS
                WHERE ID = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    private Veterinario mapear(ResultSet rs, int rowNum) throws SQLException {

        Veterinario v = new Veterinario();

        v.setId(rs.getLong("ID"));
        v.setNome(rs.getString("NOME"));
        v.setEmail(rs.getString("EMAIL"));
        v.setSenha(rs.getString("SENHA"));
        v.setCrmv(rs.getString("CRMV"));
        v.setTelefone(rs.getString("TELEFONE"));
        v.setEspecialidade(rs.getString("ESPECIALIDADE"));

        if (rs.getTimestamp("DATA_CADASTRO") != null) {
            v.setDataCadastro(
                    rs.getTimestamp("DATA_CADASTRO")
                            .toLocalDateTime()
            );
        }

        v.setAtivo(rs.getInt("ATIVO") == 1);
        v.setRole(rs.getString("ROLE"));

        return v;
    }
}