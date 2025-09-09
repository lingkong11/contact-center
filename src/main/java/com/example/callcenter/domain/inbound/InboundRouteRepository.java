package com.example.callcenter.domain.inbound;

import java.util.List;
import java.util.Optional;

public interface InboundRouteRepository {
	InboundRoute save(InboundRoute route);
	Optional<InboundRoute> findById(String id);
	Optional<InboundRoute> findByDid(String didNumber);
	List<InboundRoute> findAll();
	void deleteById(String id);
}

