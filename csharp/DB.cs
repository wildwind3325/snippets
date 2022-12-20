public static string Run(string tableType, string dbUrl, string namespc)
        {
            using (SqlConnection conn = new SqlConnection(dbUrl))
            {
                conn.Open();
                DataTable table = conn.GetSchema("Tables");
                List<string> listTable = new List<string>();
                List<string> listView = new List<string>();
                foreach (DataRow row in table.Rows)
                {
                    if (row["TABLE_TYPE"].ToString().Equals("BASE TABLE", StringComparison.CurrentCultureIgnoreCase))
                    {
                        listTable.Add(row["TABLE_NAME"].ToString());
                    }
                    else if (row["TABLE_TYPE"].ToString().Equals("VIEW", StringComparison.CurrentCultureIgnoreCase))
                    {
                        listView.Add(row["TABLE_NAME"].ToString());
                    }
                }
                listTable.Sort();
                listView.Sort();
                listTable.AddRange(listView);
                StringBuilder sb = new StringBuilder();
                sb.Append("using System;\r\n\r\nnamespace " + namespc + "\r\n{\r\n");
                for (int i = 0; i < listTable.Count; i++)
                {
                    if (tableType.Equals("系统表", StringComparison.CurrentCultureIgnoreCase)
                        && !listTable[i].StartsWith("Sys_", StringComparison.CurrentCultureIgnoreCase)
                        && !listTable[i].StartsWith("V_Sys_", StringComparison.CurrentCultureIgnoreCase))
                    {
                        continue;
                    }
                    if (tableType.Equals("业务表", StringComparison.CurrentCultureIgnoreCase)
                        && (listTable[i].StartsWith("Sys_", StringComparison.CurrentCultureIgnoreCase)
                        || listTable[i].StartsWith("V_Sys_", StringComparison.CurrentCultureIgnoreCase)))
                    {
                        continue;
                    }
                    sb.Append("    #region " + listTable[i] + "\r\n");
                    sb.Append("    public class " + listTable[i] + "\r\n    {\r\n");
                    SqlCommand cmd = new SqlCommand("SELECT * FROM [" + listTable[i] + "]", conn);
                    SqlDataReader reader = cmd.ExecuteReader(CommandBehavior.KeyInfo);
                    table = reader.GetSchemaTable();
                    reader.Close();
                    for (int j = 0; j < table.Rows.Count; j++)
                    {
                        if ((bool)table.Rows[j]["IsHidden"])
                        {
                            continue;
                        }
                        sb.Append("        public ");
                        Type type = Type.GetType(table.Rows[j]["DataType"].ToString());
                        sb.Append(type.Name);
                        if (type.IsValueType && (bool)table.Rows[j]["AllowDBNull"])
                        {
                            sb.Append("?");
                        }
                        sb.Append(" " + table.Rows[j]["ColumnName"].ToString() + ";\r\n");
                    }
                    sb.Append("    }\r\n    #endregion\r\n");
                    if (i < listTable.Count - 1)
                    {
                        sb.Append("\r\n");
                    }
                }
                sb.Append("}");
                conn.Close();
                return sb.ToString();
            }
        }
        
public static string Run(string tableType, string dbUrl, string namespc)
        {
            using (MySqlConnection conn = new MySqlConnection(dbUrl))
            {
                conn.Open();
                DataTable table = conn.GetSchema("Tables");
                List<string> listTable = new List<string>();
                List<string> listView = new List<string>();
                foreach (DataRow row in table.Rows)
                {
                    if (row["TABLE_TYPE"].ToString().Equals("BASE TABLE", StringComparison.CurrentCultureIgnoreCase))
                    {
                        listTable.Add(Util.FirstToUpper(row["TABLE_NAME"].ToString()));
                    }
                    else if (row["TABLE_TYPE"].ToString().Equals("VIEW", StringComparison.CurrentCultureIgnoreCase))
                    {
                        listView.Add(Util.FirstToUpper(row["TABLE_NAME"].ToString()));
                    }
                }
                listTable.Sort();
                listView.Sort();
                listTable.AddRange(listView);
                StringBuilder sb = new StringBuilder();
                sb.Append("using System;\r\n\r\nnamespace " + namespc + "\r\n{\r\n");
                for (int i = 0; i < listTable.Count; i++)
                {
                    sb.Append("    #region " + listTable[i] + "\r\n");
                    sb.Append("    public class " + listTable[i] + "\r\n    {\r\n");
                    MySqlCommand cmd = new MySqlCommand("SELECT * FROM `" + listTable[i] + "`", conn);
                    MySqlDataReader reader = cmd.ExecuteReader(CommandBehavior.KeyInfo);
                    table = reader.GetSchemaTable();
                    reader.Close();
                    for (int j = 0; j < table.Rows.Count; j++)
                    {
                        if (table.Rows[j]["IsHidden"] != DBNull.Value)
                        {
                            continue;
                        }
                        sb.Append("        public ");
                        Type type = Type.GetType(table.Rows[j]["DataType"].ToString());
                        sb.Append(type.Name);
                        if (type.IsValueType && (bool)table.Rows[j]["AllowDBNull"])
                        {
                            sb.Append("?");
                        }
                        sb.Append(" " + table.Rows[j]["ColumnName"].ToString() + ";\r\n");
                    }
                    sb.Append("    }\r\n    #endregion\r\n");
                    if (i < listTable.Count - 1)
                    {
                        sb.Append("\r\n");
                    }
                }
                sb.Append("}");
                conn.Close();
                return sb.ToString();
            }
        }