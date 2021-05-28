package vn.tech.website.store.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.Account;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends Account {
    private String roleName;
    private String provinceName;
    private String districtName;
    private String communeName;
    private String rePassword;
}
