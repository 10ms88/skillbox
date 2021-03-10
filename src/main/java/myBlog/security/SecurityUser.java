//package myBlog.security;
//
//import java.util.List;
//import java.util.Map;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class SecurityUser implements UserDetails {
//
//
//  private Integer id;
//  private String username;
//  private String password;
//  private List<SimpleGrantedAuthority> authorities;
//  @Builder.Default
//  private boolean accountNonExpired = true;
//  @Builder.Default
//  private boolean accountNonLocked = true;
//  @Builder.Default
//  private boolean credentialsNonExpired = true;
//  @Builder.Default
//  private boolean enabled = true;
//  private Map<String, Object> attributes;
//
//  public String getName() {
//    return username;
//  }
//}


package myBlog.security;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
