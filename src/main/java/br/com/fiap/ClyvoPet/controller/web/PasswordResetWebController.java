package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class PasswordResetWebController {

    private final PasswordResetService passwordResetService;

    // Defina APP_BASE_URL em produção (ex.: https://clyvo-pet-rm561497.azurewebsites.net)
    @Value("${app.base-url:}")
    private String baseUrlConfigurada;

    public PasswordResetWebController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/esqueci-senha")
    public String showEsqueciSenha() {
        return "esqueci-senha";
    }

    @PostMapping("/esqueci-senha")
    public String solicitar(
            @RequestParam("email") String email,
            HttpServletRequest request
    ) {

        try {
            passwordResetService.solicitarRecuperacao(
                    email,
                    resolverBaseUrl(request)
            );
        } catch (Exception e) {
            // Não revela nada ao usuário; apenas registra no log
            System.err.println(
                    "❌ Falha ao processar recuperação de senha: " + e.getMessage()
            );
        }

        // Resposta sempre igual, exista o e-mail ou não
        return "redirect:/esqueci-senha?enviado";
    }

    @GetMapping("/redefinir-senha")
    public String showRedefinirSenha(
            @RequestParam(value = "token", required = false) String token,
            Model model
    ) {

        if (!passwordResetService.tokenValido(token)) {
            return "redirect:/esqueci-senha?invalido";
        }

        model.addAttribute("token", token);

        return "redefinir-senha";
    }

    @PostMapping("/redefinir-senha")
    public String redefinir(
            @RequestParam("token") String token,
            @RequestParam("senha") String senha,
            @RequestParam("confirmarSenha") String confirmarSenha,
            Model model
    ) {

        if (!passwordResetService.tokenValido(token)) {
            return "redirect:/esqueci-senha?invalido";
        }

        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("token", token);
            model.addAttribute("erro", "As senhas não conferem.");
            return "redefinir-senha";
        }

        try {
            passwordResetService.redefinirSenha(token, senha);
        } catch (IllegalArgumentException e) {
            model.addAttribute("token", token);
            model.addAttribute("erro", e.getMessage());
            return "redefinir-senha";
        }

        return "redirect:/login?senhaAlterada";
    }

    private String resolverBaseUrl(HttpServletRequest request) {

        if (baseUrlConfigurada != null && !baseUrlConfigurada.isBlank()) {
            return baseUrlConfigurada.replaceAll("/+$", "");
        }

        return ServletUriComponentsBuilder
                .fromContextPath(request)
                .build()
                .toUriString();
    }
}
