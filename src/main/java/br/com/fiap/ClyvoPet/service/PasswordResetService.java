package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.model.Veterinario;
import br.com.fiap.ClyvoPet.repository.PasswordResetTokenRepository;
import br.com.fiap.ClyvoPet.repository.VeterinarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class PasswordResetService {

    private static final int VALIDADE_MINUTOS = 30;
    private static final int TAMANHO_MINIMO_SENHA = 6;

    private final VeterinarioRepository veterinarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResetService(
            VeterinarioRepository veterinarioRepository,
            PasswordResetTokenRepository tokenRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.veterinarioRepository = veterinarioRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gera um token e envia o link de redefinição por e-mail.
     * Se o e-mail não existir, nada acontece (o chamador não deve
     * revelar ao usuário se o e-mail está cadastrado ou não).
     */
    @Transactional
    public void solicitarRecuperacao(String email, String baseUrl) {

        if (email == null || email.isBlank()) {
            return;
        }

        Optional<Veterinario> opt =
                veterinarioRepository.buscarPorEmail(email.trim());

        if (opt.isEmpty() || !Boolean.TRUE.equals(opt.get().getAtivo())) {
            return;
        }

        Veterinario veterinario = opt.get();

        // Só o link mais recente vale
        tokenRepository.invalidarTokensAtivos(veterinario.getId());

        String token = gerarToken();

        tokenRepository.salvar(
                veterinario.getId(),
                hash(token),
                LocalDateTime.now().plusMinutes(VALIDADE_MINUTOS)
        );

        String link = baseUrl + "/redefinir-senha?token=" + token;

        String mensagem = """
                Olá, %s!

                Recebemos uma solicitação para redefinir a senha da sua conta no ClyvoPet.

                Para criar uma nova senha, acesse o link abaixo (válido por %d minutos):
                %s

                Se você não fez essa solicitação, ignore este e-mail. Sua senha continuará a mesma.

                Equipe ClyvoPet 🐾
                """.formatted(veterinario.getNome(), VALIDADE_MINUTOS, link);

        emailService.enviarEmail(
                veterinario.getEmail(),
                "Redefinição de senha - ClyvoPet 🐾",
                mensagem
        );
    }

    public boolean tokenValido(String token) {

        if (token == null || token.isBlank()) {
            return false;
        }

        return tokenRepository
                .buscarVeterinarioIdPorTokenValido(hash(token))
                .isPresent();
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha) {

        if (novaSenha == null || novaSenha.length() < TAMANHO_MINIMO_SENHA) {
            throw new IllegalArgumentException(
                    "A senha deve ter no mínimo " + TAMANHO_MINIMO_SENHA + " caracteres."
            );
        }

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Link inválido ou expirado.");
        }

        Long veterinarioId = tokenRepository
                .buscarVeterinarioIdPorTokenValido(hash(token))
                .orElseThrow(() ->
                        new IllegalArgumentException("Link inválido ou expirado.")
                );

        veterinarioRepository.atualizarSenha(
                veterinarioId,
                passwordEncoder.encode(novaSenha)
        );

        // Consome este token e qualquer outro que ainda esteja ativo
        tokenRepository.invalidarTokensAtivos(veterinarioId);
    }

    // ===== AUXILIARES =====

    private String gerarToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(
                    digest.digest(token.getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
