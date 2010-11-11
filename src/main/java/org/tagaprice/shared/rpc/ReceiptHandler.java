/*
 * Copyright 2010 TagAPrice.org
 * 
 * Licensed under the Creative Commons License. You may not
 * use this file except in compliance with the License. 
 *
 * http://creativecommons.org/licenses/by-nc/3.0/
*/

/**
 * Project: TagAPrice
 * Filename: ReceiptHandler.java
 * Date: 30.05.2010
*/
package org.tagaprice.shared.rpc;

import java.util.ArrayList;

import org.tagaprice.shared.data.ReceiptData;
import org.tagaprice.shared.exception.InvalidLoginException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc/receipt")
public interface ReceiptHandler extends RemoteService {
	
	/**
	 * 
	 * @param id If 0 new Draft will be created
	 * @return
	 * @throws IllegalArgumentException
	 */
	ReceiptData get(ReceiptData data) throws IllegalArgumentException, InvalidLoginException;
	
	ArrayList<ReceiptData> getUserReceipts() throws IllegalArgumentException, InvalidLoginException;
	
	ReceiptData save(ReceiptData data) throws IllegalArgumentException, InvalidLoginException;
}
