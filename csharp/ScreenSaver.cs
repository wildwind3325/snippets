// ������һ���Ӧ�ó������𲻴�ֻ��Ҫ�����ض��ķ�ʽȥ���Ϳ����ˣ�

// FormBorderStyle.None��ShowInTaskbar = false��FormWindowState.Maximized

// ����Program.cs��������������壺

using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Globalization;

namespace MySS
{
    static class Program
    {
        /// <summary>
        /// Ӧ�ó��������ڵ㡣
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
                        MessageBox.Show("��Ч�������в��� :" + arg, "��Ч�������в���", MessageBoxButtons.OK, MessageBoxIcon.Error);
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