package magic.management.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false ,unique = true,name = "student_no")
    private  String studentNo;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false,name = "college_id")
    private String collegeId;

    @Column(nullable = false,name = "class_id")
    private String classId;

    @Column(nullable = false,name = "enroll_date")
    private String enrollDate;
}
