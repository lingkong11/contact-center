package com.example.callcenter.interfaces.web;

import com.example.callcenter.application.inbound.InboundRouteService;
import com.example.callcenter.domain.inbound.InboundRoute;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/inbound-routes")
@Validated
public class InboundRouteController {
	private final InboundRouteService service;

	public InboundRouteController(InboundRouteService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<InboundRoute> create(@RequestBody Map<String, String> body) {
		String did = body.get("didNumber");
		String dtype = body.get("destinationType");
		String dval = body.get("destinationValue");
		InboundRoute created = service.create(did, dtype, dval);
		return ResponseEntity.ok(created);
	}

	@GetMapping
	public List<InboundRoute> list() {
		return service.list();
	}

	@PutMapping("/{id}")
	public ResponseEntity<InboundRoute> update(
			@PathVariable String id,
			@RequestBody Map<String, String> body
	) {
		Optional<InboundRoute> updated = service.update(id, body.get("didNumber"), body.get("destinationType"), body.get("destinationValue"));
		return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}

