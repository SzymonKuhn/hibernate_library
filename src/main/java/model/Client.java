package model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity

public class Client implements IBaseEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    @Column(unique = true, nullable = false)
    private String idNumber;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany (mappedBy = "client", fetch = FetchType.EAGER)
    private Set<BookLent> lentBooks;

}
