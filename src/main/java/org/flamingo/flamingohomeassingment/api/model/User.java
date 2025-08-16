package org.flamingo.flamingohomeassingment.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String userName;

    @Override
    public String toString() {
        return "User [id=" + id + ", First Name=" + firstName +
                ", Last Name=" + lastName + ", User Name=" + userName + "]";
    }
}
