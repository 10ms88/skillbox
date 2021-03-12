package myBlog.enumuration;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
//  USER(Set.of(Permission.USER)),
//  MODERATOR(Set.of(Permission.USER, Permission.MODERATE));
  USER(Stream.of(Permission.USER)
    .collect(Collectors.toCollection(HashSet::new))),
    MODERATOR(Stream.of(Permission.USER, Permission.MODERATE)
  .collect(Collectors.toCollection(HashSet::new)));

  private final Set<Permission> permissions;

  Role(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  public Set<Permission> getPermissions() {
    return permissions;
  }

  public Set<SimpleGrantedAuthority> getAuthorities() {
    return permissions
        .stream()
        .map(p -> new SimpleGrantedAuthority(p.getPermission()))
        .collect(Collectors.toSet());
  }

}
