package com.bugzillalive.model.request;

public enum OutboundEndpoint {

	BUGS_FOR_NUMBERS("/rest/bug?id=%s"),
	BUGS_FOR_USER("/rest/bug?assigned_to=%s"),
	BUG_COMMENTS("/rest/bug/%s/comment"),
	BUG_ATTACHMENTS("/rest/bug/%s/attachment");

	private String uri;

	private OutboundEndpoint(String uri) {
		this.uri = uri;
	}

	public String uri(String baseUri, Object... uriParams) {
		return baseUri + String.format(this.uri, uriParams);
	}
}
