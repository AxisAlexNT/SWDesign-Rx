package ru.ifmo.rain.serdiukov.reactive.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Table(name = "Users")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class User {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    public long id;
    @Column(nullable = false)
    public String name;
    @Column(unique = true, nullable = false)
    public String login;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public Currency preferredCurrency;
}
