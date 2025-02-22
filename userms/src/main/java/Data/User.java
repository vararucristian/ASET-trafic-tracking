package Data;

import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public")
@NamedQueries({
        @NamedQuery(name = "User.find", query = "select u from User u where u.userName = ?1 and u.passWord = ?2"),
        @NamedQuery(name = "User.getMaxId", query = "select count(u)+1 from User u"),
        @NamedQuery(name = "User.getUser", query = "select u from User u where u.userName = ?1")

})
public class User {
    @Column(name = "username")
    String userName;

    @Column(name = "password")
    String passWord;

    @Column(name = "fname")
    String fName;

    @Column(name = "lname")
    String lName;

    @Column(name = "id")
    @Id
    int id;

    public User() {
    }

    public User(String userName, String passWord, String fName, String lName, int id) {
        this.userName = userName;
        this.passWord = passWord;
        this.fName = fName;
        this.lName = lName;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(passWord, user.passWord) &&
                Objects.equals(fName, user.fName) &&
                Objects.equals(lName, user.lName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, passWord, fName, lName, id);
    }
}
