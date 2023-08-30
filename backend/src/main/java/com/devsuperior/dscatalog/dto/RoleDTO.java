package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String authority;

    private RoleDTO(Role entity) {
        id = entity.getId();
        authority = entity.getAuthority();
    }

    public static RoleDTO parseToDTO(Role role) {
        return new RoleDTO(role);
    }
}
