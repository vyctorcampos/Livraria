package com.desafio.livraria.model;

public enum UserRole {

		READ("read"),
	    WRITE("write");

	    private String role;

	    UserRole(String role){
	        this.role = role;
	    }

	    public String getRole(){
	        return role;
	    }
}
