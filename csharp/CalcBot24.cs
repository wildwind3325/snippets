using System;
using System.Collections.Generic;
using System.Text;

namespace TestApp
{
    /// <summary>
    /// ��������24�����Ϸ�ⷨ��
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
        /// ������������ֵ��������ֵ
        /// </summary>
        public double Precision
        {
            get { return dPrecision; }
            set { dPrecision = value; }
        }

        /// <summary>
        /// �Ƿ�����ӷ�
        /// </summary>
        public bool OperatorAdd
        {
            get { return bAdd; }
            set { bAdd = value; }
        }

        /// <summary>
        /// �Ƿ��������
        /// </summary>
        public bool OperatorSubtraction
        {
            get { return bSubtraction; }
            set { bSubtraction = value; }
        }

        /// <summary>
        /// �Ƿ�����˷�
        /// </summary>
        public bool OperatorMultiplication
        {
            get { return bMultiplication; }
            set { bMultiplication = value; }
        }

        /// <summary>
        /// �Ƿ��������
        /// </summary>
        public bool OperatorDivision
        {
            get { return bDivision; }
            set { bDivision = value; }
        }

        /// <summary>
        /// �Ƿ�����������
        /// </summary>
        public bool OperatorPower
        {
            get { return bPower; }
            set { bPower = value; }
        }

        /// <summary>
        /// �Ƿ������������
        /// </summary>
        public bool OperatorEqual
        {
            get { return bEqual; }
            set { bEqual = value; }
        }

        /// <summary>
        /// �Ƿ�������������
        /// </summary>
        public bool OperatorUneqaul
        {
            get { return bUnequal; }
            set { bUnequal = value; }
        }

        /// <summary>
        /// �Ƿ��������������
        /// </summary>
        public bool OperatorLogarithm
        {
            get { return bLogarithm; }
            set { bLogarithm = value; }
        }

        /// <summary>
        /// ��ʼ�� ZeroEnna.Algorithm.CalcBot24 �����ʵ��
        /// </summary>
        /// <param name="Values">���������������</param>
        /// <param name="Result">�����Ľ��ֵ</param>
        public CalcBot24(double[] Values, double Result)
        {
            daValues = Values;
            dResult = Result;
            dPrecision = 1E-6;
            iCount = Values.Length;
            if (iCount == 0)
            {
                throw new IndexOutOfRangeException("���ٱ�����1��������");
            }
            saExp = new string[iCount];
            for (int i = 0; i < iCount; i++)
            {
                saExp[i] = daValues[i].ToString();
            }
        }

        /// <summary>
        /// ��ʼ�� ZeroEnna.Algorithm.CalcBot24 �����ʵ��
        /// </summary>
        /// <param name="Values">���������������</param>
        /// <param name="Result">�����Ľ��ֵ</param>
        /// <param name="Precision">������������ֵ��������ֵ</param>
        public CalcBot24(double[] Values, double Result, double Precision)
        {
            daValues = Values;
            dResult = Result;
            dPrecision = Precision;
            iCount = Values.Length;
            if (iCount == 0)
            {
                throw new IndexOutOfRangeException("���ٱ�����1��������");
            }
            saExp = new string[iCount];
            for (int i = 0; i < iCount; i++)
            {
                saExp[i] = daValues[i].ToString();
            }
        }

        /// <summary>
        /// ��ʼ�� ZeroEnna.Algorithm.CalcBot24 �����ʵ��
        /// </summary>
        /// <param name="Values">���������������</param>
        /// <param name="Result">�����Ľ��ֵ</param>
        /// <param name="BasicOperators">�Ƿ�����Ϊ��������������ֻ�мӼ��˳�</param>
        public CalcBot24(double[] Values, double Result, bool BasicOperators)
        {
            daValues = Values;
            dResult = Result;
            dPrecision = 1E-6;
            iCount = Values.Length;
            if (iCount == 0)
            {
                throw new IndexOutOfRangeException("���ٱ�����1��������");
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
                        saExp[i] = "��" + sA + "��" + sB + "��";
                        daValues[i] = dA + dB;
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bSubtraction)
                    {
                        saExp[i] = "��" + sA + "��" + sB + "��";
                        daValues[i] = dA - dB;
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                        saExp[i] = "��" + sB + "��" + sA + "��";
                        daValues[i] = dB - dA;
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bMultiplication)
                    {
                        saExp[i] = "��" + sA + "��" + sB + "��";
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
                            saExp[i] = "��" + sA + "��" + sB + "��";
                            daValues[i] = dA / dB;
                            if (Recursion(n - 1))
                            {
                                return true;
                            }
                        }
                        if (dA != 0)
                        {
                            saExp[i] = "��" + sB + "��" + sA + "��";
                            daValues[i] = dB / dA;
                            if (Recursion(n - 1))
                            {
                                return true;
                            }
                        }
                    }
                    if (bPower)
                    {
                        saExp[i] = "��" + sA + "^" + sB + "��";
                        daValues[i] = Math.Pow(dA, dB);
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                        saExp[i] = "��" + sB + "^" + sA + "��";
                        daValues[i] = Math.Pow(dB, dA);
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bEqual)
                    {
                        saExp[i] = "��" + sA + "==" + sB + "��";
                        daValues[i] = Convert.ToInt32(dA == dB);
                        if (Recursion(n - 1))
                        {
                            return true;
                        }
                    }
                    if (bUnequal)
                    {
                        saExp[i] = "��" + sA + "!=" + sB + "��";
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
                            saExp[i] = "���S" + sA + "��" + sB + "��";
                            daValues[i] = Math.Log(dB, dA);
                            if (Recursion(n - 1))
                            {
                                return true;
                            }
                        }
                        if (dB > 0 && dB != 1)
                        {
                            saExp[i] = "���S" + sB + "��" + sA + "��";
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
        /// ȡ�ñ��ʽ��ʽ�Ľ�
        /// </summary>
        /// <returns>�������õĽ⣬����޽ⷵ��N/A</returns>
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
