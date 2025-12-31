package com.sonoda.login.model;

public class UserEntity {
    private Integer idUser;
    private String username;
    private String password;
    private String email;
    private Rol rol;

    public UserEntity() {
    }


    public UserEntity(Integer idUser, String username, String password, String email, Rol rol) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + rol +
                '}';
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Rol getRol() {return rol;}
    public void setRol(Rol rol) {this.rol = rol;}
}
