package com.banco.crudbanco.exception;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDetails {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	@JsonProperty("timestamp")
	private Date timestamp;

	@JsonProperty("status")
	private int httpStatus;

	@JsonProperty("error")
	private String errorDescription;

	@JsonProperty("message")
	private String message;

	@JsonProperty("message-details")
	private List<String> messageDetails;

	@JsonProperty("stacktrace")
	private String stacktrace;

	@JsonProperty("path")
	private String path;

	public ErrorDetails() {
	}

	public ErrorDetails(Date timestamp, HttpStatus status, String message, List<String> messageDetails, String path,
			String stacktrace) {
		this.timestamp = timestamp;
		this.httpStatus = status.value();
		this.errorDescription = status.getReasonPhrase();
		this.message = message;
		this.messageDetails = messageDetails;
		this.path = path;
		this.stacktrace = stacktrace;
	}

	public ErrorDetails(Date timestamp, HttpStatus status, String message, List<String> messageDetails, String path) {
		this(timestamp, status, message, messageDetails, path, "");
	}

	public ErrorDetails(HttpStatus status, String path) {
		this(new Date(), status, status.getReasonPhrase(), Collections.emptyList(), path);
	}

	public ErrorDetails(HttpStatus status, String message, String path, String stacktrace) {
		this(new Date(), status, message, Collections.emptyList(), path, stacktrace);
	}

	public ErrorDetails(HttpStatus status, String message, String path) {
		this(new Date(), status, message, Collections.emptyList(), path, "");
	}

	public ErrorDetails(HttpStatus status, String message, List<String> messageDetails, String path,
			String stacktrace) {
		this(new Date(), status, message, messageDetails, path, stacktrace);
	}

	public ErrorDetails(HttpStatus status, String message, List<String> messageDetails, String path) {
		this(new Date(), status, message, messageDetails, path, "");
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}
}