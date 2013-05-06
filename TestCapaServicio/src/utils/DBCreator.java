package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Persistence;

public class DBCreator {
	
	public static void main(String[] args) {
		final String driver = "com.mysql.jdbc.Driver";
		final String url = "jdbc:mysql://localhost:3306";
		final String schema = "prueba";
		final String user = "root";
		final String pass = "102030";
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,user, pass);			
		} catch (ClassNotFoundException e) {
			System.out.println("PROBLEMAS CON LAS LIBRERIAS!!! (Consultar al emi)");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Problemas con el motor!!! (A renegar un rato, no sea VAGO!!!)");
			e.printStackTrace();
		}		
		try {
			ps = con.prepareStatement("drop schema "+schema);
			ps.executeUpdate();
			System.out.println("Esquema borrado con exito!");
		} catch (SQLException e) {
			if(e.getErrorCode()!=1008){
				System.out.println("Problema desconocido!!!");
				e.printStackTrace();
				throw new RuntimeException();
			}else{
				System.out.println("El Esquema 'Prueba' no existia");
			}
		}
		try {
			ps = con.prepareStatement("create schema "+schema);
			ps.executeUpdate();
			System.out.println("Esquema creado con exito!");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ps != null){
					ps.close();
				}
				if(con != null){
					con.close();
				}				
			} catch (SQLException e) {
				throw new Error("Error Inesperado!!! (Estas hasta las manos");
			}
		}
		
		Map<String,String> m = new HashMap<String,String>();
		m.put("hibernate.connection.driver_class", driver);		
		m.put("hibernate.connection.url", url+"/"+schema);
		m.put("hibernate.connection.username", user);
		m.put("hibernate.connection.password", pass);
		m.put("hibernate.hbm2ddl.auto", "create");
		m.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		System.out.println("*** CREANDO ESQUEMA ***");
		Persistence.createEntityManagerFactory("Dominio", m);
		System.out.println("*** ESQUEMA ESCRITO ***");
	}

}
