/*
 * Copyright 2010 TagAPrice.org
 * 
 * Licensed under the Creative Commons License. You may not
 * use this file except in compliance with the License. 
 *
 * http://creativecommons.org/licenses/by-nc/3.0/
*/

/**
 * Project: TagAPriceUI
 * Filename: PropertyDAO.java
 * Date: 14.07.2010
*/
package org.tagaprice.server.dao.postgres;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;
import org.tagaprice.server.DBConnection;
import org.tagaprice.server.dao.interfaces.IProductDAO;
import org.tagaprice.shared.ProductData;
import org.tagaprice.shared.exception.DAOException;

public class ProductDAO implements IProductDAO {
	private EntityDAO _entityDAO;
	private DBConnection _db;
	private static Logger _log = Logger.getLogger(ProductDAO.class);
	
	public ProductDAO(DBConnection db) {
		this._db=db;
		_entityDAO= new EntityDAO(db);
	}
	
	@Override
	public ProductData getById(long id) throws DAOException {
		_log.debug("id:"+id);
		
		ProductData product;
		//Get Entity Data
		product = _entityDAO.getById(new ProductData(), id);
		if(product == null)	
			return null;
		
		// TODO implement fetching of a specific product revision
		
		//Get Product Data
		String sql = "SELECT brand_id, type_id, imageurl " +
				"FROM productrevision " +
				"INNER JOIN ENTITY ON(ent_id = prod_id) " +
				"WHERE prod_id = ? AND rev = ?";
		try {
		PreparedStatement pstmt = _db.prepareStatement(sql);
			pstmt.setLong(1, product.getId());
		pstmt.setLong(2, product.getRev());
		ResultSet res = pstmt.executeQuery();
		
		if (!res.next()) 
			return null;
		
		if (res.getString("brand_id") != null) 
			product.setBrandId(res.getLong("brand_id"));
		else 
			product.setBrandId(null);
		if (res.getString("type_id") != null) 
			product.setTypeId(res.getLong("type_id"));
		else 
			product.setTypeId(null);
		product.setImageSrc(res.getString("imageurl"));
		return product;	
		} catch (SQLException e) {
			String msg = "Failed to retrieve unit from database. SQLException: "+e.getMessage()+".";
			_log.error(msg + " Chaining and rethrowing.");
			_log.debug(e.getStackTrace());
			throw new DAOException(msg, e);
		}
	}

	@Override
	public boolean save(ProductData product) throws DAOException {
		
		if(!_entityDAO.save(product))
			return false;
		
		PreparedStatement pstmt;
		try {
			if (product.getRev() == 1) {
				// create a new Product
				pstmt = _db.prepareStatement("INSERT INTO product (prod_id) VALUES (?)");
				pstmt.setLong(1, product.getId());
				pstmt.executeUpdate();
			}
			else if (product.getRev() < 1) {
				throw new DAOException("EntityDAO returned shop with revision < 0!");
			}

			String sql = "INSERT INTO productRevision (prod_id, rev, brand_id, type_id, imageUrl) VALUES (?, ?, ?, ?, ?)";
			pstmt = _db.prepareStatement(sql);
			pstmt.setLong(1, product.getId());
			pstmt.setInt(2, product.getRev());

			if (product.getBrandId() != null) pstmt.setLong(3, product.getBrandId());
			else pstmt.setNull(3, Types.BIGINT);

			if (product.getTypeId() != null) pstmt.setLong(4, product.getTypeId());
			else pstmt.setNull(4, Types.BIGINT);

			pstmt.setString(5, product.getImageSrc());

			pstmt.executeUpdate();
			return false;
		} catch (SQLException e) {
			String msg = "Failed to retrieve unit from database. SQLException: "+e.getMessage()+".";
			_log.error(msg + " Chaining and rethrowing.");
			_log.debug(e.getStackTrace());
			throw new DAOException(msg, e);
		}
		
	}
}
