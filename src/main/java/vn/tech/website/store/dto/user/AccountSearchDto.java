package vn.tech.website.store.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.dto.BaseSearchDto;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountSearchDto extends BaseSearchDto {
    private Long accountId;
    private Integer roleId;
    private String username;
    private String fullName;
    private Integer gender;
    private Date dateOfBirth;
    private String address;
    private String phone;
    private String email;
    private Integer accountStatus;

    public AccountSearchDto(Integer roleId, String username) {
        this.roleId = roleId;
        this.username = username;
    }
}
