package com.zenith.dao;

import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.zenith.exception.CustomErrorException;
import com.zenith.model.FootballMatch;

public class FootballMatchDaoImpl implements FootballMatchDao{

	public FootballMatch saveMatch(FootballMatch footballMatch) throws InterruptedException, ExecutionException, CustomErrorException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> collectionAPIFuture = dbFirestore.collection("football").document(footballMatch.getId()).set(footballMatch);
		while(!collectionAPIFuture.isDone()) {	
		}
		if(collectionAPIFuture.isDone())
			return footballMatch;
		throw new CustomErrorException("Match couldn't be saved"); 
	}
	
	public FootballMatch getMatch(String matchId) throws InterruptedException, ExecutionException, CustomErrorException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		DocumentReference documentReference = dbFirestore.collection("football").document(matchId);
		
		DocumentSnapshot document = documentReference.get().get();
		
		if(document.exists()) 
			return document.toObject(FootballMatch.class);
		
		throw new CustomErrorException("No match found with this id");
	}
	

}
