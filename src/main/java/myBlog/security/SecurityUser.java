package myBlog.security;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import myBlog.model.User;

@Data
@AllArgsConstructor
public class SecurityUser implements UserDetails {

  private Integer id;
  private String username;
  private String password;
  private List<SimpleGrantedAuthority> authorities;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private boolean enabled;


  public static UserDetails fromUser(User user) {
    return new SecurityUser(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        new ArrayList<SimpleGrantedAuthority>(user.getRole().getAuthorities()),
        true,
        true,
        true,
        true);
  }
}
