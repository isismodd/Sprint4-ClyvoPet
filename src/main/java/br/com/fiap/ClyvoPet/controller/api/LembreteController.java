package br.com.fiap.ClyvoPet.controller.api;

import br.com.fiap.ClyvoPet.model.Lembrete;
import br.com.fiap.ClyvoPet.service.LembreteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lembretes")
public class LembreteController {

    private final LembreteService service;

    public LembreteController(LembreteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Lembrete> criar(@RequestBody Lembrete lembrete) {
        service.salvar(lembrete);
        return ResponseEntity.status(HttpStatus.CREATED).body(lembrete);
    }

    @GetMapping
    public ResponseEntity<List<Lembrete>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lembrete> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<List<Lembrete>> buscarPorConsulta(
            @PathVariable Long consultaId) {

        return ResponseEntity.ok(service.buscarPorConsulta(consultaId));
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<Lembrete>> buscarPendentes() {
        return ResponseEntity.ok(service.buscarPendentes());
    }

    @PostMapping("/enviar/{lembreteId}")
    public ResponseEntity<Void> enviarLembrete(
            @PathVariable Long lembreteId) {

        service.enviarLembrete(lembreteId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/criar/{consultaId}")
    public ResponseEntity<Lembrete> criarLembrete(
            @PathVariable Long consultaId) {

        Lembrete lembrete =
                service.criarLembrete(consultaId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lembrete);
    }

    @PostMapping("/criar-enviar/{consultaId}")
    public ResponseEntity<Void> criarEEnviarLembrete(
            @PathVariable Long consultaId) {

        service.criarEEnviarLembrete(consultaId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/enviar-todos")
    public ResponseEntity<String> enviarTodosPendentes() {
        service.enviarTodosLembretesPendentes();

        return ResponseEntity.ok(
                "Lembretes pendentes enviados com sucesso!"
        );
    }

    @PatchMapping("/{id}/marcar-enviado")
    public ResponseEntity<Void> marcarComoEnviado(
            @PathVariable Long id) {

        service.marcarComoEnviado(id);
        return ResponseEntity.noContent().build();
    }
}