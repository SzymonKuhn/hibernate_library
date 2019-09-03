


import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book implements IBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private int yearWritten;
    private int numOfPages;
    private int numOfAvailableCopies;

    @Formula(value="(SELECT COUNT(*) FROM bookLent l WHERE l.book_id = id and bl.dateReturned is null)")
    private int numOfBorrowedCopies;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany (mappedBy = "book", fetch = FetchType.EAGER)
    private Set<BookLent> currentLents;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER)
    private Set<Author> authors;

    @Formula(value = "(year(now()) - yearWritten)")
    private int howOld;

    @ManyToOne
    private PublishingHouse publishingHouse;
}
