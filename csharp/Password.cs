using System;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;

namespace WinTest
{
    public class Password
    {
        /// <summary>
        /// 对一个字符串进行MD5加密
        /// </summary>
        /// <param name="password">待加密的密码</param>
        /// <returns>加密结果</returns>
        public static string MD5(string password)
        {
            MD5 md5 = new MD5CryptoServiceProvider();
            return BitConverter.ToString(md5.ComputeHash(Encoding.UTF8.GetBytes(password))).Replace("-", "");
        }

        /// <summary>
        /// 随机产生一个密码
        /// </summary>
        /// <returns>随机生成的密码</returns>
        public static string GeneratePassword()
        {
            Random random = new Random();
            string s = "";
            while (!CheckPassword(s))
            {
                s = random.Next(4096, 65536).ToString("X") + "=" + random.Next(4096, 65536).ToString("x");
            }
            return s;
        }

        /// <summary>
        /// 检测密码复杂度
        /// </summary>
        /// <param name="password">待检测密码</param>
        /// <returns>检测结果</returns>
        public static bool CheckPassword(string password)
        {
            Regex regex = new Regex("^[a-zA-Z0-9=+@!-]{8,}$", RegexOptions.Singleline);
            return regex.IsMatch(password);
        }

        /// <summary>
        /// 对一个字符串进行SHA1加密
        /// </summary>
        /// <param name="password">待加密的密码</param>
        /// <returns>加密结果</returns>
        public static string SHA1(string password)
        {
            SHA1Managed sha1 = new SHA1Managed();
            byte[] data = Encoding.UTF8.GetBytes(password);
            byte[] hash = sha1.ComputeHash(data);
            return BitConverter.ToString(hash).ToLower().Replace("-", "");
        }
    }
}
