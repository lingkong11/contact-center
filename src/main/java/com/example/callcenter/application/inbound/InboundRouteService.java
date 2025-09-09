package com.example.callcenter.application.inbound;

import com.example.callcenter.domain.inbound.InboundRoute;
import com.example.callcenter.domain.inbound.InboundRouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InboundRouteService {
	private final InboundRouteRepository repository;

	public InboundRouteService(InboundRouteRepository repository) {
		this.repository = repository;
	}

	public InboundRoute create(String didNumber, String destinationType, String destinationValue) {
		String id = UUID.randomUUID().toString();
		InboundRoute route = new InboundRoute(id, didNumber, destinationType, destinationValue);
		return repository.save(route);
	}

	public Optional<InboundRoute> findByDid(String didNumber) {
		return repository.findByDid(didNumber);
	}

	public List<InboundRoute> list() {
		return repository.findAll();
	}

	public Optional<InboundRoute> update(String id, String didNumber, String destinationType, String destinationValue) {
		Optional<InboundRoute> existing = repository.findById(id);
		if (existing.isEmpty()) return Optional.empty();
		InboundRoute route = new InboundRoute(id, didNumber, destinationType, destinationValue);
		repository.save(route);
		return Optional.of(route);
	}

	public void delete(String id) {
		repository.deleteById(id);
	}
}

