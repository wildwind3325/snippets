// ʹ��FileSystemObject�������Ҫ����Microsoft Scripting Runtime

Dim fso As New FileSystemObject
Dim ts As TextStream
Set ts = fso.OpenTextFile(ActiveWorkbook.Path & "\" & "output.txt", ForReading)
Dim line As String
Do While ts.AtEndOfStream <> True
line = ts.ReadLine // ��ȡһ��
Loop
ts.Close

// ��ʹ���κ����ֱ��д�ı��ļ�

Dim DestFile As String
Dim FileNum As Integer
DestFile = InputBox("����д�����ļ����ƣ�", "��ʾ", "E:\1.txt")
If DestFile = "" Then
//δѡ��
End If
FileNum = FreeFile()
Open DestFile For Output As #FileNum
Print #FileNum, "Sampler Text" & Chr(13) & Chr(10)//д������
Close #FileNum