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
public class AdministratorsCentralStaffSearchDto extends BaseSearchDto {
    private Long accountId;
    private Integer roleId;
    private String fullName;
    private Integer sex;
    private Date dateOfBirth;
    private String address;
    private String phone;
    private String email;
    private String password;
    private String idCardNumber;
    private String placeOfIssue;
    private Date dateOfIssue;
    private boolean relativeInfor;
    private String permanentResidence;
    private String orgName;
    private String businessLicense;
    private String orgAddress;
    private String position;
    private String orgPhone;
    private int status;
    private String username;
}
