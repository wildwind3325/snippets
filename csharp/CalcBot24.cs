using System;
using System.Collections.Generic;
using System.Text;

namespace TestApp
{
    /// <summary>
    /// 计算类似24点的游戏解法类
    /// </summary>
    public class CalcBot24
    {
        private double[] daValues;
        private double dResult;
        private double dPrecision;
        private int iCount;
        private string[] saExp;
        private bool bAdd = true;
        private bool bSubtraction = true;
        private bool bMultiplication = true;
        private bool bDivision = true;
        private bool bPower = true;
        private bool bEqual = true;
        private bool bUnequal = true;
        private bool bLogarithm = true;

        /// <summary>
        /// 计算结果与期望值允许的误差值
        /// </summary>
        public double Precision
        {
            get { return dPrecision; }
            set { dPrecision = value; }
        }

        /// <summary>
        /// 是否允许加法
        /// </summary>
        public bool OperatorAdd
        {
            get { return bAdd; }
            set { bAdd = value; }
        }

        /// <summary>
        /// 是否允许减法
        /// </summary>
        public bool OperatorSubtraction
        {
            get { return bSubtraction; }
            set { bSubtraction = value; }
        }

        /// <summary>
        /// 是否允许乘法
        /// </summary>
        public bool OperatorMultiplication
        {
            get { return bMultiplication; }
            set { bMultiplication = value; }
        }

        /// <summary>
        /// 是否允许除法
        /// </summary>
        public bool OperatorDivision
        {
            get { return bDivision; }
            set { bDivision = value; }
        }

        /// <summary>
        /// 是否允许幂运算
        /// </summary>
        public bool OperatorPower
        {
            get { return bPower; }
            set { bPower = value; }
        }

        /// <summary>
        /// 是否允许等于运算
        /// </summary>
        public bool OperatorEqual
        {
            get { return bEqual; }
            set { bEqual = value; }
        }

        /// <summary>
        /// 是否允许不等于运算
        /// </summary>
        public bool OperatorUneqaul
        {
            get { return bUnequal; }
            set { bUnequal = value; }
        }

        /// <summary>
        /// 是否允许求对数运算
        /// </summary>
        public bool OperatorLogarithm
        {
            get { return bLogarithm; }
            set { bLogarithm = value; }
        }

        /// <summary>
        /// 初始化 ZeroEnna.Algorithm.CalcBot24 类的新实例
        /// </summary>
        /// <param name="Values">待计算的数据数组</param>
        /// <param name="Result">期望的结果值</param>
        public CalcBot24(double[] Values, double Result)
        {
            daValues = Values;
            dResult = Result;
            dPrecision = 1E-6;
            iCount = Values.Length;
            if (iCount == 0)
            {
                throw new IndexOutOfRangeException("至少必须有1个操作数");
            }
            saExp = new string[iCount];
            for (int i = 0; i < iCount; i++)
            {
                saExp[i] = daValues[i].ToString();
            }
        }

        /// <summary>
        /// 初始化 ZeroEnna.Algorithm.CalcBot24 类的新实例
        /// </summary>
        /// <param name="Values">待计算的数据数组</param>
        /// <param name="Result">期望的结果值</param>
        /// <param name="Precision">计算结果与期望值允许的误差值</param>
        public CalcBot24(double[] Values, double Result, double Precision)
        {
            daValues = Values;
            dResult = Result;
            dPrecision = Precision;
            iCount = Values.Length;
            if (iCount == 0)
            {
                throw new IndexOutOfRangeException("至少必须有1个操作数");
            }
            saExp = new string[iCount];
            for (int i = 0; i < iCount; i++)
            {
                saExp[i] = daValues[i].ToString();
            }
        }

        /// <summary>
        /// 初始化 ZeroEnna.Algorithm.CalcBot24 类的新实例
        /// </summary>
        /// <param name="Values">待计算的数据数组</param>
        /// <param name="Result">期望的结果值</param>
        /// <param name="BasicOperators">是否限制为基本操作符，即只有加减乘除</param>
        public CalcBot24(double[] Values, double Result, bool BasicOperators)
        {
            daValues = Values;
            dResult = Result;
            dPrecision = 1E-6;
            iCount = Values.Length;
            if (iCount == 0)
            {
                throw new IndexOutOfRangeException("至少必须有1个操作数");
            }
            saExp = new string[iCount];
            for (int i = 0; i < iCount; i++)
            {
                saExp[i] = daValues[i].ToString();
            }
            if (BasicOperators)
            {
                bPower = false;
                bEqual = false;
                bUnequal = false;
                bLogarithm = false;
            }
        }

        private bool Recursion(int n)
        {
            if (n == 1)
            {
                if (Math.Abs(daValues[0] - dResult) < dPrecision)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            for (int i = 0; i < n; i++)
            {
                for (int j = i + 1; j < n; j++)
                {
                    double dA = daValues[i], dB = daValues[j];
                    string sA = saExp[i], sB = saExp[j];
                    daValues[j] = daValues[n - 1];
                    saExp[j] = saExp[n - 1];
                    if (bAdd)
                    {
                        saExp[i] = "（" + sA + "＋" + sB + "）";
                        daValues[i] = dA + dB;
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bSubtraction)
                    {
                        saExp[i] = "（" + sA + "－" + sB + "）";
                        daValues[i] = dA - dB;
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                        saExp[i] = "（" + sB + "－" + sA + "）";
                        daValues[i] = dB - dA;
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bMultiplication)
                    {
                        saExp[i] = "（" + sA + "×" + sB + "）";
                        daValues[i] = dA * dB;
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bDivision)
                    {
                        if (dB != 0)
                        {
                            saExp[i] = "（" + sA + "÷" + sB + "）";
                            daValues[i] = dA / dB;
                            if (Recursion(n - 1))
                            {
                                return true;
                            }
                        }
                        if (dA != 0)
                        {
                            saExp[i] = "（" + sB + "÷" + sA + "）";
                            daValues[i] = dB / dA;
                            if (Recursion(n - 1))
                            {
                                return true;
                            }
                        }
                    }
                    if (bPower)
                    {
                        saExp[i] = "（" + sA + "^" + sB + "）";
                        daValues[i] = Math.Pow(dA, dB);
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                        saExp[i] = "（" + sB + "^" + sA + "）";
                        daValues[i] = Math.Pow(dB, dA);
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bEqual)
                    {
                        saExp[i] = "（" + sA + "==" + sB + "）";
                        daValues[i] = Convert.ToInt32(dA == dB);
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bUnequal)
                    {
                        saExp[i] = "（" + sA + "!=" + sB + "）";
                        daValues[i] = Convert.ToInt32(dA != dB);
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bLogarithm)
                    {
                        if (dA > 0 && dA != 1)
                        {
                            saExp[i] = "（㏒" + sA + "＼" + sB + "）";
                            daValues[i] = Math.Log(dB, dA);
                            if (Recursion(n - 1))
                            {
                                return true;
                            }
                        }
                        if (dB > 0 && dB != 1)
                        {
                            saExp[i] = "（㏒" + sB + "＼" + sA + "）";
                            daValues[i] = Math.Log(dA, dB);
                            if (Recursion(n - 1))
                            {
                                return true;
                            }
                        }
                    }
                    daValues[i] = dA;
                    daValues[j] = dB;
                    saExp[i] = sA;
                    saExp[j] = sB;
                }
            }
            return false;
        }

        /// <summary>
        /// 取得表达式形式的解
        /// </summary>
        /// <returns>计算所得的解，如果无解返回N/A</returns>
        public string GetExpress()
        {
            if (Recursion(daValues.Length))
            {
                return saExp[0];
            }
            else
            {
                return "N/A";
            }
        }
    }
}
