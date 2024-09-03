using Microsoft.Win32;
using System;
using System.Diagnostics;
using System.IO;

namespace Decryptor
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length == 0) return;
            try
            {
                if ("install".Equals(args[0]))
                {
                    string path = AppDomain.CurrentDomain.BaseDirectory;
                    string file = Path.Combine(path, "code.exe");
                    RegistryKey rk = Registry.ClassesRoot;
                    RegistryKey fe = rk.CreateSubKey("*\\shell\\Decryptor");
                    fe.SetValue("", "通过 Decryptor 解密", RegistryValueKind.ExpandString);
                    fe.SetValue("Icon", file, RegistryValueKind.ExpandString);
                    RegistryKey fc = rk.CreateSubKey("*\\shell\\Decryptor\\command");
                    fc.SetValue("", "\"" + file + "\" \"%1\"", RegistryValueKind.ExpandString);
                    RegistryKey de = rk.CreateSubKey("Directory\\shell\\Decryptor");
                    de.SetValue("", "通过 Decryptor 解密", RegistryValueKind.ExpandString);
                    de.SetValue("Icon", file, RegistryValueKind.ExpandString);
                    RegistryKey dc = rk.CreateSubKey("Directory\\shell\\Decryptor\\command");
                    dc.SetValue("", "\"" + file + "\" \"%V\"", RegistryValueKind.ExpandString);
                    rk.Close();
                    return;
                }
                if ("uninstall".Equals(args[0]))
                {
                    RegistryKey rk = Registry.ClassesRoot;
                    rk.DeleteSubKeyTree("*\\shell\\Decryptor");
                    rk.DeleteSubKeyTree("Directory\\shell\\Decryptor");
                    rk.Close();
                    return;
                }
                if (Directory.Exists(args[0]))
                {
                    Dec(new DirectoryInfo(args[0]));
                    return;
                }
                if (File.Exists(args[0]))
                {
                    Dec(new FileInfo(args[0]));
                    return;
                }
            }
            catch (Exception ex)
            {
                EventLog log = new EventLog();
                log.Source = "Decryptor";
                log.WriteEntry("运行时发生错误：" + ex.Message, EventLogEntryType.Error);
            }
        }

        static void Dec(DirectoryInfo di)
        {
            foreach (DirectoryInfo sub in di.GetDirectories())
            {
                Dec(sub);
            }
            foreach (FileInfo fi in di.GetFiles())
            {
                Dec(fi);
            }
        }

        static void Dec(FileInfo fi)
        {
            try
            {
                byte[] data = File.ReadAllBytes(fi.FullName);
                fi.Delete();
                File.WriteAllBytes(fi.FullName, data);
                fi.IsReadOnly = true;
                Console.WriteLine("已处理" + fi.FullName);
            }
            catch (Exception ex)
            {
                string message = "处理" + fi.FullName + "时发生异常：" + ex.Message;
                Console.WriteLine(message);
                EventLog log = new EventLog();
                log.Source = "Decryptor";
                log.WriteEntry(message, EventLogEntryType.Error);
            }
        }
    }
}