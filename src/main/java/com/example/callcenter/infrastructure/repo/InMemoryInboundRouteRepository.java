package com.example.callcenter.infrastructure.repo;

import com.example.callcenter.domain.inbound.InboundRoute;
import com.example.callcenter.domain.inbound.InboundRouteRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryInboundRouteRepository implements InboundRouteRepository {
	private final Map<String, InboundRoute> idToRoute = new ConcurrentHashMap<>();
	private final Map<String, String> didToId = new ConcurrentHashMap<>();

	@Override
	public InboundRoute save(InboundRoute route) {
		idToRoute.put(route.getId(), route);
		didToId.put(route.getDidNumber(), route.getId());
		return route;
	}

	@Override
	public Optional<InboundRoute> findById(String id) {
		return Optional.ofNullable(idToRoute.get(id));
	}

	@Override
	public Optional<InboundRoute> findByDid(String didNumber) {
		String id = didToId.get(didNumber);
		return id == null ? Optional.empty() : Optional.ofNullable(idToRoute.get(id));
	}

	@Override
	public List<InboundRoute> findAll() {
		return new ArrayList<>(idToRoute.values());
	}

	@Override
	public void deleteById(String id) {
		InboundRoute removed = idToRoute.remove(id);
		if (removed != null) {
			didToId.remove(removed.getDidNumber());
		}
	}
}

