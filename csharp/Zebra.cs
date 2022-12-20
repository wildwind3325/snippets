using System;
using System.Collections.Generic;
using System.Drawing;
using System.Text;

namespace WinTest
{
    public class BarcodeUtil
    {
        static List<char> codes = new List<char>();
        static List<int> repeats = new List<int>();

        static BarcodeUtil()
        {
            for (int i = 0; i < 19; i++)
            {
                char ch = (char)('G' + i);
                codes.Add(ch);
                repeats.Add(i + 1);
            }
            for (int i = 0; i < 20; i++)
            {
                char ch = (char)('g' + i);
                codes.Add(ch);
                repeats.Add(20 * (i + 1));
            }
        }

        public static string Text2String(string content, Font font = null, int x = 0, int y = 0, int zoomX = 1, int zoomY = 1)
        {
            if (font == null)
            {
                font = new Font("SimSun", 12, FontStyle.Regular, GraphicsUnit.Pixel);
            }
            Bitmap bmp = new Bitmap(1, 1);
            Graphics g = Graphics.FromImage(bmp);
            float f = g.MeasureString(content, font).Width;
            int width = (int)(f / 8) * 8;
            if (f % 8 != 0)
            {
                width += 8;
            }
            int height = font.Height;
            g.Dispose();
            bmp.Dispose();
            bmp = new Bitmap(width, height);
            g = Graphics.FromImage(bmp);
            g.Clear(Color.White);
            g.DrawString(content, font, Brushes.Black, 0, 0);
            string result = Image2String(bmp, x, y, zoomX, zoomY);
            g.Dispose();
            bmp.Dispose();
            return result;
        }

        public static string Image2String(Bitmap bmp, int x = 0, int y = 0, int zoomX = 1, int zoomY = 1)
        {
            if (bmp.Width % 8 != 0)
            {
                int width = (bmp.Width / 8 + 1) * 8;
                Bitmap fixedBmp = new Bitmap(width, bmp.Height);
                Graphics g = Graphics.FromImage(fixedBmp);
                g.Clear(Color.White);
                g.DrawImage(bmp, 0, 0);
                string result = Image2String(fixedBmp, x, y, zoomX, zoomY);
                g.Dispose();
                fixedBmp.Dispose();
                return result;
            }
            StringBuilder sbContent = new StringBuilder();
            for (int i = 0; i < bmp.Height; i++)
            {
                for (int j = 0; j < bmp.Width / 4; j++)
                {
                    int value = 0;
                    for (int k = 0; k < 4; k++)
                    {
                        Console.WriteLine(bmp.GetPixel((j * 4) + k, i).B);
                        if (bmp.GetPixel((j * 4) + k, i).B == 0)
                        {
                            value += (int)Math.Pow(2, 3 - k);
                        }
                    }
                    sbContent.Append(value.ToString("X"));
                }
            }
            Console.WriteLine(sbContent.Length + ": " + sbContent.ToString());
            StringBuilder sbResult = new StringBuilder();
            sbResult.Append("~DGOUTSTR01," + bmp.Width * bmp.Height / 8 + "," + bmp.Width / 8 + ",");
            int count = 1;
            for (int i = 1; i < sbContent.Length; i++)
            {
                if (sbContent[i] == sbContent[i - 1])
                {
                    count++;
                    if (i == sbContent.Length - 1)
                    {
                        sbResult.Append(CompressCode(count, true) + sbContent[i]);
                    }
                }
                else
                {
                    sbResult.Append(CompressCode(count, true) + sbContent[i - 1]);
                    count = 1;
                }
            }
            sbResult.Append("\r\n^FO" + x + "," + y + "^XGOUTSTR01," + zoomX + "," + zoomY + ",^FS");
            return sbResult.ToString();
        }

        public static string CompressCode(int len, bool original)
        {
            if (len == 1 && !original)
            {
                return codes[0].ToString();
            }
            if (len <= 1)
            {
                return "";
            }
            else
            {
                for (int i = repeats.Count - 1; i >= 0; i--)
                {
                    if (len >= repeats[i])
                    {
                        return codes[i] + CompressCode(len - repeats[i], false);
                    }
                }
                return "";
            }
        }
    }
}