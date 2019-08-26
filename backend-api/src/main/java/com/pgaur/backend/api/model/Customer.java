package com.pgaur.backend.api.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "field can't be empty")
    @Size(min = 3, max = 12, message = "firstName has to be between 3 and 12")
    @Column(nullable = false)
    private String firstName;

    @NotEmpty(message = "field can't be empty")
    private String lastName;

    @NotEmpty(message = "field can't be empty")
    @Email(message = "it is not a well formed email address")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "field can't be empty")
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    private String photo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
