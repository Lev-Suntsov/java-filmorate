package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {
    private int id;
    private String email;
    private String login;
    private  String name;
    private LocalDate birthday;

}
