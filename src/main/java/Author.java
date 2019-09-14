
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author implements IBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private LocalDate dateOfBirth;

    // możemy z tej strony dodawać (książki do autorów) żeby tworzyć relacje
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Set<Book> books;

    @Formula(value = ("(SELECT COUNT(*) FROM author_book ab WHERE ab.authors_id = id)"))
    private Long numberOfBooks;

    public void addBook (Book book) {
        if (books == null) {
            books = new HashSet<>();
        }
        books.add(book);
    }
}
