


import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int yearWritten;
    private int numOfPages;
    private int numOfAllCopies;

    @Formula(value="(SELECT COUNT(*) FROM booklent bl WHERE bl.book_id = id and bl.dateReturned is null)")
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

    public Book(Long id, String title, int yearWritten, int numOfPages, int numOfAllCopies) {
        this.id = id;
        this.title = title;
        this.yearWritten = yearWritten;
        this.numOfPages = numOfPages;
        this.numOfAllCopies = numOfAllCopies;
    }

    public Book(Long id, String title, int yearWritten, int numOfPages, int numOfAllCopies, int numOfBorrowedCopies, int howOld) {
        this.id = id;
        this.title = title;
        this.yearWritten = yearWritten;
        this.numOfPages = numOfPages;
        this.numOfAllCopies = numOfAllCopies;
        this.numOfBorrowedCopies = numOfBorrowedCopies;
        this.howOld = howOld;
    }

    public Book(Long id, String title, int yearWritten, int numOfPages, int numOfAllCopies, int numOfBorrowedCopies, PublishingHouse publishingHouse) {
        this.id = id;
        this.title = title;
        this.yearWritten = yearWritten;
        this.numOfPages = numOfPages;
        this.numOfAllCopies = numOfAllCopies;
        this.numOfBorrowedCopies = numOfBorrowedCopies;
               this.publishingHouse = publishingHouse;
    }
}
