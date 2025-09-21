package com.mercatic.mercaticBack.modells.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AuthRequest {

    private String id;
    private String email;
    private String password;


    public String getId(){return this.id;}
    public void setId(String id){this.id = id;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
