PrincipalContext pc = null;
            UserPrincipal user = null;
            try
            {
                pc = new PrincipalContext(ContextType.Domain, "192.168.110.102", "ldapuser@skoito.com", "Apa=34567");
                user = UserPrincipal.FindByIdentity(pc, "junz02");
                if (user != null)
                {
                    user.SetPassword("Ape=34567");
                    user.UnlockAccount();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (user != null)
                {
                    user.Dispose();
                    user = null;
                }
                if (pc != null)
                {
                    pc.Dispose();
                    pc = null;
                }
            }
            
DirectoryEntry entry = null;
try
{
    entry = new DirectoryEntry("LDAP://192.168.110.109", account, password, AuthenticationTypes.Secure);
    string name = entry.Name;
    return true;
}
catch (Exception)
{
    return false;
}