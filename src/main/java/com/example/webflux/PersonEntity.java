package com.example.webflux;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Can.Mogol on 12/4/2017.
 */
@Document(collection = "person")
public class PersonEntity {

    @Id
    private String id;

    @NotBlank
    @Size(max = 50, min = 2)
    private String name;

    @NotNull
    private Date createdAt = new Date();

    public PersonEntity() {
    }

    public PersonEntity(String id, @NotBlank @Size(max = 50, min = 2) String name, @NotNull Date createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
