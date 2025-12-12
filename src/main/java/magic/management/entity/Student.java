package magic.management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "student")
@Data
public class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_no", referencedColumnName = "student_no", nullable = false, unique = true)
    private Users user;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String higherMathematics; // 改为小驼峰

    @Column(nullable = false)
    private String english;

    @Column(nullable = false)
    private String geometry;

    @Column(nullable = false)
    private String mentalHealth; // 改为小驼峰
}