package com.tilab.ca.sda_kurento_demo_app.utils.se;

import com.google.gson.JsonObject;
import org.kurento.jsonrpc.Transaction;
import org.kurento.jsonrpc.message.Request;
import org.kurento.room.api.pojo.ParticipantRequest;

public class WsSubEndpointsParams {
    
    private Transaction transaction;
    private Request<JsonObject> request;
    private ParticipantRequest participantRequest;

    public WsSubEndpointsParams(Transaction transaction, Request<JsonObject> request, ParticipantRequest participantRequest) {
        this.transaction = transaction;
        this.request = request;
        this.participantRequest = participantRequest;
    }
   
    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Request<JsonObject> getRequest() {
        return request;
    }

    public void setRequest(Request<JsonObject> request) {
        this.request = request;
    }

    public ParticipantRequest getParticipantRequest() {
        return participantRequest;
    }

    public void setParticipantRequest(ParticipantRequest participantRequest) {
        this.participantRequest = participantRequest;
    }

}
