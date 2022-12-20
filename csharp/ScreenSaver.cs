// 屏保和一般的应用程序区别不大，只需要按照特定的方式去做就可以了：

// FormBorderStyle.None、ShowInTaskbar = false、FormWindowState.Maximized

// 对于Program.cs，传入参数的意义：

using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Globalization;

namespace MySS
{
    static class Program
    {
        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main(string[] args)
        {
            if (args.Length > 0)
            {
                string arg = args[0].ToLower(CultureInfo.InvariantCulture).Trim().Substring(0, 2);
                switch (arg)
                {
                    case "/c":
                        ShowOptions();
                        break;
                    case "/p":
                        break;
                    case "/s":
                        ShowScreenSaver();
                        break;
                    default:
                        MessageBox.Show("无效的命令行参数 :" + arg, "无效的命令行参数", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        break;
                }
            }
            else
            {
                ShowScreenSaver();
            }
        }

        private static void ShowScreenSaver()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new FormScreen());
        }

        private static void ShowOptions()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new FormOption());
        }
    }
}