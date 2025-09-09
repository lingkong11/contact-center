package com.example.callcenter.interfaces.freeswitch;

import com.example.callcenter.application.inbound.InboundRouteService;
import com.example.callcenter.domain.inbound.InboundRoute;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class XmlCurlController {
	private final InboundRouteService routeService;

	public XmlCurlController(InboundRouteService routeService) {
		this.routeService = routeService;
	}

	@GetMapping(value = "/freeswitch/xml", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<Object> xmlCurl(
			@RequestParam(name = "Section", required = false) String section,
			@RequestParam(name = "Caller-Destination-Number", required = false) String calledNumber
	) {
		if (!"dialplan".equalsIgnoreCase(section)) {
			return ResponseEntity.ok(new EmptyResult());
		}

		Optional<InboundRoute> maybeRoute = routeService.findByDid(calledNumber);
		if (maybeRoute.isEmpty()) {
			return ResponseEntity.ok(new EmptyResult());
		}

		InboundRoute route = maybeRoute.get();
		DialplanResponse response = DialplanResponse.forRoute(route);
		return ResponseEntity.ok(response);
	}

	@JacksonXmlRootElement(localName = "document")
	static class DialplanResponse {
		@JacksonXmlProperty(isAttribute = true, localName = "type")
		public String type = "freeswitch/xml";

		@JacksonXmlProperty(localName = "section")
		public Section section;

		static DialplanResponse forRoute(InboundRoute route) {
			Section sec = new Section();
			Context ctx = new Context();
			ctx.name = "public";
			Extension ext = new Extension();
			ext.name = "inbound-" + route.getDidNumber();
			Condition cond = new Condition();
			cond.field = "destination_number";
			cond.expression = "^" + route.getDidNumber() + "$";

			Action answer = Action.of("answer");
			Action sleep = Action.of("sleep", "500");
			Action setCodec = Action.of("set", "absolute_codec_string=PCMA,PCMU,OPUS");

			cond.actions.add(answer);
			cond.actions.add(sleep);
			cond.actions.add(setCodec);

			String dtype = route.getDestinationType();
			String dval = route.getDestinationValue();
			if ("QUEUE".equalsIgnoreCase(dtype)) {
				cond.actions.add(Action.of("set", "queue_name=" + dval));
				cond.actions.add(Action.of("callcenter", dval));
			} else if ("USER".equalsIgnoreCase(dtype)) {
				cond.actions.add(Action.of("bridge", "user/" + dval));
			} else if ("URI".equalsIgnoreCase(dtype)) {
				cond.actions.add(Action.of("bridge", dval));
			} else {
				cond.actions.add(Action.of("playback", "ivr/ivr-that_was_an_invalid_entry.wav"));
			}

			ext.conditions.add(cond);
			ctx.extensions.add(ext);
			sec.context = ctx;
			DialplanResponse resp = new DialplanResponse();
			resp.section = sec;
			return resp;
		}
	}

	static class Section {
		@JacksonXmlProperty(isAttribute = true, localName = "name")
		public String name = "dialplan";
		@JacksonXmlProperty(localName = "context")
		public Context context;
	}

	static class Context {
		@JacksonXmlProperty(isAttribute = true, localName = "name")
		public String name;
		@JacksonXmlProperty(localName = "extension")
		public java.util.List<Extension> extensions = new java.util.ArrayList<>();
	}

	static class Extension {
		@JacksonXmlProperty(isAttribute = true, localName = "name")
		public String name;
		@JacksonXmlProperty(localName = "condition")
		public java.util.List<Condition> conditions = new java.util.ArrayList<>();
	}

	static class Condition {
		@JacksonXmlProperty(isAttribute = true, localName = "field")
		public String field;
		@JacksonXmlProperty(isAttribute = true, localName = "expression")
		public String expression;
		@JacksonXmlProperty(localName = "action")
		public java.util.List<Action> actions = new java.util.ArrayList<>();
	}

	static class Action {
		@JacksonXmlProperty(isAttribute = true, localName = "application")
		public String application;
		@JacksonXmlProperty(isAttribute = true, localName = "data")
		public String data;

		static Action of(String application) {
			Action a = new Action();
			a.application = application;
			return a;
		}
		static Action of(String application, String data) {
			Action a = new Action();
			a.application = application;
			a.data = data;
			return a;
		}
	}

	@JacksonXmlRootElement(localName = "document")
	static class EmptyResult {
		@JacksonXmlProperty(isAttribute = true, localName = "type")
		public String type = "freeswitch/xml";
	}
}

