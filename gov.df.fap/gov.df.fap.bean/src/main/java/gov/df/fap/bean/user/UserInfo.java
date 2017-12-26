// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   UserInfo.java

package gov.df.fap.bean.user;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

public class UserInfo
    implements UserDetails
{
	private static final long serialVersionUID = 1L;
    private UserDTO user;
    private String selectYear;
    private String selectDBType;
    private boolean _acegi_security_remember_me;
    private GrantedAuthority roles[];

    public UserDTO getUser()
    {
        return user;
    }

    public void setUser(UserDTO user)
    {
        this.user = user;
    }


    public UserInfo(UserDTO user, GrantedAuthority roles[])
    {
        this.user = user;
        this.roles = roles;
    }

    public boolean isAccountNonExpired()
    {
        return true;
    }

    public boolean isAccountNonLocked()
    {
        return true;
    }

    public GrantedAuthority[] getAuthorities()
    {
        return roles;
    }

    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    public boolean isEnabled()
    {
        return true;
    }

    public String getPassword()
    {
        return user.getPassword();
    }

    public String getUsername()
    {
        return user.getUser_code();
    }

    public GrantedAuthority[] getRoles()
    {
        return roles;
    }

    public void setRoles(GrantedAuthority roles[])
    {
        this.roles = roles;
    }

	public boolean is_acegi_security_remember_me() {
		return _acegi_security_remember_me;
	}

	public void set_acegi_security_remember_me(boolean _acegi_security_remember_me) {
		this._acegi_security_remember_me = _acegi_security_remember_me;
	}

	public String getSelectDBType() {
		return selectDBType;
	}

	public void setSelectDBType(String selectDBType) {
		this.selectDBType = selectDBType;
	}

	public String getSelectYear() {
		return selectYear;
	}

	public void setSelectYear(String selectYear) {
		this.selectYear = selectYear;
	}
}
