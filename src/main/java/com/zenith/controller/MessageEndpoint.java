package com.zenith.controller;

import com.zenith.service.MessagingService;

import nl.martijndwars.webpush.Subscription;

public class MessageEndpoint {
	private MessagingService messageService;

	  public MessageEndpoint(MessagingService messageService) {
	    this.messageService = messageService;
	  }

	  public String getPublicKey() {
	    return messageService.getPublicKey();
	  }

	  public void subscribe(Subscription subscription) {
	    messageService.subscribe(subscription);
	  }

	  public void unsubscribe(String endpoint) {
	    messageService.unsubscribe(endpoint);
	  }
}
